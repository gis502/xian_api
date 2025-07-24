package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.vo.FactorVO;
import com.ruoyi.system.mapper.GeologicalDisasterRiskMapper;
import com.ruoyi.system.service.IModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IModelServiceImpl implements IModelService {
    private static final double INTERCEPT = 1.208;          // 常量
    private static final double ELEVATION_COEF = -2.327e-5; // 高程
    private static final double SLOPE_COEF = -0.036;        // 坡度
    private static final double SOIL_TYPE_COEF = -0.004;    // 岩土类型
    private static final double LAND_USE_COEF = -0.034;     // 土地利用类型
    private static final double VEGETATION_COEF = -0.008;   // 植被覆盖率
    private static final double CURVATURE_COEF = 0.049;     // 坡面曲率
    private static final double SAND_CONTENT_COEF = 0.026;   // 土壤沙砾度
    private static final double SLOPE_SHAPE_COEF = 0.018;    // 坡型


    public List<HashMap<String, String>> rainSlideTrigger(List<List<FactorVO>> factorList){
        List<HashMap<String, String>> list = new ArrayList<>();
        for(int i=0;i<factorList.size();i++){
            HashMap<String, String> map = new HashMap<>();
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
            double probability = calculateLandslideProbability(elevation,slope,soilType,landUseType,vegetationCover,curvature,sandContent,slopeShape);
            map.put("probability",String.valueOf(probability));
            if(probability<=0.3){
                map.put("level","低");
            }else if(probability<=0.7){
                map.put("level","中");
            }else{
                map.put("level","高");
            }
            list.add(map);
        }
        return list;
    };

    public static double calculateLandslideProbability(
            double elevation, double slope, int soilType, int landUseType,
            double vegetationCover, double curvature, double sandContent, int slopeShape) {

        // 计算各项的贡献值
        double elevationTerm = ELEVATION_COEF * elevation;
        double slopeTerm = SLOPE_COEF * slope;
        double soilTypeTerm = SOIL_TYPE_COEF * soilType;
        double landUseTerm = LAND_USE_COEF * landUseType;
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
                + vegetationTerm
                + curvatureTerm
                + sandContentTerm
                + slopeShapeTerm;

        // 确保概率在合理范围内（0-1之间）
        return Math.max(0, Math.min(1, probability));
//        return probability;
    }

    public static int soilTypeToCode(String soilType) {
        if (soilType == null) throw new IllegalArgumentException("soilType 不能为 null");
        switch (soilType.trim()) {
            case "碎石土": return 1;
            case "黄土":   return 2;
            case "石渣土": return 3;
            case "砂类土": return 4;
            default: throw new IllegalArgumentException("未知岩土类型: " + soilType);
        }
    }
    public static int landUseToCode(String landUseType) {
        if (landUseType == null) throw new IllegalArgumentException("landUseType 不能为 null");
        switch (landUseType.trim()) {
            case "林地": return 1;
            case "耕地": return 2;
            case "居民用地": return 3;
            default: throw new IllegalArgumentException("未知土地利用类型: " + landUseType);
        }
    }
    public static int slopeShapeToCode(String slopeShape) {
        if (slopeShape == null) throw new IllegalArgumentException("slopeShape 不能为 null");
        switch (slopeShape.trim()) {
            case "阶梯": return 1;
            case "直线": return 2;
            case "凸型": return 3;
            case "凹型": return 4;
            default: throw new IllegalArgumentException("未知坡型: " + slopeShape);
        }
    }
}
