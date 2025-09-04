package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 多种灾害发生后相关新闻
 */
@Data
@TableName(value = "xian_news")
public class XianNews {
    /**
     * 新闻id
     */
    @TableId(value = "new_id", type = IdType.INPUT)
    private Integer newId;

    /**
     * 新闻网址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 新闻标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 发布时间
     */
    @TableField(value = "publish_time")
    private Date publishTime;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 来源（网站中文名）
     */
    @TableField(value = "source_name")
    private String sourceName;

    /**
     * 新闻分类实体
     */
    @TableField(value = "new_entity")
    private String newEntity;

    /**
     * 地震id
     */
    @TableField(value = "earthquake_id")
    private Integer earthquakeId;

    /**
     * 暴雨id
     */
    @TableField(value = "rain_id")
    private Integer rainId;

    /**
     * 插入时间
     */
    @TableField(value = "insert_time")
    private Date insertTime;

    /**
     * 发布人姓名
     */
    @TableField(value = "publish_name")
    private String publishName;

    /**
     * 雪灾id
     */
    @TableField(value = "snow_id")
    private Integer snowId;

    /**
     * 冷害id
     */
    @TableField(value = "cold_damage_id")
    private Integer coldDamageId;

    /**
     * 坍塌id
     */
    @TableField(value = "collapse_id")
    private Integer collapseId;

    /**
     * 滑坡id
     */
    @TableField(value = "landslide_id")
    private Integer landslideId;

    /**
     * 泥石流id
     */
    @TableField(value = "debris_flow_id")
    private Integer debrisFlowId;

    /**
     * 大风冰雹id
     */
    @TableField(value = "gale_hail_id")
    private Integer galeHailId;

    /**
     * 沙尘暴id
     */
    @TableField(value = "sandstorm_id")
    private Integer sandstormId;

    /**
     * 干旱id
     */
    @TableField(value = "drought_id")
    private Integer droughtId;

    /**
     * 热浪id
     */
    @TableField(value = "heatwave_id")
    private Integer heatwaveId;

    /**
     * 野火id
     */
    @TableField(value = "wildfire_id")
    private Integer wildfireId;

    /**
     * 生物灾害id
     */
    @TableField(value = "bio_disaster_id")
    private Integer bioDisasterId;

    /**
     * 安全事故id
     */
    @TableField(value = "safety_accident_id")
    private Integer safetyAccidentId;
}

