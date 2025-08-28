package com.ruoyi.system.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class EarthquakeRainDTO {

    /** 公共字段 */
    private String disasterName;            // 灾害名称
    private Timestamp occurTime;            // 发生时间
    private String disasterType;            // 灾害类型（earthquake、rain、collapse、landslide...）

    /** 各灾害类型专属 ID */
    private String earthquakeDisasterId;   // 地震
    private String rainDisasterId;         // 暴雨
    private String snowDisasterId;         // 积雪
    private String coldDamageDisasterId;   // 冷害
    private String collapseDisasterId;     // 崩塌
    private String landslideDisasterId;    // 滑坡
    private String debrisFlowDisasterId;   // 泥石流
    private String galeHailDisasterId;     // 风雹
    private String sandstormDisasterId;    // 沙尘暴
    private String droughtDisasterId;      // 干旱
    private String heatwaveDisasterId;     // 高温
    private String wildfireDisasterId;     // 森林火灾
    private String bioDisasterId;          // 生物灾害
    private String safetyAccidentDisasterId; // 安全事故




}
