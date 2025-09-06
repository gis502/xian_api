package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.entity.XianImpactInAreaEntity;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.mapper.XianImpactInAreaMapper;
import com.ruoyi.system.service.CarrierInformationService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wzy
 * @description: TODO()
 * @date 2025/9/6 下午4:56
 */
@Service
public class ICarrierInformationServiceImpl implements CarrierInformationService {

    private final XianDisasterRainMapper xianDisasterRainMapper;

    private final XianImpactInAreaMapper xianImpactInAreaMapper;

    public ICarrierInformationServiceImpl(XianDisasterRainMapper xianDisasterRainMapper,
                                          XianImpactInAreaMapper xianImpactInAreaMapper) {
        this.xianDisasterRainMapper = xianDisasterRainMapper;
        this.xianImpactInAreaMapper = xianImpactInAreaMapper;
    }

    @Override
    public List<Map<String, Object>> queryDisasterNames() {
        return xianDisasterRainMapper.queryDisasterNames();
    }

    @Override
    public Map<String, Object> queryPeople(Long disasterId) {
        List<XianImpactInAreaEntity> queryDatas = xianImpactInAreaMapper.queryPeople(disasterId);
        return integrateData("受影响数量", queryDatas, "getPeople");
    }

    @Override
    public List<Map<String, Object>> queryTraffic(Long disasterId) {
        List<XianImpactInAreaEntity> queryDatas = xianImpactInAreaMapper.queryTraffic(disasterId);
        if(queryDatas == null || queryDatas.size() == 0 || queryDatas.get(0) == null){
            return null;
        }
        List<Map<String, Object>> traffic = new ArrayList<>();

        // 国道
        Map<String, Object> nationalHighway = new HashMap<>();
        nationalHighway.put("name", "受影响国道");
        nationalHighway.put("value", queryDatas.get(0).getNationalRoad());
        traffic.add(nationalHighway);

        // 高速
        Map<String, Object> highSpeed = new HashMap<>();
        highSpeed.put("name", "受影响高速");
        highSpeed.put("value", queryDatas.get(0).getHeightway());
        traffic.add(highSpeed);

        // 市政道路
        Map<String, Object> street = new HashMap<>();
        street.put("name", "受影响市政道路");
        street.put("value", queryDatas.get(0).getStreet());
        traffic.add(street);
        return traffic;
    }

    @Override
    public Map<String, Object> queryDanger(Long disasterId) {
        List<XianImpactInAreaEntity> queryDatas = xianImpactInAreaMapper.queryDanger(disasterId);
        return integrateData("受影响数量", queryDatas, "getDangerousPoint");
    }

    @Override
    public Map<String, Object> queryStation(Long disasterId) {
        List<XianImpactInAreaEntity> queryDatas = xianImpactInAreaMapper.queryStation(disasterId);
        return integrateData("受影响数量", queryDatas, "getStation");
    }

    private Map<String, Object> integrateData(
            String name,
            List<XianImpactInAreaEntity> queryDatas,
            String getterMethodName
    ) {
        // 1. 参数校验：避免空方法名导致反射异常
        if (StringUtils.isEmpty(getterMethodName)) {
            throw new IllegalArgumentException("getter方法名不能为空（如'getPeople'）");
        }
        if (queryDatas == null) {
            queryDatas = new ArrayList<>(); // 避免空指针，返回空数据结构
        }

        Map<String, Object> map = new HashMap<>();
        List<String> xdata = new ArrayList<>();
        List<Integer> vNum = new ArrayList<>();

        // 2. 构建series结构（逻辑不变，仅数据来源动态化）
        List<Map<String, Object>> series = new ArrayList<>();
        Map<String, Object> seriesMap = new HashMap<>();
        seriesMap.put("name", name);
        seriesMap.put("data", vNum);
        series.add(seriesMap);

        map.put("xdata", xdata);
        map.put("series", series);

        for (XianImpactInAreaEntity data : queryDatas) {
            xdata.add(data.getDistrict());

            try {
                Method getterMethod = XianImpactInAreaEntity.class.getMethod(getterMethodName);
                Object result = getterMethod.invoke(data);
                if (result == null) {
                    vNum.add(0);
                } else if (result instanceof Integer) {
                    vNum.add((Integer) result);
                } else if (result instanceof Long) {
                    vNum.add(((Long) result).intValue());
                } else {
                    throw new IllegalArgumentException(
                            String.format("方法[%s]返回值类型不支持（当前类型：%s），仅支持Integer/Long",
                                    getterMethodName, result.getClass().getSimpleName())
                    );
                }
            } catch (Exception e) {
               e.printStackTrace();
            }
        }

        return map;
    }

}