package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.params.DisasterParam;
import com.ruoyi.system.service.IDisasterChainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/disasterChain")
public class DisasterChainController {

    @Resource
    private IDisasterChainService disasterChainService;

    @GetMapping("/getRain")
    @ApiOperation("获取所有暴雨灾害信息")
    public AjaxResult getRain() {
        return AjaxResult.success(disasterChainService.getAllRainChain());
    }

    @GetMapping("/getEarthQuake")
    @ApiOperation("获取所有地震灾害信息")
    public AjaxResult getEarthQuake() {
        return AjaxResult.success(disasterChainService.getAllEarthquakeList());
    }

    @PostMapping("/getRainProbability")
    @ApiOperation("获取指定灾害概率")
    public AjaxResult getRainProbability(@RequestBody DisasterParam disasterParam) {
        return AjaxResult.success(disasterChainService.getRainProbabilityByType(disasterParam.getDisasterId(), disasterParam.getDisasterType()));
    }

    @PostMapping("/getEarthQuakeProbability")
    @ApiOperation("获取指定地震灾害概率")
    public AjaxResult getEarthQuakeProbability(@RequestBody DisasterParam disasterParam) {
        return AjaxResult.success(disasterChainService.getEarthQuakeProbabilityByType(disasterParam.getDisasterId(), disasterParam.getDisasterType()));
    }
}
