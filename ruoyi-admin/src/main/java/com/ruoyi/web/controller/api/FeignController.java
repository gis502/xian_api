package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.TriggerDTO;
import com.ruoyi.system.domain.params.ThematicQuery;
import com.ruoyi.system.service.IFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author: xiaodemos
 * @date: 2025-08-26 11:38
 * @description: 第三方接口调用
 */

@RestController
@RequestMapping("/feign")
public class FeignController {


    @Resource
    private IFeignService feignService;


    @ApiOperation(value = "地震触发")
    @PostMapping("/eq/trigger")
    public AjaxResult trigger(@RequestBody TriggerDTO triggerDTO) {
        ThematicQuery triggered = feignService.trigger(triggerDTO);
        return AjaxResult.success(triggered);
    }

    @ApiOperation(value = "专题图件产出")
    @PostMapping("/thematic/map")
    public AjaxResult thematicMap(@RequestBody ThematicQuery query) {
        return AjaxResult.success(feignService.thematicMap(query));
    }

    @ApiOperation(value = "灾情报告下载")
    @CrossOrigin(origins = "*")
    @GetMapping("/download/{eqId}/{eqqueueId}")
    public void getReport(@PathVariable String eqId, @PathVariable String eqqueueId, HttpServletResponse resp) throws IOException {
        feignService.downloadReport(eqId, eqqueueId, resp);
    }
}
