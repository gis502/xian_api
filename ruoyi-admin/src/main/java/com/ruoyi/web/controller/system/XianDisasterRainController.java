package com.ruoyi.web.controller.system;


import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.DisasterRainDTO;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.mapper.XianRainfallDuringPeriodMapper;
import com.ruoyi.system.service.IXianDisasterRainService;
import io.swagger.annotations.ApiOperation;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Validated
@RestController
@RequestMapping("/XianDisasterRain")

public class XianDisasterRainController {

    @Resource
    private IXianDisasterRainService disasterRainService;
    @Resource
    private XianRainfallDuringPeriodMapper xianRainfallDuringPeriodMapper;


    @GetMapping("/getAllDisasterRain")
    @ApiOperation(value = "获取所有暴雨灾害事件")
    public AjaxResult selectAllEq() {
        return AjaxResult.success(disasterRainService.selectAllEq());
    }

    @PostMapping("/getDisasterRainById")
    @ApiOperation(value = "根据Id获取暴雨灾害事件")
    public AjaxResult getDisasterRainById(@RequestParam(value = "id") Long Id) {
        return AjaxResult.success(disasterRainService.getDisasterRainById(Id));
    }

    @PostMapping("/getRainPeriodInfoByDisasterId")
    @ApiOperation(value = "实际降雨")
    public AjaxResult getRainPeriodInfoByDisasterId(@RequestParam(value = "id") String Id) {
        System.out.println(Id+"getRainPeriodInfo Id");
        System.out.println(xianRainfallDuringPeriodMapper.getRainPeriodInfoByDisasterId(Id)+"xianRainfallDuringPeriodMapper.getRainPeriodInfoByDisasterId(Id)");
        return AjaxResult.success(xianRainfallDuringPeriodMapper.getRainPeriodInfoByDisasterId(Id));
    }

    @PostMapping("/saver/rain")
    @ApiOperation(value = "保存暴雨灾害信息")
    public AjaxResult saveRain(@RequestBody DisasterRainDTO disasterRainDTO) {
        Long data = disasterRainService.saveDisasterRain(disasterRainDTO);
        System.out.println("1mdmdmdmdm"+data);
        Map<String,Long> rainDisasterId = new HashMap<>();
        rainDisasterId.put("rainDisasterId",data);
        System.out.println("data123"+JSON.toJSONString(rainDisasterId));
        return AjaxResult.success(rainDisasterId);
    }

    @GetMapping("/getRainFallHours")
    public AjaxResult getRainFallHours() {
//        disasterRainService
        return AjaxResult.success();
    }
}
