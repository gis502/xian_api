package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.RainTriggerDTO;
import com.ruoyi.system.domain.dto.ReportDTO;
import com.ruoyi.system.domain.dto.TriggerDTO;
import com.ruoyi.system.domain.params.RainQuery;
import com.ruoyi.system.domain.params.ThematicQuery;
import com.ruoyi.system.service.IFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: xiaodemos
 * @date: 2025-08-26 11:38
 * @description: 第三方接口调用
 */
@Slf4j
@RestController
@RequestMapping("/admins/feign")
public class FeignController {

    @Resource
    private IFeignService feignService;

    @ApiOperation(value = "地震触发")
    @PostMapping("/eq/trigger")
    public AjaxResult trigger(@RequestBody TriggerDTO triggerDTO) {
        log.info("触发参数：{}", triggerDTO);
        return AjaxResult.success(feignService.trigger(triggerDTO));
    }

    @ApiOperation(value = "地震专题图件产出")
    @PostMapping("/thematic/map")
    public AjaxResult thematicMap(@RequestBody ThematicQuery query) {
        return AjaxResult.success(feignService.thematicMap(query));
    }

    @ApiOperation(value = "地震灾情报告产出")
    @GetMapping("/disaster/report")
    public AjaxResult disasterReport(@RequestBody ThematicQuery query) {
        return AjaxResult.success(feignService.disasterReport(query));
    }

    @ApiOperation(value = "暴雨触发")
    @PostMapping("/rain/trigger")
    public AjaxResult trigger(@RequestBody RainTriggerDTO triggerDTO) {
        return AjaxResult.success(feignService.trigger(triggerDTO));
    }

    @ApiOperation(value = "暴雨专题图件产出")
    @PostMapping("/rain/map")
    public AjaxResult thematicMap(@RequestBody RainQuery query) {
        return AjaxResult.success(feignService.thematicMap(query));
    }

    @ApiOperation(value = "灾情报告下载")
    @PostMapping("/download")
    public String getReport(@RequestBody ReportDTO reportDTO){
        return feignService.downloadReport(reportDTO);
    }

}
