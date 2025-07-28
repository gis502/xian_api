package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_disaster_real")


public class XianDisasterReal {
  @TableField("disaster_id")
  private long disasterId;
  @TableField("disaster_trigger")
  private String disasterTrigger;
  @TableId
  @TableField("id")
  private long id;
  @TableField("geom")
  private String geom;
  @TableField("disaster_type")
  private String disasterType;
  @TableField("occurrence_time")
  private java.sql.Timestamp occurrenceTime;
  @TableField("end_time")
  private java.sql.Timestamp endTime;
  @TableField("state")
  private String state;
  @TableField("disaster_name")
  private String disasterName;
  @TableField("people_injure")
  private String peopleInjure;


}
