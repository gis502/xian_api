package com.ruoyi.system.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 13129
 * @description: TODO()
 * @date 2025/9/6 下午4:54
 */
public interface CarrierInformationService {
    public List<Map<String, Object>> queryDisasterNames();

    public Map<String, Object> queryPeople(Long disasterId);

    public List<Map<String, Object>> queryTraffic(Long disasterId);

    public Map<String, Object> queryDanger(Long disasterId);

    public Map<String, Object> queryStation(Long disasterId);
}
