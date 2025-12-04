package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IGeologicalDisasterHideService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 19:15
 * @description: 风险区控制类
 */


@RestController
@RequestMapping("/admins/hide")
public class GeologicalDisasterHideController {

    @Resource
    private IGeologicalDisasterHideService geologicalDisasterHideService;

    @GetMapping("/slide")
    public AjaxResult getGeologicalDisasterHideByLandSlideList()
    {
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterHideByLandSlideList());
    }

    @GetMapping("/flow")
    public AjaxResult getGeologicalDisasterHideByFlowList()
    {
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterHideByFlowList());
    }

    @GetMapping("/allslide")
    public AjaxResult getAllGeologicalDisasterHideByLandSlideList()
    {
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalAllDisasterHideByLandSlideList());
    }

    @GetMapping("/allflow")
    public AjaxResult getAllGeologicalDisasterHideByFlowList()
    {
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalAllDisasterHideByFlowList());
    }

    @GetMapping("/getSlide")
    public AjaxResult getGeologicalDisasterAllSlideList(){
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterBySlideList());
    }

    @GetMapping("/getFlow")
    public AjaxResult getGeologicalDisasterAllFlowList(){
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterByFlowList());
    }

    @GetMapping("/getFlashFlood")
    public AjaxResult getGeologicalDisasterAllFlashFloodList(){
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterByFlashFloodList());
    }

    @GetMapping("/getWater")
    public AjaxResult getGeologicalDisasterAllWaterList(){
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterByWaterLogging());
    }
}
