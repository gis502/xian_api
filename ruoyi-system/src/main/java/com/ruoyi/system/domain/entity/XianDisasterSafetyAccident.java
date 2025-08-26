package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
    * 安全生产类事故灾难信息表
    */
@Data
@TableName(value = "xian_disaster_safety_accident")
public class XianDisasterSafetyAccident {
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
     * 安全生产事故名称
     */
    @TableField(value = "disaster_name")
    private String disasterName;

    /**
     * 位置
     */
    @TableField(value = "\"position\"")
    private String position;

    /**
     * 事故发生时间
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
     * 事故类型（矿山、工厂、交通运输等）
     */
    @TableField(value = "accident_type")
    private String accidentType;

    /**
     * 死亡人数
     */
    @TableField(value = "death_toll")
    private Integer deathToll;

    /**
     * 受伤人数
     */
    @TableField(value = "injured_count")
    private Integer injuredCount;

    /**
     * 经济损失（万元）
     */
    @TableField(value = "economic_loss")
    private BigDecimal economicLoss;
}
