package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
    * 高温灾害信息表
    */
@Data
@TableName(value = "xian_disaster_heatwave")
public class XianDisasterHeatwave {
    /**
     * 灾害ID
     */
    @TableId(value = "disaster_id", type = IdType.INPUT)
    private Integer disasterId;

    /**
     * 空间几何信息
     */
    @TableField(value = "geom")
    private Object geom;

    /**
     * 高温灾害名称
     */
    @TableField(value = "disaster_name")
    private String disasterName;

    /**
     * 位置
     */
    @TableField(value = "\"position\"")
    private String position;

    /**
     * 高温发生时间
     */
    @TableField(value = "occurrence_time")
    private Date occurrenceTime;

    /**
     * 是否删除（0 表示未删除，1 表示已删除）
     */
    @TableField(value = "is_deleted")
    private Short isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 经度
     */
    @TableField(value = "longitude")
    private Float longitude;

    /**
     * 纬度
     */
    @TableField(value = "latitude")
    private Float latitude;

    /**
     * 类型（正式/测试）
     */
    @TableField(value = "rain_type")
    private String rainType;

    /**
     * 最高温度
     */
    @TableField(value = "max_temperature")
    private String maxTemperature;

    /**
     * 持续天数
     */
    @TableField(value = "duration_days")
    private String durationDays;
}
