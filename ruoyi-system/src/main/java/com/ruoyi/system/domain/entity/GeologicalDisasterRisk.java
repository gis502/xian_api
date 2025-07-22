package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 16:57
 * @description: 地质灾害风险区
 */

@Data
@TableName("geological_disaster_risk")
public class GeologicalDisasterRisk {

    @TableId
    private Integer id;
    @TableField("risk_name")
    private String riskName;
    @TableField("unified_id")
    private String unifiedId;
    @TableField("risk_level")
    private String riskLevel;
    @TableField("area")
    private Double area;
    @TableField("province")
    private String province;
    @TableField("city")
    private String city;
    @TableField("county")
    private String county;
    @TableField("country")
    private String country;
    @TableField("village")
    private String village;
    @TableField("location")
    private String location;
    @TableField("household")
    private Integer household;
    @TableField("house_people")
    private Integer housePeople;
    @TableField("risk_property")
    private Integer riskProperty;
    @TableField("permanent_population")
    private Integer permanentPopulation;
    @TableField("house")
    private Integer house;
    @TableField("inspector_name")
    private String inspectorName;
    @TableField("inspector_tele")
    private String inspectorTele;


}
