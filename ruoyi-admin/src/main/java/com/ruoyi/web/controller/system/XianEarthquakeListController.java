package com.ruoyi.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.EqDTO;
import com.ruoyi.system.domain.entity.XianEarthquakeList;
import com.ruoyi.system.domain.vo.EarthquakeVo;
import com.ruoyi.system.mapper.XianEarthquakeListMapper;
import com.ruoyi.system.service.IXianEarthquakeListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



@Validated
@RestController
@RequestMapping("/admins/XianEarthquakeList")

public class XianEarthquakeListController {

    private static final Logger log = LoggerFactory.getLogger(XianEarthquakeListController.class);
    @Resource
    private IXianEarthquakeListService eqListService;

    @GetMapping("/getAllEarthquakeList")
    public AjaxResult selectAllEq() {
        return AjaxResult.success(eqListService.selectAllEq()) ;
    }
    @PostMapping("/getEarthquakeEventById")
    public AjaxResult getEarthquakeEventById(@RequestParam(value = "id") Long Id) {
        return AjaxResult.success(eqListService.getEarthquakeEventById(Id));
    }
    @GetMapping("/getEarthquakeListByKey")
    public List<XianEarthquakeList> getEarthquakeListByKey(@RequestParam(value = "queryValue", required = false) String queryValue) {

        LambdaQueryWrapper<XianEarthquakeList> queryWrapper = new LambdaQueryWrapper<>();
        // 全局条件
        queryWrapper.eq(XianEarthquakeList::getIsDeleted, 0);

        // 模糊搜索条件封装到嵌套语句中，避免 or 冲掉 eq 条件
        if (StringUtils.isNotBlank(queryValue)) {
            queryWrapper.and(w -> w
                    .like(XianEarthquakeList::getDisasterName, queryValue)
                    .or().like(XianEarthquakeList::getEarthquakeFullName, queryValue)
                    .or().like(XianEarthquakeList::getPosition, queryValue)
                    .or().apply("ST_AsText(geom) LIKE {0}", "%" + queryValue + "%")
                    .or().apply("to_char(occurrence_time, 'YYYY-MM-DD HH24:MI:SS') LIKE {0}", "%" + queryValue + "%")
            );
        }

        queryWrapper.orderByDesc(XianEarthquakeList::getOccurrenceTime);
        return eqListService.list(queryWrapper);
    }

    @PostMapping("/disaster/add")
    public AjaxResult disasterAdd(@RequestBody EarthquakeVo earthquake) {
        return AjaxResult.success(eqListService.insertDisaster(earthquake));
    }

    @PostMapping("/earthquake/add")
    public AjaxResult earthquakeAdd(@RequestBody EarthquakeVo earthquake) {
        return AjaxResult.success(eqListService.insertEarthquake(earthquake));
    }

    @PostMapping("/allAffectPoints/get")
    public AjaxResult getAllAffectPoints(@RequestBody EarthquakeVo earthquake) {
        return AjaxResult.success(eqListService.selectAffectPoints(earthquake));
    }
}
