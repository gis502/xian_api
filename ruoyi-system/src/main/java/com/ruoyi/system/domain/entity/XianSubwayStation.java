package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_subway_stations_have_attributes")
public class XianSubwayStation {

    @TableField("id")
    private Integer id;
    @TableField("station_name")
    private String stationName;
    @TableField("line")
    private String line;
    @TableField("refer_to_the_water_accumulation_point")
    private String referToWater;
    @TableField("depth_of_accumulated_water")
    private String depthOfWater;
    @TableField("accumulated_water_after_accounting")
    private String accumulatedWaterAfterAccounting;
    @TableField("lon")
    private Double lon;
    @TableField("lat")
    private Double lat;
}
