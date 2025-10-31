package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterForestFireServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 森林草原火灾信息表(xian_disaster_forest_fire)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/admins/xian_disaster_forest_fire")
public class XianDisasterForestFireController {
/**
* 服务对象
*/
@Resource
private XianDisasterForestFireServiceImpl xianDisasterForestFireServiceImpl;



}
