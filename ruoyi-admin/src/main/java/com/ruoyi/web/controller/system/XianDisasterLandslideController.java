package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterLandslideServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 滑坡灾害信息表(xian_disaster_landslide)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/admins/xian_disaster_landslide")
public class XianDisasterLandslideController {
/**
* 服务对象
*/
@Resource
private XianDisasterLandslideServiceImpl xianDisasterLandslideServiceImpl;



}
