package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.DemSlopeDTO;
import com.ruoyi.system.domain.dto.ModelGetDataFactorListEntityIdDTO;
import com.ruoyi.system.domain.vo.FactorVO;
import com.ruoyi.system.domain.vo.ImpactAreaRequest;
import com.ruoyi.system.domain.vo.TriggerRequest;
import com.ruoyi.system.domain.vo.TriggerUpdate;
import com.ruoyi.system.service.IModelService;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.system.domain.dto.LatLonDTO;


import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/admins/model")
public class ModelController {

    @Resource
    private IModelService modelService;

    @PostMapping("/rainSlideTrigger")
    public AjaxResult rainSlideTrigger(@RequestBody List<List<FactorVO>> request)
    {
        return AjaxResult.success(modelService.rainSlideTrigger(request));
    }

    @PostMapping("/rainSlideFactorUpdata")
    public AjaxResult rainSlideFactorUpdata(@RequestBody List<FactorVO> request)
    {
        return AjaxResult.success(modelService.rainSlideFactorUpdata(request));
    }

    @PostMapping("/eqSlideTrigger")
    public AjaxResult eqSlideTrigger(@RequestBody List<ModelGetDataFactorListEntityIdDTO> request)
    {
        return AjaxResult.success(modelService.eqSlideTrigger(request));
    }

    @PostMapping("/eqSlideFactorUpdata")
    public AjaxResult eqSlideFactorUpdata(@RequestBody List<FactorVO> request)
    {
        return AjaxResult.success(modelService.eqSlideFactorUpdata(request));
    }

    @PostMapping("/getPoliejiao")
    public AjaxResult getPoliejiao(@RequestBody LatLonDTO request)
    {
        return AjaxResult.success(modelService.getPoliejiao(request));
    }

    @PostMapping("/affectArea")
    public AjaxResult affectArea(@RequestBody List<LatLonDTO> request)
    {
        return AjaxResult.success(modelService.getEffactArea(request));

    }

    @PostMapping("/getLandslideArea")
    public AjaxResult getLandslideArea(@RequestBody DemSlopeDTO request)
    {
        return AjaxResult.success(modelService.getLandslideArea(request));
    }


    @PostMapping("/rain/trigger")
    public AjaxResult rainTrigger(@RequestBody TriggerRequest request)
    {
        return AjaxResult.success(modelService.rainTrigger(request));
    }


    @PostMapping("/eq/trigger")
    public AjaxResult eqTrigger(@RequestBody TriggerRequest request)
    {
        return AjaxResult.success(modelService.eqTrigger(request));
    }


    @PostMapping("/factor/update")
    public AjaxResult rainFactorUpdate(@RequestBody TriggerUpdate request)
    {
        return AjaxResult.success(modelService.rainFactorUpdate(request));
    }


    @PostMapping("/impactinsert")
    public AjaxResult impactInsert(@RequestBody List<ImpactAreaRequest> request)

    {
        return AjaxResult.success(modelService.impactInsert(request));
    }



}
