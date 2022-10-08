package com.journey.flower.service;

import com.journey.flower.core.model.ResponseBase;
import com.journey.flower.core.model.ZebraPrintTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author JourneyOfFlower
 * @version v1.0
 * @date 2022.09.23 11:02
 * @description ZB_TODO
 */
public interface PrintService {
    /**
     * 打印历史版本 1
     *
     * @param params
     * @return
     */
    ResponseBase Print_h1(Map<String, Object> params);

    /**
     * 打印
     *
     * @param record
     * @return
     */
    ResponseBase Print_h2(ZebraPrintTemplate record);

    /**
     * 打印
     *
     * @param printContents
     * @return
     */
    void Print_h3(List<String> printContents);

    /**
     * 打印
     *
     * @param printContents
     * @return
     */
    void Print(List<String> printContents);


    /**
     * 查询打印模板
     *
     * @param record
     * @return
     */
    ResponseBase getZPLTemplateList(ZebraPrintTemplate record);
}
