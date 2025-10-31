package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.DisasterQueryDTO;
import com.ruoyi.system.domain.dto.EarthquakeRainDTO;
import com.ruoyi.system.service.EarthquakeRainService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admins/earthquake-rain")
public class EarthquakeRainController {

    @Resource
    private EarthquakeRainService earthquakeRainService;

    @PostMapping("/list") // POST接口路径
    public AjaxResult getDisasterList(@RequestBody DisasterQueryDTO queryDTO) {
        try {
            // 1. 解析请求参数
            int pageNum = queryDTO.getPageNum();
            int pageSize = queryDTO.getPageSize();
            List<String> disasterTypes = queryDTO.getDisasterTypes();

            // 2. 调用业务层查询数据
            List<EarthquakeRainDTO> records = earthquakeRainService.getPagedList(pageNum, pageSize, disasterTypes);
            int total = earthquakeRainService.getTotalCount(disasterTypes);

            // 3. 封装响应结果（包含总条数和当前页数据）
            Map<String, Object> result = new HashMap<>();
            result.put("total", total); // 符合条件的总数据量
            result.put("records", records); // 当前页的数据列表

            // 4. 返回成功响应
            return AjaxResult.success(result);
        } catch (Exception e) {
            // 异常处理：打印日志并返回错误信息
            e.printStackTrace();
            return AjaxResult.error("查询灾害数据失败：" + e.getMessage());
        }
    }



}
