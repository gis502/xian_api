package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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

  @TableId(type = IdType.AUTO)
  @TableField("disaster_id")
  private Long disasterId;
  @TableField("disaster_name")
  private String disasterName;
  @TableField("occurrence_time")
  private LocalDateTime occurrenceTime;
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
  @TableField(value = "geom", typeHandler = GeometryTypeHandler.class)
  @JsonSerialize(using = GeometrySerializer.class)
  @JsonDeserialize(using = GeometryDeserializer.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)  // 仅序列化非空字段
  private Geometry geom; //经纬度


}
