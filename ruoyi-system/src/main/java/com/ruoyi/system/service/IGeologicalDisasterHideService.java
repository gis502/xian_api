package com.ruoyi.system.service;

import com.ruoyi.system.domain.entity.GeologicalDisasterHide;
import com.ruoyi.system.domain.vo.HideVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IGeologicalDisasterHideService {

    // 获取滑坡隐患点数据
    public List<HideVO> getGeologicalDisasterHideByLandSlideList();

    // 获取泥石流隐患点数据
    public List<HideVO> getGeologicalDisasterHideByFlowList();

    public List<HideVO> getGeologicalDisasterByFlashFloodList();

    public List<HideVO> getGeologicalDisasterByWaterLogging();

    /**
     * 根据区县名称查询隐患点（包含因子数据）
     * @param countyName 区县名称
     * @return 隐患点 VO 列表
     */
    List<HideVO> getHiddenDisasterPointsByCounty(String countyName);

    /**
     * 批量根据区县名称查询隐患点
     * @param countyNames 区县名称列表
     * @return 隐患点 VO 列表（按区县分组）
     */
    Map<String, List<HideVO>> getHiddenDisasterPointsByCounties(List<String> countyNames);

    // 获取全部滑坡点数据
    public List<GeologicalDisasterHide> getGeologicalAllDisasterHideByLandSlideList();

    // 获取全部泥石流点数据
    public List<GeologicalDisasterHide> getGeologicalAllDisasterHideByFlowList();

    public HashMap<String, List> getGeologicalDisasterBySlideList();

    public HashMap<String, List> getGeologicalDisasterByFlowList();

    public HashMap<String, List> getGeologicalDisasterByFloodList();

    public HashMap<String, List> getGeologicalDisasterByWaterList();
}
