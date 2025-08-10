package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ruoyi.system.handler.GeometryTypeHandler;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;
import org.n52.jackson.datatype.jts.GeometryDeserializer;
import org.n52.jackson.datatype.jts.GeometrySerializer;

import java.time.LocalDateTime;

@Data
@TableName("xian_disaster_rain")
public class XianDisasterRain {

  @TableField("disaster_id")
  private Long disasterId;
  @TableField("disaster_name")
  private String disasterName;
  @TableField("occurrence_time")
  private LocalDateTime occurrenceTime;
  @TableField(value = "geom", typeHandler = GeometryTypeHandler.class)
  private Geometry geom; //经纬度
  @TableField("longitude")
  private Double longitude;
  @TableField("latitude")
  private Double latitude;
  @TableField("rainfall")
  private String rainfall;
  @TableField("duration")
  private String duration;
  @TableField("position")
  private String position;
  @TableField("create_time")
  private LocalDateTime createTime;
  @TableField("update_time")
  private LocalDateTime updateTime;
  @TableField("is_deleted")
  private Integer isDeleted;

}
