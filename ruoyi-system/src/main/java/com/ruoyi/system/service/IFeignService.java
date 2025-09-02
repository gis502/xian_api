package com.ruoyi.system.service;


import com.ruoyi.system.domain.dto.OutputDTO;
import com.ruoyi.system.domain.dto.RainOutputDTO;
import com.ruoyi.system.domain.dto.RainTriggerDTO;
import com.ruoyi.system.domain.dto.TriggerDTO;
import com.ruoyi.system.domain.params.RainQuery;
import com.ruoyi.system.domain.params.ThematicQuery;

import java.util.List;

public interface IFeignService {

    // 第三方地震触发
    public ThematicQuery trigger(TriggerDTO triggerDTO);
    // 第三方地震专题图件产出
    public List<OutputDTO> thematicMap(ThematicQuery query);
    // 第三方地震灾情报告产出
    public List<OutputDTO> disasterReport(ThematicQuery query);

    // 第三方暴雨触发
    public RainQuery trigger(RainTriggerDTO triggerDTO);
    // 第三方暴雨专题图件产出
    public List<RainOutputDTO> thematicMap(RainQuery query);



}
