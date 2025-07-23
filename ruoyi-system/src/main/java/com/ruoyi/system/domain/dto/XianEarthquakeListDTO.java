package com.ruoyi.system.domain.dto;

import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:00
 * @description: 风险区
 */


@Data
public class XianEarthquakeListDTO {
    private String eqid;
    private String eqqueueId;
    private String earthquakeName;
    private String earthquakeFullName;
    private String eqAddr;
    private String geom;
    private String intensity;
    private String depth;
    private java.sql.Timestamp occurrenceTime;
    private String eqType;
    private String source;
    private String eqAddrCode;
    private String townCode;
    private String pac;
    private String type;
    private long isDeleted;
    private String magnitude;
    private java.sql.Date createTime;
    private java.sql.Date updateTime;
    private String district;
    private String province;
    private String city;
}
