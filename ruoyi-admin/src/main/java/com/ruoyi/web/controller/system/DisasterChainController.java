package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.params.DisasterParam;
import com.ruoyi.system.service.IDisasterChainService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admins/disasterChain")
public class DisasterChainController {

    @Resource
    private IDisasterChainService disasterChainService;

    @GetMapping("/getRain")
    public AjaxResult getRain() {
        return AjaxResult.success(disasterChainService.getAllRainChain());
    }

    @GetMapping("/getEarthQuake")
    public AjaxResult getEarthQuake() {
        return AjaxResult.success(disasterChainService.getAllEarthquakeList());
    }

    @PostMapping("/getRainProbability")
    public AjaxResult getRainProbability(@RequestBody DisasterParam disasterParam) {
        return AjaxResult.success(disasterChainService.getRainProbabilityByType(disasterParam.getDisasterId(), disasterParam.getDisasterType()));
    }

    @PostMapping("/getEarthQuakeProbability")
    public AjaxResult getEarthQuakeProbability(@RequestBody DisasterParam disasterParam) {
        return AjaxResult.success(disasterChainService.getEarthQuakeProbabilityByType(disasterParam.getDisasterId(), disasterParam.getDisasterType()));
    }
}
