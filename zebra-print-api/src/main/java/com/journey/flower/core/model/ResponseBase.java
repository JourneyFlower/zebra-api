package com.journey.flower.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JourneyOfFlower
 * @version v1.0
 * @date 2022.09.23 11:00
 * @description 服务接口统一的返回编码
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBase {
    public Integer rtnCode;
    private String msg;
    private Object data;
}
