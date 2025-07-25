package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IGeologicalDisasterRiskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: xiaodemos
 * @date: 2025-07-24 20:03
 * @description: 风险区控制类
 */


@RestController
@RequestMapping("/risk")
public class GeologicalDisasterRiskController {

    @Resource
    private IGeologicalDisasterRiskService geologicalDisasterRiskService;

    @GetMapping("/villages")
    @ApiOperation(value = "风险区村庄数据")
    public AjaxResult getGeologicalDisasterRiskList()
    {
        return AjaxResult.success(geologicalDisasterRiskService.getGeologicalDisasterRiskList());
    }

    @GetMapping("/getAllvillages")
    @ApiOperation(value = "获取全部风险区村庄数据")
    public AjaxResult getGeologicalDisasterAllRiskList()
    {
        return AjaxResult.success(geologicalDisasterRiskService.getGeologicalAllRisk());
    }




}
