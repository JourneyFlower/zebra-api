package com.journey.flower.core.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author JourneyOfFlower
 * @version v1.0
 * @date 2022.09.29 17:58
 * @description 打印参数
 */
@Data
public class PrintParam implements Serializable {

    private static final long serialVersionUID = -7027500191751997240L;

    /**
     * 标签模板code
     * 1电解铅，2板栅，3生板，4极板，5铅带
     */
    private int tempLateCode;

    /**
     * 标签模板名称
     */
    private String tempLateName;

    /**
     * 收货单号
     */
    private String receiptNo;
    /**
     * 批次编码
     */
    private String batchCode;
    /**
     * 质检人
     */
    private String QCPerson;
    /**
     * 产品编码
     */
    private String materialCode;
    /**
     * 产品名称
     */
    private String materialName;
    /**
     * 合格数量
     */
    private String goodAmount;
    /**
     * 交货日期
     */
    private String deliveryDate;
    /**
     * 质检日期
     */
    private String QCDate;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 工单号
     */
    private String workNo;
    /**
     * 生产日期
     */
    private String productionDate;
    /**
     * 终止使用日期
     */
    private String terminateUesDate;
    /**
     * 生产线
     */
    private String productionLine;
    /**
     * 班组
     */
    private String workGroup;
    /**
     * 班次
     */
    private String workClasses;
    /**
     * 批次数量
     */
    private BigDecimal batchAmount;
    /**
     * 固化室
     */
    private String curingRoom;
}
