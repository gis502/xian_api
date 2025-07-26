package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.dto.FactorAttributeDTO;
import com.ruoyi.system.domain.dto.FactorValueDTO;
import com.ruoyi.system.domain.dto.GeologicalDisasterHideDTO;
import com.ruoyi.system.domain.entity.GeologicalDisasterHide;
import com.ruoyi.system.domain.vo.FactorVO;
import com.ruoyi.system.domain.vo.HideVO;
import com.ruoyi.system.mapper.GeologicalDisasterHideMapper;
import com.ruoyi.system.service.IFactorAttributeService;
import com.ruoyi.system.service.IFactorValueService;
import com.ruoyi.system.service.IGeologicalDisasterHideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 18:02
 * @description: 获取地质灾害隐患点实现类
 */

@Slf4j
@Service
public class GeologicalDisasterHideServiceImpl implements IGeologicalDisasterHideService {

    @Resource
    private GeologicalDisasterHideMapper geologicalDisasterHideMapper;
    @Resource
    private IFactorValueService factorValueService;
    @Resource
    private IFactorAttributeService factorAttributeService;

    // 获取滑坡隐患点数据
    @Override
    public List<HideVO> getGeologicalDisasterHideByLandSlideList() {

        // 获取滑坡隐患点数据
        List<GeologicalDisasterHide> landSlide = geologicalDisasterHideMapper.
                selectList(new QueryWrapper<GeologicalDisasterHide>().eq("disaster_type", "滑坡"));
        // 滑坡数据传输对象
        List<GeologicalDisasterHideDTO> hideDTOlist = new ArrayList<>();
        // 隐患点视图对象
        List<HideVO> hideVOlist = new ArrayList<>();

        // 获取因子值表中所有的 hideId
        List<FactorValueDTO> factorValueDTOs = factorValueService.getAllHideId();

        Set<Integer> hideIdSet = factorValueDTOs.stream()
                // 过滤掉hideId为null的情况，避免空指针
                .filter(dto -> dto.getHideId() != null)
                .map(FactorValueDTO::getHideId)
                .collect(Collectors.toSet());

        // 筛选出所有存在因子的 hideId
        for (GeologicalDisasterHide disaster : landSlide) {
            Integer disasterId = disaster.getId();

            if (disasterId != null && hideIdSet.contains(disasterId)) {
                GeologicalDisasterHideDTO hideDTO = new GeologicalDisasterHideDTO();
                BeanUtils.copyProperties(disaster, hideDTO);
                hideDTOlist.add(hideDTO);
            }
        }

        // 根据隐患点Id查询对应的因子值
        for (GeologicalDisasterHideDTO hideDTO : hideDTOlist) {
            // 根据ID查询 value 中的值  联表查询
            List<FactorVO> valueDTOList = factorValueService.getFactorValueByHideId(hideDTO.getId());
            HideVO merged = mergeData(hideDTO, valueDTOList,"滑坡");

            hideVOlist.add( merged);
        }

        // 返回视图对象
        return hideVOlist;
    }

    // 获取泥石流隐患点数据
    @Override
    public List<HideVO> getGeologicalDisasterHideByFlowList() {

        // 获取泥石流隐患点数据
        List<GeologicalDisasterHide> debrisFlows = geologicalDisasterHideMapper.
                selectList(new QueryWrapper<GeologicalDisasterHide>().eq("disaster_type", "泥石流"));
//        // 创建传输对象
        List<GeologicalDisasterHideDTO> hideDTOlist = new ArrayList<>();
//        // 隐患点视图对象
        List<HideVO> hideVOlist = new ArrayList<>();
//
//        // 获取因子值表中所有的 hideId
//        List<FactorValueDTO> factorValueDTOs = factorValueService.getAllHideId();
//
//        Set<Integer> hideIdSet = factorValueDTOs.stream()
//                // 过滤掉hideId为null的情况，避免空指针
//                .filter(dto -> dto.getHideId() != null)
//                .map(FactorValueDTO::getHideId)
//                .collect(Collectors.toSet());
//
//        // 筛选出所有存在因子的 hideId
        for (GeologicalDisasterHide disaster : debrisFlows) {
//            Integer disasterId = disaster.getId();

//            if (disasterId != null && hideIdSet.contains(disasterId)) {
                GeologicalDisasterHideDTO hideDTO = new GeologicalDisasterHideDTO();
                BeanUtils.copyProperties(disaster, hideDTO);
                hideDTOlist.add(hideDTO);
//            }
        }

        // 根据隐患点Id查询对应的因子值
        for (GeologicalDisasterHideDTO hideDTO : hideDTOlist) {
            // 根据ID查询 value 中的值  联表查询
//            List<FactorVO> valueDTOList = factorValueService.getFactorValueByHideId(hideDTO.getId());
            HideVO merged = mergeData(hideDTO, null,"泥石流");

            hideVOlist.add(merged);
        }

        // 返回视图对象
        return hideVOlist;
    }

    // 获取全部滑坡点数据
    @Override
    public List<GeologicalDisasterHide> getGeologicalAllDisasterHideByLandSlideList(){
        List<GeologicalDisasterHide> debrisSlides = geologicalDisasterHideMapper.selectList(new QueryWrapper<GeologicalDisasterHide>().eq("disaster_type", "滑坡"));
        return debrisSlides;
    }

    //获取全部泥石流点数据
    @Override
    public List<GeologicalDisasterHide> getGeologicalAllDisasterHideByFlowList(){
        List<GeologicalDisasterHide> debrisFlows = geologicalDisasterHideMapper.selectList(new QueryWrapper<GeologicalDisasterHide>().eq("disaster_type", "泥石流"));
        return debrisFlows;
    }

    @Override
    public HashMap<String,List> getGeologicalDisasterBySlideList() {

        QueryWrapper wrapper = new QueryWrapper();
        // 崩塌、地裂缝、地面塌陷、滑坡、泥石流
        wrapper.eq("disaster_type","滑坡");
        // 滑坡数据
        List<GeologicalDisasterHide> landSlide = geologicalDisasterHideMapper.selectList(new QueryWrapper<GeologicalDisasterHide>().eq("disaster_type", "滑坡"));
        // 泥石流数据
        Map<String,List> map = processDisasters(landSlide, "滑坡");

        return (HashMap<String, List>) map;
    }

    @Override
    public HashMap<String,List> getGeologicalDisasterByFlowList() {

        QueryWrapper wrapper = new QueryWrapper();
        // 崩塌、地裂缝、地面塌陷、滑坡、泥石流
        wrapper.eq("disaster_type","泥石流");
        // 滑坡数据
        List<GeologicalDisasterHide> Flow = geologicalDisasterHideMapper.selectList(new QueryWrapper<GeologicalDisasterHide>().eq("disaster_type", "泥石流"));
        // 泥石流数据
        Map<String,List> map = processDisasters(Flow, "泥石流");

        return (HashMap<String, List>) map;
    }

    private Map<String, List> processDisasters(List<GeologicalDisasterHide> disasters, String disasterType) {

        if (disasters == null || disasters.isEmpty()) {
            return null;
        }

        List lists = new ArrayList<>();
        Map<String, List> features = new HashMap<>();

        for (GeologicalDisasterHide disaster : disasters) {
            // 创建特征对象
            Map<String, Object> feature = new HashMap<>();

            // 创建属性对象
            Map<String, Object> properties = new HashMap<>();
            properties.put("fieldCode", disaster.getFieldCode());
            properties.put("disasterName", disaster.getDisasterName());
            properties.put("longitude", disaster.getLongitude());
            properties.put("latitude", disaster.getLatitude());
            properties.put("lon", disaster.getLon());
            properties.put("lat", disaster.getLat());
            properties.put("position", disaster.getPosition());
            properties.put("disasterType", disasterType);
            properties.put("scaleGrade", disaster.getScaleGrade());
            properties.put("riskGrade", disaster.getRiskGrade());

            Map<String, Object> geometry = new HashMap<>();
            // coordinates为经度、纬度的数组
            List<Double> coordinates = new ArrayList<>();
            coordinates.add(disaster.getLon());  // 经度
            coordinates.add(disaster.getLat());  // 纬度
            geometry.put("coordinates", coordinates);

            // 组装特征对象
            feature.put("properties", properties);
            feature.put("geometry", geometry);

            lists.add(feature);
        }

        features.put("features", lists);

        return features;
    }

    private HideVO mergeData(GeologicalDisasterHideDTO hideDTO,List<FactorVO> valueDTOList,String type){

        HideVO hideVO = new HideVO();
        if (type.equals("泥石流")){
            hideVO.setGeologicalDisasterHideDTO(hideDTO);
            hideVO.setFactorVoList(null);
            return hideVO;
        }
        // TODO 目前只有滑坡有分析值
        hideVO.setGeologicalDisasterHideDTO(hideDTO);
        hideVO.setFactorVoList(valueDTOList);

        return hideVO;
    }


}
