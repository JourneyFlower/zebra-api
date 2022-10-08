package com.journey.flower.core.util;

import com.journey.flower.core.code.TwoDimensionCode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author JourneyOfFlower
 * @version v1.0
 * @date 2022.09.23 11:34
 * @description ZB_TODO
 */
public class ImageProducerUtil {

    //标题字体
    private static Font titleFont = new Font("宋体", Font.BOLD, 36);
    //内容字体
    private static Font valueFont = new Font("宋体", Font.BOLD, 10);
    //标签字体
    private static Font labelFont = new Font("宋体", Font.BOLD, 24);

    private static int w = 630, h = 380;

    public static void main(String[] args) throws Exception {
        //ImageProducerUtil.createImage();
        //ImageProducerUtil.imgElectrolyticLead();
        //ImageProducerUtil.imgGridBoard();
        ImageProducerUtil.imgProducePlate();
    }


    /**
     * 获取字体高度
     *
     * @param font
     * @return
     */
    private static int getFontHeight(Font font) {
        return sun.font.FontDesignMetrics.getMetrics(font).getHeight();
    }


    /**
     * 先把整个标签做成图像缓存
     *
     * @throws Exception
     */
    public static BufferedImage createImage() throws Exception {

        Font font1 = new Font("黑体", Font.BOLD, 36);//标题字体
        Font font2 = new Font("宋体", Font.BOLD, 28);//内容字体
        Font font3 = new Font("宋体", Font.BOLD, 24);//编号字体

        // 创建图片
        BufferedImage image = new BufferedImage(480, 320, BufferedImage.TYPE_INT_BGR);// 创建图片画布
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE); // 先用白色填充整张图片,也就是背景
        g.fillRect(0, 0, 480, 320);// 画出矩形区域，以便于在矩形区域内写入文字
        g.setColor(Color.black);// 再换成黑色，以便于写入文字
        g.setFont(font1);// 设置画笔字体
        g.drawString("样品标识", 120, 42);// 画出一行字符串
        g.setFont(font2);
        g.drawString("样品编号:", 5, 74);
        g.setFont(font3);
        g.drawString("SGSIL-18/03/29-001-1-20", 5, 106);
        g.setFont(font2);
        g.drawString("委托编号:", 5, 140);
        g.setFont(font3);
        g.drawString("CEPR1-SGSIL-2018-9999", 5, 170);


        //二维码

        TwoDimensionCode handler = new TwoDimensionCode();
        String encoderContentStr = "样品编号：TX2-18/03/29-003-123456-123\n委托编号：CEPR1-TX2-2018-130-1234567";
        BufferedImage codeImg = handler.encoderQRCode(encoderContentStr, "png", 8);
        g.drawImage(codeImg, 343, 40, codeImg.getWidth(), codeImg.getHeight(), null);

        g.setFont(font2);
        g.drawString("样品名称:一二三四五六七八九十一", 5, 240);


        g.drawString("待检", 10, 300);
        g.drawRect(70, 279, 40, 25);
        g.drawString("待检", 115, 300);
        g.drawRect(175, 279, 40, 25);
        g.drawString("待检", 220, 300);
        g.drawRect(280, 279, 40, 25);
        g.drawString("待检", 325, 300);
        g.drawRect(385, 279, 40, 25);


        g.dispose();

        return image;
    }

    public static BufferedImage createImage1() throws Exception {

        // 创建图片
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);// 创建图片画布
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE); // 先用白色填充整张图片,也就是背景
        g.fillRect(0, 0, w, h);// 画出矩形区域，以便于在矩形区域内写入文字
        g.setColor(Color.black);// 再换成黑色，以便于写入文字
        g.setFont(valueFont);// 设置画笔字体
        g.drawString("商品名称1234,,,,，，，，，，（）（）", 10, 30);// 画出一行字符串
        g.setFont(titleFont);


        //二维码
        TwoDimensionCode handler = new TwoDimensionCode();
        String encoderContentStr = "样品编号：TX2-18/03/29-003-123456-123\n委托编号：CEPR1-TX2-2018-130-1234567";
        BufferedImage codeImg = handler.encoderQRCode(encoderContentStr, "png", 9);
        g.drawImage(codeImg, 40, 50, codeImg.getWidth(), codeImg.getHeight(), null);
        g.dispose();
        return image;
    }

    /**
     * 电解铅 标签
     *
     * @return
     * @throws Exception
     */
    public static BufferedImage imgElectrolyticLead() throws Exception {

//       return ImageIO.read(new File("C:\\电解铅.png"));

        //创建图片画布
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
        //创建画笔
        Graphics g = image.getGraphics();
        // 先用白色填充整张图片,也就是背景
        g.setColor(Color.WHITE);
        // 画出矩形区域，以便于在矩形区域内写入文字
        g.fillRect(0, 0, w, h);

        //二维码
        TwoDimensionCode handler = new TwoDimensionCode();
        String encoderContentStr = "F0302208YSH0010";
        BufferedImage codeImg = handler.encoderQRCode(encoderContentStr, "png", 5);
        g.drawImage(codeImg, 30, 10, codeImg.getWidth(), codeImg.getHeight(), null);

        // 再换成黑色，以便于写入文字
        g.setColor(Color.black);
        // 切换画笔字体
        g.setFont(labelFont);
        //左
        g.drawString(String.format("收货单号:%s", "F0302208YSH0010"), 10, 170);
        g.drawString(String.format("批次编码:%s", "2022/08/02"), 10, 220);
        g.drawString(String.format("质检人员:%s", "002"), 10, 270);
        g.drawString(String.format("供应商:%s", "内蒙古兴安银铅冶炼有限公司"), 80, 370);
        //右上
        g.drawString(String.format("产品编码:%s", "34060010"), 250, 30);
        g.drawString(String.format("产品名称:%s", "电解铅"), 250, 75);
        g.drawString(String.format("合格数量:%s", "999"), 250, 120);
        //右下
        g.drawString(String.format("送货日期:%s", "2022/08/02"), 375, 170);
        g.drawString(String.format("质检日期:%s", "2022/09/29"), 375, 220);

        //切换画笔字体
        g.setFont(valueFont);
        g.dispose();

        ImageIO.write(image, "png", new File("C:\\电解铅.png"));

        return image;
    }

    /**
     * 板栅 标签
     *
     * @return
     * @throws Exception
     */
    public static BufferedImage imgGridBoard() throws Exception {

//       return ImageIO.read(new File("C:\\板栅.png"));

        //创建图片画布
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
        //创建画笔
        Graphics g = image.getGraphics();
        // 先用白色填充整张图片,也就是背景
        g.setColor(Color.WHITE);
        // 画出矩形区域，以便于在矩形区域内写入文字
        g.fillRect(0, 0, w, h);

        //二维码
        TwoDimensionCode handler = new TwoDimensionCode();
        String encoderContentStr = "F0302208YSH0010";
        BufferedImage codeImg = handler.encoderQRCode(encoderContentStr, "png", 5);
        g.drawImage(codeImg, 10, 10, codeImg.getWidth(), codeImg.getHeight(), null);

        // 再换成黑色，以便于写入文字
        g.setColor(Color.black);
        // 切换画笔字体
        g.setFont(labelFont);
        //左
        g.drawString(String.format("批次号:%s", "QDA1729092N201711050602C1"), 10, 170);
        g.drawString(String.format("工单号:%s", "QD-061201711050001"), 10, 230);
        g.drawString(String.format("生产日期:%s", "2022-8-17 16时"), 10, 290);
        g.drawString(String.format("终止使用日期:%s", "2022-8-17"), 10, 350);
        //右上
        g.setFont(titleFont);
        g.drawString(String.format("%s", "板栅"), 300, 60);
        //右下
        g.setFont(labelFont);
        g.drawString(String.format("产线:%s", "01#"), 470, 170);
        g.drawString(String.format("班组:%s", "QD-A1"), 470, 230);
        g.drawString(String.format("班次:%s", "中班"), 470, 290);
        g.drawString(String.format("数量:%s", "100"), 470, 350);

        //切换画笔字体
        g.setFont(valueFont);
        g.dispose();

        ImageIO.write(image, "png", new File("C:\\板栅.png"));

        return image;
    }

    /**
     * 板栅 标签
     *
     * @return
     * @throws Exception
     */
    public static BufferedImage imgProducePlate() throws Exception {

//       return ImageIO.read(new File("D:\\work\\保定风帆\\501包装车间\\Doc\生板.png"));

        //创建图片画布
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
        //创建画笔
        Graphics g = image.getGraphics();
        // 先用白色填充整张图片,也就是背景
        g.setColor(Color.WHITE);
        // 画出矩形区域，以便于在矩形区域内写入文字
        g.fillRect(0, 0, w, h);

        //二维码
        TwoDimensionCode handler = new TwoDimensionCode();
        String encoderContentStr = "F0302208YSH0010";
        BufferedImage codeImg = handler.encoderQRCode(encoderContentStr, "png", 5);
        g.drawImage(codeImg, 10, 10, codeImg.getWidth(), codeImg.getHeight(), null);

        // 再换成黑色，以便于写入文字
        g.setColor(Color.black);
        // 切换画笔字体
        g.setFont(labelFont);
        //左
        g.drawString(String.format("批次号:%s", "QDA1729092N201711050602C1"), 10, 170);
        g.drawString(String.format("工单号:%s", "QD-061201711050001"), 10, 230);
        g.drawString(String.format("生产日期:%s", "2022-8-17 16时"), 10, 290);
        g.drawString(String.format("终止使用日期:%s", "2022-8-17"), 10, 350);
        //右上
        g.setFont(titleFont);
        g.drawString(String.format("%s", "生板"), 300, 60);
        g.setFont(labelFont);
        g.drawString(String.format("固化室:%s", "01"), 350, 120);
        //右下
        g.setFont(labelFont);
        g.drawString(String.format("产线:%s", "01#"), 470, 170);
        g.drawString(String.format("班组:%s", "QD-A1"), 470, 230);
        g.drawString(String.format("班次:%s", "中班"), 470, 290);
        g.drawString(String.format("数量:%s", "100"), 470, 350);

        //切换画笔字体
        g.setFont(valueFont);
        g.dispose();

        ImageIO.write(image, "png", new File("C:\\生板.png"));

        return image;
    }

}
