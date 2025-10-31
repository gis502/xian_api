package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterHeatwaveServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 高温灾害信息表(xian_disaster_heatwave)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/admins/xian_disaster_heatwave")
public class XianDisasterHeatwaveController {
/**
* 服务对象
*/
@Resource
private XianDisasterHeatwaveServiceImpl xianDisasterHeatwaveServiceImpl;



}
