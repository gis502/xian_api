package com.ruoyi.web.controller.system;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.params.DisasterParam;
import com.ruoyi.system.service.IHistoricalSimilarityAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 历史分析页面暴雨部分
 */
@Slf4j
@RestController
@RequestMapping("/HistoricalSimilarityAnalysis")
public class HistoricalSimilarityAnalysisController {

    @Resource
    private IHistoricalSimilarityAnalysisService historicalSimilarityAnalysisService;

    @PostMapping("/rainAffect/get")
    public AjaxResult getRainAffect(@RequestBody DisasterParam disasterParam) {
        return AjaxResult.success(historicalSimilarityAnalysisService.getRainAffectPoints(disasterParam));
    }
}
