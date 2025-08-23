package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterDroughtServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 干旱灾害信息表(xian_disaster_drought)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/xian_disaster_drought")
public class XianDisasterDroughtController {
/**
* 服务对象
*/
@Resource
private XianDisasterDroughtServiceImpl xianDisasterDroughtServiceImpl;



}
