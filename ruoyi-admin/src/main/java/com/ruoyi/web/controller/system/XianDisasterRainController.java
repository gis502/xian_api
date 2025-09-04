package com.ruoyi.web.controller.system;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.DisasterRainDTO;
import com.ruoyi.system.domain.entity.XianDisasterRain;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.mapper.XianRainfallDuringPeriodMapper;
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

    @GetMapping("/getDisasterRainByKey")
    public List<XianDisasterRain> getDisasterRainByKey(@RequestParam(value = "queryValue", required = false) String queryValue) {
        System.out.println(queryValue);
        LambdaQueryWrapper<XianDisasterRain> queryWrapper = new LambdaQueryWrapper<>();
        // 全局条件
        queryWrapper.eq(XianDisasterRain::getIsDeleted, 0);

        // 模糊搜索条件封装到嵌套语句中，避免 or 冲掉 eq 条件
        if (StringUtils.isNotBlank(queryValue)) {
            queryWrapper.and(w -> w
                    .like(XianDisasterRain::getDisasterName, queryValue)
                    .or().like(XianDisasterRain::getPosition, queryValue)
                    .or().apply("ST_AsText(geom) LIKE {0}", "%" + queryValue + "%")
                    .or().apply("to_char(occurrence_time, 'YYYY-MM-DD HH24:MI:SS') LIKE {0}", "%" + queryValue + "%")
            );
        }

        queryWrapper.orderByDesc(XianDisasterRain::getOccurrenceTime);
        return disasterRainService.list(queryWrapper);
    }

    @PostMapping("/saver/rain")
    @ApiOperation(value = "保存暴雨灾害信息")
    public AjaxResult saveRain(@RequestBody DisasterRainDTO disasterRainDTO) {
        disasterRainService.saveDisasterRain(disasterRainDTO);
        return AjaxResult.success("保存成功");
    }


}
