package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.entity.XianApiDfPlatformOdsSwjJcsjQxjkDzld;
import com.ruoyi.system.service.IXianApiDfPlatformOdsSwjJcsjQxjkDzldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 气象监控电子雷达Controller
 *
 * @author ckw
 * @date 2025-01-25
 */
@RestController
@RequestMapping("/admins/radar")
public class XianApiDfPlatformOdsSwjJcsjQxjkDzldController{

    @Resource
    private IXianApiDfPlatformOdsSwjJcsjQxjkDzldService xianApiDfPlatformOdsSwjJcsjQxjkDzldService;

//    /**
//     * 查询气象监控电子雷达列表
//     */
//    @GetMapping("/list")
//    public TableDataInfo list(XianApiDfPlatformOdsSwjJcsjQxjkDzld xianApiDfPlatformOdsSwjJcsjQxjkDzld) {
//        startPage();
//        List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> list = xianApiDfPlatformOdsSwjJcsjQxjkDzldService.selectXianApiDfPlatformOdsSwjJcsjQxjkDzldList(xianApiDfPlatformOdsSwjJcsjQxjkDzld);
//        return getDataTable(list);
//    }

    /**
     * 根据时间范围查询雷达数据
     */
    @GetMapping("/timeRange")
    public AjaxResult getByTimeRange(
            @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> list = xianApiDfPlatformOdsSwjJcsjQxjkDzldService.selectByTimeRange(startTime, endTime);
        return AjaxResult.success(list);
    }

    /**
     * 根据雷达编号查询数据
     */
    @GetMapping("/radarnum/{radarnum}")
    public AjaxResult getByRadarnum(@PathVariable String radarnum) {
        List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> list = xianApiDfPlatformOdsSwjJcsjQxjkDzldService.selectByRadarnum(radarnum);
        return AjaxResult.success(list);
    }

    /**
     * 获取最新的雷达数据
     */
    @GetMapping("/latest")
    public AjaxResult getLatestData(@RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> list = xianApiDfPlatformOdsSwjJcsjQxjkDzldService.selectLatestData(limit);
        return AjaxResult.success(list);
    }

    /**
     * 导出气象监控电子雷达列表
     */
    @Log(title = "气象监控电子雷达", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XianApiDfPlatformOdsSwjJcsjQxjkDzld xianApiDfPlatformOdsSwjJcsjQxjkDzld) {
        List<XianApiDfPlatformOdsSwjJcsjQxjkDzld> list = xianApiDfPlatformOdsSwjJcsjQxjkDzldService.selectXianApiDfPlatformOdsSwjJcsjQxjkDzldList(xianApiDfPlatformOdsSwjJcsjQxjkDzld);
        ExcelUtil<XianApiDfPlatformOdsSwjJcsjQxjkDzld> util = new ExcelUtil<XianApiDfPlatformOdsSwjJcsjQxjkDzld>(XianApiDfPlatformOdsSwjJcsjQxjkDzld.class);
        util.exportExcel(response, list, "气象监控电子雷达数据");
    }

//    /**
//     * 新增气象监控电子雷达
//     */
//    // @PreAuthorize("@ss.hasPermi('system:radar:add')")
//    @Log(title = "气象监控电子雷达", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody XianApiDfPlatformOdsSwjJcsjQxjkDzld xianApiDfPlatformOdsSwjJcsjQxjkDzld) {
//        return toAjax(xianApiDfPlatformOdsSwjJcsjQxjkDzldService.insertXianApiDfPlatformOdsSwjJcsjQxjkDzld(xianApiDfPlatformOdsSwjJcsjQxjkDzld));
//    }

//    /**
//     * 修改气象监控电子雷达
//     */
//    // @PreAuthorize("@ss.hasPermi('system:radar:edit')")
//    @Log(title = "气象监控电子雷达", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody XianApiDfPlatformOdsSwjJcsjQxjkDzld xianApiDfPlatformOdsSwjJcsjQxjkDzld) {
//        return toAjax(xianApiDfPlatformOdsSwjJcsjQxjkDzldService.updateXianApiDfPlatformOdsSwjJcsjQxjkDzld(xianApiDfPlatformOdsSwjJcsjQxjkDzld));
//    }

//    /**
//     * 删除气象监控电子雷达
//     */
//    @Log(title = "气象监控电子雷达", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable String[] ids) {
//        return toAjax(xianApiDfPlatformOdsSwjJcsjQxjkDzldService.deleteXianApiDfPlatformOdsSwjJcsjQxjkDzldByIds(ids));
//    }
}
