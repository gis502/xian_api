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
     * 图片
     */
    @TableField(value = "image")
    private String image;

    /**
     * 来源（网站中文名）
     */
    @TableField(value = "source_name")
    private String sourceName;

    /**
     * 发布人姓名
     */
    @TableField(value = "publish_name")
    private String publishName;

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
    @TableField(value = "disaster_id")
    private Integer disasterId;
}