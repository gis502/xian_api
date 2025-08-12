package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_rainfall_during_period")
public class XianRainfallDuringPeriod {
  @TableId
  @TableField("id")
  private long id;
  @TableField("disaster_id")
  private long disasterId;
  @TableField("disaster_trigger")
  private String disasterTrigger;
  @TableField("rain_period_start")
  private java.sql.Timestamp rainPeriodStart;
  @TableField("rainfall")
  private double rainfall;
  @TableField("rain_period_end")
  private java.sql.Timestamp rainPeriodEnd;
  @TableField("position")
  private String position;

}
