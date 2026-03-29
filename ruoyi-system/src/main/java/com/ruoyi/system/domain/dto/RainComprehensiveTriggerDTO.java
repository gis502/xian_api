package com.ruoyi.system.domain.dto;

import com.ruoyi.system.domain.vo.FactorVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RainComprehensiveTriggerDTO {

    // 基础信息（用于保存暴雨灾害记录）
    private String rainfall;           // 降雨量（逗号分隔多个区县）
    private String duration;           // 持续时间（默认 12 小时）
    private Double longitude;          // 经度（主灾区）
    private Double latitude;           // 纬度（主灾区）
    private String position;           // 位置/区县（逗号分隔多个区县）
    private String rainType;           // 暴雨类型（Z-正式/T-测试）
    private String disasterName;       // 灾害名称
    private LocalDateTime occurrenceTime; // 发生时间

    // 模型计算所需数据（用于隐患点匹配和风险计算）
    private List<RainModelData> modelDataList;

    /**
     * 内部类：模型计算所需的数据项
     */
    @Data
    public static class RainModelData {
        private String entityId;       // 实体 ID
        private String disasterType;   // 灾害类型
        private Double lon;            // 经度
        private Double lat;            // 纬度
        private List<FactorVO> factors; // 致灾因子列表
    }
}
