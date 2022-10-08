package com.journey.flower.controller;

import com.journey.flower.core.model.ResponseBase;
import com.journey.flower.core.model.ZebraPrintTemplate;
import com.journey.flower.service.PrintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JourneyOfFlower
 * @version v1.0
 * @date 2022.09.23 10:57
 * @description ZB_TODO
 */
@Slf4j
@RestController
@RequestMapping("/zebraZPLPrint")
public class ZebraZPLPrint {

    /**
     * 打印
     */
    @Autowired
    PrintService printService;

    @PostMapping("/print")
    public void Print(@RequestBody List<String> printContents) {
        this.printService.Print(printContents);
    }

    /**
     * 查询打印模板
     *
     * @param record
     * @return
     */
    @PostMapping("/getZPLTemplateList")
    public ResponseBase getZPLTemplateList(@RequestBody ZebraPrintTemplate record) {
        return printService.getZPLTemplateList(record);
    }

}
