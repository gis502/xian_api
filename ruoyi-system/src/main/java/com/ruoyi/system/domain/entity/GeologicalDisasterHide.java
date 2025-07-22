package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 16:49
 * @description: 地质灾害隐患点
 */

@Data
@TableName("geological_disaster_hide")
public class GeologicalDisasterHide {

    @TableId
    private Integer id;
    @TableField("field_id")
    private String fieldId;
    @TableField("province")
    private String province;
    @TableField("province_id")
    private Integer provinceId;
    @TableField("city")
    private String city;
    @TableField("city_id")
    private Integer cityId;
    @TableField("county")
    private String county;
    @TableField("county_id")
    private Integer countyId;
    @TableField("village")
    private String village;
    @TableField("disaster_name")
    private String disasterName;
    @TableField("latitude")
    private String latitude;
    @TableField("longitude")
    private String longitude;
    @TableField("lon")
    private Double lon;
    @TableField("lat")
    private Double lat;
    @TableField("location")
    private String location;
    @TableField("disaster_type")
    private String disasterType;
    @TableField("disaster_scale")
    private String disasterScale;
    @TableField("risk_level")
    private String riskLevel;


}
