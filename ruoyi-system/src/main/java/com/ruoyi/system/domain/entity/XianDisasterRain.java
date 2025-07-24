package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_disaster_rain")
public class XianDisasterRain {
  @TableField("disaster_name")
  private String disasterName;
  @TableField("occurrence_time")
  private java.sql.Timestamp occurrenceTime;
  @TableField("rainfall")
  private String rainfall;
  @TableField("duration")
  private String duration;
  @TableField("position")
  private String position;
  @TableField("create_time")
  private java.sql.Timestamp createTime;
  @TableField("update_time")
  private java.sql.Timestamp updateTime;
  @TableField("is_deleted")
  private String isDeleted;
  @TableField("geom")
  private String geom;
  @TableId
  @TableField("disaster_id")
  private long disasterId;

}
