package com.ruoyi.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-08-26 11:20
 * @description: 地震触发DTO类
 */

@Data
@NoArgsConstructor
public class TriggerDTO {

    private String eqName;  // 地震名称
    private String eqAddr;  //震发位置
    private LocalDateTime eqTime;   // 地震时间
    private double longitude;   // 经度
    private double latitude;    // 纬度
    private double eqDepth;   // 震源深度
    private double magnitude;   // 震级
    private String eqType;  // 地震类型
    /**
     * 地震评估报告所需参数
     */
    private String faultZone; // 距离震中最近断裂带
    private String circleArea; // 重灾区面积
    private String rotation; // 椭圆旋转角度
    private String semiMajorAxis; // 椭圆长轴
    private String semiMinorAxis; // 椭圆短轴
    private String affectPop; // 影响人数
    private String diePop; // 可能死亡人数
    private String densityPop; //人口密度
    private String[] country; // 影响街道
    private String intensity; // 震区烈度
    private String sumGdp; // 灾区GDP

}
