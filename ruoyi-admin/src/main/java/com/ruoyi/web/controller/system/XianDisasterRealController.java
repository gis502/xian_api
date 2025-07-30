package com.ruoyi.web.controller.system;


import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.domain.entity.XianDisasterReal;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.mapper.XianDisasterRealMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Validated
@RestController
@RequestMapping("/XianDisasterReal")

public class XianDisasterRealController {

    @Resource
    private XianDisasterRealMapper xianDisasterRealMapper;

    @PostMapping("/selectDisasterRealByDisasterId")
    public List<XianDisasterReal> selectDisasterRealByDisasterId(@RequestParam(value = "disasterId") String disasterId ,@RequestParam(value = "disasterTrigger") String disasterTrigger) {

        return xianDisasterRealMapper.selectDisasterRealByDisasterId(disasterId,disasterTrigger);
    }

}
