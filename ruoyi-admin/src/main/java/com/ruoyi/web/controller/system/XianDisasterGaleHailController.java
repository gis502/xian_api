package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterGaleHailServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 风雹灾害信息表(xian_disaster_gale_hail)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/admins/xian_disaster_gale_hail")
public class XianDisasterGaleHailController {
/**
* 服务对象
*/
@Resource
private XianDisasterGaleHailServiceImpl xianDisasterGaleHailServiceImpl;



}
