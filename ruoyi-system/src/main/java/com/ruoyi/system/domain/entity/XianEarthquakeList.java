package com.ruoyi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;
import org.n52.jackson.datatype.jts.GeometryDeserializer;
import org.n52.jackson.datatype.jts.GeometrySerializer;

import java.time.LocalDateTime;

@Data
@TableName("xian_earthquake_list")
public class XianEarthquakeList {
  @TableId
  @TableField("disaster_id")
  private Long disasterId;
  @TableField("disaster_name")
  private String disasterName;
  @TableField("earthquake_full_name")
  private String earthquakeFullName;
  @TableField("position")
  private String position;
  @TableField("geom")
  private String geom;
  @TableField("latitude")
  private Double latitude;
  @TableField("longitude")
  private Double longitude;
  @TableField("intensity")
  private String intensity;
  @TableField("depth")
  private String depth;
  @TableField("occurrence_time")
  private LocalDateTime occurrenceTime;
  @TableField("eq_type")
  private String eqType;
  @TableField("source")
  private String source;
  @TableField("eq_addr_code")
  private String eqAddrCode;
  @TableField("town_code")
  private String townCode;

  @TableField("is_deleted")
  private long isDeleted;
  @TableField("magnitude")
  private String magnitude;
  @TableField("create_time")
  private LocalDateTime createTime;
  @TableField("update_time")
  private LocalDateTime updateTime;
  @TableField("district")
  private String district;
  @TableField("province")
  private String province;
  @TableField("city")
  private String city;

}
