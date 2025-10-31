package com.ruoyi.web.controller.system;
import com.ruoyi.system.service.impl.XianDisasterBiologicalServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 重大生物灾害信息表(xian_disaster_biological)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/admins/xian_disaster_biological")
public class XianDisasterBiologicalController {
/**
* 服务对象
*/
@Resource
private XianDisasterBiologicalServiceImpl xianDisasterBiologicalServiceImpl;



}
