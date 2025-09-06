package com.ruoyi.system.domain.entity;

import com.ruoyi.common.enums.TypesOfSecondaryDisasters;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 暴雨分析报告实体类
 */
@Data
public class RainReportEntity {
    /*
     * 表头时间
     */
    private String reportTime;                              // 报告时间

    /*
     * 降雨概述
     */
    private String rainTime;                                // 降雨时间
    private List<String> rainAreaPosition;                  // 降雨区域
    private List<String> rainAreaQuantity;                  // 区域降雨量
    private String concentratedAreaPosition;                // 降雨集中区域
    private String concentratedAreaQuantity;                // 降雨集中区域雨量
    private String concentratedAreaAverageQuantity;         // 降雨集中区域平均雨量
    private List<String> concentratedAreaDetailStreet;      // 降雨集中区域街道
    private List<String> concentratedAreaDetailQuantity;    // 降雨集中街道雨量
    private List<String> concentratedAreaDetailGrade;       // 降雨集中街道等级
    private List<String> extremelyHeavyRainstormStreet;     // 特大暴雨监测街道
    private List<String> extremelyHeavyRainQuantity;        // 特大暴雨监测雨量
    private List<String> rainstormStreet;                   // 暴雨或大暴雨街道
    private List<String> rainstormQuantity;                 // 暴雨或大暴雨雨量

    /*
     * 风险评估
     */
    private List<String> riskArea;                          // 风险地区
    private Integer riskAreaQuantity;                       // 风险区数量
    private List<String> hideArea;                          // 隐患地区
    private Integer hideAreaQuantity;                       // 隐患点数量
    private List<String> hazards;                           // 致灾因子
    private String disasterChain;                           // 灾害链
    private String significantIncreaseArea;                 // 风险显著上升区域
    private List<String> significantIncreaseAreaStreet;     // 显著上升街道
    private Integer significantIncreaseAreaRiskQuantity;    // 显著上升区域中重点关注风险点
    private Integer significantIncreaseAreaHideQuantity;    // 显著上升区域中重点关注隐患点
    // 次生灾害
    private List<SecondaryDisasterReportEntity> secondaryDisasterReport;

    /*
     * 应急处置建议
     */
    private List<String> workScheduleArea;                  // 工作安排部署区域
    private List<String> evacuateTheCrowdArea;              // 山洪、泥石流人员疏散区域
    private List<String> focusArea;                         // 内涝关注区域

    /**
     * 次生灾害风险报告
     */
    @Data
    public class SecondaryDisasterReportEntity {
        private TypesOfSecondaryDisasters disasterType;     // 灾害类型（滑坡、泥石流等）
        private String riskStreet;                          // 风险集中的街道
        private String riskPointName;                       // 风险集中点位
        private String riskPointProbability;                // 风险集中点位概率
        private Long influencePeopleQuantity;               // 影响人数
        private List<String> seriousArea;                   // 中大型区域

        // 表格数据
        private List<SecondaryDisasterTableData> disasterTableData;

        private String extraLargeArea;                      // 特大型区域地址
        private String extraLargeAreaPoint;                 // 特大型风险区或隐患区
        private Integer extraLargeAreaRiskQuantity;         // 特大型风险区数量
        private Integer extraLargeAreaRiskPeopleQuantity;   // 特大型风险区影响人数
        private String smallArea;                           // 中小大型区域地址
        private String smallAreaPoint;                      // 中小型风险区或隐患区
        private Integer smallAreaRiskPeopleQuantity;        // 中小型风险区影响人数

        /**
         * 次生灾害表数据
         */
        @Data
        public class SecondaryDisasterTableData {
            private String position;                        // 位置
            private String probability;                     // 概率
            private String grade;                           // 等级
        }

        /**
         * 构造器初始化数据
         */
        public SecondaryDisasterReportEntity() {
            this.seriousArea = new ArrayList<>();
            this.disasterTableData = new ArrayList<>();
        }
    }

    /**
     * 构造器初始化数据
     */
    public RainReportEntity() {
        this.rainAreaPosition = new ArrayList<>();
        this.rainAreaQuantity = new ArrayList<>();
        this.concentratedAreaDetailStreet = new ArrayList<>();
        this.extremelyHeavyRainstormStreet = new ArrayList<>();
        this.extremelyHeavyRainQuantity = new ArrayList<>();
        this.rainstormStreet = new ArrayList<>();
        this.rainstormQuantity = new ArrayList<>();
        this.riskArea = new ArrayList<>();
        this.hideArea = new ArrayList<>();
        this.hazards = new ArrayList<>();
        this.significantIncreaseAreaStreet = new ArrayList<>();
        this.secondaryDisasterReport = new ArrayList<>();
        this.workScheduleArea = new ArrayList<>();
        this.evacuateTheCrowdArea = new ArrayList<>();
        this.focusArea = new ArrayList<>();
    }
}
