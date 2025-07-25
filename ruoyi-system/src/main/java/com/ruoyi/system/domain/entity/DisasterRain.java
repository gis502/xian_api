package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-07-23 12:55
 * @description: 暴雨事件表
 */

@Data
@TableName("xian_disaster_rain")
public class DisasterRain {

    @TableField("disaster_id")
    private Integer disasterId;//编码ID
    @TableField("disaster_name")
    private String disasterName;//暴雨灾害名称
    @TableField("occur_time")
    private LocalDateTime occurTime;//暴雨发生时间
    @TableField("rainfall")
    private String rainfall;//暴雨降雨量
    @TableField("duration")
    private String duration;//暴雨持续时间
    @TableField("position")
    private String position;//灾害发生地点
    @TableField("geom")
    private Geometry geom;//经纬度
    @TableField("create_time")
    private LocalDateTime createTime;//创建时间
    @TableField("update_time")
    private LocalDateTime updateTime;//更新时间
    @TableField("is_deleted")
    private Integer isDeleted;//逻辑删除



}
