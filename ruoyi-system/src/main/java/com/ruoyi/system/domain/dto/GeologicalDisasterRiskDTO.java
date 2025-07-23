package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:00
 * @description: 风险区
 */


@Data
public class GeologicalDisasterRiskDTO {


    private String disasterName;

    private String unitCode;

    private String riskLevel;

    private Double area;

    private String position;

    private Integer residentCounts;

    private Integer addressPopulation;

    private Integer riskProperty;

    private Integer permanentPopulation;

    private Integer housing;

    private String inspectorName;

    private String inspectorTele;

    private Double lon;

    private Double lat;


}
