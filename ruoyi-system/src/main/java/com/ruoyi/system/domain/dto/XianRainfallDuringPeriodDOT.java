package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class XianRainfallDuringPeriodDOT {
  private long id;
  private long disasterId;
  private String disasterTrigger;
  private java.sql.Timestamp rainPeriodStart;
  private double rainfall;
  private java.sql.Timestamp rainPeriodEnd;
  private String position;

}
