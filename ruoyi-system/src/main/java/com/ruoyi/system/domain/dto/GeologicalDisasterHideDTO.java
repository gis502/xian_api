package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 17:47
 * @description: 风险隐患点DTO
 */

@Data
public class GeologicalDisasterHideDTO {

    // 野外编号
    private String fieldCode;
    // 县
    private String county;
    // 县编号
    private Integer countyId;
    // 乡镇
    private String village;
    // 灾害点名称
    private String disasterName;
    // 纬度
    private String latitude;
    // 经度
    private String longitude;
    // 纬度
    private Double lon;
    // 经度
    private Double lat;
    // 位置
    private String position;
    private String disasterType;
    private String scaleGrade;
    private String riskGrade;


}
