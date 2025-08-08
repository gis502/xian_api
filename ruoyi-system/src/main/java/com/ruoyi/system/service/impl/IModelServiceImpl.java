package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.dto.*;
import com.ruoyi.system.domain.entity.*;
import com.ruoyi.system.domain.vo.FactorAnalysisLevelProbabilityVO;
import com.ruoyi.system.domain.vo.FactorVO;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IFactorValueService;
import com.ruoyi.system.service.IModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class IModelServiceImpl extends ServiceImpl<FactorAnalysisMapper,FactorAnalysis> implements IModelService {

    @Resource
    private GeologicalDisasterHideMapper geologicalDisasterHideMapper;

    @Resource
    private FactorAnalysisMapper factorAnalysisMapper;

    @Resource
    private FactorValueMapper factorValueMapper;

    @Resource
    private IFactorValueService factorValueService;

    @Resource
    private XianDemMapper xianDemMapper;

    @Resource
    private BridgeMapper bridgeMapper;

    @Resource
    private ReservoirMapper reservoirMapper;

    @Resource
    private HighwayMapper highwayMapper;

    @Resource
    private RoadMapper roadMapper;

    @Resource
    private WaterPipeMapper waterPipeMapper;

    @Resource
    private PeopleMapper peopleMapper;

    @Resource
    private CropsMapper cropsMapper;

    @Override
    public List<ModelGetDataDTO> rainSlideTrigger(List<List<FactorVO>> factorList){
        List<ModelGetDataDTO> list = new ArrayList<>();
        for(int i=0;i<factorList.size();i++){
            if(factorList.get(i)==null){
                continue;
            }
            int hideId = factorList.get(i).get(0).getHideId();
            double elevation = Double.parseDouble(factorList.get(i).stream()
                                                                    .filter(factor -> "高程".equals(factor.getAttributeName()))
                                                                    .map(factor -> (String) factor.getFactorValue())
                                                                    .findFirst()
                                                                    .orElse(null));
            double slope = Double.parseDouble(factorList.get(i).stream()
                                                                .filter(factor -> "坡度".equals(factor.getAttributeName()))
                                                                .map(factor -> (String) factor.getFactorValue())
                                                                .findFirst()
                                                                .orElse(null));
            int soilType = soilTypeToCode(factorList.get(i).stream()
                                                            .filter(factor -> "岩土类型".equals(factor.getAttributeName()))
                                                            .map(factor -> (String) factor.getFactorValue())
                                                            .findFirst()
                                                            .orElse(null));
            int landUseType = landUseToCode(factorList.get(i).stream()
                                                                .filter(factor -> "土地利用类型".equals(factor.getAttributeName()))
                                                                .map(factor -> (String) factor.getFactorValue())
                                                                .findFirst()
                                                                .orElse(null));
            double breakDistance = Double.parseDouble(factorList.get(i).stream()
                    .filter(factor -> "断层距离".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double waterDistance = Double.parseDouble(factorList.get(i).stream()
                    .filter(factor -> "水系距离".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double rain = Double.parseDouble(factorList.get(i).stream()
                    .filter(factor -> "降雨量".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double vegetationCover = Double.parseDouble(factorList.get(i).stream()
                                                                            .filter(factor -> "植被覆盖率".equals(factor.getAttributeName()))
                                                                            .map(factor -> (String) factor.getFactorValue())
                                                                            .findFirst()
                                                                            .orElse(null));
            double curvature = Double.parseDouble(factorList.get(i).stream()
                                                                    .filter(factor -> "坡面曲率".equals(factor.getAttributeName()))
                                                                    .map(factor -> (String) factor.getFactorValue())
                                                                    .findFirst()
                                                                    .orElse(null));
            double sandContent = Double.parseDouble(factorList.get(i).stream()
                                                                        .filter(factor -> "土壤沙砾度".equals(factor.getAttributeName()))
                                                                        .map(factor -> (String) factor.getFactorValue())
                                                                        .findFirst()
                                                                        .orElse(null));
            int slopeShape = slopeShapeToCode(factorList.get(i).stream()
                                                                .filter(factor -> "坡型".equals(factor.getAttributeName()))
                                                                .map(factor -> (String) factor.getFactorValue())
                                                                .findFirst()
                                                                .orElse(null));
            double probability = calculateRainLandslideProbability(elevation,slope,soilType,landUseType,breakDistance,waterDistance,rain,vegetationCover,curvature,sandContent,slopeShape);
            String level;
            if(probability<=0.3){
                level="低";
            }else if(probability<=0.7){
                level="中";
            }else{
                level="高";
            }
            FactorAnalysisLevelProbabilityVO predict = new FactorAnalysisLevelProbabilityVO();
            predict.setLevel(level);
            predict.setProbability(probability);
            List<FactorVO> factorVO = factorValueService.getFactorValueByHideId(hideId);
            ModelGetDataDTO modelGetDataDTO = getGeologicalDisasterHideByLandSlideById(hideId,predict,factorVO);

            list.add(modelGetDataDTO);
            insertFactorAnalysis(modelGetDataDTO.getFactorVoList(),modelGetDataDTO.getPredict());
        }
        return list;
    };

    @Override
    public ModelGetDataDTO rainSlideFactorUpdata(List<FactorVO> factorList){
        if(factorList==null||factorList.size()==0){
            return null;
        }
        int hideId = factorList.get(0).getHideId();
        double elevation = Double.parseDouble(factorList.stream()
                .filter(factor -> "高程".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double slope = Double.parseDouble(factorList.stream()
                .filter(factor -> "坡度".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        int soilType = soilTypeToCode(factorList.stream()
                .filter(factor -> "岩土类型".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        int landUseType = landUseToCode(factorList.stream()
                .filter(factor -> "土地利用类型".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double breakDistance = Double.parseDouble(factorList.stream()
                .filter(factor -> "断层距离".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double waterDistance = Double.parseDouble(factorList.stream()
                .filter(factor -> "水系距离".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double rain = Double.parseDouble(factorList.stream()
                .filter(factor -> "降雨量".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double vegetationCover = Double.parseDouble(factorList.stream()
                .filter(factor -> "植被覆盖率".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double curvature = Double.parseDouble(factorList.stream()
                .filter(factor -> "坡面曲率".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double sandContent = Double.parseDouble(factorList.stream()
                .filter(factor -> "土壤沙砾度".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        int slopeShape = slopeShapeToCode(factorList.stream()
                .filter(factor -> "坡型".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double probability = calculateRainLandslideProbability(elevation,slope,soilType,landUseType,breakDistance,waterDistance,rain,vegetationCover,curvature,sandContent,slopeShape);
        String level;
        if(probability<=0.3){
            level="低";
        }else if(probability<=0.7){
            level="中";
        }else{
            level="高";
        }
        FactorAnalysisLevelProbabilityVO factorAnalysisLevelProbability = new FactorAnalysisLevelProbabilityVO();
        factorAnalysisLevelProbability.setLevel(level);
        factorAnalysisLevelProbability.setProbability(probability);
        ModelGetDataDTO modelGetDataDTO = getGeologicalDisasterHideByLandSlideById(hideId,factorAnalysisLevelProbability,factorList);
        updataFactorValue(modelGetDataDTO.getFactorVoList());
        insertFactorAnalysis(modelGetDataDTO.getFactorVoList(),modelGetDataDTO.getPredict());
        return modelGetDataDTO;
    }

    @Override
    public List<ModelGetDataDTO> eqSlideTrigger(List<ModelGetDataFactorListEntityIdDTO> factorList){

        List<ModelGetDataDTO> list = new ArrayList<>();
        String entityId = "";
        for(int i=0;i<factorList.size();i++){
            if(factorList.get(i)==null){
                continue;
            }
            entityId =  factorList.get(i).getEntityId();
            int hideId = factorList.get(i).getFactorVoList().get(0).getHideId();
            double elevation = Double.parseDouble(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "高程".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double slope = Double.parseDouble(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "坡度".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            int soilType = soilTypeToCode(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "岩土类型".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double breakDistance = Double.parseDouble(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "断层距离".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double waterDistance = Double.parseDouble(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "水系距离".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            int landUseType = landUseToCode(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "土地利用类型".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double rain = Double.parseDouble(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "降雨量".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double vegetationCover = Double.parseDouble(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "植被覆盖率".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double curvature = Double.parseDouble(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "坡面曲率".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double sandContent = Double.parseDouble(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "土壤沙砾度".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            int slopeShape = slopeShapeToCode(factorList.get(i).getFactorVoList().stream()
                    .filter(factor -> "坡型".equals(factor.getAttributeName()))
                    .map(factor -> (String) factor.getFactorValue())
                    .findFirst()
                    .orElse(null));
            double probability = calculateEqLandslideProbability(elevation,slope,soilType,landUseType,breakDistance,waterDistance,rain,vegetationCover,curvature,sandContent,slopeShape);
            String level;
            if(probability<=0.3){
                level="低";
            }else if(probability<=0.7){
                level="中";
            }else{
                level="高";
            }
            FactorAnalysisLevelProbabilityVO factorAnalysisLevelProbability = new FactorAnalysisLevelProbabilityVO();
            factorAnalysisLevelProbability.setLevel(level);
            factorAnalysisLevelProbability.setProbability(probability);
            List<FactorVO> factorVO = factorValueService.getFactorValueByHideId(hideId);
            ModelGetDataDTO modelGetDataDTO = getGeologicalDisasterHideByLandSlideById(hideId,factorAnalysisLevelProbability,factorVO);
            modelGetDataDTO.setEntityId(entityId);
            list.add(modelGetDataDTO);
            insertFactorAnalysis(modelGetDataDTO.getFactorVoList(),modelGetDataDTO.getPredict());
        }
        return list;
    }

    @Override
    public ModelGetDataDTO eqSlideFactorUpdata(List<FactorVO> factorList){
        if(factorList==null||factorList.size()==0){
            return null;
        }
        int hideId = factorList.get(0).getHideId();
        double elevation = Double.parseDouble(factorList.stream()
                .filter(factor -> "高程".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double slope = Double.parseDouble(factorList.stream()
                .filter(factor -> "坡度".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        int soilType = soilTypeToCode(factorList.stream()
                .filter(factor -> "岩土类型".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        int landUseType = landUseToCode(factorList.stream()
                .filter(factor -> "土地利用类型".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double breakDistance = Double.parseDouble(factorList.stream()
                .filter(factor -> "断层距离".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double waterDistance = Double.parseDouble(factorList.stream()
                .filter(factor -> "水系距离".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double rain = Double.parseDouble(factorList.stream()
                .filter(factor -> "降雨量".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double vegetationCover = Double.parseDouble(factorList.stream()
                .filter(factor -> "植被覆盖率".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double curvature = Double.parseDouble(factorList.stream()
                .filter(factor -> "坡面曲率".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double sandContent = Double.parseDouble(factorList.stream()
                .filter(factor -> "土壤沙砾度".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        int slopeShape = slopeShapeToCode(factorList.stream()
                .filter(factor -> "坡型".equals(factor.getAttributeName()))
                .map(factor -> (String) factor.getFactorValue())
                .findFirst()
                .orElse(null));
        double probability = calculateEqLandslideProbability(elevation,slope,soilType,landUseType,breakDistance,waterDistance,rain,vegetationCover,curvature,sandContent,slopeShape);
        String level;
        if(probability<=0.3){
            level="低";
        }else if(probability<=0.7){
            level="中";
        }else{
            level="高";
        }
        FactorAnalysisLevelProbabilityVO factorAnalysisLevelProbability = new FactorAnalysisLevelProbabilityVO();
        factorAnalysisLevelProbability.setLevel(level);
        factorAnalysisLevelProbability.setProbability(probability);
        ModelGetDataDTO modelGetDataDTO = getGeologicalDisasterHideByLandSlideById(hideId,factorAnalysisLevelProbability,factorList);
        updataFactorValue(modelGetDataDTO.getFactorVoList());
        insertFactorAnalysis(modelGetDataDTO.getFactorVoList(),modelGetDataDTO.getPredict());
        return modelGetDataDTO;
    }

    // 3x3网格范围的距离（度），约等于45米
    private static final double GRID_DISTANCE = 0.000405;

    // 最大迭代次数
    private static final int MAX_ITERATIONS = 30;

    @Override
    public List<XianDem> getPoliejiao(LatLonDTO latLonDTO){

        List<XianDem> path = findElevationMinimumPath(latLonDTO.getLon(), latLonDTO.getLat());
        return path;
    }

    /**
     * 寻找高程最低点路径
     */
    @Override
    public List<XianDem> findElevationMinimumPath(Double lon, Double lat) {
        List<XianDem> path = new ArrayList<>();

        // 找到起点所在的网格
        XianDem currentGrid = xianDemMapper.findGridContainingPoint(lon, lat);
        if (currentGrid == null) {
            throw new IllegalArgumentException("经纬度点不在任何网格范围内");
        }

        int iterations = 0;

        // 迭代寻找最低点
        while (iterations < MAX_ITERATIONS) {
            // 添加当前网格到路径
            path.add(new XianDem(
                    currentGrid.getCenterLat(),
                    currentGrid.getCenterLon(),
                    currentGrid.getElevation()
            ));

            // 查找3x3范围内高程最低的网格
            XianDem minElevationGrid = xianDemMapper.findMinElevationGridIn3x3(
                    currentGrid.getCenterLon(),
                    currentGrid.getCenterLat(),
                    GRID_DISTANCE
            );

            if (minElevationGrid == null) {
                throw new RuntimeException("无法找到周围网格");
            }

            // 检查当前网格是否是最低点
            if (currentGrid.getElevation().equals(minElevationGrid.getElevation())) {
                break;
            }

            // 移动到最低点网格
            currentGrid = minElevationGrid;
            iterations++;
        }

        if (iterations >= MAX_ITERATIONS) {
            throw new RuntimeException("已达到最大迭代次数，可能未找到绝对最低点");
        }

        return path;
    }

    @Override
    public EffactAreaDTO getEffactArea(List<LatLonDTO> latLonDTOList) {
        EffactAreaDTO effactAreaDTO = new EffactAreaDTO();

        // 将前端传来的经纬度列表转换为WKT格式的面
        String wktPolygon = convertLatLonListToWKT(latLonDTOList);

        try {
            // 查询与面相交或在面内的各类数据
            List<Bridge> bridges = bridgeMapper.findIntersectingBridges(wktPolygon);
            List<Reservoir> reservoirs = reservoirMapper.findIntersectingReservoirs(wktPolygon);
            List<Highway> highways = highwayMapper.findIntersectingHighways(wktPolygon);
            List<Road> roads = roadMapper.findIntersectingRoads(wktPolygon);
            List<WaterPipe> waterPipes = waterPipeMapper.findIntersectingWaterPipes(wktPolygon);
            List<People> people = peopleMapper.findIntersectingPeople(wktPolygon);
            List<Crops> crops = cropsMapper.findIntersectingCrops(wktPolygon);

            // 设置到DTO中
            effactAreaDTO.setBridgeList(bridges);
            effactAreaDTO.setReservoirList(reservoirs);
            effactAreaDTO.setHighwayList(highways);
            effactAreaDTO.setRoadList(roads);
            effactAreaDTO.setWaterPipeList(waterPipes);
            effactAreaDTO.setPeopleList(people);
            effactAreaDTO.setCropsList(crops);

            log.info("查询影响区域数据完成，桥梁:{}, 水库:{}, 高速:{}, 道路:{}, 供水管网:{}, 人口:{}, 农作物:{}",
                    bridges.size(), reservoirs.size(), highways.size(), roads.size(),
                    waterPipes.size(), people.size(), crops.size());

        } catch (Exception e) {
            log.error("查询影响区域数据失败", e);
            throw new RuntimeException("查询影响区域数据失败: " + e.getMessage());
        }

        return effactAreaDTO;
    }

    /**
     * 将经纬度列表转换为WKT格式的面
     * @param latLonDTOList 经纬度列表
     * @return WKT格式的面字符串
     */
    private String convertLatLonListToWKT(List<LatLonDTO> latLonDTOList) {
        if (latLonDTOList == null || latLonDTOList.isEmpty()) {
            throw new IllegalArgumentException("经纬度列表不能为空");
        }

        StringBuilder wkt = new StringBuilder("POLYGON((");

        for (int i = 0; i < latLonDTOList.size(); i++) {
            LatLonDTO point = latLonDTOList.get(i);
            if (i > 0) {
                wkt.append(", ");
            }
            wkt.append(point.getLon()).append(" ").append(point.getLat());
        }

        // 闭合多边形（第一个点和最后一个点相同）
        if (!latLonDTOList.get(0).equals(latLonDTOList.get(latLonDTOList.size() - 1))) {
            LatLonDTO firstPoint = latLonDTOList.get(0);
            wkt.append(", ").append(firstPoint.getLon()).append(" ").append(firstPoint.getLat());
        }

        wkt.append("))");

        return wkt.toString();
    }

    // 暴雨滑坡模型计算概率值
    private static double calculateRainLandslideProbability(double elevation, double slope, int soilType, int landUseType,double breakDistance,double waterDistance, double rain, double vegetationCover, double curvature, double sandContent, int slopeShape) {
//        double INTERCEPT = 1.108;          // 常量
//        double INTERCEPT = -0.644;          // 常量
//        double ELEVATION_COEF = -2.327e-5; // 高程
//        double SLOPE_COEF = 0.036;        // 坡度
//        double SOIL_TYPE_COEF = -0.004;    // 岩土类型
//        double LAND_USE_COEF = -0.034;     // 土地利用类型
//        double RAIN = 0.000046;              // 降雨量
//        double VEGETATION_COEF = -0.008;   // 植被覆盖率
//        double CURVATURE_COEF = 0.049;     // 坡面曲率
//        double SAND_CONTENT_COEF = 0.026;   // 土壤沙砾度
//        double SLOPE_SHAPE_COEF = 0.018;    // 坡型

        double INTERCEPT = -0.41;          // 常量
        double ELEVATION_COEF = 0.0002; // 高程
        double SLOPE_COEF = 0.004;        // 坡度
        double SOIL_TYPE_COEF = -0.009;    // 岩土类型
        double LAND_USE_COEF = -0.006;     // 土地利用类型
        double BREAK_DISTANCE_COEF = 1.777e-5;//断层距离
        double WATER_DISTANCE_COEF = -3.111e-5;//水系距离
        double RAIN = 0.004;              // 降雨量
        double VEGETATION_COEF = 0.005;   // 植被覆盖率
        double CURVATURE_COEF = 0.010;     // 坡面曲率
        double SAND_CONTENT_COEF = 0.002;   // 土壤沙砾度
        double SLOPE_SHAPE_COEF = 0.017;    // 坡型

        // 计算各项的贡献值
        double elevationTerm = ELEVATION_COEF * elevation;
        double slopeTerm = SLOPE_COEF * slope;
        double soilTypeTerm = SOIL_TYPE_COEF * soilType;
        double landUseTerm = LAND_USE_COEF * landUseType;
        double breakDistanceTerm = BREAK_DISTANCE_COEF * breakDistance;
        double waterDistanceTerm = WATER_DISTANCE_COEF * waterDistance;
        double rainTerm = RAIN * rain;
        double vegetationTerm = VEGETATION_COEF * vegetationCover;
        double curvatureTerm = CURVATURE_COEF * curvature;
        double sandContentTerm = SAND_CONTENT_COEF * sandContent;
        double slopeShapeTerm = SLOPE_SHAPE_COEF * slopeShape;

        // 计算总概率（使用线性组合）
        double probability = INTERCEPT
                + elevationTerm
                + slopeTerm
                + soilTypeTerm
                + landUseTerm
                +breakDistanceTerm
                + waterDistanceTerm
                + rainTerm
                + vegetationTerm
                + curvatureTerm
                + sandContentTerm
                + slopeShapeTerm;

        // 确保概率在合理范围内（0-1之间）
        return Math.max(0, Math.min(1, probability));
//        return probability;
    }

    // 地震滑坡模型计算概率值
    private static double calculateEqLandslideProbability(double elevation, double slope, int soilType, int landUseType,double breakDistance,double waterDistance,double rain, double vegetationCover, double curvature, double sandContent, int slopeShape) {
//        double INTERCEPT = 1.308;          // 常量
//        double INTERCEPT = -0.644;          // 常量
//        double ELEVATION_COEF = -2.327e-5; // 高程
//        double SLOPE_COEF = 0.036;        // 坡度
//        double SOIL_TYPE_COEF = -0.004;    // 岩土类型
//        double LAND_USE_COEF = -0.034;     // 土地利用类型
//        double RAIN = 0;              // 降雨量
//        double VEGETATION_COEF = -0.008;   // 植被覆盖率
//        double CURVATURE_COEF = 0.049;     // 坡面曲率
//        double SAND_CONTENT_COEF = 0.026;   // 土壤沙砾度
//        double SLOPE_SHAPE_COEF = 0.018;    // 坡型
//        double INTERCEPT = -0.649;          // 常量
//        double ELEVATION_COEF = -2.327e-5; // 高程
//        double SLOPE_COEF = 0.036;        // 坡度
//        double SOIL_TYPE_COEF = -0.004;    // 岩土类型
//        double LAND_USE_COEF = -0.034;     // 土地利用类型
//        double RAIN = 0.000046;              // 降雨量
//        double VEGETATION_COEF = -0.008;   // 植被覆盖率
//        double CURVATURE_COEF = 0.049;     // 坡面曲率
//        double SAND_CONTENT_COEF = 0.026;   // 土壤沙砾度
//        double SLOPE_SHAPE_COEF = 0.018;    // 坡型

        double INTERCEPT = 0.368;          // 常量
        double ELEVATION_COEF = 0.00002; // 高程
        double SLOPE_COEF = 0.007;        // 坡度
        double SOIL_TYPE_COEF = -0.014;    // 岩土类型
        double LAND_USE_COEF = 0.001;     // 土地利用类型
        double BREAK_DISTANCE_COEF = 2.736e-5;//断层距离
        double WATER_DISTANCE_COEF = 0;//水系距离
        double RAIN = 0;              // 降雨量
        double VEGETATION_COEF = 0.0005;   // 植被覆盖率
        double CURVATURE_COEF = 0.002;     // 坡面曲率
        double SAND_CONTENT_COEF = 0.0001;   // 土壤沙砾度
        double SLOPE_SHAPE_COEF = 0.022;    // 坡型

        // 计算各项的贡献值
        double elevationTerm = ELEVATION_COEF * elevation;
        double slopeTerm = SLOPE_COEF * slope;
        double soilTypeTerm = SOIL_TYPE_COEF * soilType;
        double landUseTerm = LAND_USE_COEF * landUseType;
        double breakDistanceTerm = BREAK_DISTANCE_COEF * breakDistance;
        double waterDistanceTerm = WATER_DISTANCE_COEF * waterDistance;
        double rainTerm = RAIN * rain;
        double vegetationTerm = VEGETATION_COEF * vegetationCover;
        double curvatureTerm = CURVATURE_COEF * curvature;
        double sandContentTerm = SAND_CONTENT_COEF * sandContent;
        double slopeShapeTerm = SLOPE_SHAPE_COEF * slopeShape;

        // 计算总概率（使用线性组合）
        double probability = INTERCEPT
                + elevationTerm
                + slopeTerm
                + soilTypeTerm
                + landUseTerm
                +breakDistanceTerm
                + waterDistanceTerm
                + rainTerm
                + vegetationTerm
                + curvatureTerm
                + sandContentTerm
                + slopeShapeTerm;

        // 确保概率在合理范围内（0-1之间）
        return Math.max(0, Math.min(1, probability));
//        return probability;
    }

    // 更新FactorValue表数据
    private void updataFactorValue(List<FactorVO> factorVoList){
        for(FactorVO f:factorVoList){
            FactorValue factorValue = new FactorValue();
            BeanUtils.copyProperties(f, factorValue);
            UpdateWrapper<FactorValue> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("value_id", f.getValueId()) // 更新ID为1,2,3的记录
                    .set("update_time", LocalDateTime.now())  // 设置更新时间
                    .set("factor_value",f.getFactorValue());
            factorValueMapper.update(null, updateWrapper);
        }
    }
    // 插入数据到FactorAnalysis
    private void insertFactorAnalysis(List<FactorVO> factorVoList,FactorAnalysisLevelProbabilityVO factorAnalysisLevelProbability){
        List<FactorAnalysis> factorAnalysisLeve = new ArrayList<>();
        for(FactorVO f:factorVoList){
            FactorAnalysis factorAnalysis = new FactorAnalysis();
            BeanUtils.copyProperties(f, factorAnalysis);
            factorAnalysis.setCreateTime(LocalDateTime.now());
            factorAnalysis.setUpdateTime(LocalDateTime.now());
            factorAnalysis.setIsDeleted(0);
            factorAnalysis.setProbability(factorAnalysisLevelProbability.getProbability());
            factorAnalysis.setLevel(factorAnalysisLevelProbability.getLevel());
            factorAnalysisLeve.add(factorAnalysis);
        }
        saveBatch(factorAnalysisLeve);
    }
    // 拼接ModelGetDataDTO
    private ModelGetDataDTO getGeologicalDisasterHideByLandSlideById(Integer id, FactorAnalysisLevelProbabilityVO factorAnalysisLevelProbability,List<FactorVO> factorVO){
        // 创建模型返回前端格式数据的DTO
        ModelGetDataDTO modelGetDataDTO = new ModelGetDataDTO();

        // 根据id取出数据库中那一条，并BeanUtils.copyProperties，赋值给GeologicalDisasterHide
        GeologicalDisasterHide landSlide = geologicalDisasterHideMapper.selectById(id);
        GeologicalDisasterHideDTO hideDTO = new GeologicalDisasterHideDTO();
        BeanUtils.copyProperties(landSlide, hideDTO);

        // 赋值给factorVO
        List<FactorVO> fVO = factorVO;

        // 组合拼接成ModelGetDataDTO
        modelGetDataDTO.setGeologicalDisasterHideDTO(hideDTO);
        modelGetDataDTO.setFactorVoList(fVO);
        modelGetDataDTO.setPredict(factorAnalysisLevelProbability);

        return modelGetDataDTO;
    }
    // 判断岩土类型
    private static int soilTypeToCode(String soilType) {
        if (soilType == null) throw new IllegalArgumentException("soilType 不能为 null");
        switch (soilType.trim()) {
            case "碎石土": return 1;
            case "黄土":   return 2;
            case "砂类土": return 3;
            case "粘性土": return 4;
            case "红粘土": return 5;
            case "膨胀土": return 6;
            default: throw new IllegalArgumentException("未知岩土类型: " + soilType);
        }
    }
    // 判断土地利用类型
    private static int landUseToCode(String landUseType) {
        if (landUseType == null) throw new IllegalArgumentException("landUseType 不能为 null");
        switch (landUseType.trim()) {
            case "林地": return 1;
            case "耕地": return 2;
            case "居民用地": return 3;
            case "草地": return 4;
            case "难利用土地": return 5;
            case "园地": return 6;
            default: throw new IllegalArgumentException("未知土地利用类型: " + landUseType);
        }
    }
    // 判断滑坡坡型
    private static int slopeShapeToCode(String slopeShape) {
        if (slopeShape == null) throw new IllegalArgumentException("slopeShape 不能为 null");
        switch (slopeShape.trim()) {
            case "阶梯": return 1;
            case "直线": return 2;
            case "凸型": return 3;
            case "凹型": return 4;
            default: throw new IllegalArgumentException("未知坡型: " + slopeShape);
        }
    }

    //计算滑坡影响面积
    @Override
    public LandslideAreaDto getLandslideArea(DemSlopeDTO DemSlopeDTO){
        LandslideAreaDto landslideAreaDto = new LandslideAreaDto();
        double PHI_MIN = 18.0; // 内摩擦角最小值(度)
        double PHI_MAX = 28.0; // 内摩擦角最大值(度)
        double WIDTH_MIN_RATIO = 0.5; // 滑坡宽度最小比例
        double WIDTH_MAX_RATIO = 1.2; // 滑坡宽度最大比例
        Random random = new Random();
        double phi = PHI_MIN + (PHI_MAX - PHI_MIN) * random.nextDouble();
        double widthRatio = WIDTH_MIN_RATIO + (WIDTH_MAX_RATIO - WIDTH_MIN_RATIO) * random.nextDouble();
        double theta = (DemSlopeDTO.getSlope() + phi) / 2.0;
        double h = DemSlopeDTO.getDem();
        //计算过程
        double thetaRadians = Math.toRadians(theta);
        double l = h / Math.tan(thetaRadians);
        double width = l * widthRatio;
        double ap = l * width;
        double a = ap / Math.cos(thetaRadians);
        landslideAreaDto.setLandslideArea(a);
        return landslideAreaDto;
    }
}
