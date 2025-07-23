package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:00
 * @description: 风险区
 */


@Data
public class XianDisaterRainDTO {
    private String disasterName;
    private java.sql.Timestamp occurTime;
    private String rainfall;
    private String duration;
    private String position;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp updateTime;
    private String isDeleted;
    private String geom;
    private long disasterId;

}
