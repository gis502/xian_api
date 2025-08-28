package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
    * 风雹灾害信息表
    */
@Data
@TableName(value = "xian_disaster_gale_hail")
public class XianDisasterGaleHail {
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
     * 风雹灾害名称
     */
    @TableField(value = "disaster_name")
    private String disasterName;

    /**
     * 位置
     */
    @TableField(value = "\"position\"")
    private String position;

    /**
     * 风雹灾害发生时间
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
    @TableField(value = "type")
    private String type;


    /**
     * 风速
     */
    @TableField(value = "wind_speed")
    private String windSpeed;

    /**
     * 冰雹直径
     */
    @TableField(value = "hail_diameter")
    private String hailDiameter;

    /**
     * 灾害等级
     */
    @TableField(value = "damage_level")
    private String damageLevel;
}
