package com.ruoyi.system.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class EarthquakeRainDTO {

    private String earthquakeDisasterId;
    private String rainDisasterId;
    private String disasterName;
    private String earthquakeDisasterPosition;
    private String rainDisasterPosition;
    private String earthquakeFullName;
    private String magnitude;
    private String intensity;
    private Timestamp occurTime;
    private String eqType;
    private String rainfall;
    private String duration;
    private String disasterType;

}
