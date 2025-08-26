package com.ruoyi.system.service;


import com.ruoyi.system.domain.dto.DisasterRainDTO;

import java.util.List;

public interface IXianDisasterRealService {
    List<Object> getExcelPlotInfo(List<String> plotTypes, List<String> plotIds);
    Object getPlotInfos(String plotType, String plotId);

}
