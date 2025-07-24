package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.HideVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IGeologicalDisasterHideService {

    // 获取滑坡隐患点数据
    public List<HideVO> getGeologicalDisasterHideByLandSlideList();

    // 获取泥石流隐患点数据
    public List<HideVO> getGeologicalDisasterHideByFlowList();

}
