package com.ruoyi.web.controller.system;


import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.service.IXianDisasterRainService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Validated
@RestController
@RequestMapping("/XianDisasterRain")

public class XianDisasterRainController {

    @Resource
    private IXianDisasterRainService xianDisasterRainService;

    @GetMapping("/getAllDisasterRain")
    public List<XianDisasterRain> selectAllEq() {
        return xianDisasterRainService.selectAllEq();
    }
}
