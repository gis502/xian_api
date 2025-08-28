package com.ruoyi.system.domain.entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("xian_disaster_real")
public class XianDisasterReal {

  private long disasterId;
  private String disasterTrigger;

  private String plotId;
  private String geom;
  private String plotType;
  private java.sql.Timestamp startTime;
  private java.sql.Timestamp endTime;
  private String drawtype;
  private String angle;
  private java.sql.Timestamp creationTime;
  private String isDeleted;
  private String belongProvince;
  private String belongCity;
  private String belongCounty;
  private String belongTown;
  private String locationAddress;
  private double locationAddressDistance;
  private String locationAddressPosition;
  private String loocationPoi;
  private double locationPoiDistance;
  private String locationRoad;
  private double locationRoadDistance;

}
