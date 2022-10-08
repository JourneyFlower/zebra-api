package com.journey.flower.service.impl;

import com.journey.flower.core.code.TwoDimensionCode;
import com.journey.flower.core.model.ResponseBase;
import com.journey.flower.core.model.ZebraPrintTemplate;
import com.journey.flower.core.util.*;
import com.journey.flower.dao.ZebraPrintTemplateDao;
import com.journey.flower.service.PrintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author JourneyOfFlower
 * @version v1.0
 * @date 2022.09.23 11:04
 * @description ZB_TODO
 */
@Slf4j
@Service
@Transactional
public class PrintServiceImpl implements PrintService {
    final String PRINTER_URI = "Zebra ZD888 (203 dpi) - ZPL";

    /**
     * 模板
     */
    @Autowired
    ZebraPrintTemplateDao zebraPrintTemplateDao;

    /**
     * 打印历史版本 1
     *
     * @param params
     * @return
     */
    @Override
    public ResponseBase Print_h1(@RequestBody Map<String, Object> params) {
        ResponseBase result = new ResponseBase();
        try {
            /*printBarcode();
            printQRCOde();*/
            ZplPrinter zplPrinter = new ZplPrinter(PRINTER_URI);

            /**第一种方法**/

            String codeBegin = "";
            BufferedImage bufferedImage = null;//二维码
            TwoDimensionCode handler = new TwoDimensionCode();
            String encoderContentStr = "样品编号：TX2-18/03/29-003-123456-123\n委托编号：CEPR1-TX2-2018-130-1234567";
            bufferedImage = handler.encoderQRCode(encoderContentStr, "png", 8);
            codeBegin = Image2Zpl.image2Zpl(bufferedImage);

            zplPrinter.setText("样品标识", 100, 20, 30, 30, 15, 1, 1, 24);

            zplPrinter.setBegin(zplPrinter.getBegin() + codeBegin);
            zplPrinter.setContent(zplPrinter.getContent() + "^FO50,70^XG" + Image2Zpl.imgLength + ",1,1^FS\n");

            zplPrinter.setText("样品编号:", 5, 50, 25, 25, 13, 1, 1, 24);
            zplPrinter.setChar("TX2-18/03/09-0003", 5, 85, 22, 22);
            zplPrinter.setText("委托编号:", 5, 120, 25, 25, 13, 1, 1, 24);
            zplPrinter.setChar("CEPR1-TX2-2018-1300", 5, 155, 22, 22);
            zplPrinter.setText("样品名称:生产制造执行系统", 5, 200, 25, 25, 13, 1, 1, 24);

            zplPrinter.setText("待检", 5, 288, 30, 30, 15, 1, 1, 24);
            zplPrinter.setBox(55, 288, 40, 25, 1);
            zplPrinter.setText("在检", 105, 288, 30, 30, 15, 1, 1, 24);
            zplPrinter.setBox(155, 288, 40, 25, 1);
            zplPrinter.setText("已检", 205, 288, 30, 30, 15, 1, 1, 24);
            zplPrinter.setBox(255, 290, 40, 25, 1);
            zplPrinter.setText("留样", 305, 288, 30, 30, 15, 1, 1, 24);
            zplPrinter.setBox(355, 288, 40, 25, 1);

            //content += "^PQ2";//打印2张
            String zpl2 = zplPrinter.getZpl();
            System.out.println("zpl2=======" + zpl2);


            //第二种方法
            try {
                BufferedImage labelImg = ImageProducerUtil.createImage1();
                zpl2 = Image2Zpl.image2Zpl2(labelImg);
                System.out.println("zpl2=======" + zpl2);
            } catch (Exception e) {
                e.printStackTrace();
            }


            boolean result2 = zplPrinter.print(zpl2);
        } catch (Exception e) {
            e.printStackTrace();
            result.setRtnCode(600);
            result.setMsg(e.getMessage());
            result.setData(e);
        }
        return result;
    }

    /**
     * 打印
     *
     * @param record
     * @return
     */
    @Override
    public ResponseBase Print_h2(ZebraPrintTemplate record) {
        ResponseBase result = new ResponseBase();
        try {
            //数据库查询到指定模板
            final List<ZebraPrintTemplate> templateList = this.zebraPrintTemplateDao.getZPLTemplateList(record);
            final ZebraPrintTemplate zebraPrintTemplate = templateList.stream().findFirst().orElse(null);
            if (null == zebraPrintTemplate) {
                result.setRtnCode(601);
                result.setMsg("未查询到指定模板:" + record.getTemplateCode());
                result.setData(templateList);
                return result;
            }
            //构造模板文件
            final File file = new File(zebraPrintTemplate.getTemplatePath());
            StringBuilder sbZPL = new StringBuilder();
            if (file.exists()) {
                //读取指定模板
                getZPL(file, sbZPL);
            } else {
                result.setRtnCode(602);
                result.setMsg("不存在指定模板:" + record.getTemplatePath());
                result.setData(templateList);
                return result;
            }
            //打印模板
            ZplPrinter zplPrinter = new ZplPrinter(PRINTER_URI);
            boolean result2 = zplPrinter.print(sbZPL.toString());
        } catch (Exception e) {
            e.printStackTrace();
            result.setRtnCode(600);
            result.setMsg(e.getMessage());
            result.setData(e);
        }
        return result;
    }


    /**
     * 打印
     *
     * @param printContents
     * @return
     */
    @Override
    public void Print_h3(List<String> printContents) {
        try {
            //初始化打印机
            ZplPrinter zplPrinter = new ZplPrinter(PRINTER_URI);
            boolean printResult = false;
            for (int i = 0; i < printContents.size(); i++) {
                //打印内容
                String printContent = printContents.get(i);
                System.out.println(printContent);
                printResult = zplPrinter.print(printContent);
                if (printResult) {
                    continue;
                } else {
                    //如果打印失败，则终止打印
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 打印
     *
     * @param printContents
     * @return
     */
    @Override
    public void Print(List<String> printContents) {
        ResponseBase result = new ResponseBase();
        try {
            ZplPrinter zplPrinter = new ZplPrinter(PRINTER_URI);
            String strZpl = "";
            //标签图片
            BufferedImage labelImg = null;
            try {
                //电解铅
                labelImg = ImageProducerUtil.imgElectrolyticLead();
                //板栅
                labelImg = ImageProducerUtil.imgGridBoard();
                //生板
                labelImg = ImageProducerUtil.imgProducePlate();
                /*这个图片转ZPL 有缺陷
                strZpl = Image2Zpl.image2Zpl2(labelImg);*/
                //图片转ZPL
                strZpl = ImgToZpl.convertFromImg(labelImg);
                System.out.println("zpl2=======" + strZpl);
                boolean result2 = zplPrinter.print(strZpl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setRtnCode(600);
            result.setMsg(e.getMessage());
            result.setData(e);
        }
        return;
    }


    /**
     * 查询打印模板
     *
     * @param record
     * @return
     */
    @Override
    public ResponseBase getZPLTemplateList(ZebraPrintTemplate record) {
        ResponseBase result = new ResponseBase();
        ZebraPrintTemplate zebraPrintTemplate = null;
        try {
            zebraPrintTemplate = this.zebraPrintTemplateDao.getZPLTemplateList(record).stream().findFirst().orElse(null);
            if (null == zebraPrintTemplate) {
                result.setRtnCode(602);
                result.setMsg(String.format("未查询到指定模板 %s", record.getTemplateCode()));
            } else {
                final File templateFile = SMBFile.getTemplateFile(zebraPrintTemplate.getTemplatePath());
                StringBuilder sbZPL = new StringBuilder();
                if (null != templateFile && templateFile.exists()) {
                    //读取指定模板
                    getZPL(templateFile, sbZPL);
                }
                zebraPrintTemplate.setPrintTemplateStr(sbZPL.toString());
                result.setRtnCode(200);
                result.setMsg("查询模板成功");
                //解析模板内容

            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setRtnCode(600);
            result.setMsg(e.getMessage());
        }
        result.setData(zebraPrintTemplate);
        return result;
    }

    private void getZPL(File templateFile, StringBuilder sbZPL) throws IOException {
        FileInputStream fis = new FileInputStream(templateFile);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            //逐行读取
            System.out.println(line);
            sbZPL.append(line);
        }
        br.close();
    }
}
