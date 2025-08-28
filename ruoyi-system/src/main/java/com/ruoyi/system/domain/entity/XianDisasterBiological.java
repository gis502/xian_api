package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
    * 重大生物灾害信息表
    */
@Data
@TableName(value = "xian_disaster_biological")
public class XianDisasterBiological {
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
     * 重大生物灾害名称
     */
    @TableField(value = "disaster_name")
    private String disasterName;

    /**
     * 位置
     */
    @TableField(value = "\"position\"")
    private String position;

    /**
     * 生物灾害发生时间
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
     * 生物灾害类型（如动物疫情、植物病害等）
     */
    @TableField(value = "bio_type")
    private String bioType;

    /**
     * 受灾物种
     */
    @TableField(value = "affected_species")
    private String affectedSpecies;

    /**
     * 灾害严重等级
     */
    @TableField(value = "severity_level")
    private String severityLevel;
}
