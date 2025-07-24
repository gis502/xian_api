package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IGeologicalDisasterHideService;
import com.ruoyi.system.service.IGeologicalDisasterRiskService;
import com.ruoyi.system.service.IMountainTorrentDisasterService;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping("/getRisk")
    @ApiOperation(value = "风险区村庄数据")
    public AjaxResult getGeologicalDisasterRiskList()
    {
        return AjaxResult.success(geologicalDisasterRiskService.getGeologicalDisasterRiskList());
    }

//    @GetMapping("/getHide")
//    public AjaxResult getGeologicalDisasterHideList()
//    {
//        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterHideList());
//    }

    @GetMapping("/getSlide")
    @ApiOperation(value = "获取滑坡隐患点数据")
    public AjaxResult getGeologicalDisasterHideByLandSlideList()
    {
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterHideByLandSlideList());
    }

    @GetMapping("/getFlow")
    @ApiOperation(value = "获取泥石流隐患点数据")
    public AjaxResult getGeologicalDisasterHideByFlowList()
    {
        return AjaxResult.success(geologicalDisasterHideService.getGeologicalDisasterHideByFlowList());
    }

    @GetMapping("/mountain_torrent")
    public AjaxResult getMountainTorrentDisasterList()
    {
        return AjaxResult.success(mountainTorrentDisasterService.getMountainTorrentDisasterList());
    }


}
