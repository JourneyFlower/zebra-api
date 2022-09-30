连接斑马GB888T（GPL） 打印，第一次连接这种打印机打印需要采用ZPL打印,，使用极度不爽，记录下来！
成果：打印条形码，二维码，中英文结合（中文的标点符号第二种方法可以实现），我的显示大小7*3.5cm
zpl 在线显示：http://labelary.com/viewer.html



首先在命令行输入命令，将jar（二维码）导入maven：
`mvn install:install-file   -Dfile=D:\work\Code\zebra-api\zebra-print-api\src\main\resources\QRCode.jar -DgroupId="cn.zebra"  -DartifactId=QRCode -Dversion="1.3.0" -Dpackaging=jar`

pom.xml
```xml
 <dependencies>
        <dependency>
            <groupId>cn.zebra</groupId>
            <artifactId>QRCode</artifactId>
            <version>1.3.0</version>
        </dependency>
    </dependencies>
```



- 第一种方案：直接使用zpl指令打印
  需要点阵字库的支持 ts24.lib
  如果要打印二维码还需要QRCode.jar
  缺点：无法更改字体样式，只能修改字体倍率

  注：关于二维码（一般采用BQ格式），如果二维码中不包含汉字则可以直接使用BQ指令 ^BQa,b,c,d,e
  如果有汉字，则首先使用QRCode.jar生成png图片或者直接生成图像缓存BufferedImage，然后把图片转换成十六进制字符串，使用DG和XG指令打印(DG和XG后的名字一定要一致)



- 第二种方案：先把整个标签做成图片，然后把整个图片转换成十六进制字符串生成整个图片的zpl指令进行打印，也是使用DG和XG指令
  如果需要生成二维码则需要QRCode.jar，否则不需要引入任何第三方的库
  第二种方法可以随意更改字体、字号及样式，以及实现中文特殊字符打印

有关zpl指令的问题请参考  ZPL语言中文手册_ZHCN.pdf

代码展示
```java
package com.sdz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.standard.PrinterName;

import com.sdz.code.TwoDimensionCode;
import lombok.Data;


@Data
public class ZplPrinter {
    private String printerURI = null;// 打印机完整路径
    private PrintService printService = null;// 打印机服务
    private byte[] dotFont;
    private String begin = "^XA"; // 标签格式以^XA开始
    private String end = "^XZ"; // 标签格式以^XZ结束
    private String content = "";

    public static void  printBarcode(){
        String electronicCode = "123457FFFFEEEB";

        ZplPrinter zplPrinter = new ZplPrinter("ZDesigner ZT410-300dpi ZPL");
        String productName = "product.getName";
        productName = productName.replace("，",",");
        productName = productName.replace("（","(");
        productName = productName.replace("）",")");
        productName = productName.replace("×","*");
        productName = "product.getName";
        if(productName.length() < 25){
            zplPrinter.setText(productName, 15, 10, 25, 25, 15, 1, 1, 24);
        }else if(productName.length() > 25){
            if(productName.length() > 50){
                productName = productName.substring(0,50) + "...";
            }
            zplPrinter.setText(productName.substring(0, 25), 15, 10, 25, 25, 15, 1, 1, 24);
            zplPrinter.setText(productName.substring(25, productName.length()), 15, 40, 25, 25, 15, 1, 1, 24);
        }
        String bar0Zpl = "^FO25,80^BY3,3.0,150^BCN,,Y,N,N^FD${data}^FS";//条码样式模板
        zplPrinter.setBarcode(electronicCode, bar0Zpl);
        System.out.println(zplPrinter.getZpl());
        zplPrinter.print(zplPrinter.getZpl());
    }

    public static void printQRCOde(){
        // 打印二维码
        String stockCode = "123457FFFFEEEB";

        ZplPrinter codeZplPrinter = new ZplPrinter("ZDesigner ZT410-300dpi ZPL");
        TwoDimensionCode handler = new TwoDimensionCode();
        BufferedImage bufferedImage = handler.encoderQRCode(stockCode, "png", 9);
        String codeBegin= Image2Zpl.image2Zpl(bufferedImage);

        codeZplPrinter.setBegin(codeZplPrinter.getBegin() + codeBegin);
        codeZplPrinter.setContent(codeZplPrinter.getContent() + "^FO50,70^XG"+Image2Zpl.imgLength+",1,1^FS\n");

        codeZplPrinter.setChar(stockCode, 250, 135, 22, 22);

//        content += "^PQ2";//打印2张
        codeZplPrinter.print(codeZplPrinter.getZpl());

        codeZplPrinter.print(codeZplPrinter.getZpl());
    }
    
    public static void main(String[] args) {

//        printBarcode();
//        printQRCOde();
        ZplPrinter p = new ZplPrinter("ZDesigner ZT410-300dpi ZPL");

        /**第一种方法**/

        String codeBegin="";
        BufferedImage bufferedImage = null;//二维码
        TwoDimensionCode handler = new TwoDimensionCode();
        String encoderContentStr = "样品编号：TX2-18/03/29-003-123456-123\n委托编号：CEPR1-TX2-2018-130-1234567";
        bufferedImage = handler.encoderQRCode(encoderContentStr, "png", 8);
        codeBegin= Image2Zpl.image2Zpl(bufferedImage);

        p.setText("样品标识", 100, 20, 30, 30, 15, 1, 1, 24);

        p.setBegin(p.getBegin() + codeBegin);
        p.setContent(p.getContent() + "^FO50,70^XG"+Image2Zpl.imgLength+",1,1^FS\n");

        p.setText("样品编号:", 5, 50, 25, 25, 13, 1, 1, 24);
        p.setChar("TX2-18/03/09-0003", 5, 85, 22, 22);
        p.setText("委托编号:", 5, 120, 25, 25, 13, 1, 1, 24);
        p.setChar("CEPR1-TX2-2018-1300", 5, 155, 22, 22);
        p.setText("样品名称:生产制造执行系统", 5, 200, 25, 25, 13, 1, 1, 24);

        p.setText("待检", 5, 288, 30, 30, 15, 1, 1, 24);
        p.setBox(55, 288, 40, 25, 1);
        p.setText("在检", 105, 288, 30, 30, 15, 1, 1, 24);
        p.setBox(155, 288, 40, 25, 1);
        p.setText("已检", 205, 288, 30, 30, 15, 1, 1, 24);
        p.setBox(255, 290, 40, 25, 1);
        p.setText("留样", 305, 288, 30, 30, 15, 1, 1, 24);
        p.setBox(355, 288, 40, 25, 1);

//        content += "^PQ2";//打印2张
        String zpl2 = p.getZpl();
        System.out.println("zpl2======="+zpl2);


//      第二种方法
        try {
			BufferedImage labelImg = ImageProducerUtil.createImage1();
            zpl2 = Image2Zpl.image2Zpl2(labelImg);
			System.out.println("zpl2======="+zpl2);
		} catch (Exception e) {
			e.printStackTrace();
		}


        boolean result2 = p.print(zpl2);
    }
    
    
    /**
     * 构造方法
     * 
     * @param printerURI
     *            打印机路径
     */
    public ZplPrinter(String printerURI) {
        this.printerURI = printerURI;
        // 加载点阵字库 汉字需要
        File file = new File("D:\\Desktop\\Project\\斑马打印机\\zebraPrint\\src\\main\\resources\\ts24.lib");
        if (file.exists()) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                dotFont = new byte[fis.available()];
                fis.read(dotFont);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("d://ts24.lib文件不存在");
        }
        // 初始化打印机
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        if (services != null && services.length > 0) {
            for (PrintService service : services) {
                if (printerURI.equals(service.getName())) {
                    printService = service;
                    break;
                }
            }
        }
        if (printService == null) {
            System.out.println("没有找到打印机：[" + printerURI + "]");
            // 循环出所有的打印机
            if (services != null && services.length > 0) {
                System.out.println("可用的打印机列表：");
                for (PrintService service : services) {
                    System.out.println("[" + service.getName() + "]");
                }
            }
        } else {
            System.out.println("找到打印机：[" + printerURI + "]");
            System.out.println("打印机名称：[" + printService.getAttribute(PrinterName.class).getValue() + "]");
        }
    }

    /**
     * 设置条形码
     * 
     * @param barcode
     *            条码字符
     * @param zpl
     *            条码样式模板
     */
    public void setBarcode(String barcode, String zpl) {
        content += zpl.replace("${data}", barcode);
    }

    /**
     * 中文字符、英文字符(包含数字)混合
     * 
     * @param str
     *            中文、英文
     * @param x
     *            x坐标
     * @param y
     *            y坐标
     * @param eh
     *            英文字体高度height
     * @param ew
     *            英文字体宽度width
     * @param es
     *            英文字体间距spacing
     * @param mx
     *            中文x轴字体图形放大倍率。范围1-10，默认1
     * @param my
     *            中文y轴字体图形放大倍率。范围1-10，默认1
     * @param ms
     *            中文字体间距。24是个比较合适的值。
     */
    public void setText(String str, int x, int y, int eh, int ew, int es, int mx, int my, int ms) {
        byte[] ch = str2bytes(str);
        for (int off = 0; off < ch.length;) {
            if (((int) ch[off] & 0x00ff) >= 0xA0) {//ASCII码值"0xa0"表示汉字的开始
                int qcode = ch[off] & 0xff;
                int wcode = ch[off + 1] & 0xff;
                content += String.format("^FO%d,%d^XG0000%01X%01X,%d,%d^FS\n", x, y, qcode, wcode, mx, my);
                begin += String.format("~DG0000%02X%02X,00072,003,\n", qcode, wcode);
                qcode = (qcode + 128 - 32) & 0x00ff;//区码：或qcode = (qcode - 128 - 32) & 0x00ff;（一般这样用）可达到减去0xA0的效果
                wcode = (wcode + 128 - 32) & 0x00ff;//位码：或wcode = (wcode - 128 - 32) & 0x00ff;（一般这样用）可达到减去0xA0的效果
                int offset = ((int) qcode - 16) * 94 * 72 + ((int) wcode - 1) * 72;//偏移量是指字模首字节距离文件头的相对位置
                for (int j = 0; j < 72; j += 3) {
                    qcode = (int) dotFont[j + offset] & 0x00ff;
                    wcode = (int) dotFont[j + offset + 1] & 0x00ff;
                    int qcode1 = (int) dotFont[j + offset + 2] & 0x00ff;
                    begin += String.format("%02X%02X%02X\n", qcode, wcode, qcode1);//X  --- 以十六进制显示，不足两位则补零
                }
                x = x + ms * mx;
                off = off + 2;//中文包含两个字节
            } else if (((int) ch[off] & 0x00FF) < 0xA0) {
                setChar(String.format("%c", ch[off]), x, y, eh, ew);
                x = x + es;
                off++;//英文包含一个字节
            }
        }
    }
    

    /**
     * 英文字符串(包含数字)
     * 
     * @param str
     *            英文字符串
     * @param x
     *            x坐标
     * @param y
     *            y坐标
     * @param h
     *            高度
     * @param w
     *            宽度
     */
    public void setChar(String str, int x, int y, int h, int w) {
        content += "^FO" + x + "," + y + "^A0," + h + "," + w + "^FD" + str + "^FS";
    }
    
	
	
	/**
	 * BQ 二维码 ^BQa,b,c,d,e
	 * 
	 * @param data
	 * @param x
	 *            x坐标
	 * @param y
	 *            y坐标
	 * @param b
	 *            模型 默认值:2(增强) 推荐 其他值:1
	 * @param c
	 *            放大倍数 默认值:1在150 dpi打印机 2在200 dpi打印机 3在300 dpi打印机 其他值:4至10
	 * @param d
	 *            校验等级，H Q M L L级：约可纠错7%的数据码字 M级：约可纠错15%的数据码字 Q级：约可纠错25%的数据码字
	 *            H级：约可纠错30%的数据码字
	 */
	public void setCodeByBQ(String data, int x, int y, int b, int c, String d) {
		content += "^FT" + x + "," + y + "^BQ," + b + "," + c + "," + d + "^FDQA," + data + "^FS";
	}
	
	
	/**
	 * 打印方框
	 * ^FO10,10\n^GB50,760,3,B^FS
	 * @param x x坐标
	 * @param y y坐标
	 * @param w 方框宽度
	 * @param h 方框高度
	 * @param t 线宽
	 */
	public void setBox(int x, int y,int w, int h, int t) {
		content += "^FO" + x + "," + y + "^GB"+w+"," + h + "," + t + "^FS";
	}
	
	

    /**
     * 英文字符(包含数字)顺时针旋转90度
     * 
     * @param str
     *            英文字符串
     * @param x
     *            x坐标
     * @param y
     *            y坐标
     * @param h
     *            高度
     * @param w
     *            宽度
     */
    public void setCharR(String str, int x, int y, int h, int w) {
        content += "^FO" + x + "," + y + "^A0R," + h + "," + w + "^FD" + str + "^FS";
    }
    

    /**
     * 获取完整的ZPL
     * 
     * @return
     */
    public String getZpl() {
        return begin + content + end;
    }

    /**
     * 重置ZPL指令，当需要打印多张纸的时候需要调用。
     */
    public void resetZpl() {
        begin = "^XA";
        end = "^XZ";
        content = "";
    }

    /**
     * 打印
     * 
     * @param zpl
     *            完整的ZPL
     */
    public boolean print(String zpl) {
        if (printService == null) {
            System.out.println("打印出错：没有找到打印机：[" + printerURI + "]");
            return false;
        }
        DocPrintJob job = printService.createPrintJob();
        byte[] by = zpl.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(by, flavor, null);
        try {
            job.print(doc, null);
            System.out.println("已打印");
            return true;
        } catch (PrintException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 字符串转byte[]
     * 
     * @param s
     * @return
     */
    private static byte[] str2bytes(String s) {
        if (null == s || "".equals(s)) {
            return null;
        }
        byte[] abytes = null;
        try {
            abytes = s.getBytes("gb2312");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return abytes;
    }
    
}
```

## 通过 `src\main\resources\service_tool` 下的工具，可以将该`zebra-api`安装为本地打印服务。