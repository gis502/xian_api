package com.ruoyi.web.controller.system;

import com.ruoyi.common.constant.XianConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.DisasterRainDTO;
import com.ruoyi.system.domain.dto.RainComprehensiveTriggerDTO;
import com.ruoyi.system.domain.dto.RainComprehensiveTriggerVO;
import com.ruoyi.system.domain.dto.RainTriggerDTO;
import com.ruoyi.system.domain.entity.RainReportEntity;
import com.ruoyi.system.domain.params.RainQuery;
import com.ruoyi.system.domain.vo.FactorVO;
import com.ruoyi.system.domain.vo.HideVO;
import com.ruoyi.system.domain.vo.TriggerRequest;
import com.ruoyi.system.domain.vo.TriggerVO;
import com.ruoyi.system.service.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/admins/rain")
public class RainTriggerController extends BaseController {

    @Resource
    private IXianDisasterRainService disasterRainService;

    @Resource
    private IModelService modelService;

    @Resource
    private IFeignService feignService;

    @Resource
    private IGeologicalDisasterHideService geologicalDisasterHideService;

    @Resource
    private DownloadreportService downloadreportService;

    @PostMapping("/trigger")
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
            saveDTO.setDisasterName(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) + triggerDTO.getPosition() + "暴雨");
            triggerDTO.setOccurrenceTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            saveDTO.setOccurrenceTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

            Integer rainDisasterId = Integer.parseInt(disasterRainService.saveDisasterRain(saveDTO).toString());
            result.setRainDisasterId(rainDisasterId);

            // 2. 调用第三方服务生成专题图（取降雨量最大的区县）
            RainTriggerDTO thematicDTO = buildRainTriggerDTO(triggerDTO);
            RainQuery rainQuery = feignService.trigger(thematicDTO);

            result.setRainId(rainQuery.getRainId());
            result.setRainQueueId(rainQuery.getRainQueueId());
//            Thread.sleep(20000);

            result.setReportDownloadUrl(XianConstants.IP3 + "/docs/output/storm-disaster/reports/" + rainQuery.getRainId() + "评估报告.docx");

            result.setRainFullName(thematicDTO.getPosition() + thematicDTO.getRainfall() + "毫米降雨量");

            // 3. 执行模型计算（隐患点匹配和风险计算）- 集成 model/rain/trigger 功能
            List<TriggerVO> modelResults;
            if (triggerDTO.getModelDataList() != null && !triggerDTO.getModelDataList().isEmpty()) {
                // 将前端的 modelDataList 转换为 TriggerRequest 格式
                TriggerRequest triggerRequest = convertToTriggerRequest(triggerDTO.getModelDataList());

                // 调用 modelService.rainTrigger() 进行模型计算
                modelResults = modelService.rainTrigger(triggerRequest);
            } else {
                // 如果前端没有传递 modelDataList，则后端自动从数据库查询
                // 解析区县名称列表
                List<String> countyNames = parseCountyNames(triggerDTO.getPosition());

                // 从数据库查询所有区县的隐患点
                List<HideVO> allPoints = getAllDisasterPointsByCounties(countyNames);

                // 将灾害点数据转换为 TriggerRequest 格式，并设置对应的降雨量
                TriggerRequest triggerRequest = buildTriggerRequestFromDatabase(
                        allPoints, triggerDTO.getRainfall(), countyNames);

                // 调用 modelService.rainTrigger() 进行模型计算
                modelResults = modelService.rainTrigger(triggerRequest);
            }
//            result.setModelResults(modelResults);

            RainReportEntity rainReportEntity = downloadreportService.generateRainReportEntity(rainDisasterId);
            String content = String.format(
                    "受持续强降雨影响，在%s%d个地质灾害风险区、%d个地质灾害在测隐患点的范围内，结合了%s和近年来历史灾害数据共11类致灾因子的632条数据进行评估，" +
                            "本次暴雨预计可能形成%s复合灾害链，并评估得到%s的地质灾害风险显著上升，需高度警惕其中%d个地质灾害在测隐患点发生山洪、泥石流等次生灾害发生的可能性。",
                    rainReportEntity.getConcentratedAreaPosition(),                    // %s
                    rainReportEntity.getRiskAreaQuantity(),                           // %d
                    rainReportEntity.getHideAreaQuantity(),                           // %d
                    rainReportEntity.getHazards() != null ?
                            String.join("、", rainReportEntity.getHazards()) : "",         //手动转换List
                    rainReportEntity.getDisasterChain(),                              // %s
                    rainReportEntity.getSignificantIncreaseArea(),                    // %s
                    rainReportEntity.getSignificantIncreaseAreaHideQuantity()         // %d
            );
            result.setContent(content);
//            System.out.println(modelResults);

            // 异步产生报告
            new Thread(() -> {
                while(true) {
                    try {
                        Thread.sleep(1000);
                        int count = feignService.getGraphCount(rainQuery);
                        if(count == 15){
                            downloadreportService.generateRainReport(rainQuery.getRainId(),rainQuery.getRainQueueId(), rainDisasterId);
                            break;
                        }
                    }catch (Exception ignored){}
                }
            }).start();

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
     * 批量根据区县名称查询灾害点（仅隐患点）
     * @param countyNames 区县名称列表
     * @return 灾害点 VO 列表
     */
    private List<HideVO> getAllDisasterPointsByCounties(List<String> countyNames) {
        List<HideVO> allPoints = new ArrayList<>();

        // 查询隐患点（滑坡、泥石流、山洪、内涝）
        for (String countyName : countyNames) {
            List<HideVO> countyPoints = geologicalDisasterHideService.getHiddenDisasterPointsByCounty(countyName);
            if (countyPoints != null && !countyPoints.isEmpty()) {
                allPoints.addAll(countyPoints);
            }
        }

        return allPoints;
    }

    /**
     * 从数据库查询的数据构建 TriggerRequest
     * @param allPoints 所有灾害点列表
     * @param rainfallStr 降雨量字符串（逗号分隔）
     * @param countyNames 区县名称列表
     */
    private TriggerRequest buildTriggerRequestFromDatabase(
            List<HideVO> allPoints,
            String rainfallStr,
            List<String> countyNames) {

        TriggerRequest request = new TriggerRequest();
        List<TriggerVO> triggerVOList = new ArrayList<>();

        // 解析降雨量数组
        String[] rainfalls = rainfallStr.split(",");

        // 按区县分组灾害点
        Map<String, List<HideVO>> pointsByCounty = new HashMap<>();
        for (HideVO point : allPoints) {
            String county = point.getGeologicalDisasterHideDTO().getCounty();
            pointsByCounty.computeIfAbsent(county, k -> new ArrayList<>()).add(point);
        }

        // 遍历每个区县的灾害点
        for (int i = 0; i < countyNames.size(); i++) {
            String countyName = countyNames.get(i);
            List<HideVO> points = pointsByCounty.getOrDefault(countyName, new ArrayList<>());

            // 获取当前区县的降雨量（如果降雨量数量少于区县数量，使用最后一个）
            double currentRainfall = 0;
            if (i < rainfalls.length) {
                currentRainfall = Double.parseDouble(rainfalls[i]);
            } else if (rainfalls.length > 0) {
                currentRainfall = Double.parseDouble(rainfalls[rainfalls.length - 1]);
            }

            // 为该区县的所有灾害点设置降雨量因子
            for (HideVO point : points) {
                TriggerVO triggerVO = new TriggerVO();

                // 设置 entityId（与前端逻辑一致）
                String entityId = buildEntityId(point);
                triggerVO.setEntityId(entityId);

                triggerVO.setDisasterType(point.getGeologicalDisasterHideDTO().getDisasterType());
                triggerVO.setLon(point.getGeologicalDisasterHideDTO().getLon());
                triggerVO.setLat(point.getGeologicalDisasterHideDTO().getLat());

                // 处理因子列表，更新降雨量
                List<FactorVO> factors = point.getFactorVoList();
//                System.out.println("factors: " + factors);
                if (factors != null) {
                    for (FactorVO factor : factors) {
                        if ("rainfall".equals(factor.getAttributeNameAlias())) {
                            factor.setFactorValue(String.valueOf(currentRainfall));
//                            System.out.println("更新降雨量：" + factor.getFactorValue());
                            break;
                        }
                    }
                }else{
                    factors = new ArrayList<>();
                }

                triggerVO.setFactors(factors);
                triggerVOList.add(triggerVO);
                // 初始化概率、等级、灾害类型列表
                triggerVO.setProbability(new ArrayList<>());
                triggerVO.setLevel(new ArrayList<>());
                triggerVO.setDisaster(new ArrayList<>());
            }
        }

        request.setData(triggerVOList);
        return request;
    }

    /**
     * 构建实体 ID（与前端逻辑一致）
     */
    private String buildEntityId(HideVO point) {
        String disasterType = point.getGeologicalDisasterHideDTO().getDisasterType();
        Integer id = point.getGeologicalDisasterHideDTO().getId();
        if ("滑坡".equals(disasterType)) {
            return "滑坡隐患点" + id;
        } else if ("泥石流".equals(disasterType)) {
            return "泥石流隐患点" + id;
        } else if ("内涝".equals(disasterType)) {
            return "内涝隐患点" + id;
        } else if ("山洪".equals(disasterType)) {
            return "山洪隐患点" + id;
        }
        return "隐患点" + id;
    }
}
