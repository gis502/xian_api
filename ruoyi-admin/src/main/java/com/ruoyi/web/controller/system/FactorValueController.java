package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IFactorValueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: xiaodemos
 * @date: 2025-07-26 11:38
 * @description: 因子值控制层
 */

@RestController
@RequestMapping("/admins/factor")
public class FactorValueController {


    @Resource
    private IFactorValueService factorValueService;

    @GetMapping("/type")
    public AjaxResult getFactorValueList() {
        return AjaxResult.success(factorValueService.getFactorValueList());
    }



}
