package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IGeologicalDisasterHideService;
import com.ruoyi.system.service.IGeologicalDisasterRiskService;
import com.ruoyi.system.service.IMountainTorrentDisasterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/around_analysis")
public class AroundAnalysisController {

    @Resource
    private IGeologicalDisasterRiskService geologicalDisasterRiskService;
    @Resource
    private IGeologicalDisasterHideService geologicalDisasterHideService;
    @Resource
    private IMountainTorrentDisasterService mountainTorrentDisasterService;

    @GetMapping("/risk_disaster")
    public AjaxResult getGeologicalDisasterRiskList()
    {
        return AjaxResult.success(geologicalDisasterRiskService.getGeologicalDisasterRiskList());
    }

    @GetMapping("/hide_disaster")
    public AjaxResult getGeologicalDisasterHideList()
    {
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterHideList());
    }

    @GetMapping("/mountain_torrent")
    public AjaxResult getMountainTorrentDisasterList()
    {
        return AjaxResult.success(mountainTorrentDisasterService.getMountainTorrentDisasterList());
    }


}
