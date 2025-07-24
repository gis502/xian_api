package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IGeologicalDisasterHideService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 19:15
 * @description: 风险区控制类
 */


@RestController
@RequestMapping("/hide")
public class GeologicalDisasterHideController {

    @Resource
    private IGeologicalDisasterHideService geologicalDisasterHideService;

    @GetMapping("/slide")
    @ApiOperation(value = "获取滑坡隐患点数据")
    public AjaxResult getGeologicalDisasterHideByLandSlideList()
    {
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterHideByLandSlideList());
    }

    @GetMapping("/flow")
    @ApiOperation(value = "获取泥石流隐患点数据")
    public AjaxResult getGeologicalDisasterHideByFlowList()
    {
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterHideByFlowList());
    }



}
