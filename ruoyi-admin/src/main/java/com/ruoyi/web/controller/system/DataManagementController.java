package com.ruoyi.web.controller.system;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.vo.DataManagementVO;
import com.ruoyi.system.service.IDataManagementService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author: zzw
 * @date: 2025-09-09 20:18
 * @description: 数据库管理
 */

@Slf4j
@RestController
@RequestMapping("/admins/data_management")
public class DataManagementController {

    @Resource
    private IDataManagementService dataManagementService;

    @ApiOperation(value = "获取数据库表")
    @PostMapping("/queryTableName")
    public AjaxResult queryTableName(@RequestBody String remark) {
        return AjaxResult.success(dataManagementService.selectTable(remark));
    }

    @ApiOperation(value = "获取表信息")
    @PostMapping("/queryTableInfo")
    public AjaxResult queryTableInfo(@RequestBody DataManagementVO dataManagementVO) {
        return AjaxResult.success(dataManagementService.queryInformation(dataManagementVO));
    }

    @ApiOperation(value = "删除表信息")
    @DeleteMapping("/deleteTableInfo")
    public AjaxResult deleteTableInfo(@RequestBody DataManagementVO dataManagementVO) {
        return AjaxResult.success(dataManagementService.deleteInformation(dataManagementVO));
    }
}
