package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-01-27
 * @description: 西安影响区域实体类
 */

@Data
@TableName("xian_impact_in_area")
public class XianImpactInAreaEntity {

    @TableId
    private Integer id;
    
    @TableField("disaster_id")
    private Integer disasterId;
    
    @TableField("second_disaster_id")
    private Integer secondDisasterId;
    
    @TableField("people")
    private String people;
    
    @TableField("national_road")
    private String nationalRoad;
    
    @TableField("heighway")
    private String heighway;
    
    @TableField("street")
    private String street;
    
    @TableField("dangerous_point")
    private String dangerousPoint;
    
    @TableField("station")
    private String station;
    
    @TableField("dangerous_point_pos")
    private String dangerousPointPos;
    
    @TableField("station_pos")
    private String stationPos;
    
    @TableField("district")
    private String district;
}