package com.journey.flower.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JourneyOfFlower
 * @version v1.0
 * @date 2022.09.23 15:12
 * @description ZB_TODO
 */
@Data
public class ZebraPrintTemplate implements Serializable {
    private static final long serialVersionUID = -2547432653445743870L;
    private String id;

    private String templateName;

    private String templateCode;

    private String templatePath;

    private Date createDate;

    private String createUser;

    private Date updateDate;

    private String updateUser;

    private String printTemplateStr;
}
