package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.DisasterType;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import com.ruoyi.system.mapper.XianFactorAnalysisMapper;
import com.ruoyi.system.service.IXianEarthquakeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Validated
@RestController
@RequestMapping("/XianFactorAnalysis")

public class XianFactorAnalysisController {

    @Autowired
    private XianFactorAnalysisMapper xianFactorAnalysisMapper;

    @PostMapping("/queryDisasterEstimationGetAll")
    public List queryDisasterEstimationGetAll(@RequestParam(value = "disasterId") String disasterId, @RequestParam(value = "disasterTrigger") String disasterTrigger) {
        DisasterType disasterType = DisasterType.EARTHQUAKE;
        if ("暴雨".equals(disasterTrigger)) {
            disasterType = DisasterType.RAINSTORM;
        }

        // 2. 字符串转 Long
        Long disasterIdLong = Long.valueOf(disasterId);   // 或 parseLong
//        List<Map<String, Object>> disasterEstimation = xianFactorAnalysisMapper.queryDisasterEstimationGetAll(152292L,  DisasterType.RAINSTORM);
        List<Map<String, Object>> disasterEstimation = xianFactorAnalysisMapper.queryDisasterEstimationGetAll(disasterIdLong,  disasterType);
        return disasterEstimation;
    }
}
