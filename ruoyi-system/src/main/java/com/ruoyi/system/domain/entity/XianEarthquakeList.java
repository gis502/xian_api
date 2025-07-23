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

@Data
@TableName("xian_earthquake_list")
public class XianEarthquakeList {
  @TableId
  @TableField("eqid")
  private String eqid;
  @TableField("earthquake_name")
  private String earthquakeName;
  @TableField("earthquake_full_name")
  private String earthquakeFullName;
  @TableField("eq_addr")
  private String eqAddr;

  @TableField("geom")
  private String geom;
//
  @TableField("intensity")
  private String intensity;
  @TableField("depth")
  private String depth;
  @TableField("occurrence_time")
  private java.sql.Timestamp occurrenceTime;
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
  private java.sql.Date createTime;
  @TableField("update_time")
  private java.sql.Date updateTime;
  @TableField("district")
  private String district;
  @TableField("province")
  private String province;
  @TableField("city")
  private String city;

}
