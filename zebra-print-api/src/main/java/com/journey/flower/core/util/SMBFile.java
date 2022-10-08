package com.journey.flower.core.util;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.file.Paths;

/**
 * @author JourneyOfFlower
 * @version v1.0
 * @date 2022.09.27 09:15
 * @description ZB_TODO
 */
public class SMBFile {
    //远程服务器的地址
    static String host = "192.168.0.146";
    //用户名
    static String username = "ZebraPrintShared";
    //密码
    static String password = "fengfanShared_001";
    //远程服务器共享文件夹名称
    static String smbPath = "smb://ZebraPrintShared:fengfanShared_001@192.168.0.146/ZebraPrintTemplate";
    static String localPath = "C:/aaaaaaaaaaaaaaaa";

    public static void main(String[] args) {
        //getTemplateFile("");
        InputStream in = null;
        OutputStream out = null;
        try {
            //测试文件
            File localFile = new File("D:/work/保定风帆/501包装车间/Doc/电解铅.prn");
            String remoteUrl = "smb://" + username + ":" + password + "@" + host + smbPath + (smbPath.endsWith("/") ? "" : "/");
            SmbFile remoteFile = new SmbFile(remoteUrl + localFile.getName());

            remoteFile.connect();

            in = new BufferedInputStream(new FileInputStream(localFile));
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));

            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            String msg = "发生错误：" + e.getLocalizedMessage();
            System.out.println(msg);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 得到服务器上的模板信息
     *
     * @return
     */
    public static File getTemplateFile(String remoteUrlPath) {
        BufferedOutputStream output = null;
        BufferedInputStream input = null;
        File localFile = null;
        try {
            final String remoteFileName = Paths.get(remoteUrlPath).getFileName().toString();
            //远端指令文件路径
            //String remoteUrl = "smb://" + username + ":" + password + "@" + host + smbPath + (smbPath.endsWith("/") ? "" : "/") + remoteFileName;
            String remoteUrl = smbPath + (smbPath.endsWith("/") ? "" : "/") + remoteFileName;
            String localUrl = localPath + (localPath.endsWith("/") ? "" : "/") + remoteFileName;
            //本地指令文件路径
            localFile = new File(localUrl);
            //本地指令存在
            if (localFile.exists()) {
                try {
                    final boolean IsSameFile = isSameFile(localUrl, remoteUrl);
                    if (IsSameFile) {
                        //文件相同，则直接使用本地文件
                        return localFile;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //服务器文件异常 直接使用本地指令
                    return localFile;
                }
            }
            //本地指令不存在存在 从服务器下载最新指令
            SmbFile remoteFile = new SmbFile(remoteUrl);
            remoteFile.connect();
            input = new BufferedInputStream(new SmbFileInputStream(remoteFile));
            output = new BufferedOutputStream(new FileOutputStream(localFile));
            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = input.read(buffer, 0, buffer.length)) != -1) {
                output.write(buffer, 0, len);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
            }
        }
        return localFile;
    }

    /**
     * 判断本地文件和服务器文件MD5是否一致
     *
     * @param filePath
     * @param smbFilePath
     * @return
     * @throws IOException
     */
    public static boolean isSameFile(String filePath, String smbFilePath) throws IOException {
        final String localMD5 = DigestUtils.md5Hex(new FileInputStream(filePath));
        SmbFile remoteFile = new SmbFile(smbFilePath);
        final String remoteMD5 = DigestUtils.md5Hex(new SmbFileInputStream(remoteFile));
        if (localMD5.equals(remoteMD5)) {
            return true;
        }
        return false;
    }
}
