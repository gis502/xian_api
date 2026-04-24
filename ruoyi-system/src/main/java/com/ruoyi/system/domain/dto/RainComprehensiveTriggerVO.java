package com.ruoyi.system.domain.dto;

import com.ruoyi.system.domain.vo.TriggerVO;
import lombok.Data;

import java.util.List;

@Data
public class RainComprehensiveTriggerVO {

    private Integer rainDisasterId;      // 暴雨灾害 ID（保存返回）
    private String rainId;            // 专题图 ID（第三方返回）
    private String rainQueueId;       // 专题图批次 ID（第三方返回）
    private String rainFullName;      // 完整的雨名
    private String content;        // 初步描述
    private String reportDownloadUrl;      // 报告下载链接
//    private List<TriggerVO> modelResults; // 模型计算结果（隐患点风险分析）
}
