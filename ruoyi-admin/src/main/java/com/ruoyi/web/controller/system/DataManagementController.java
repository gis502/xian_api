package com.ruoyi.web.controller.system;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.vo.DataManagementVO;
import com.ruoyi.system.service.IDataManagementService;
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

    @PostMapping("/queryTableName")
    public AjaxResult queryTableName(@RequestBody String remark) {
        return AjaxResult.success(dataManagementService.selectTable(remark));
    }

    @PostMapping("/queryTableInfo")
    public AjaxResult queryTableInfo(@RequestBody DataManagementVO dataManagementVO) {
        return AjaxResult.success(dataManagementService.queryInformation(dataManagementVO));
    }

    @DeleteMapping("/deleteTableInfo")
    public AjaxResult deleteTableInfo(@RequestBody DataManagementVO dataManagementVO) {
        boolean isSuccess = dataManagementService.deleteInformation(dataManagementVO);
        return AjaxResult.success("删除成功", isSuccess);
    }

    @PostMapping("/add")
    public AjaxResult addTableInfo(@RequestBody DataManagementVO dataManagementVO) {
        boolean isSuccess = dataManagementService.addInformation(dataManagementVO);
        return AjaxResult.success("插入成功", isSuccess);
    }
    @PostMapping("/update")
    public AjaxResult updateTableInfo(@RequestBody DataManagementVO dataManagementVO) {
        boolean isSuccess = dataManagementService.updateInformation(dataManagementVO);
        return AjaxResult.success("更新成功", isSuccess);
    }
}
