package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.ModelGetDataFactorListEntityIdDTO;
import com.ruoyi.system.domain.vo.FactorVO;
import com.ruoyi.system.service.IGeologicalDisasterHideService;
import com.ruoyi.system.service.IModelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/model")
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
//
//    @PostMapping("/rainSlideFactorUpdata")
//    public AjaxResult rainSlideFactorUpdata(@RequestBody List<FactorVO> request)
//    {
//        return AjaxResult.success(modelService.rainSlideFactorUpdata(request));
//    }

}
