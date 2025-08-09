package com.ruoyi.web.controller.system;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.DisasterRainDTO;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.service.IXianDisasterRainService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Validated
@RestController
@RequestMapping("/XianDisasterRain")

public class XianDisasterRainController {

    @Resource
    private XianDisasterRainMapper xianDisasterRainMapper;

    @Resource
    private IXianDisasterRainService disasterRainService;


    @GetMapping("/getAllDisasterRain")
    public List<XianDisasterRain> selectAllEq() {
        return xianDisasterRainMapper.selectAllEq();
    }

    @PostMapping("/getDisasterRainById")
    public XianDisasterRain getDisasterRainById(@RequestParam(value = "id") String id) {
        System.out.println(id+"getDisasterRainById id");
        System.out.println(xianDisasterRainMapper.getDisasterRainById(id)+"xianDisasterRainMapper.getDisasterRainById(id)");
        return xianDisasterRainMapper.getDisasterRainById(id);
    }

    @PostMapping("/saver/rain")
    @ApiOperation(value = "保存暴雨灾害信息")
    public AjaxResult saveRain(@RequestBody DisasterRainDTO disasterRainDTO) {
        disasterRainService.saveDisasterRain(disasterRainDTO);
        return AjaxResult.success("保存成功");
    }


}
