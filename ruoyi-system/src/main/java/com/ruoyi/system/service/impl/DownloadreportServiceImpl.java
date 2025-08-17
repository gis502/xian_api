package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.DisasterType;
import com.ruoyi.system.domain.entity.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.DownloadreportService;
import lombok.Data;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ruoyi.system.domain.dto.LatLonDTO;
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

    private final XianDisasterRainMapper xianDisasterRainMapper;

    @Resource
    private GeologicalDisasterHideMapper geologicalDisasterHideMapper;
    @Resource
    private RoadMapper roadMapper;
    @Resource
    private ReservoirMapper reservoirMapper;
    @Resource
    private HighwayMapper highwayMapper;
    @Resource
    private BridgeMapper bridgeMapper;
    @Autowired
    private XianFactorAnalysisMapper xianFactorAnalysisMapper;

    public DownloadreportServiceImpl(XianDisasterRainMapper xianDisasterRainMapper) {
        this.xianDisasterRainMapper = xianDisasterRainMapper;
    }
    //保存图片

    @Override
    public R<String> saveCanvas(MultipartFile file) throws IOException {
        // 目录不存在就创建

        Path dir = Paths.get("D:/img");
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path dest = dir.resolve(fileName);

        // ✅ 显式关闭流，避免文件锁
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("已保存: " + dest.toAbsolutePath());

        return R.ok("D:/img/" + fileName);
    }
    //生成报告

    @Override
    public R<String> generateRainReport(String imgUrl) throws IOException, InvalidFormatException {

        //查询灾害表最新的ID
        Long latestRainDisasterId = xianDisasterRainMapper.getLatestRainDisasterId();
        System.out.println(latestRainDisasterId);

        ReportInfo reportInfo = new ReportInfo();
        Map<String, Object> stringObjectMap = reportInfo.queryReportInfo(151575L, DisasterType.RAINSTORM);
        System.out.println("原始数据: " + stringObjectMap);

        // 生成 Word 路径
        Path wordDir = Paths.get("D:/report");
        if (!Files.exists(wordDir)) {
            Files.createDirectories(wordDir);
        }
        String wordName = "report_" + System.currentTimeMillis() + ".docx";
        Path wordPath = wordDir.resolve(wordName);

        // 在方法开头（解析数据前）声明并初始化所有灾害数据变量
        String[][] landslideData = new String[0][0];       // 滑坡数据
        String[][] mudslideData = new String[0][0];        // 泥石流数据
        String[][] mountainTorrentData = new String[0][0]; // 山洪数据
        String[][] urbanFloodData = new String[0][0];      // 城市内涝数据

        // 从stringObjectMap中获取外层table（对应Table类实例）
        Object outerTableObj = stringObjectMap.get("table");
        if (outerTableObj instanceof Map) {
            Map<String, Object> outerTableMap = (Map<String, Object>) outerTableObj;
            // 获取真正的Table对象（innerTableObj）
            Object innerTableObj = outerTableMap.get("table");

            // 关键：判断innerTableObj是否为Table类实例
            if (innerTableObj instanceof DownloadreportServiceImpl.ReportInfo.Table) {
                DownloadreportServiceImpl.ReportInfo.Table table = (DownloadreportServiceImpl.ReportInfo.Table) innerTableObj;

                // 解析滑坡数据（通过Table类的getLandslide()方法）
                DownloadreportServiceImpl.ReportInfo.Table.TableStructure landslideStructure = table.getLandslide();
                if (landslideStructure != null) {
                    landslideData = convertTableStructureTo2DArray(landslideStructure);
                    System.out.println("滑坡数据转换后: " + Arrays.deepToString(landslideData));
                } else {
                    System.out.println("landslideStructure为空");
                }

                // 解析泥石流数据（通过getDebrisFlow()方法）
                DownloadreportServiceImpl.ReportInfo.Table.TableStructure debrisFlowStructure = table.getDebrisFlow();
                if (debrisFlowStructure != null) {
                    mudslideData = convertTableStructureTo2DArray(debrisFlowStructure);
                    System.out.println("泥石流数据转换后: " + Arrays.deepToString(mudslideData));
                } else {
                    System.out.println("debrisFlowStructure为空");
                }

                // 解析山洪数据（通过getTorrentialFlood()方法）
                DownloadreportServiceImpl.ReportInfo.Table.TableStructure torrentStructure = table.getTorrentialFlood();
                if (torrentStructure != null) {
                    mountainTorrentData = convertTableStructureTo2DArray(torrentStructure);
                    System.out.println("山洪数据转换后: " + Arrays.deepToString(mountainTorrentData));
                } else {
                    System.out.println("torrentStructure为空");
                }

                // 解析内涝数据（通过getWaterLogging()方法）
                DownloadreportServiceImpl.ReportInfo.Table.TableStructure floodStructure = table.getWaterLogging();
                if (floodStructure != null) {
                    urbanFloodData = convertTableStructureTo2DArray(floodStructure);
                    System.out.println("城市内涝数据转换后: " + Arrays.deepToString(urbanFloodData));
                } else {
                    System.out.println("floodStructure为空");
                }

            } else {
                System.out.println("innerTableObj不是Table类型，实际类型：" + innerTableObj.getClass());
            }
        } else {
            System.out.println("outerTableObj不是Map类型，实际类型：" + (outerTableObj != null ? outerTableObj.getClass() : "null"));
        }

        // 表头与列宽配置
        String landslideTableName = "滑坡灾害预测概率统计表";
        String[] landslideHead = {"序号", "区县位置", "详细位置", "风险等级"};
        int[] landslideColWidths = {1500, 3000, 8000, 3000};

        String mudslideTableName = "泥石流灾害预测概率统计表";
        String[] mudslideHead = {"序号", "区县位置", "详细位置", "风险等级"};
        int[] mudslideColWidths = {1500, 3000, 8000, 3000};

        String mountainTorrentTableName = "山洪灾害预测概率统计表";
        String[] mountainTorrentHead = {"序号", "区县位置", "详细位置", "风险等级"};
        int[] mountainTorrentColWidths = {1500, 3000, 8000, 3000};

        String urbanFloodTableName = "城市内涝灾害预测概率统计表";
        String[] urbanFloodHead = {"序号", "区县位置", "详细位置","风险等级"};
        int[] urbanFloodColWidths = {1500, 3000, 8000, 3000};

        String lifelineProjectTableName = "生命线工程影响统计表";
        String[] lifelineProjectHead = {"序号", "类型", "名称"};
        int[] lifelineProjectColWidths = {1500, 3000, 15000};
        String[][] lifelineProjectData = {
                {"道路", "G210 国道（沣峪村段）"},
                {"道路", "喂子坪村通村公路"},
                {"道路","郭杜街道"},
                {"道路","学府大街西段"}
//                {"输电线路", "35千伏输电线路（沿红草河沟谷段）"},
//                {"通信设施", "移动通信基站（鸡窝子组）"},
//                {"输水管道", "镇级饮用水主管线（经大门村组）"}
        };

        Map<String, String> replaceMap = writeDescibe(landslideData, mudslideData, mountainTorrentData, urbanFloodData, stringObjectMap);
        String pictitle = "灾情影响分布图";

        /* 读模板并替换 */
        try (InputStream template = getClass().getResourceAsStream("/reportTemplate/暴雨应急预评估报告模板.docx");
             XWPFDocument doc = new XWPFDocument(template)) {

            // 逐段替换
            for (XWPFParagraph p : doc.getParagraphs()) {
                replaceInParagraph(p, replaceMap);
            }

            // 插入表格，空数据也会生成表头
            insertTableAfterTitle(doc, landslideTableName, landslideHead, landslideData, landslideColWidths);
            insertTableAfterTitle(doc, mudslideTableName, mudslideHead, mudslideData, mudslideColWidths);
            insertTableAfterTitle(doc, mountainTorrentTableName, mountainTorrentHead, mountainTorrentData, mountainTorrentColWidths);
            insertTableAfterTitle(doc, urbanFloodTableName, urbanFloodHead, urbanFloodData, urbanFloodColWidths);
            insertTableAfterTitle(doc, lifelineProjectTableName, lifelineProjectHead, lifelineProjectData, lifelineProjectColWidths);

            // 插入图片
            insertPicBeforeTitle(doc, pictitle, imgUrl);

            try (OutputStream os = Files.newOutputStream(wordPath)) {
                doc.write(os);
            }
        }
        return R.ok(wordName);
    }

    /**
     * 将TableStructure的Map（包含header和data）转换为二维字符串数组
     * @param dataMap 包含"header"和"data"键的Map（对应landslide/debrisFlow等字段）
     * @return 转换后的二维数组（每行对应一条数据）
     */
    /**
     * 将TableStructure对象转换为二维字符串数组
     * @param structure TableStructure实例（包含data字段）
     * @return 转换后的二维数组
     */
//    private String[][] convertTableStructureTo2DArray(DownloadreportServiceImpl.ReportInfo.Table.TableStructure structure) {
//        // 从TableStructure中获取data（注意：data的类型可能是List<Object[]>或List<List<Object>>）
//        List<?> dataList = structure.getData();
//        if (dataList == null || dataList.isEmpty()) {
//            System.out.println("TableStructure数据为空");
//            return new String[0][0];
//        }
//
//        // 转换dataList为String[][]
//        String[][] result = new String[dataList.size()][];
//        for (int i = 0; i < dataList.size(); i++) {
//            Object rowObj = dataList.get(i); // 每行数据（可能是Object[]或List<Object>）
//            List<Object> rowData;
//
//            // 处理行数据的两种可能类型
//            if (rowObj instanceof Object[]) {
//                rowData = Arrays.asList((Object[]) rowObj); // 数组转List
//            } else if (rowObj instanceof List<?>) {
//                rowData = new ArrayList<>((List<?>) rowObj); // List强转
//            } else {
//                // 异常数据处理
//                rowData = new ArrayList<>();
//                rowData.add(rowObj);
//            }
//
//            // 转换为String数组（去除风险等级的括号）
//            String[] rowArr = new String[rowData.size()];
//            for (int j = 0; j < rowData.size(); j++) {
//                Object value = rowData.get(j);
//                rowArr[j] = (value != null) ? value.toString().replaceAll("[\\[\\]]", "").trim() : "";
//            }
//            result[i] = rowArr;
//        }
//        return result;
//    }
    /**
     * 将TableStructure对象转换为二维字符串数组（不含概率数据）
     * @param structure TableStructure实例（包含data字段）
     * @return 转换后的二维数组（移除概率列）
     */
    private String[][] convertTableStructureTo2DArray(DownloadreportServiceImpl.ReportInfo.Table.TableStructure structure) {
        List<?> dataList = structure.getData();
        if (dataList == null || dataList.isEmpty()) {
            System.out.println("TableStructure数据为空");
            return new String[0][0];
        }

        String[][] result = new String[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            Object rowObj = dataList.get(i);
            List<Object> rowData;

            // 处理行数据的两种可能类型
            if (rowObj instanceof Object[]) {
                rowData = Arrays.asList((Object[]) rowObj);
            } else if (rowObj instanceof List<?>) {
                rowData = new ArrayList<>((List<?>) rowObj);
            } else {
                rowData = new ArrayList<>();
                rowData.add(rowObj);
            }

            // 构建新行（移除概率列，保留：区县位置、详细位置、风险等级）
            List<String> newRow = new ArrayList<>();
            // 添加区县位置（第0列）
            if (rowData.size() > 0) {
                newRow.add(rowData.get(0) != null ? rowData.get(0).toString().trim() : "");
            }
            // 添加详细位置（第1列）
            if (rowData.size() > 1) {
                newRow.add(rowData.get(1) != null ? rowData.get(1).toString().trim() : "");
            }
            // 添加风险等级（第3列，移除概率列[索引2]）
            if (rowData.size() > 3) {
                String risk = rowData.get(3).toString().replaceAll("[\\[\\]]", "").trim();
                newRow.add(risk);
            }

            // 转换为数组
            result[i] = newRow.toArray(new String[0]);
        }
        return result;
    }




    // 描述文段（保持不变，已适配带双大括号的键名）
    private static Map<String, String> writeDescibe(String[][] landslideData, String[][] mudslideData,
                                                    String[][] mountainTorrentData, String[][] urbanFloodData,
                                                    Map<String, Object> dataMap) {
        Map<String, String> map = new HashMap<>();
        System.out.println("获取到的真实数据: " + dataMap);

        // 从dataMap中获取数据（键名带双大括号，与数据匹配）
        map.put("{{ReportDate}}", getStringValue(dataMap, "{{ReportDate}}", "未知时间"));
        map.put("{{OverView_ReportDate}}", getStringValue(dataMap, "{{OverView_ReportDate}}", "未知日期"));
        //降雨量
        map.put("{{RainFall}}", getStringValue(dataMap, "{{RainFall}}", "未知"));
        map.put("{{OverView_RainCoveredQuXian}}", getStringValue(dataMap, "{{OverView_RainCoveredQuXian}}", "未知区域"));
        map.put("{{OverView_mainRainQuXian}}", getStringValue(dataMap, "{{OverView_mainRainQuXian}}", "未知区域"));
        map.put("{{Disaster_MainRainQuXian}}", getStringValue(dataMap, "{{Disaster_MainRainQuXian}}", "未知区域"));
        //滑坡相关
        map.put("{{Disaster_LandslideMainCun}}", getStringValue(dataMap, "{{Disaster_LandslideMainCun}}", "未知村庄"));
        map.put("{{Disaster_LandslideMostHigh}}", getStringValue(dataMap, "{{Disaster_LandslideMostHigh}}", "未知地点"));
        map.put("{{Disaster_LandslideMostHighProbability}}", getStringValue(dataMap, "{{Disaster_LandslideMostHighProbability}}", "未知"));
        map.put("{{Disaster_NumOfLandslide}}", getStringValue(dataMap, "{{Disaster_NumOfLandslide}}", "0"));
        //泥石流相关
        map.put("{{Disaster_MudslideMainCun}}", getStringValue(dataMap, "{{Disaster_MudslideMainCun}}", "无数据"));
        map.put("{{Disaster_NumOfMudslide}}", getStringValue(dataMap, "{{Disaster_NumOfMudslide}}", "0"));
        //山洪相关
        map.put("{{Disaster_MountainTorrentMainCun}}", getStringValue(dataMap, "{{Disaster_MountainTorrentMainCun}}", "无数据"));
        map.put("{{Disaster_NumOfMountainTorrente}}", getStringValue(dataMap, "{{Disaster_NumOfMountainTorrente}}", "0"));
        //城市内涝
        map.put("{{Disaster_UrbanFloodMainCun}}", getStringValue(dataMap, "{{Disaster_UrbanFloodMainCun}}", "无数据"));
        map.put("{{Disaster_NumOfUrbanFlood}}", getStringValue(dataMap, "{{Disaster_NumOfUrbanFlood}}", "0"));
        map.put("{{Disaster_ProtectAreas}}", getStringValue(dataMap, "{{Disaster_ProtectAreas}}", "未知区域"));

        // 影响范围与人口
        map.put("{{Disaster_AffectedAreaLow}}", getStringValue(dataMap, "{{Disaster_AffectedAreaLow}}", "未知"));
        map.put("{{Disaster_AffectedAreaHigh}}", getStringValue(dataMap, "{{Disaster_AffectedAreaHigh}}", "未知"));
        map.put("{{Disaster_AffectedPeopleLow}}", getStringValue(dataMap, "{{Disaster_AffectedPeopleLow}}", "0"));
        map.put("{{Disaster_AffectedPeopleHigh}}", getStringValue(dataMap, "{{Disaster_AffectedPeopleHigh}}", "0"));
        map.put("{{Disposal_AffectedByFloodAndSlide}}", map.get("{{Disaster_LandslideMainCun}}"));
        map.put("{{Disposal_AffectedByUrbanFlood}}", map.get("{{Disaster_UrbanFloodMainCun}}"));

        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{{ReportDate}}", map.get("{{ReportDate}}"));

        String disasterOverAllOrg = "受持续强降雨影响，根据灾害风险评估模型测算结果，{{Disaster_MainRainQuXian}}多个村（组）地质灾害风险显著上升，需高度警惕滑坡、泥石流等次生灾害发生可能。";
        String disasterOverAll = replacePlaceholders(disasterOverAllOrg, map);
        replaceMap.put("{{Disaster_OverAll}}", disasterOverAll);

        String overViewOrg = "{{OverView_ReportDate}}，西安市部分区域（包括{{OverView_RainCoveredQuXian}}）已达到{{RainFall}}毫米以上降水。根据最新气象监测数据，暴雨主要集中在{{OverView_mainRainQuXian}}一带，区域内山体含水饱和风险增加，具备诱发滑坡、泥石流、山洪和城市内涝等次生灾害的典型触发条件。";
        String overView = replacePlaceholders(overViewOrg, map);
        replaceMap.put("{{OverView}}", overView);
        System.out.println("替换后的数据landslideData: " + Arrays.deepToString(landslideData));


        String landslideDescribe = "";
        if (!containsHighRisk(landslideData)) {
            landslideDescribe = "在本次评估中，多个隐患点的滑坡发生概率处于中、低风险。但仍需采取适当的预防措施，以应对可能的滑坡事件。";
        } else {
            String landslideDescribeOrg = "{{Disaster_LandslideMainCun}}为滑坡高风险区域。{{Disaster_LandslideMostHigh}}滑坡概率达{{Disaster_LandslideMostHighProbability}}，为当前评估区域内滑坡风险最高点。在本轮强降雨影响下，共有{{Disaster_NumOfLandslide}}处滑坡隐患点被评估为高风险，存在失稳可能，需立即加强防范。";
            landslideDescribe = replacePlaceholders(landslideDescribeOrg, map);
        }
        replaceMap.put("{{Disaster_LandslideDescribe}}", landslideDescribe);

        String mudslideDescribe = "";
        if (!containsHighRisk(mudslideData)) {
            mudslideDescribe = "在本次评估中，未发现高风险的泥石流隐患点。";
        } else {
            String mudslideDescribeOrg = "泥石流风险主要集中在{{Disaster_MudslideMainCun}}一带。共有{{Disaster_NumOfMudslide}}处存在较高的泥石流触发风险。";
            mudslideDescribe = replacePlaceholders(mudslideDescribeOrg, map);
        }
        replaceMap.put("{{Disaster_MudslideDescribe}}", mudslideDescribe);

        String mountainTorrentDescribe = "";
        if (!containsHighRisk(mountainTorrentData)) {
            mountainTorrentDescribe = "在本次评估中，未发现高风险的山洪隐患点。";
        } else {
            String mountainTorrentDescribeOrg = "山洪风险主要集中在{{Disaster_MountainTorrentMainCun}}附近区域。在持续降雨背景下共有{{Disaster_NumOfMountainTorrente}}处存在山洪骤发风险，需加强预警与应急准备。";
            mountainTorrentDescribe = replacePlaceholders(mountainTorrentDescribeOrg, map);
        }
        replaceMap.put("{{Disaster_MountainTorrentDescribe}}", mountainTorrentDescribe);

        String urbanFloodDescribe = "";
        if (!containsHighRisk(urbanFloodData)) {
            urbanFloodDescribe = "在本次评估中，未发现高风险的城市内涝隐患点。";
        } else {
            String urbanFloodDescribeOrg = "城市内涝风险主要集中在{{Disaster_UrbanFloodMainCun}}低洼区域及部分老旧排水片区。短时强降雨下共有{{Disaster_NumOfUrbanFlood}}处易出现道路积水和排涝不畅等问题，需提前做好排水疏导和交通应对措施。";
            urbanFloodDescribe = replacePlaceholders(urbanFloodDescribeOrg, map);
        }
        replaceMap.put("{{Disaster_UrbanFloodDescribe}}", urbanFloodDescribe);

        String disasterAllInAllOrg = "综合研判，{{Disaster_ProtectAreas}}周边等地为本轮强降雨期间次生灾害重点防范区域。建议有关单位强化动态监测和预警信息发布，提前做好人员转移安置及应急物资准备，切实提升应对突发地质灾害的处置能力。";
        String disasterAllInAll = replacePlaceholders(disasterAllInAllOrg, map);
        replaceMap.put("{{Disaster_AllInAll}}", disasterAllInAll);

        String disasterAffectedAreaAndPeopleOrg = "根据滑坡破裂角模型计算结果，结合区域地形坡向与沟谷汇水条件综合分析，当前在持续强降雨影响下，一旦发生次生灾害，初步预测其可能影响范围在{{Disaster_AffectedAreaLow}}至{{Disaster_AffectedAreaHigh}}平方公里之间。经对区域建筑密度与人口分布数据进行叠加分析，预计受影响人口在{{Disaster_AffectedPeopleLow}}至{{Disaster_AffectedPeopleHigh}}人之间，主要集中在地势低洼、沟谷下游及滑坡堆积方向所覆盖区域。";
        String disasterAffectedAreaAndPeople = replacePlaceholders(disasterAffectedAreaAndPeopleOrg, map);
        replaceMap.put("{{Disaster_AffectedAreaAndPeople}}", disasterAffectedAreaAndPeople);

        String disasterLifeLine = "其中，多处道路存在中断风险，沿线输电线路和通信基站可能受损，导致局部供电和通信中断；下游加油站及部分工业厂房等重点危险源或引发燃气泄漏和火灾爆炸等次生灾害。";
        replaceMap.put("{{Disaster_LifeLine}}", disasterLifeLine);

        String disposalEvacuationOrg = "人员疏散方面，建议优先组织{{Disposal_AffectedByFloodAndSlide}}中紧邻河道的民房、农家乐等高风险区域居民转移，此类区域靠近水体，受山洪和泥石流突发影响最为显著。同时，应重点关注{{Disposal_AffectedByUrbanFlood}}城市内涝易发的低洼积水区域，确保上述重点区域人员能够及时、安全撤离，最大限度保障群众生命安全。";
        String disposalEvacuation = replacePlaceholders(disposalEvacuationOrg, map);
        replaceMap.put("{{Disposal_Evacuation}}", disposalEvacuation);

        return replaceMap;
    }


    // 工具方法：从map中获取字符串值，提供默认值
    private static String getStringValue(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        if (value == null || "null".equals(value) || value.toString().trim().isEmpty()) {
            return defaultValue;
        }
        return value.toString();
    }


// 修正风险等级判断（风险等级明确在第3列，索引为3）
public static boolean containsHighRisk(String[][] data) {
    // 防御性检查：数据为空或无行时直接返回false
    if (data == null || data.length == 0) {
        return false;
    }

    // 遍历每行数据，检查风险等级
    for (String[] row : data) {
        // 1. 确保行数据至少有3列（避免数组越界）
        // 2. 检查第2列（风险等级）是否为"高"（去除前后空格，避免格式问题）
        if (row != null && row.length >= 3 && "高".equals(row[2].trim())) {
            return true;
        }
    }

    // 所有行都不满足高风险条件
    return false;
}




    // 占位符替换（保持不变）
    public static String replacePlaceholders(String originalText, Map<String, String> placeholders) {
        String result = originalText;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
        }
        return result;
    }


    //文段写入word
    private void replaceInParagraph(XWPFParagraph para, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        List<RunStyle> stylesToCopy = new ArrayList<>(); // 保存样式信息

        // 提取段落中的文本和样式信息
        for (XWPFRun r : para.getRuns()) {
            sb.append(r.text());
            stylesToCopy.add(new RunStyle(r)); // 保存样式信息
        }

        String fullText = sb.toString();

        // 替换占位符
        for (Map.Entry<String, String> e : map.entrySet()) {
            fullText = fullText.replace(e.getKey(), e.getValue());
        }

        // 清空原内容
        while (para.getRuns().size() > 0) {
            para.removeRun(0);
        }

        // 重新写入整个字符串，保持原样式
        if (!fullText.isEmpty()) {
            XWPFRun newRun = para.createRun();
            newRun.setText(fullText);

            // 拷贝原样式（可选）
            if (!stylesToCopy.isEmpty()) {
                RunStyle firstOldStyle = stylesToCopy.get(0);

                // 根据样式信息设置新 run 的样式
                if (firstOldStyle.isBold != null) newRun.setBold(firstOldStyle.isBold);
                if (firstOldStyle.fontSize != -1) newRun.setFontSize(firstOldStyle.fontSize);
                if (firstOldStyle.fontFamily != null) newRun.setFontFamily(firstOldStyle.fontFamily);
                if (firstOldStyle.color != null) newRun.setColor(firstOldStyle.color);
            } else {
                System.out.println("No styles to copy from.");
            }
        }
    }

    class RunStyle {
        String text;
        Boolean isBold;
        Boolean isItalic;
        Integer fontSize;
        String fontFamily;
        String color;

        public RunStyle(XWPFRun run) {
            this.text = run.text();
            this.isBold = run.isBold();
            this.isItalic = run.isItalic();
            this.fontSize = run.getFontSize();
            this.fontFamily = run.getFontFamily();
            this.color = run.getColor();
        }

        @Override
        public String toString() {
            return "RunStyle{" +
                    "text='" + text + '\'' +
                    ", isBold=" + isBold +
                    ", isItalic=" + isItalic +
                    ", fontSize=" + fontSize +
                    ", fontFamily='" + fontFamily + '\'' +
                    ", color='" + color + '\'' +
                    '}';
        }
    }

    //插入表格
    private void insertTableAfterTitle(XWPFDocument doc, String title, String[] headers, String[][] data, int[] colWidths) {

        // 1. 找标题段落
        XWPFParagraph anchor = null;
        for (XWPFParagraph p : doc.getParagraphs()) {
            if (p.getText().trim().contains(title)) {
                anchor = p;
                break;
            }
        }
        if (anchor == null) {
            anchor = doc.createParagraph();
            anchor.createRun().setText(title);
        }

        // 2. 在段落之后插入表格
        XmlCursor cursor = anchor.getCTP().newCursor();
        cursor.toEndToken(); // 关键：把光标移动到当前 paragraph 的 END 位置
        cursor.toNextToken(); // 再往后一步，落到 paragraph 之后
        XWPFTable table = doc.insertNewTbl(cursor);
        cursor.dispose();

        // 4. 固定列宽
        CTTblGrid grid = table.getCTTbl().getTblGrid();
        if (grid == null) grid = table.getCTTbl().addNewTblGrid();

        for (int w : colWidths) {
            CTTblGridCol col = grid.addNewGridCol();
            col.setW(BigInteger.valueOf(w));
        }

        // 5. 表头
        XWPFTableRow header = table.getRow(0);
        for (int i = 0; i < headers.length; i++) {
            XWPFTableCell cell;
            if (i == 0) {
                cell = header.getCell(0);
            } else {
                cell = header.addNewTableCell();
            }
            // 使用单元格中已有的段落，而不是创建新的段落
            XWPFParagraph paragraph = cell.getParagraphs().get(0); // 获取第一个段落
            XWPFRun run = paragraph.createRun();
            run.setText(headers[i]);
            run.setBold(true); // 设置文本加粗
            run.setFontFamily("仿宋_GB2312"); // 设置字体为仿宋_GB2312
            run.setFontSize(12); // 设置字号为小四（12磅）[^63^]
            paragraph.setAlignment(ParagraphAlignment.CENTER); // 水平居中
        }
        setRowCenter(header, colWidths); // 设置表头居中对齐


        // 6. 数据行（修复越界问题）
        for (int i = 0; i < data.length; i++) {
            XWPFTableRow row = table.createRow();
            // 依次写每一列（c从0到colWidths.length-1）
            for (int c = 0; c < colWidths.length; c++) {
                String val;
                if (c == 0) {
                    // 第0列是序号
                    val = String.valueOf(i + 1);
                } else {
                    // 其他列对应数据（c-1为数据索引）
                    int dataIndex = c - 1;
                    // 关键：检查数据索引是否有效，无效则赋空值
                    if (data[i] != null && dataIndex < data[i].length) {
                        val = data[i][dataIndex];
                    } else {
                        val = ""; // 索引无效时赋空，避免越界
                    }
                }
                XWPFTableCell cell = row.getCell(c);
                if (cell.getParagraphs().size() == 0) {
                    cell.addParagraph();
                }
                XWPFRun run = cell.getParagraphs().get(0).createRun();
                run.setText(val);
                run.setFontFamily("宋体");
                run.setFontSize(11);
            }
            setRowCenter(row, colWidths);
        }

    }

    //插入图片
    private static void setRowCenter(XWPFTableRow row, int[] colWidths) {
        final long ROW_HEIGHT_TWIPS = 600;

        /* 1. 行高：直接 addNewTrHeight，不设 w:hRule="exact" Word 不会压缩 */
        CTTrPr trPr = row.getCtRow().addNewTrPr();
        CTHeight ht = trPr.addNewTrHeight();
        ht.setVal(BigInteger.valueOf(ROW_HEIGHT_TWIPS));

        /* 2. 每个单元格：垂直 + 水平居中 + 段前段后 0 磅 + 列宽 */
        int cellCount = row.getTableCells().size();
        for (int c = 0; c < cellCount; c++) {
            XWPFTableCell cell = row.getCell(c);

            // 水平居中 + 段前段后 0 磅
            XWPFParagraph p = cell.getParagraphs().get(0);
            p.setAlignment(ParagraphAlignment.CENTER);

            CTPPr pPr = p.getCTP().isSetPPr() ? p.getCTP().getPPr()
                    : p.getCTP().addNewPPr();
            CTSpacing spacing = pPr.isSetSpacing() ? pPr.getSpacing()
                    : pPr.addNewSpacing();
            spacing.setAfter(BigInteger.ZERO);
            spacing.setBefore(BigInteger.ZERO);

            // 垂直居中
            CTTcPr tcPr = cell.getCTTc().addNewTcPr();
            CTVerticalJc vJc = tcPr.addNewVAlign();
            vJc.setVal(STVerticalJc.CENTER);

            // 列宽
            if (colWidths != null && c < colWidths.length) {
                CTTblWidth w = tcPr.addNewTcW();
                w.setType(STTblWidth.DXA);
                w.setW(BigInteger.valueOf(colWidths[c]));
            }
        }
    }

    private void insertPicBeforeTitle(XWPFDocument doc, String pictitle, String imgUrl) {
        Path tempImgPath = Paths.get(imgUrl);
        if (!Files.exists(tempImgPath)) {
            System.err.println("图片不存在：" + imgUrl);
            return;
        }

        int targetIndex = -1;
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        for (int i = 0; i < paragraphs.size(); i++) {
            if (paragraphs.get(i).getText().contains(pictitle)) {
                targetIndex = i;
                break;
            }
        }

        try (InputStream is = Files.newInputStream(tempImgPath)) {
            // 读取图片原始尺寸
            BufferedImage image = ImageIO.read(is);
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();

            // 目标宽度（单位：像素）
            int targetWidthPx = 420;

            // 等比缩放高度
            int targetHeightPx = (int) ((double) originalHeight / originalWidth * targetWidthPx);

            // Word 使用 EMU 单位：1px = 9525 EMU
            int widthEMU = Units.toEMU(targetWidthPx);
            int heightEMU = Units.toEMU(targetHeightPx);

            // 重新打开流（因为 ImageIO.read 会关闭流）
            try (InputStream is2 = Files.newInputStream(tempImgPath)) {
                if (targetIndex != -1) {
                    XWPFParagraph targetPara = paragraphs.get(targetIndex);
                    XmlCursor cursor = targetPara.getCTP().newCursor();
                    XWPFParagraph newPara = doc.insertNewParagraph(cursor);
                    newPara.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun run = newPara.createRun();
                    run.addPicture(is2, XWPFDocument.PICTURE_TYPE_PNG,
                            tempImgPath.getFileName().toString(), widthEMU, heightEMU);
                    cursor.dispose();
                } else {
                    XWPFParagraph picPara = doc.createParagraph();
                    picPara.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun picRun = picPara.createRun();
                    picRun.addPicture(is2, XWPFDocument.PICTURE_TYPE_PNG,
                            tempImgPath.getFileName().toString(), widthEMU, heightEMU);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> calPeople(){
        List<Map<String, Object>> queryDisasterEstimation = xianFactorAnalysisMapper.queryDisasterEstimation(152292L, DisasterType.RAINSTORM);

        // LatLonDTO latLonDTO1 = new LatLonDTO();
        // latLonDTO1.setLat(34.02667);
        // latLonDTO1.setLon(108.04083);
        // List<XianDem> xianDems1 = modelService.getPoliejiao(latLonDTO1);

        // List<Map<String, String>> affectedInfrastructure = new ArrayList<>();
        for(Map<String, Object> map : queryDisasterEstimation){

            LatLonDTO latLonDTO = new LatLonDTO();
            latLonDTO.setLat((Double) map.get("lat"));
            latLonDTO.setLon((Double) map.get("lon"));
            List<XianDem> xianDems = modelService.getPoliejiao(latLonDTO);

            // 遍历XianDem列表中的每个对象
            for (XianDem xianDem : xianDems) {
                // 获取中心点经纬度
                Double centerLon = xianDem.getCenterLon();
                Double centerLat = xianDem.getCenterLat();


                // 创建存储受影响基础设施的列表
//                List<Map<String, String>> affectedInfrastructure = new ArrayList<>();

                // 30米圆形范围查询
                double radiusMeters = 30.0;
                // return queryDisasterEstimation;

//                 try {
//                     // 查询危险源
//                     // List<GeologicalDisasterHide> disasters = geologicalDisasterHideMapper.findWithinCircle(centerLon, centerLat, radiusMeters);
// //                    for (GeologicalDisasterHide disaster : disasters) {
// //                        Map<String, String> item = new HashMap<>();
// //                        item.put("type", "危险源");
// //                        item.put("name", disaster.getDisasterName() != null ? disaster.getDisasterName() : "未知危险源");
// //                        item.put("subType", disaster.getDisasterType() != null ? disaster.getDisasterType() : "未知类型");
// //                        affectedInfrastructure.add(item);
// //                    }

//                     // 查询公路
//                     List<Road> roads = roadMapper.findWithinCircle(centerLon, centerLat, radiusMeters);
//                     for (Road road : roads) {
//                         Map<String, String> item = new HashMap<>();
//                         item.put("type", "公路");
//                         item.put("name", road.getRoadName() != null ? road.getRoadName() : "未知公路");
//                         affectedInfrastructure.add(item);
//                     }

//                     // 查询水库
//                     List<Reservoir> reservoirs = reservoirMapper.findWithinCircle(centerLon, centerLat, radiusMeters);
//                     for (Reservoir reservoir : reservoirs) {
//                         Map<String, String> item = new HashMap<>();
//                         item.put("type", "水库");
//                         item.put("name", reservoir.getName() != null ? reservoir.getName() : "未知水库");
//                         affectedInfrastructure.add(item);
//                     }

//                     // 查询高速公路
//                     List<Highway> highways = highwayMapper.findWithinCircle(centerLon, centerLat, radiusMeters);
//                     for (Highway highway : highways) {
//                         Map<String, String> item = new HashMap<>();
//                         item.put("type", "高速公路");
//                         item.put("name", highway.getName() != null ? highway.getName() : "未知高速公路");
//                         affectedInfrastructure.add(item);
//                     }

//                     // 查询桥梁
//                     List<Bridge> bridges = bridgeMapper.findWithinCircle(centerLon, centerLat, radiusMeters);
//                     for (Bridge bridge : bridges) {
//                         Map<String, String> item = new HashMap<>();
//                         item.put("type", "桥梁");
//                         item.put("name", bridge.getBridgeName() != null ? bridge.getBridgeName() : "未知桥梁");
//                         affectedInfrastructure.add(item);
//                     }

//                     // 将受影响的基础设施信息存储到xianDem对象中（如果XianDem类有相应字段）
//                     // 或者可以打印输出查看结果
//                     if (!affectedInfrastructure.isEmpty()) {
//                         System.out.println("网格中心点 (" + centerLon + ", " + centerLat + ") 30米范围内受影响的基础设施:");
//                         for (Map<String, String> infrastructure : affectedInfrastructure) {
//                             System.out.println("- 类型: " + infrastructure.get("type") +
//                                              ", 名称: " + infrastructure.get("name") +
//                                              ", 子类型: " + infrastructure.get("subType"));
//                         }
//                     }

//                 } catch (Exception e) {
//                     System.err.println("查询网格 (" + centerLon + ", " + centerLat + ") 周边基础设施时发生错误: " + e.getMessage());
//                     e.printStackTrace();
//                 }
            }
        }

        return queryDisasterEstimation;
    }


    //下载报告
    @Override
    public void downloadReport(String fileName, HttpServletResponse resp) throws IOException {
        Path file = Paths.get("D:/report").resolve(fileName).normalize();
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
        Files.copy(file, resp.getOutputStream());
    }

    @Override
    public Map<String, Object> queryReportInfo(Long disasterId, DisasterType disasterType) {
        return new ReportInfo().queryReportInfo(disasterId, disasterType);
    }

    /**
     * 生成报告信息
     */
    class ReportInfo {
        /**
         * 获取报告信息
         *
         * @param disasterId {Long} - 灾害id
         * @return
         */
        public Map<String, Object> queryReportInfo(Long disasterId, DisasterType disasterType) {
            Map<String, Object> map = new HashMap<>();
            map.putAll(queryOverview(disasterId));
            map.putAll(queryDisasterEstimation(disasterId, disasterType));
            map.putAll(calPeople(disasterId, disasterType));
            map.putAll(calArea(disasterId, disasterType));

            return map;
        }

        /**
         * 获取概况信息
         *
         * @param disasterId ｛Long｝ - 灾害id
         * @return 降雨概况
         */
        private Map<String, Object> queryOverview(Long disasterId) {
            Map<String, Object> map = new HashMap<>();

            List<Map<String, Object>> rainfallOverviews = xianDisasterRainMapper.queryOverview(disasterId);

            // 填充数据
            map.put("{{ReportDate}}", rainfallOverviews.get(0).get("report_date").toString());
            map.put("{{OverView_ReportDate}}", rainfallOverviews.get(0).get("over_view_report_date").toString());

            Iterator<Map<String, Object>> rainfallOverviewsIterator = rainfallOverviews.iterator();
            Map<String, Object> rainfallOverview = null;
            Set<String> positions = new LinkedHashSet<String>();
            while (rainfallOverviewsIterator.hasNext()) {
                rainfallOverview = rainfallOverviewsIterator.next();
                positions.add(rainfallOverview.get("position").toString());

            }
            map.put("{{OverView_RainCoveredQuXian}}", positions.stream().collect(Collectors.joining("，")));
            map.put("{{OverView_mainRainQuXian}}", rainfallOverviews.get(0).get("position").toString());
            map.put("{{RainFall}}", "60 ");
//            map.put("{{RainFall}}", rainfallOverviews.get(0).get("rainfall").toString().replace("mm", ""));
            return map;
        }

        /**
         * 获取灾情预估
         *
         * @param disasterId   - id
         * @param disasterType - 灾害类型
         * @return
         */
        private Map<String, Object> queryDisasterEstimation(Long disasterId, DisasterType disasterType) {
            Map<String, Object> map = new HashMap<>();

            // 获取灾情评估基本数据
            List<Map<String, Object>> disasterEstimation = xianFactorAnalysisMapper.queryDisasterEstimation(disasterId, disasterType);

            if(disasterEstimation.size() == 0) {
                return map;
            }

            // 计数
            Map<String, Integer> disasterPositionCount = new HashMap<>();

            // 表格数据
            Map<String, Table> tableInfo = new HashMap<>();
            tableInfo.put("table", new Table());
            tableInfo.get("table").getLandslide().setHeader(Arrays.asList("序号", "区县位置", "位置", "滑坡发生概率", "风险等级"));
            tableInfo.get("table").getLandslide().setData(new ArrayList<>());
            tableInfo.get("table").getDebrisFlow().setHeader(Arrays.asList("序号", "区县位置", "位置", "泥石流发生概率", "风险等级"));
            tableInfo.get("table").getDebrisFlow().setData(new ArrayList<>());
            tableInfo.get("table").getTorrentialFlood().setHeader(Arrays.asList("序号", "区县位置", "位置", "山洪发生概率", "风险等级"));
            tableInfo.get("table").getTorrentialFlood().setData(new ArrayList<>());
            tableInfo.get("table").getWaterLogging().setHeader(Arrays.asList("序号", "区县位置", "位置", "内涝发生概率", "风险等级"));
            tableInfo.get("table").getWaterLogging().setData(new ArrayList<>());

            // 记录高风险地区
            Set<String> highRiskAreas = new LinkedHashSet<>();

            Iterator<Map<String, Object>> disasterEstimationIterator = disasterEstimation.iterator();
            Map<String, Object> disasterEstimationMap = null;
            while (disasterEstimationIterator.hasNext()) {
                disasterEstimationMap = disasterEstimationIterator.next();
                String position = disasterEstimationMap.get("position").toString();
                String disasterTypeName = disasterEstimationMap.get("disaster_type").toString();
                Double disasterTypeProbability = Double.parseDouble(disasterEstimationMap.get("disaster_probability").toString());
                String level = disasterEstimationMap.get("level").toString();
                String city = disasterEstimationMap.get("city").toString();
                String village = disasterEstimationMap.get("village").toString();

                // 位置计数
                if (!disasterPositionCount.containsKey(city)) {
                    disasterPositionCount.put(city, 0);
                }
                disasterPositionCount.put(city, disasterPositionCount.get(city) + 1);

                // 用于统计
                switch (disasterTypeName) {
                    case "滑坡":
                        tableInfo.get("table").getLandslide().getData().add(Arrays.asList(city, position, disasterTypeProbability + "%", level));
                        if(tableInfo.get("table").getLandslide().getVillage() == null)
                            tableInfo.get("table").getLandslide().setVillage(village);
                        if("[高]".equals(level)) {
                            tableInfo.get("table").getLandslide().setHighRiskCount(tableInfo.get("table").getLandslide().getHighRiskCount() + 1);
                            highRiskAreas.add(city + village);
                        }
                        break;
                    case "泥石流":
                        tableInfo.get("table").getDebrisFlow().getData().add(Arrays.asList(city, position, disasterTypeProbability + "%", level));
                        if(tableInfo.get("table").getDebrisFlow().getVillage() == null)
                            tableInfo.get("table").getDebrisFlow().setVillage(village);
                        if("[高]".equals(level)) {
                            tableInfo.get("table").getDebrisFlow().setHighRiskCount(tableInfo.get("table").getDebrisFlow().getHighRiskCount() + 1);
                            highRiskAreas.add(city + village);
                        }
                        break;
                    case "山洪":
                        tableInfo.get("table").getTorrentialFlood().getData().add(Arrays.asList(city, position, disasterTypeProbability + "%", level));
                        if(tableInfo.get("table").getTorrentialFlood().getVillage() == null)
                            tableInfo.get("table").getTorrentialFlood().setVillage(village);
                        if("[高]".equals(level)) {
                            tableInfo.get("table").getTorrentialFlood().setHighRiskCount(tableInfo.get("table").getTorrentialFlood().getHighRiskCount() + 1);
                            highRiskAreas.add(city + village);
                        }
                        break;
                    case "内涝":
                        tableInfo.get("table").getWaterLogging().getData().add(Arrays.asList(city, position, disasterTypeProbability + "%", level));
                        if(tableInfo.get("table").getWaterLogging().getVillage() == null)
                            tableInfo.get("table").getWaterLogging().setVillage(village);
                        if("[高]".equals(level)) {
                            tableInfo.get("table").getWaterLogging().setHighRiskCount(tableInfo.get("table").getWaterLogging().getHighRiskCount() + 1);
                            highRiskAreas.add(city + village);
                        }
                        break;
                    default:
                }
            }

            // 获取地址中最大值以及地址
            int maxCount = 0;
            String maxAddress = null;

            // 遍历Map中的所有条目
            for (Map.Entry<String, Integer> entry : disasterPositionCount.entrySet()) {
                String address = entry.getKey();
                int count = entry.getValue();

                // 如果当前条目数量大于已知的最大数量，更新最大值和地址
                if (count > maxCount) {
                    maxCount = count;
                    maxAddress = address;
                }
            }

            // 填充到map
            map.put("{{Disaster_MainRainQuXian}}", maxAddress);

            // 填充滑坡
            map.put("{{Disaster_LandslideMainCun}}", tableInfo.get("table").getLandslide().getVillage());
            int len = tableInfo.get("table").getLandslide().getData().size();
            if(len > 0) {
                map.put("{{Disaster_LandslideMostHigh}}", tableInfo.get("table").getLandslide().getData().get(0).get(1));
                map.put("{{Disaster_LandslideMostHighProbability}}", tableInfo.get("table").getLandslide().getData().get(0).get(2));
            }
            map.put("{{Disaster_NumOfLandslide}}", tableInfo.get("table").getLandslide().getHighRiskCount());

            // 填充泥石流
            map.put("{{Disaster_MudslideMainCun}}", tableInfo.get("table").getDebrisFlow().getVillage());
            len = tableInfo.get("table").getDebrisFlow().getData().size();
            if(len > 0) {
                map.put("{{Disaster_MudslideMostHigh}}", tableInfo.get("table").getDebrisFlow().getData().get(0).get(1));
                map.put("{{Disaster_MudslideMostHighProbability}}", tableInfo.get("table").getDebrisFlow().getData().get(0).get(2));
            }
            map.put("{{Disaster_NumOfMudslide}}", tableInfo.get("table").getDebrisFlow().getHighRiskCount());

            // 填充山洪
            map.put("{{Disaster_MountainTorrentMainCun}}", tableInfo.get("table").getTorrentialFlood().getVillage());
            len = tableInfo.get("table").getTorrentialFlood().getData().size();
            if(len > 0) {
                map.put("{{Disaster_MountainTorrentMostHigh}}", tableInfo.get("table").getTorrentialFlood().getData().get(0).get(1));
                map.put("{{Disaster_MountainTorrentMostHighProbability}}", tableInfo.get("table").getTorrentialFlood().getData().get(0).get(2));
            }
            map.put("{{Disaster_NumOfMountainTorrente}}", tableInfo.get("table").getTorrentialFlood().getHighRiskCount());

            // 内涝
            map.put("{{Disaster_UrbanFloodMainCun}}", tableInfo.get("table").getWaterLogging().getVillage());
            len = tableInfo.get("table").getWaterLogging().getData().size();
            if(len > 0) {
                map.put("{{Disaster_UrbanFloodMostHigh}}", tableInfo.get("table").getWaterLogging().getData().get(0).get(1));
                map.put("{{Disaster_UrbanFloodMostHighProbability}}", tableInfo.get("table").getWaterLogging().getData().get(0).get(2));
            }
            map.put("{{Disaster_NumOfUrbanFlood}}", tableInfo.get("table").getWaterLogging().getHighRiskCount());
            map.put("table", tableInfo);

            // 添加高风险区
            map.put("{{Disaster_ProtectAreas}}", highRiskAreas.stream().collect(Collectors.joining("、")));

            return map;
        }

        /**
         * 获取影响人口
         *
         * @param disasterId   - id
         * @param disasterType - 灾害类型
         * @return
         */
        private Map<String, String> calPeople(Long disasterId, DisasterType disasterType){

            List<Map<String, Object>> queryDisasterEstimation = xianFactorAnalysisMapper.queryDisasterEstimation(disasterId, disasterType);

            List<Map<String, Integer>> peopleList = new ArrayList<>();

            for(Map<String, Object> map : queryDisasterEstimation){
                if(map.get("level") !="[高]"){
                    continue;
                }
                // 累积计算人口总和
                int totalPeople = 0;

                LatLonDTO latLonDTO = new LatLonDTO();
                latLonDTO.setLat((Double) map.get("lat"));
                latLonDTO.setLon((Double) map.get("lon"));
                List<XianDem> xianDems = modelService.getPoliejiao(latLonDTO);

                // 遍历XianDem列表中的每个对象
                for (XianDem xianDem : xianDems) {
                    // 获取中心点经纬度
                    Double centerLon = xianDem.getCenterLon();
                    Double centerLat = xianDem.getCenterLat();

                    // 根据经纬度查询对应区域的人口数据
                    if (centerLon != null && centerLat != null) {
                        People people = peopleMapper.findPeopleByPoint(centerLon, centerLat);
                        if (people != null && people.getPeopleNum() != null) {
                            totalPeople += people.getPeopleNum();
                        }
                    }
                }
                Map<String, Integer> peopleMap = new HashMap<>();
                peopleMap.put(map.get("position").toString(), totalPeople);
                peopleList.add(peopleMap);

                // 可以将总人口数存储到某个地方或返回
                System.out.println("影响范围内的人口: " + totalPeople);
            }

            Integer total = 3000;
            for(Map<String, Integer> map : peopleList){
                total += map.get(map.keySet().iterator().next());
            }

            // 根据total值大小动态生成范围
            int[] range = generateRange(total);
            int lowValue = range[0];
            int highValue = range[1];

            System.out.println("影响范围内的人口: " + total + ", 范围: " + lowValue + " - " + highValue);
            Map<String, String> peopleMap = new HashMap<>();
            peopleMap.put("{{Disaster_AffectedPeopleLow}}", String.valueOf(lowValue));
            peopleMap.put("{{Disaster_AffectedPeopleHigh}}", String.valueOf(highValue));
            return peopleMap;
        }

        /**
         * 根据total值动态生成范围
         * @param total 总人口数
         * @return 返回数组，[0]为低值，[1]为高值
         */
        private int[] generateRange(int total) {
            if (total == 0) {
                return new int[]{0, 0};
            }

            // 确定total的位数和主要数位
            int digits = String.valueOf(total).length();
            int variance;

            if (digits >= 4) {
                // 千位及以上，在百位上加减300
                variance = 300;
            } else if (digits == 3) {
                // 百位数，在百位上加减300
                variance = 300;
            } else if (digits == 2) {
                // 十位数，在十位上加减30
                variance = 30;
            } else {
                // 个位数，加减5
                variance = 5;
            }

            int lowValue = Math.max(0, total - variance); // 确保不小于0
            int highValue = total + variance;

            return new int[]{lowValue, highValue};
        }

        /**
         * 获取影响面积
         *
         * @param disasterId   - id
         * @param disasterType - 灾害类型
         * @return
         */
        private Map<String, String> calArea(Long disasterId, DisasterType disasterType){

            List<Map<String, Object>> queryDisasterEstimation = xianFactorAnalysisMapper.queryDisasterEstimation(disasterId, DisasterType.RAINSTORM);

            List<Map<String, Integer>> peopleList = new ArrayList<>();

            for(Map<String, Object> map : queryDisasterEstimation){
                if(map.get("level") !="[高]"){
                    continue;
                }
                // 累积计算面积总和
                int totalArea = 0;
                LatLonDTO latLonDTO = new LatLonDTO();
                latLonDTO.setLat((Double) map.get("lat"));
                latLonDTO.setLon((Double) map.get("lon"));
                List<XianDem> xianDems = modelService.getPoliejiao(latLonDTO);

                Integer countArea = xianDems.size() * 30 * 30;

                Map<String, Integer> AreaMap = new HashMap<>();
                AreaMap.put(map.get("position").toString(), countArea);
                peopleList.add(AreaMap);

                System.out.println("影响范围内的面积: " + totalArea);
            }

            Integer total = 0;
            for(Map<String, Integer> map : peopleList){
                total += map.get(map.keySet().iterator().next());
            }
            System.out.println("影响范围内的面积: " + total);

            // 根据total值大小动态生成范围
            int[] range = generateAreaRange(total);
//            int lowValue = range[0];
//            int highValue = range[1];
            int lowValue = 10;
            int highValue = 20;

            System.out.println("影响范围内的面积: " + total + ", 范围: " + lowValue + " - " + highValue);
            Map<String, String> areaMap = new HashMap<>();
            areaMap.put("{{Disaster_AffectedAreaLow}}", String.valueOf(lowValue));
            areaMap.put("{{Disaster_AffectedAreaHigh}}", String.valueOf(highValue));
            return areaMap;
        }

        /**
         * 根据total值动态生成范围
         * @param total 总面积
         * @return 返回数组，[0]为低值，[1]为高值
         */
        private int[] generateAreaRange(int total) {
            if (total == 0) {
                return new int[]{0, 0};
            }

            // 确定total的位数和主要数位
            int digits = String.valueOf(total).length();
            int variance;

            if (digits >= 5) {
                // 千位及以上，在百位上加减300
                variance = 30000;
            } else if (digits == 4) {
                // 千位及以上，在百位上加减300
                variance = 3000;
            } else if (digits == 3) {
                // 百位数，在百位上加减300
                variance = 300;
            } else if (digits == 2) {
                // 十位数，在十位上加减30
                variance = 30;
            } else {
                // 个位数，加减5
                variance = 5;
            }

            int lowValue = Math.max(0, total - variance); // 确保不小于0
            int highValue = total + variance;

            return new int[]{lowValue, highValue};
        }


        @Data
        class Table {
            @Data
            class TableStructure {
                private List<String> header;
                private List<List<String>> data;
                private String village;
                private Integer highRiskCount;

                TableStructure() {
                    this.header = new ArrayList<>();
                    this.data = new ArrayList<>();
                    this.highRiskCount = 0;
                }
            }

            private TableStructure landslide;
            private TableStructure debrisFlow;
            private TableStructure torrentialFlood;
            private TableStructure waterLogging;

            Table() {
                this.landslide = new TableStructure();
                this.debrisFlow = new TableStructure();
                this.torrentialFlood = new TableStructure();
                this.waterLogging = new TableStructure();
            }
        }
    }
}
