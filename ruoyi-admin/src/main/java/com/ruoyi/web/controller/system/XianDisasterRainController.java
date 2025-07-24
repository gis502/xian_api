package com.ruoyi.web.controller.system;


import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
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
}
