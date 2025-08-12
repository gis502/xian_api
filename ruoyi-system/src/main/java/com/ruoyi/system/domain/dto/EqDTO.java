package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-08-10 17:38
 * @description: 地震DTO
 */

@Data
public class EqDTO {

    private Long disasterId;
    private String disasterName;
    private String earthquakeFullName;
    private String position;
    private String geom;
    private Double latitude;
    private Double longitude;
    private String intensity;
    private String depth;
    private LocalDateTime occurrenceTime;
    private String eqType;
    private String source;
    private String eqAddrCode;
    private String townCode;
    private String magnitude;
    private String district;
    private String province;
    private String city;

}
