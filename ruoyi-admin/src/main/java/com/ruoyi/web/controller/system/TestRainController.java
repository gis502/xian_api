package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.DisasterPointDTO;
import com.ruoyi.system.domain.dto.DisasterRainDTO;
import com.ruoyi.system.domain.dto.RainComprehensiveTriggerDTO;
import com.ruoyi.system.domain.dto.RainComprehensiveTriggerVO;
import com.ruoyi.system.domain.dto.RainTriggerDTO;
import com.ruoyi.system.domain.entity.GeologicalDisasterRisk;
import com.ruoyi.system.domain.params.RainQuery;
import com.ruoyi.system.domain.vo.FactorVO;
import com.ruoyi.system.domain.vo.HideVO;
import com.ruoyi.system.domain.vo.TriggerRequest;
import com.ruoyi.system.domain.vo.TriggerVO;
import com.ruoyi.system.service.IFeignService;
import com.ruoyi.system.service.IGeologicalDisasterHideService;
import com.ruoyi.system.service.IGeologicalDisasterRiskService;
import com.ruoyi.system.service.IModelService;
import com.ruoyi.system.service.IXianDisasterRainService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/disaster")
public class TestRainController extends BaseController {

    @Resource
    private IXianDisasterRainService disasterRainService;

    @Resource
    private IModelService modelService;

    @Resource
    private IFeignService feignService;

    @Resource
    private IGeologicalDisasterHideService geologicalDisasterHideService;

    @Resource
    private IGeologicalDisasterRiskService geologicalDisasterRiskService;

    @PostMapping("/testRain/trigger")
    public AjaxResult rainComprehensiveTrigger(@RequestBody RainComprehensiveTriggerDTO triggerDTO) {
        try {
            RainComprehensiveTriggerVO result = new RainComprehensiveTriggerVO();

            // 1. 保存暴雨灾害数据
            DisasterRainDTO saveDTO = new DisasterRainDTO();
            saveDTO.setRainfall(triggerDTO.getRainfall());
            saveDTO.setDuration(triggerDTO.getDuration());
            saveDTO.setLongitude(triggerDTO.getLongitude());
            saveDTO.setLatitude(triggerDTO.getLatitude());
            saveDTO.setPosition(triggerDTO.getPosition());
            saveDTO.setRainType(triggerDTO.getRainType());
            saveDTO.setDisasterName(triggerDTO.getDisasterName());
            saveDTO.setOccurrenceTime(triggerDTO.getOccurrenceTime());

            Long rainDisasterId = disasterRainService.saveDisasterRain(saveDTO);
            result.setRainDisasterId(rainDisasterId);

            // 2. 调用第三方服务生成专题图（取降雨量最大的区县）
            RainTriggerDTO thematicDTO = buildRainTriggerDTO(triggerDTO);
            RainQuery rainQuery = feignService.trigger(thematicDTO);

            result.setRainId(rainQuery.getRainId());
            result.setRainQueueId(rainQuery.getRainQueueId());
            result.setRainFullName(thematicDTO.getPosition() + thematicDTO.getRainfall() + "毫米降雨量");

            // 3. 执行模型计算（隐患点匹配和风险计算）- 集成 model/rain/trigger 功能
            List<TriggerVO> modelResults = new ArrayList<>();
            if (triggerDTO.getModelDataList() != null && !triggerDTO.getModelDataList().isEmpty()) {
                // 将前端的 modelDataList 转换为 TriggerRequest 格式
                TriggerRequest triggerRequest = convertToTriggerRequest(triggerDTO.getModelDataList());

                // 调用 modelService.rainTrigger() 进行模型计算
                modelResults = modelService.rainTrigger(triggerRequest);
            } else {
                // 如果前端没有传递 modelDataList，则后端自动从数据库查询
                // 解析区县名称列表
                List<String> countyNames = parseCountyNames(triggerDTO.getPosition());

                // 从数据库查询所有区县的隐患点和风险区域
                List<DisasterPointDTO> allPoints = getAllDisasterPointsByCounties(countyNames);

                // 将灾害点数据转换为 TriggerRequest 格式，并设置对应的降雨量
                TriggerRequest triggerRequest = buildTriggerRequestFromDatabase(
                        allPoints, triggerDTO.getRainfall(), countyNames);

                // 调用 modelService.rainTrigger() 进行模型计算
                modelResults = modelService.rainTrigger(triggerRequest);
            }
            result.setModelResults(modelResults);

            return AjaxResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("暴雨综合触发失败：" + e.getMessage());
        }
    }

    /**
     * 构建第三方专题图触发 DTO（取降雨量最大的区县）
     */
    private RainTriggerDTO buildRainTriggerDTO(RainComprehensiveTriggerDTO triggerDTO) {
        RainTriggerDTO dto = new RainTriggerDTO();

        // 解析多个区县的降雨量，找到最大值
        String[] positions = triggerDTO.getPosition().split(",");
        String[] rainfalls = triggerDTO.getRainfall().split(",");
        String longitudeStr = triggerDTO.getLongitude().toString();
        String latitudeStr = triggerDTO.getLatitude().toString();

        // 注意：longitude 和 latitude 可能是单个值（中心点）或逗号分隔的数组
        String[] longitudes = longitudeStr.contains(",") ? longitudeStr.split(",") : new String[]{longitudeStr};
        String[] latitudes = latitudeStr.contains(",") ? latitudeStr.split(",") : new String[]{latitudeStr};

        double maxRainfall = 0;
        int maxIndex = 0;

        for (int i = 0; i < rainfalls.length; i++) {
            double rainfall = Double.parseDouble(rainfalls[i]);
            if (rainfall > maxRainfall) {
                maxRainfall = rainfall;
                maxIndex = i;
            }
        }

        dto.setPosition(positions[maxIndex]);
        dto.setRainfall(String.valueOf(maxRainfall));

        // 确保索引不越界
        int lonIndex = Math.min(maxIndex, longitudes.length - 1);
        int latIndex = Math.min(maxIndex, latitudes.length - 1);

        dto.setLongitude(Double.parseDouble(longitudes[lonIndex]));
        dto.setLatitude(Double.parseDouble(latitudes[latIndex]));
        dto.setDuration(triggerDTO.getDuration());
        dto.setRainType(triggerDTO.getRainType());
        dto.setOccurrenceTime(triggerDTO.getOccurrenceTime());

        return dto;
    }

    /**
     * 将 ModelData 列表转换为 TriggerRequest 格式
     */
    private TriggerRequest convertToTriggerRequest(List<RainComprehensiveTriggerDTO.RainModelData> dataList) {
        TriggerRequest request = new TriggerRequest();
        List<TriggerVO> triggerVOList = new ArrayList<>();

        for (RainComprehensiveTriggerDTO.RainModelData data : dataList) {
            TriggerVO triggerVO = new TriggerVO();
            triggerVO.setEntityId(data.getEntityId());
            triggerVO.setDisasterType(data.getDisasterType());
            triggerVO.setLon(data.getLon());
            triggerVO.setLat(data.getLat());
            triggerVO.setFactors(data.getFactors());

            triggerVOList.add(triggerVO);
        }

        request.setData(triggerVOList);
        return request;
    }

    /**
     * 解析区县名称列表
     */
    private List<String> parseCountyNames(String positionStr) {
        List<String> countyNames = new ArrayList<>();
        if (positionStr != null && !positionStr.isEmpty()) {
            String[] positions = positionStr.split(",");
            for (String position : positions) {
                countyNames.add(position.trim());
            }
        }
        return countyNames;
    }

    /**
     * 批量根据区县名称查询灾害点（包括隐患点和风险区域）
     * @param countyNames 区县名称列表
     * @return 灾害点 DTO 列表
     */
    private List<DisasterPointDTO> getAllDisasterPointsByCounties(List<String> countyNames) {
        List<DisasterPointDTO> allPoints = new ArrayList<>();

        // 1. 查询隐患点（滑坡、泥石流、山洪、内涝）
        for (String countyName : countyNames) {
            List<HideVO> countyPoints = geologicalDisasterHideService.getHiddenDisasterPointsByCounty(countyName);
            for (HideVO hidePoint : countyPoints) {
                DisasterPointDTO dto = DisasterPointDTO.fromHideDTO(hidePoint.getGeologicalDisasterHideDTO());
                if (dto != null) {
                    allPoints.add(dto);
                }
            }
        }

        // 2. 查询风险区域
        for (String countyName : countyNames) {
            List<DisasterPointDTO> riskPoints = getRiskAreasByCounty(countyName);
            allPoints.addAll(riskPoints);
        }

        return allPoints;
    }

    /**
     * 根据区县名称查询风险区域，并转换为 DisasterPointDTO 格式
     * @param countyName 区县名称
     * @return 风险区域 DTO 列表
     */
    private List<DisasterPointDTO> getRiskAreasByCounty(String countyName) {
        List<DisasterPointDTO> riskDTOList = new ArrayList<>();

        // 查询该区县的风险区域
        List<GeologicalDisasterRisk> riskList = geologicalDisasterRiskService.selectByCounty(countyName);

        if (riskList == null || riskList.isEmpty()) {
            return riskDTOList;
        }

        // 转换为 DisasterPointDTO
        for (GeologicalDisasterRisk risk : riskList) {
            DisasterPointDTO dto = new DisasterPointDTO();
            dto.setId(risk.getId());
            dto.setCode(risk.getUnitCode());  // 风险区域使用 unitCode
            dto.setCounty(risk.getCounty());
            dto.setCountyId(null);  // 风险区域可能没有 countyId
            dto.setVillage(risk.getVillage());
            dto.setDisasterName(risk.getDisasterName());
            dto.setLatitude(risk.getLat().toString());
            dto.setLongitude(risk.getLon().toString());
            dto.setLon(risk.getLon());
            dto.setLat(risk.getLat());
            dto.setPosition(risk.getPosition());
            dto.setDisasterType("风险区域");
            dto.setScaleGrade(risk.getRiskLevel());  // 风险区域使用 riskLevel
            dto.setRiskGrade(null);

            riskDTOList.add(dto);
        }

        return riskDTOList;
    }

    /**
     * 从数据库查询的数据构建 TriggerRequest
     * @param allPoints 所有灾害点列表
     * @param rainfallStr 降雨量字符串（逗号分隔）
     * @param countyNames 区县名称列表
     */
    private TriggerRequest buildTriggerRequestFromDatabase(
            List<DisasterPointDTO> allPoints,
            String rainfallStr,
            List<String> countyNames) {

        TriggerRequest request = new TriggerRequest();
        List<TriggerVO> triggerVOList = new ArrayList<>();

        // 解析降雨量数组
        String[] rainfalls = rainfallStr.split(",");

        // 按区县分组灾害点
        Map<String, List<DisasterPointDTO>> pointsByCounty = new HashMap<>();
        for (DisasterPointDTO point : allPoints) {
            String county = point.getCounty();
            pointsByCounty.computeIfAbsent(county, k -> new ArrayList<>()).add(point);
        }

        // 遍历每个区县的灾害点
        for (int i = 0; i < countyNames.size(); i++) {
            String countyName = countyNames.get(i);
            List<DisasterPointDTO> points = pointsByCounty.getOrDefault(countyName, new ArrayList<>());

            // 获取当前区县的降雨量（如果降雨量数量少于区县数量，使用最后一个）
            double currentRainfall = 0;
            if (i < rainfalls.length) {
                currentRainfall = Double.parseDouble(rainfalls[i]);
            } else if (rainfalls.length > 0) {
                currentRainfall = Double.parseDouble(rainfalls[rainfalls.length - 1]);
            }

            // 为该区县的所有灾害点设置降雨量因子
            for (DisasterPointDTO point : points) {
                TriggerVO triggerVO = new TriggerVO();

                // 设置 entityId（与前端逻辑一致）
                String entityId = buildEntityId(point);
                triggerVO.setEntityId(entityId);

                triggerVO.setDisasterType(point.getDisasterType());
                triggerVO.setLon(point.getLon());
                triggerVO.setLat(point.getLat());

                // 处理因子列表，更新降雨量
                List<FactorVO> factors = new ArrayList<>();
                // TODO: 如果需要为风险区域添加因子，可以在这里处理
                // 目前只有隐患点有因子数据

                triggerVO.setFactors(factors);
                triggerVOList.add(triggerVO);
            }
        }

        request.setData(triggerVOList);
        return request;
    }

    /**
     * 构建实体 ID（与前端逻辑一致）
     */
    private String buildEntityId(DisasterPointDTO point) {
        String disasterType = point.getDisasterType();
        if ("风险区域".equals(disasterType)) {
            return "风险区域" + point.getCode();
        } else if ("滑坡".equals(disasterType)) {
            return "滑坡隐患点" + point.getId();
        } else if ("泥石流".equals(disasterType)) {
            return "泥石流隐患点" + point.getId();
        } else if ("内涝".equals(disasterType)) {
            return "内涝隐患点" + point.getId();
        } else if ("山洪".equals(disasterType)) {
            return "山洪隐患点" + point.getId();
        }
        return "隐患点" + point.getId();
    }
}
