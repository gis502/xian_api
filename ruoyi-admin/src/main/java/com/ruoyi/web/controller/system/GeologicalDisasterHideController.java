package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IGeologicalDisasterHideService;
import io.swagger.annotations.Api;
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
public class GeologicalDisasterHideController {

    @Resource
    private IGeologicalDisasterHideService geologicalDisasterHideService;


    @GetMapping("/hide/disaster")
    public AjaxResult getGeologicalDisasterHideList()
    {
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterHideList());
    }





}
