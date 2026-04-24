package com.ruoyi.system.service.impl;

import com.ruoyi.common.config.DocumentConfig;
import com.ruoyi.common.constant.XianConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.ImagePositionEnum;
import com.ruoyi.common.enums.ImageTypeEnum;
import com.ruoyi.common.enums.TypesOfSecondaryDisasters;
import com.ruoyi.common.utils.file.DocumentUtils;
import com.ruoyi.system.domain.dto.RainOutputDTO;
import com.ruoyi.system.domain.entity.*;
import com.ruoyi.system.domain.params.RainQuery;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.DownloadreportService;
import com.ruoyi.system.service.IFeignService;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.ruoyi.system.service.IModelService;

import javax.annotation.Resource;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:04
 * @description: 风险区实现类
 */
@Service
public class DownloadreportServiceImpl implements DownloadreportService {

    @Resource
    private IModelService modelService;

    @Resource
    private PeopleMapper peopleMapper;

    @Resource
    private AnalysisRainMapper analysisRainMapper;

    @Resource
    private final XianDisasterRainMapper xianDisasterRainMapper;

    @Resource
    private FactorAttributeMapper factorAttributeMapper;

    @Resource
    private XianStreetMapper xianStreetMapper;

    @Resource
    private XianFactorAnalysisMapper xianFactorAnalysisMapper;

    @Resource
    private GeologicalDisasterHideMapper geologicalDisasterHideMapper;

    @Resource
    private XianImpactInAreaMapper xianImpactInAreaMapper;

    @Resource
    private GeologicalDisasterRiskMapper geologicalDisasterRiskMapper;

    @Resource

    // word保存路径
    @Value("${document.path.rain.report}")
    private String wordPath;

    @Resource
    private IFeignService feignService;

    @Resource
    private RainAssessmentOutputMybatisMapper rainAssessmentOutputMybatisMapper;

    public DownloadreportServiceImpl(XianDisasterRainMapper xianDisasterRainMapper) {
        this.xianDisasterRainMapper = xianDisasterRainMapper;
    }

    //生成报告
    @Override
    public R<String> generateRainReport(String rainId, String rainQueueId, Integer rainDisasterId) throws IOException {
        // 获取报告数据
        RainReportEntity rainReportEntity = generateRainReportEntity(rainDisasterId);
        rainReportEntity.setRainQueueId(rainQueueId);
        rainReportEntity.setRainId(rainId);

        // 生成 Word 路径
        Path wordDir = Paths.get(wordPath);
        if (!Files.exists(wordDir)) {
            Files.createDirectories(wordDir);
        }
        String wordName = rainId + "评估报告.docx";
        String wordPath = wordDir.resolve(wordName).toAbsolutePath().toString();

        // 表头宽度
        // int[] widths = {1500, 3000, 8000, 3000};
        try {
            createRainReport(wordPath, rainReportEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // word 文档存库
        RainAssessmentOutput output = new RainAssessmentOutput();
        output.setId(UUID.randomUUID().toString());
        output.setRainId(rainId);
        output.setFileType("文档");
        output.setFileName(wordName);
        output.setFileExtension(".docx");
        output.setLocalSourceFile(XianConstants.IP3 + wordPath.replace("\\", "/").substring(wordPath.indexOf("/docs")));
        output.setType(2);
        output.setCreateTime(LocalDateTime.now());
        output.setIsDeleted(0);

        try {
            rainAssessmentOutputMybatisMapper.insertRainAssessmentOutput(output);
        } catch (Exception e) {
            System.err.println("数据库插入异常：" + e.getMessage());
            e.printStackTrace();
        }

        return R.ok(wordName);
    }

    public RainReportEntity generateRainReportEntity(Integer disasterId) {
        Long id = Long.valueOf(disasterId);
        RainReportEntity rainReportEntity = new RainReportEntity();
        Map<String, String> areaCodeMap = new HashMap<>();
        areaCodeMap.put("新城区", "610102");
        areaCodeMap.put("碑林区", "610103");
        areaCodeMap.put("莲湖区", "610104");
        areaCodeMap.put("雁塔区", "610113");
        areaCodeMap.put("灞桥区", "610111");
        areaCodeMap.put("未央区", "610112");
        areaCodeMap.put("阎良区", "610114");
        areaCodeMap.put("临潼区", "610115");
        areaCodeMap.put("长安区", "610116");
        areaCodeMap.put("高陵区", "610117");
        areaCodeMap.put("鄠邑区", "610118");
        areaCodeMap.put("蓝田县", "610122");
        areaCodeMap.put("周至县", "610124");

        /* 第一部分 */
        /* 报告时间 reportTime */
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter reportTimeFormatter = DateTimeFormatter.ofPattern("MM月dd日HH时mm分");
        String reportTime = now.format(reportTimeFormatter);

        /* 降雨时间 rainTime */
        String rainTimeStr = xianDisasterRainMapper.getRainTime(id);
        String rainTime = LocalDateTime.parse(rainTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .format(DateTimeFormatter.ofPattern("YYYY年MM月dd日HH时"));

        /* 降雨区域 rainAreaPosition 处理position字符串，将逗号分隔的字符串转换为List*/
        String position = xianDisasterRainMapper.getRainAreaPosition(id);
        List<String> rainAreaPosition = new ArrayList<>();
        if (position != null && !position.trim().isEmpty()) {
            String[] areas = position.split(",");
            for (String area : areas) {
                rainAreaPosition.add(area.trim()); // trim()去除可能的空格
            }
        }

        /* 区域降雨量 rainAreaQuantity */
        String quantity = xianDisasterRainMapper.getRainAreaQuantity(id);
        List<String> rainAreaQuantity = new ArrayList<>();
        if (quantity != null && !quantity.trim().isEmpty()) {
            String[] rains = quantity.split(",");
            for (String rain : rains) {
                rainAreaQuantity.add(rain.trim()); // trim()去除可能的空格
            }
        }

        /* 降雨集中区域 concentratedAreaPosition */
        String concentratedAreaPosition = "";
        int maxIndex = -1;
        if (!rainAreaQuantity.isEmpty()) {
            double maxValue = Double.MIN_VALUE;
            for (int i = 0; i < rainAreaQuantity.size(); i++) {
                double currentValue = Double.parseDouble(rainAreaQuantity.get(i));
                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    maxIndex = i;
                }
            }
        }
        concentratedAreaPosition = rainAreaPosition.get(maxIndex);

        /* 降雨集中区域雨量 concentratedAreaQuantity */
        String concentratedAreaQuantity = "NaN";

        /* 降雨集中区域平均雨量 concentratedAreaAverageQuantity */
        String concentratedAreaAverageQuantity = "NaN";

        /* 降雨集中区域街道 concentratedAreaDetailStreet */
        List<String> concentratedAreaDetailStreet = new ArrayList<>();
        String areaCode = areaCodeMap.get(concentratedAreaPosition);
        List<AnalysisRain> analysisRains = analysisRainMapper.getRainStation(areaCode);
        // 按站点名称分组
        Map<String, List<AnalysisRain>> stationGroups = new HashMap<>();
        for (AnalysisRain rain : analysisRains) {
            String stationName = rain.getStationName();
            if (!stationGroups.containsKey(stationName)) {
                stationGroups.put(stationName, new ArrayList<>());
            }
            stationGroups.get(stationName).add(rain);
        }
        // 对每个站点的数据按时间排序
        for (String stationName : stationGroups.keySet()) {
            List<AnalysisRain> stationData = stationGroups.get(stationName);
            // 按时间降序排序（最新的在前面）
            stationData.sort((a, b) -> b.getDatetime().compareTo(a.getDatetime()));
        }
        // 创建结果列表
        List<Map<String, String>> rainfallSummary = new ArrayList<>();
        // 遍历每个站点分组
        for (String stationName : stationGroups.keySet()) {
            List<AnalysisRain> stationData = stationGroups.get(stationName);
            // 累加该站点的所有pre1h值
            double totalRainfall = 0.0;
            float lat = 0;
            float lon = 0;
            for (AnalysisRain rain : stationData) {
                if (rain.getPre1h() != null) {
                    try {
                        totalRainfall += Double.parseDouble(rain.getPre1h());
                    } catch (NumberFormatException e) {
                        // 如果转换失败，跳过这个值
                        System.out.println("无法解析降雨量数据..." );
                    }
                }
                if (lat == 0 && lon == 0) {
                    lat = rain.getLat();
                    lon = rain.getLon();
                }
            }
            // 创建Map存储站点名称和累计降雨量
            Map<String, String> stationRainfall = new HashMap<>();
            stationRainfall.put("stationName", stationName);
            stationRainfall.put("rainfall", String.valueOf(totalRainfall));
            stationRainfall.put("lat", String.valueOf(lat));
            stationRainfall.put("lon", String.valueOf(lon));
            // 添加到结果列表
            rainfallSummary.add(stationRainfall);
        }
        rainfallSummary.sort(
                (a, b) -> Double.compare(
                        Double.parseDouble(b.get("rainfall")),
                        Double.parseDouble(a.get("rainfall"))
                )
        );
        String stationStreet1 = "";
        String stationStreet2 = "";
        String stationStreet3 = "";

        // 根据rainfallSummary的实际大小安全地设置各变量值
        if(rainfallSummary != null && !rainfallSummary.isEmpty()){
            // 处理第一个元素
            if(rainfallSummary.size() > 0){
                // 修改点：接收 List，取第一个
                List<String> list1 = xianStreetMapper.inStreet(
                        Float.parseFloat(rainfallSummary.get(0).get("lat")),
                        Float.parseFloat(rainfallSummary.get(0).get("lon"))
                );
                stationStreet1 = (list1 != null && !list1.isEmpty()) ? list1.get(0) : "未知";
            }

            // 处理第二个元素
            if(rainfallSummary.size() > 1){
                List<String> list2 = xianStreetMapper.inStreet(
                        Float.parseFloat(rainfallSummary.get(1).get("lat")),
                        Float.parseFloat(rainfallSummary.get(1).get("lon"))
                );
                stationStreet2 = (list2 != null && !list2.isEmpty()) ? list2.get(0) : "未知";
            }

            // 处理第三个元素
            if(rainfallSummary.size() > 2){
                List<String> list3 = xianStreetMapper.inStreet(
                        Float.parseFloat(rainfallSummary.get(2).get("lat")),
                        Float.parseFloat(rainfallSummary.get(2).get("lon"))
                );
                stationStreet3 = (list3 != null && !list3.isEmpty()) ? list3.get(0) : "未知";
            }
        }
        // 如果rainfallSummary为null或空，变量保持初始值空字符串

        concentratedAreaDetailStreet.add(stationStreet1);
        concentratedAreaDetailStreet.add(stationStreet2);
        concentratedAreaDetailStreet.add(stationStreet3);
        // 查看结果
        // System.out.println("=== 分组并排序后的数据 ===");
        // for (String stationName : stationGroups.keySet()) {
        //     List<AnalysisRain> stationData = stationGroups.get(stationName);
        //     System.out.println("站点: " + stationName + ", 数据条数: " + stationData.size());
        //     for (AnalysisRain rain : stationData) {
        //         System.out.println("  时间: " + rain.getDatetime() + ", 经度: " + rain.getLon() + ", 纬度: " + rain.getLat() + ",降雨量：" + rain.getPre1h());
        //     }
        // }
        // System.out.println(JSON.toJSONString(stationGroups));
        // System.out.println(JSON.toJSONString(rainfallSummary));

        /* 降雨集中街道雨量 concentratedAreaDetailQuantity */
        List<String> concentratedAreaDetailQuantity = new ArrayList<>();
        // 确保始终添加3个元素，根据rainfallSummary的实际大小设置值
        // 添加第一个元素
        if(rainfallSummary != null && !rainfallSummary.isEmpty() && rainfallSummary.size() > 0){
            concentratedAreaDetailQuantity.add(rainfallSummary.get(0).get("rainfall"));
        } else {
            concentratedAreaDetailQuantity.add("0");
        }

        // 添加第二个元素
        if(rainfallSummary != null && rainfallSummary.size() > 1){
            concentratedAreaDetailQuantity.add(rainfallSummary.get(1).get("rainfall"));
        } else {
            concentratedAreaDetailQuantity.add("0");
        }

        // 添加第三个元素
        if(rainfallSummary != null && rainfallSummary.size() > 2){
            concentratedAreaDetailQuantity.add(rainfallSummary.get(2).get("rainfall"));
        } else {
            concentratedAreaDetailQuantity.add("0");
        }


        /* 降雨集中街道等级  concentratedAreaDetailGrade */
        List<String> concentratedAreaDetailGrade = new ArrayList<>();
        // 降雨等级阈值和对应名称
        double[] thresholds = {0.1, 5.0, 15.0, 30.0, 70.0, 140.0};
        String[] grades = {"微量降雨(零星小雨)", "小雨", "中雨", "大雨", "暴雨", "大暴雨", "特大暴雨"};

        for (String item : concentratedAreaDetailQuantity) {
            boolean flag = true;
            double rainfall = Double.parseDouble(item);
            for (int i = 0; i < thresholds.length; i++) {
                if (rainfall < thresholds[i]) {
                    concentratedAreaDetailGrade.add(grades[i]);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                concentratedAreaDetailGrade.add(grades[grades.length - 1]);
            }
        }

        /* 特大暴雨监测街道 extremelyHeavyRainstormStreet */
        List<String> extremelyHeavyRainstormStreet = new ArrayList<>();
        /* 特大暴雨监测雨量  extremelyHeavyRainQuantity */
        List<String> extremelyHeavyRainQuantity = new ArrayList<>();
        /* 暴雨或大暴雨街道 rainstormStreet */
        List<String> rainstormStreet = new ArrayList<>();
        /* 暴雨或大暴雨雨量 rainstormQuantity */
        List<String> rainstormQuantity = new ArrayList<>();

        // 设置报告时间到实体对象中
        rainReportEntity.setReportTime(reportTime);
        rainReportEntity.setRainTime(rainTime);
        rainReportEntity.setRainAreaPosition(rainAreaPosition);
        rainReportEntity.setRainAreaQuantity(rainAreaQuantity);
        rainReportEntity.setConcentratedAreaPosition(concentratedAreaPosition);
        rainReportEntity.setConcentratedAreaQuantity(concentratedAreaQuantity);
        rainReportEntity.setConcentratedAreaAverageQuantity(concentratedAreaAverageQuantity);
        rainReportEntity.setConcentratedAreaDetailStreet(concentratedAreaDetailStreet);
        rainReportEntity.setConcentratedAreaDetailQuantity(concentratedAreaDetailQuantity);
        rainReportEntity.setConcentratedAreaDetailGrade(concentratedAreaDetailGrade);


        /* 第二部分 */

        // 获取此次灾害所有次生灾害计算的概率
        List<Map<String, Object>> disasterEstimation = xianFactorAnalysisMapper.queryDisasterEstimation(id);
        List<String> chain = new ArrayList<>();
        Map<String, List<Map<String, Object>>> groupedByDisasterType = new HashMap<>();
        // 遍历disasterEstimation列表进行分组
        for (Map<String, Object> item : disasterEstimation) {
            String disasterType = (String) item.get("disaster_type");
            if (!chain.contains(disasterType)) {
                chain.add(disasterType);
            }
            // 如果该disaster_type还没有对应的列表，创建一个新的列表
            if (!groupedByDisasterType.containsKey(disasterType)) {
                groupedByDisasterType.put(disasterType, new ArrayList<>());
            }
            // 将当前元素添加到对应的列表中
            groupedByDisasterType.get(disasterType).add(item);
        }
        // 对每个分组内的列表按disaster_probability从高到低排序
        for (List<Map<String, Object>> list : groupedByDisasterType.values()) {
            // 修改排序逻辑，使用BigDecimal
            list.sort((o1, o2) -> {
                BigDecimal prob1 = (BigDecimal) o1.get("disaster_probability");
                BigDecimal prob2 = (BigDecimal) o2.get("disaster_probability");
                return prob2.compareTo(prob1); // 降序排列
            });
        }
//        System.out.println(JSON.toJSONString(disasterEstimation));
//        System.out.println(JSON.toJSONString(groupedByDisasterType));

        /* riskAreaQuantity 风险区数量 */
        Integer riskAreaQuantity = geologicalDisasterRiskMapper.getHideNumByCounty(concentratedAreaPosition);

        /* hideAreaQuantity 隐患点数量 */
        Integer hideAreaQuantity = geologicalDisasterHideMapper.getRiskNumByCounty(concentratedAreaPosition);

        /* hazards 致灾因子 */
        List<String> hazards = new ArrayList<>();
        hazards = factorAttributeMapper.getFactorAttributeName();
        hazards.remove("持续时间");
        hazards.remove("土壤沙砾度");
        hazards.remove("坡型");

        /* disasterChain 灾害链 */
        String disasterChain = "";
        Integer countChain = 1;
        for (String item : chain) {
            countChain++;
            if (countChain <= chain.size()) {
                disasterChain = disasterChain + "暴雨-" + item + "、";
            } else {
                disasterChain = disasterChain + "暴雨-" + item;
            }
        }

        /* significantIncreaseArea 风险显著上升区域 */
        String significantIncreaseArea = "";
        StringBuilder sb = new StringBuilder();
        sb.append(concentratedAreaPosition);
        // 智能拼接非空的街道名称，避免多余的顿号
        List<String> validStreets = new ArrayList<>();
        if(stationStreet1 != null && !stationStreet1.isEmpty()) {
            validStreets.add(stationStreet1);
        }
        if(stationStreet2 != null && !stationStreet2.isEmpty()) {
            validStreets.add(stationStreet2);
        }
        if(stationStreet3 != null && !stationStreet3.isEmpty()) {
            validStreets.add(stationStreet3);
        }
        // 如果有非空的街道名称，则拼接它们
        if(!validStreets.isEmpty()) {
            sb.append(String.join("、", validStreets));
        }
        significantIncreaseArea = sb.toString();

        /* significantIncreaseAreaRiskQuantity 显著上升区域中重点关注风险点 */
        Integer significantIncreaseAreaRiskQuantity;
        /* significantIncreaseAreaHideQuantity 显著上升区域中重点关注隐患点 */
        Integer significantIncreaseAreaHideQuantity = 0;
        for (String key : groupedByDisasterType.keySet()) {
            significantIncreaseAreaHideQuantity = significantIncreaseAreaHideQuantity + groupedByDisasterType.get(key).size();
        }


        /* 次生灾害 */

        /* 滑坡 */
        RainReportEntity.SecondaryDisasterReportEntity landslide = handleSecondaryDisasterReport(TypesOfSecondaryDisasters.LANDSLIDE, groupedByDisasterType, "滑坡");
        /* 泥石流 */
        RainReportEntity.SecondaryDisasterReportEntity debrisFlow = handleSecondaryDisasterReport(TypesOfSecondaryDisasters.DEBRIS_FLOW, groupedByDisasterType, "泥石流");
        /* 山洪 */
        RainReportEntity.SecondaryDisasterReportEntity torrentialFlood = handleSecondaryDisasterReport(TypesOfSecondaryDisasters.TORRENTIAL_FLOOD, groupedByDisasterType, "山洪");
        /* 内涝 */
        RainReportEntity.SecondaryDisasterReportEntity waterLogging = handleSecondaryDisasterReport(TypesOfSecondaryDisasters.WATER_LOGGING, groupedByDisasterType, "内涝");


        rainReportEntity.setRiskAreaQuantity(riskAreaQuantity);
        rainReportEntity.setHideAreaQuantity(hideAreaQuantity);
        rainReportEntity.setHazards(hazards);
        rainReportEntity.setDisasterChain(disasterChain);
        rainReportEntity.setSignificantIncreaseArea(significantIncreaseArea);
        rainReportEntity.setSignificantIncreaseAreaHideQuantity(significantIncreaseAreaHideQuantity);
        rainReportEntity.getSecondaryDisasterReport().add(landslide);
        rainReportEntity.getSecondaryDisasterReport().add(debrisFlow);
        rainReportEntity.getSecondaryDisasterReport().add(torrentialFlood);
        rainReportEntity.getSecondaryDisasterReport().add(waterLogging);

        /* 第三部分 */

        /* workScheduleArea 工作安排部署区域 */
        List<String> workScheduleArea = rainAreaPosition;

        /* evacuateTheCrowdArea 山洪、泥石流人员疏散区域 */
        List<String> evacuateTheCrowdArea = concentratedAreaDetailStreet;

        /* focusArea 内涝关注区域 */
        List<String> focusArea = new ArrayList<>();

        rainReportEntity.setWorkScheduleArea(workScheduleArea);
        rainReportEntity.setEvacuateTheCrowdArea(evacuateTheCrowdArea);
        rainReportEntity.setFocusArea(focusArea);

        return rainReportEntity;
    }

    private RainReportEntity.SecondaryDisasterReportEntity handleSecondaryDisasterReport(TypesOfSecondaryDisasters t, Map<String, List<Map<String, Object>>> groupedByDisasterType, String type) {
        RainReportEntity.SecondaryDisasterReportEntity secondaryDisasterReportEntity = new RainReportEntity().new SecondaryDisasterReportEntity();
        /* 灾害类型（滑坡、泥石流等） */
        secondaryDisasterReportEntity.setDisasterType(t);
        if (!groupedByDisasterType.containsKey(type)) {
            return secondaryDisasterReportEntity;
        }
        /* 风险集中的街道 */
        String riskStreet;
        List<String> riskStreetList = new ArrayList<>();
        for (Map<String, Object> item : groupedByDisasterType.get(type)) {
            if (riskStreetList.contains(item.get("village")) && item.get("village") != "[高]") {
                continue;
            }
            riskStreetList.add(item.get("village").toString());
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < riskStreetList.size(); i++) {
            sb.append(riskStreetList.get(i));
            if (i < riskStreetList.size() - 1) {
                sb.append("、");
            }
        }
        riskStreet = sb.toString();
        secondaryDisasterReportEntity.setRiskStreet(riskStreet);
        // System.out.println(riskStreet);
        /* 风险集中点位 */
        String riskPointName;

        /* 风险集中点位概率 */
        String riskPointProbability;


        /* 影响人数 */
        Long influencePeopleQuantity = 0L;

        for (Map<String, Object> item : groupedByDisasterType.get(type)) {

            influencePeopleQuantity = influencePeopleQuantity + xianImpactInAreaMapper.getPeopleByLatLon(item.get("lat").toString(), item.get("lon").toString());

        }
        secondaryDisasterReportEntity.setInfluencePeopleQuantity(influencePeopleQuantity);

        /* 中大型区域 */
        List<String> seriousArea;

        /* 表格数据 */
        // List<RainReportEntity.SecondaryDisasterReportEntity.SecondaryDisasterTableData> disasterTableDataList = new ArrayList<>();

        for (Map<String, Object> item : groupedByDisasterType.get(type)) {
            RainReportEntity.SecondaryDisasterReportEntity.SecondaryDisasterTableData secondaryDisasterTableData = new RainReportEntity().new SecondaryDisasterReportEntity().new SecondaryDisasterTableData();
            secondaryDisasterTableData.setPosition(item.get("position").toString());
            secondaryDisasterTableData.setProbability(item.get("disaster_probability").toString());
            secondaryDisasterTableData.setGrade(removeBrackets(item.get("level").toString()));
            secondaryDisasterReportEntity.getDisasterTableData().add(secondaryDisasterTableData);
        }
        return secondaryDisasterReportEntity;
    }

    //下载报告
    @Override
    public void downloadReport(String fileName, HttpServletResponse resp) throws IOException {
        Path file = Paths.get(wordPath).resolve(fileName).normalize();

        System.out.println("尝试下载文件: {}" + file.toString());
        System.out.println("文件是否存在: {}" + Files.exists(file));

        if (!Files.exists(file)) {
            System.out.println("文件不存在..." );
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("文件不存在: " + fileName);
            return;
        }

        // 添加CORS响应头
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "*");
        resp.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));

        try {
            Files.copy(file, resp.getOutputStream());
            resp.flushBuffer();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 创建word文件
     *
     * @param filePath
     * @param rainReportEntity
     * @throws IOException
     */
    public void createRainReport(String filePath, RainReportEntity rainReportEntity) throws IOException {
        // 创建一个新的Word文档
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(filePath)) {
            // 创建保密提示
            createConfidentialityTip(document, "对内掌握");
            // 三个空行
            DocumentUtils.createBlankLine(document, 3);
            // 创建标题
            DocumentUtils.createTitle(document, "暴雨应急预评估报告");
            // 一个空行
            DocumentUtils.createBlankLine(document, 1);
            // 创建部门信息
            createDept(document, "西安市应急管理局               " + rainReportEntity.getReportTime());
            // 两个空行
            DocumentUtils.createBlankLine(document, 2);
            // 第一部分，降雨概况
            createRainfallOverview(document, rainReportEntity);
            // 第二部分，风险评估
            createRiskAssessment(document, rainReportEntity);

            // 如果没有灾害，就不添加

            if (addEmergencyResponseSuggestions(rainReportEntity)) {
                // 第三部分，应急处置建议
                createEmergencyResponseSuggestions(document, rainReportEntity);
            }

            // 保存文档
            document.write(out);
        }
    }

    /**
     * 创建保密提示
     *
     * @param doc - 文档对象
     * @param tip - 提示文本
     */
    private void createConfidentialityTip(XWPFDocument doc, String tip) {
        XWPFParagraph paragraph = DocumentUtils.addRegularParagraph(doc, tip);
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
    }

    /**
     * 创建部门信息
     *
     * @param doc
     * @param dept
     */
    private void createDept(XWPFDocument doc, String dept) {
        XWPFParagraph paragraph = DocumentUtils.addRegularParagraph(doc, null);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setFirstLineIndent(0);

        XWPFRun run = DocumentUtils.addRegularRun(paragraph, dept);
        run.setFontFamily(DocumentConfig.FONT_FANG_SONG_GB2312);

        // 添加边框线
        DocumentUtils.addParagraphBorderLine(paragraph, null);
    }

    /**
     * 创建降雨概述部分
     *
     * @param doc
     * @param rainReportEntity
     */
    private void createRainfallOverview(XWPFDocument doc, RainReportEntity rainReportEntity) {
        // 标题
        DocumentUtils.addRegularParagraph(doc, "一、降雨概况");

        // 内容
        XWPFParagraph paragraph = DocumentUtils.addRegularParagraph(doc, null);

        String maxHoursRainfall = "";
        if(Float.parseFloat(rainReportEntity.getRainAreaQuantity().get(0))>50){
            maxHoursRainfall = "单小时最大降雨量超过50毫米，";
        }

        String content = String.format("%s，%s12小时累积降雨量达到%s，%s" +
                        "根据最新实时气象监测数据，降雨主要集中在%s一带。",
                rainReportEntity.getRainTime(),
                DocumentUtils.list2Str(rainReportEntity.getRainAreaPosition(), null),
                DocumentUtils.list2Str(rainReportEntity.getRainAreaQuantity(), "毫米"),
                maxHoursRainfall,
                rainReportEntity.getConcentratedAreaPosition(),
//                rainReportEntity.getConcentratedAreaQuantity(),
//                rainReportEntity.getConcentratedAreaAverageQuantity(),
                DocumentUtils.list2Str(rainReportEntity.getConcentratedAreaDetailStreet(), null),
                DocumentUtils.list2Str(rainReportEntity.getConcentratedAreaDetailQuantity(), null),
                DocumentUtils.list2Str(rainReportEntity.getConcentratedAreaDetailGrade(), null)
//                DocumentUtils.streetPlusRainfall(rainReportEntity.getExtremelyHeavyRainstormStreet(), rainReportEntity.getExtremelyHeavyRainQuantity()),
//                DocumentUtils.streetPlusRainfall(rainReportEntity.getRainstormStreet(), rainReportEntity.getRainstormQuantity())
        );

        // 添加普通段落
        DocumentUtils.addRegularRun(paragraph, content);
    }

    /**
     * 创建风险评估
     *
     * @param doc
     * @param rainReportEntity
     */
    private void createRiskAssessment(XWPFDocument doc, RainReportEntity rainReportEntity) {
        // 标题
        DocumentUtils.addRegularParagraph(doc, "二、风险评估");

        // 第一段
        XWPFParagraph paragraph1 = DocumentUtils.addRegularParagraph(doc, null);

        String content1 = String.format("受持续强降雨影响，基于线性回归和贝叶斯模型构建的灾害风险评估模型在%s%d个地质灾害风险区、%s%d个地质灾害在测隐患点的范围内，结合了%s和近年来历史灾害数据共11类致灾因子的632条数据进行评估，" +
                        "本次暴雨预计可能形成%s复合灾害链，并评估得到%s%s的地质灾害风险显著上升，需高度警惕其中%d个地质灾害在测隐患点发生山洪、泥石流等次生灾害发生的可能性。",
                rainReportEntity.getConcentratedAreaPosition(),
                // DocumentUtils.list2Str(rainReportEntity.getRiskArea(), null),
                rainReportEntity.getRiskAreaQuantity(),
                DocumentUtils.list2Str(rainReportEntity.getHideArea(), null),
                rainReportEntity.getHideAreaQuantity(),
                DocumentUtils.list2Str(rainReportEntity.getHazards(), null),
                rainReportEntity.getDisasterChain(),
                rainReportEntity.getSignificantIncreaseArea(),
                DocumentUtils.list2Str(rainReportEntity.getSignificantIncreaseAreaStreet(), null),
//                rainReportEntity.getSignificantIncreaseAreaRiskQuantity(),
                rainReportEntity.getSignificantIncreaseAreaHideQuantity()
        );
        DocumentUtils.addRegularRun(paragraph1, content1);


        // 第二段，滑坡、泥石流、山洪、内涝分别处理
        for (int i = 0; i < rainReportEntity.getSecondaryDisasterReport().size(); i++) {
            RainReportEntity.SecondaryDisasterReportEntity disasterReport = rainReportEntity.getSecondaryDisasterReport().get(i);

            // 如果List长度为0，就不显示
            if (disasterReport.getDisasterTableData().size() != 0) {
                XWPFParagraph paragraph2 = DocumentUtils.addRegularParagraph(doc, null);
                StringBuilder contentBuilder = new StringBuilder();
                if (i == 0) {
                    contentBuilder.append("测算结果根据降雨集中区域结合地形地势因素分析得出，");
                }

                // 灾害类型
                contentBuilder.append(
                        String.format("%s风险主要集中在%s附近区域，" +
                                        "为本轮强降雨期间%s风险最高区域，需要提前疏散居民，严加防范。",
                                disasterReport.getDisasterType().getDisasterName(),
                                disasterReport.getRiskStreet(),
//                            disasterReport.getRiskPointName(),
//                            disasterReport.getRiskPointProbability(),
//                            disasterReport.getInfluencePeopleQuantity(),
                                disasterReport.getDisasterType().getDisasterName()
//                            DocumentUtils.list2Str(disasterReport.getSeriousArea(), null)
                        )
                );
                DocumentUtils.addRegularRun(paragraph2, contentBuilder.toString());

                // 设置表格标题
                XWPFParagraph tableParagraph = DocumentUtils.addRegularParagraph(doc, null);
                XWPFRun tableRun = DocumentUtils.addRegularRun(tableParagraph,
                        String.format("%s灾害预测概率统计表", disasterReport.getDisasterType().getDisasterName()));
                tableParagraph.setAlignment(ParagraphAlignment.CENTER);
                tableRun.setFontFamily(DocumentConfig.FONT_FANG_SONG_GB2312);
                tableRun.setFontSize(DocumentConfig.FONT_SIZE_FOUR);

                // 生成表格
                List<String> headers = DocumentUtils.stringArray2List(
                        "位置",
                        disasterReport.getDisasterType().getDisasterName() + "发生概率",
                        "灾害等级");
                List<String> fieldNames = DocumentUtils.stringArray2List("position", "probability", "grade");
                DocumentUtils.createGenericTable(doc,
                        disasterReport.getDisasterTableData(),
                        headers,
                        fieldNames,
                        true);

                RainQuery rainQuery = new RainQuery();
                rainQuery.setRainId(rainReportEntity.getRainId());
                rainQuery.setRainQueueId(rainReportEntity.getRainQueueId());

                String imageName;
                if (disasterReport.getDisasterType().getDisasterName().equals("滑坡")) {
                    imageName = "暴雨滑坡潜在隐患点及人口分布图";
                } else if (disasterReport.getDisasterType().getDisasterName().equals("泥石流")) {
                    imageName = "暴雨泥石流潜在隐患点及人口分布图";
                } else if (disasterReport.getDisasterType().getDisasterName().equals("内涝")) {
                    imageName = "暴雨内涝潜在隐患点及人口分布图";
                } else {
                    imageName = "暴雨山洪潜在隐患点及人口分布图";
                }
                boolean imageFound = false;
                while (!imageFound) {
                    try {
                        List<RainOutputDTO> rainOutputDTOS = feignService.thematicMap(rainQuery);
                        for (RainOutputDTO rainOutputDTO : rainOutputDTOS) {
                            if (rainOutputDTO.getFileName().equals(imageName)) {
                                DocumentUtils.addRegularParagraph(doc, null);
                                DocumentUtils.insertImageWithCaption(doc, rainOutputDTO.getSourceFile(), ImageTypeEnum.JPEG, null, null, "", ImagePositionEnum.AFTER);
                                DocumentUtils.addRegularParagraph(doc, null);
                                imageFound = true;
                                break;
                            }
                        }
                        Thread.sleep(1000);
                        if (imageFound) {
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // DocumentUtils.insertImageWithCaption(doc, "http://t1arte4v9.hb-bkt.clouddn.com/R2025071317164161010001_%E6%9A%B4%E9%9B%A8%E6%BB%91%E5%9D%A1%E6%BD%9C%E5%9C%A8%E9%9A%90%E6%82%A3%E7%82%B9%E5%8F%8A%E4%BA%BA%E5%8F%A3%E5%88%86%E5%B8%83%E5%9B%BE?e=1756894218&token=mheaTe3xRCkChSjwfueGYzB32yi7yk2sj8pemjvF:4Jn_CtsYWdUfA3gzR5klj8VzXKQ=暴雨滑坡潜在隐患点及人口分布图.jpg", ImageTypeEnum.JPEG, null, null, "", ImagePositionEnum.AFTER);

                Long originalData = disasterReport.getInfluencePeopleQuantity();
                Long[] bounds = calculateFloatBounds(originalData);
                Long data1 = bounds[0]; // 较小值
                Long data2 = bounds[1]; // 较大值

                // 第三段
                String text = String.format(
                        "其中，%s可能的大型灾害，预计影响%d到%d人，附近居民和风险影响区域居民必须撤离。",
                        disasterReport.getDisasterType().getDisasterName(),
                        data1.intValue(),
                        data2.intValue()
                );
                XWPFParagraph paragraph3 = DocumentUtils.addRegularParagraph(doc, text);
                paragraph3.setIndentationFirstLine(0);      // 首行不缩进
            }

            if((i+1)==rainReportEntity.getSecondaryDisasterReport().size()){
                RainQuery rainQuery = new RainQuery();
                rainQuery.setRainId(rainReportEntity.getRainId());
                rainQuery.setRainQueueId(rainReportEntity.getRainQueueId());
                String shelterImageName = "暴雨避难场所分布图";
                boolean shelterImageFound = false;
                while (!shelterImageFound) {
                    try {
                        List<RainOutputDTO> rainOutputDTOS = feignService.thematicMap(rainQuery);
                        for (RainOutputDTO rainOutputDTO : rainOutputDTOS) {
                            if (rainOutputDTO.getFileName().equals(shelterImageName)) {
                                DocumentUtils.addRegularParagraph(doc, null);
                                DocumentUtils.insertImageWithCaption(doc, rainOutputDTO.getSourceFile(), ImageTypeEnum.JPEG, null, null, "", ImagePositionEnum.AFTER);
                                DocumentUtils.addRegularParagraph(doc, null);
                                shelterImageFound = true;
                                break;
                            }
                        }
                        Thread.sleep(1000);
                        if (shelterImageFound) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    /**
     * 是否添加应急处置意见
     *
     * @param rainReportEntity 实体
     * @return 是否添加
     */
    private boolean addEmergencyResponseSuggestions(RainReportEntity rainReportEntity) {
        for (int i = 0; i < rainReportEntity.getSecondaryDisasterReport().size(); i++) {
            if (rainReportEntity.getSecondaryDisasterReport().get(i).getDisasterTableData().size() != 0)
                return true;
        }
        return false;
    }

    /**
     * 应急处置建议
     *
     * @param doc
     * @param rainReportEntity
     */
    private void createEmergencyResponseSuggestions(XWPFDocument doc, RainReportEntity rainReportEntity) {
        // 标题
        DocumentUtils.addRegularParagraph(doc, "三、应急处置建议");

        // 段落内容数组
        String[] contents = {
                "防范总建议：",
                String.format("1. %s政府要做好地质灾害防范工作安排部署，组织镇街、村组开展地质灾害隐患点、风险区巡查排查监测，必要时组织受威胁群众转移避险。",
                        DocumentUtils.list2Str(rainReportEntity.getWorkScheduleArea(), null)),
                "2. 资源规划部门要加强与相关部门沟通衔接，及时叫应预警区内地质灾害隐患点、风险区监测员和巡查员，督促指导有关单位做好重点区域巡查排查技术指导。",
                "3.水务、交通、旅游等部门开展预警区内水库、公路、景区等重要基础设施周边地质灾害风险隐患巡查监测。",
                "4.应急管理部门做好可能发生的地质灾害应急准备工作。",
                "处置措施建议：",
                String.format("人员疏散方面，建议优先组织%s中紧邻河道的民房、农家乐等高风险区域居民转移，此类区域靠近水体，受山洪和泥石流突发影响最为显著。" +
                                "同时，应重点关注%s等城市内涝易发的低洼积水区域，确保上述重点区域人员能够及时、安全撤离，最大限度保障群众生命安全。",
                        DocumentUtils.list2Str(rainReportEntity.getEvacuateTheCrowdArea(), null),
                        DocumentUtils.list2Str(rainReportEntity.getFocusArea(), null)),
                "人员安置方面，应优先选择地势较高、远离河道且具备较好排水条件的安全区域，村内学校、村委会等公共设施也可作为临时安置点，具备一定容纳能力和生活配套条件。对于本地安置条件受限的村组，可组织跨区域转移，安排至周边安全城镇，利用当地酒店、学校等资源保障群众基本生活与应急避险需求。",
                "救援队伍准备方面：建议提前联系消防、交通和医疗三类专业救援力量：",
                "消防救援队准备生命探测设备、环境探测设备和破拆工具等，可以及时定位被困人员，评估现场环境。",
                "交通救援队预置挖掘机、装载机和道路抢修车等，开辟抗震救灾绿色通道。",
                "医疗救援队组织具备外科、内科、急救能力的医疗人员，配备心肺复苏仪、骨折固定夹板、担架等装备及绷带、消炎药、止痛药等应急药物。",
                "救援物资准备方面，应根据预测受灾人口数量，提前调配各类救援物资。"
        };

        // 创建所有段落
        for (String content : contents) {
            DocumentUtils.addRegularParagraph(doc, content);
        }
    }

    /**
     * 根据数值位数计算浮动范围
     *
     * @param value 原始数值
     * @return 浮动范围值
     */
    private int calculateFloatRange(Long value) {
        if (value == null || value == 0) {
            return 10; // 默认浮动范围
        }

        // 计算数值的位数
        int digits = String.valueOf(Math.abs(value)).length();

        // 根据位数计算浮动范围
        if (digits <= 2) {
            return 10;
        } else if (digits <= 7) {
            return (int) Math.pow(10, digits - 1);
        } else {
            return 1000000; // 7位数的浮动范围
        }
    }

    /**
     * 根据位数截断数值
     *
     * @param value 原始数值
     * @return 截断后的数值
     */
    private Long truncateByDigits(Long value) {
        if (value == null || value == 0) {
            return value;
        }

        // 计算数值的位数
        int digits = String.valueOf(Math.abs(value)).length();

        if (digits == 1) {
            // 个位数不处理
            return value;
        } else if (digits == 2 || digits == 3) {
            // 2位数和3位数：除了最高位其他为0
            long divisor = (long) Math.pow(10, digits - 1);
            long highestDigit = value / divisor;
            return highestDigit * divisor;
        } else if (digits == 4) {
            // 4位数：最后两位为0
            return (value / 100) * 100;
        } else if (digits == 5) {
            // 5位数：最后三位为0
            return (value / 1000) * 1000;
        } else if (digits == 6) {
            // 6位数：最后四位为0
            return (value / 10000) * 10000;
        } else if (digits == 7) {
            // 7位数：最后四位为0
            return (value / 10000) * 10000;
        } else {
            // 超过7位数，按7位数处理
            return (value / 10000) * 10000;
        }
    }

    /**
     * 计算浮动范围的上下边界（带截断处理）
     *
     * @param originalValue 原始值
     * @return 长度为2的数组，[0]为下边界，[1]为上边界
     */
    private Long[] calculateFloatBounds(Long originalValue) {
        if (originalValue == null) {
            return new Long[]{0L, 0L};
        }

        int floatRange = calculateFloatRange(originalValue);

        Long lowerBound = Math.max(0L, originalValue - floatRange);
        Long upperBound = originalValue + floatRange;

        // 对边界值进行截断处理
        lowerBound = truncateByDigits(lowerBound);
        upperBound = truncateByDigits(upperBound);

        return new Long[]{lowerBound, upperBound};
    }

    // 去除中括号的辅助方法
    private String removeBrackets(String str) {
        if (str == null) {
            return null;
        }
        // 去除字符串开头和结尾的中括号
        return str.replaceAll("^\\[|\\]$", "");
    }


}


