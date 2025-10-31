package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterSnowDisasterServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 雪灾信息表(xian_disaster_snow_disaster)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/admins/xian_disaster_snow_disaster")
public class XianDisasterSnowDisasterController {
/**
* 服务对象
*/
@Resource
private XianDisasterSnowDisasterServiceImpl xianDisasterSnowDisasterServiceImpl;



}
