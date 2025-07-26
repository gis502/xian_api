package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/around")
public class AroundAnalysisController {

    @Resource
    private IDangerousSourceService dangerousSourceService;
    @Resource
    private IFireFighterService fireFighterService;
    @Resource
    private IHospitalService hospitalService;
    @Resource
    private IEmergencyShelterService emergencyShelterService;
    @Resource
    private IStorePointsService storePointsService;

    @GetMapping("/getDangerousSource")
    @ApiOperation("获取全部风险源")
    public AjaxResult getDangerousSourceList(){
        return AjaxResult.success(dangerousSourceService.getDangerousSourceList());
    }

    @GetMapping("/getFireFighter")
    @ApiOperation("获取所有消防队")
    public AjaxResult getFireFighterList(){
        return AjaxResult.success(fireFighterService.getFireFighterList());
    }

    @GetMapping("/getHospital")
    @ApiOperation("获取所有医院")
    public AjaxResult getHospitalList(){
        return AjaxResult.success(hospitalService.getHospitalList());
    }

    @GetMapping("/getEmergencyShelter")
    @ApiOperation("获取所有避难所")
    public AjaxResult getEmergencyShelterList(){
        return AjaxResult.success(emergencyShelterService.getEmergencyShelterList());
    }

    @GetMapping("/getStorePoints")
    @ApiOperation("获取所有物资储备点")
    public AjaxResult getStorePointsList(){
        return AjaxResult.success(storePointsService.getAllStorePointsList());
    }

}
