package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.DisasterType;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.mapper.XianFactorAnalysisMapper;
import com.ruoyi.system.domain.entity.XianDem;
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
import com.ruoyi.system.mapper.PeopleMapper;
import com.ruoyi.system.domain.entity.People;





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
        ReportInfo reportInfo = new ReportInfo();
        Map<String, Object> stringObjectMap = reportInfo.queryReportInfo(1L, DisasterType.RAINSTORM);
        System.out.println("原始数据: " + stringObjectMap);


        // 生成 Word 路径
        Path wordDir = Paths.get("D:/report");
        if (!Files.exists(wordDir)) {
            Files.createDirectories(wordDir);
        }
        String wordName = "report_" + System.currentTimeMillis() + ".docx";
        Path wordPath = wordDir.resolve(wordName);

        // 构造复杂的table数据结构示例（这里示范滑坡的结构）
        Map<String, Object> tableMap = new HashMap<>();



        // 从stringObjectMap中获取table（以HashMap形式处理，避免类型转换）
        Object tableObj = stringObjectMap.get("table");
        if (tableObj instanceof Map) {
            tableMap = (Map<String, Object>) tableObj; // 确认是Map类型后转换
        }

        // 定义各灾害数据的二维数组（默认空数组）
        String[][] landslideData = new String[0][0];
        String[][] mudslideData = new String[0][0];
        String[][] mountainTorrentData = new String[0][0];
        String[][] urbanFloodData = new String[0][0];

        // 1. 解析滑坡数据（tableMap中的"landslide"字段）
        Object landslideObj = tableMap.get("landslide");
        if (landslideObj instanceof Map) {
            Map<String, Object> landslideMap = (Map<String, Object>) landslideObj;
            landslideData = convertMapDataTo2DArray(landslideMap);
        }

        // 2. 解析泥石流数据（tableMap中的"debrisFlow"字段）
        Object mudslideObj = tableMap.get("debrisFlow");
        if (mudslideObj instanceof Map) {
            Map<String, Object> mudslideMap = (Map<String, Object>) mudslideObj;
            mudslideData = convertMapDataTo2DArray(mudslideMap);
        }

        // 3. 解析山洪数据（tableMap中的"torrentialFlood"字段）
        Object mountainTorrentObj = tableMap.get("torrentialFlood");
        if (mountainTorrentObj instanceof Map) {
            Map<String, Object> mountainTorrentMap = (Map<String, Object>) mountainTorrentObj;
            mountainTorrentData = convertMapDataTo2DArray(mountainTorrentMap);
        }

        // 4. 解析城市内涝数据（tableMap中的"waterLogging"字段）
        Object urbanFloodObj = tableMap.get("waterLogging");
        if (urbanFloodObj instanceof Map) {
            Map<String, Object> urbanFloodMap = (Map<String, Object>) urbanFloodObj;
            urbanFloodData = convertMapDataTo2DArray(urbanFloodMap);
        }

        System.out.println("滑坡数据转换后: " + Arrays.deepToString(landslideData));


        // 表格配置（保持不变）
        String landslideTableName = "滑坡灾害预测概率统计表";
        String[] landslideHead = {"序号", "区县位置", "详细位置", "滑坡发生概率", "风险等级"};
        int[] landslideColWidths = {1500, 3000, 8000, 3000, 2000};

        String mudslideTableName = "泥石流灾害预测概率统计表";
        String[] mudslideHead = {"序号", "区县位置", "详细位置", "泥石流发生概率", "风险等级"};
        int[] mudslideColWidths = {1500, 3000, 8000, 3000, 2000};

        String mountainTorrentTableName = "山洪灾害预测概率统计表";
        String[] mountainTorrentHead = {"序号", "区县位置", "详细位置", "山洪发生概率", "风险等级"};
        int[] mountainTorrentColWidths = {1500, 3000, 8000, 3000, 2000};

        String urbanFloodTableName = "城市内涝灾害预测概率统计表";
        String[] urbanFloodHead = {"序号", "区县位置", "详细位置", "城市内涝发生概率", "风险等级"};
        int[] urbanFloodColWidths = {1500, 3000, 8000, 3000, 2000};

        String lifelineProjectTableName = "生命线工程影响统计表";
        String[] lifelineProjectHead = {"序号", "类型", "名称"};
        int[] lifelineProjectColWidths = {1500, 3000, 15000};
        String[][] lifelineProjectData = {
                {"道路", "G210 国道（沣峪村段）"},
                {"道路", "喂子坪村通村公路"},
                {"输电线路", "35千伏输电线路（沿红草河沟谷段）"},
                {"通信设施", "移动通信基站（鸡窝子组）"},
                {"输水管道", "镇级饮用水主管线（经大门村组）"}
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

            // 插入表格
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

    // 转换方法：从Map中解析data数据并转换为二维数组
    private String[][] convertMapDataTo2DArray(Map<String, Object> disasterMap) {
        // 从灾害Map中获取data字段（你的数据中是List<List<String>>格式）
        Object dataObj = disasterMap.get("data");
        if (!(dataObj instanceof List)) {
            return new String[0][0]; // 非List类型直接返回空数组
        }

        List<?> rawDataList = (List<?>) dataObj;
        if (rawDataList.isEmpty()) {
            return new String[0][0];
        }

        // 转换为目标二维数组（5列：序号、区县位置、详细位置、概率、风险等级）
        String[][] result = new String[rawDataList.size()][5];
        for (int i = 0; i < rawDataList.size(); i++) {
            Object rowObj = rawDataList.get(i);
            if (!(rowObj instanceof List)) {
                continue; // 跳过非List类型的行
            }

            List<?> rowList = (List<?>) rowObj;
            // 填充序号（第0列）
            result[i][0] = String.valueOf(i + 1);
            // 填充区县位置（第1列，对应rowList的第0个元素）
            result[i][1] = rowList.size() > 0 ? rowList.get(0).toString() : "";
            // 填充详细位置（第2列，对应rowList的第1个元素）
            result[i][2] = rowList.size() > 1 ? rowList.get(1).toString() : "";
            // 填充概率（第3列，对应rowList的第2个元素）
            result[i][3] = rowList.size() > 2 ? rowList.get(2).toString() : "";
            // 填充风险等级（第4列，对应rowList的第3个元素）
            result[i][4] = rowList.size() > 3 ? rowList.get(3).toString() : "";
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
        map.put("{{OverView_RainCoveredQuXian}}", getStringValue(dataMap, "{{OverView_RainCoveredQuXian}}", "未知区域"));
        map.put("{{OverView_mainRainQuXian}}", getStringValue(dataMap, "{{OverView_mainRainQuXian}}", "未知区域"));
        map.put("{{Disaster_MainRainQuXian}}", getStringValue(dataMap, "{{Disaster_MainRainQuXian}}", "未知区域"));
        map.put("{{Disaster_LandslideMainCun}}", getStringValue(dataMap, "{{Disaster_LandslideMainCun}}", "未知村庄"));
        map.put("{{Disaster_LandslideMostHigh}}", getStringValue(dataMap, "{{Disaster_LandslideMostHigh}}", "未知地点"));
        map.put("{{Disaster_LandslideMostHighProbability}}", getStringValue(dataMap, "{{Disaster_LandslideMostHighProbability}}", "未知"));
        map.put("{{Disaster_NumOfLandslide}}", getStringValue(dataMap, "{{Disaster_NumOfLandslide}}", "0"));
        map.put("{{Disaster_MudslideMainCun}}", getStringValue(dataMap, "{{Disaster_MudslideMainCun}}", "无数据"));
        map.put("{{Disaster_NumOfMudslide}}", getStringValue(dataMap, "{{Disaster_NumOfMudslide}}", "0"));
        map.put("{{Disaster_MountainTorrentMainCun}}", getStringValue(dataMap, "{{Disaster_MountainTorrentMainCun}}", "无数据"));
        map.put("{{Disaster_NumOfMountainTorrente}}", getStringValue(dataMap, "{{Disaster_NumOfMountainTorrente}}", "0"));
        map.put("{{Disaster_UrbanFloodMainCun}}", getStringValue(dataMap, "{{Disaster_UrbanFloodMainCun}}", "无数据"));
        map.put("{{Disaster_NumOfUrbanFlood}}", getStringValue(dataMap, "{{Disaster_NumOfUrbanFlood}}", "0"));
        map.put("{{Disaster_ProtectAreas}}", getStringValue(dataMap, "{{Disaster_ProtectAreas}}", "未知区域"));

        // 补充其他必要的默认值
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

        String overViewOrg = "{{OverView_ReportDate}}，西安市部分区域（包括{{OverView_RainCoveredQuXian}}）已出现100毫米以上降水。根据最新气象监测数据，暴雨主要集中在{{OverView_mainRainQuXian}}一带，区域内山体含水饱和风险增加，具备诱发滑坡、泥石流、山洪和城市内涝等次生灾害的典型触发条件。";
        String overView = replacePlaceholders(overViewOrg, map);
        replaceMap.put("{{OverView}}", overView);

        String landslideDescribe = "";
        if (!containsHighRisk(landslideData)) {
            landslideDescribe = "在本次评估中，多个隐患点的滑坡发生概率处于低风险。但仍需采取适当的预防措施，以应对可能的滑坡事件。";
        } else {
            String landslideDescribeOrg = "{{Disaster_LandslideMainCun}}为滑坡高风险区域。{{Disaster_LandslideMostHigh}}滑坡概率达{{Disaster_LandslideMostHighProbability}}，为当前评估区域内滑坡风险最高点。在本轮强降雨影响下，共有{{Disaster_NumOfLandslide}}处滑坡隐患点被评估为高风险，存在失稳可能，需立即加强防范。";
            landslideDescribe = replacePlaceholders(landslideDescribeOrg, map);
        }
        replaceMap.put("{{Disaster_LandslideDescribe}}", landslideDescribe);

        String mudslideDescribe = "";
        if (mudslideData.length == 0 || !containsHighRisk(mudslideData)) {
            mudslideDescribe = "在本次评估中，未发现高风险的泥石流隐患点。";
        } else {
            String mudslideDescribeOrg = "泥石流风险主要集中在{{Disaster_MudslideMainCun}}一带。共有{{Disaster_NumOfMudslide}}处存在较高的泥石流触发风险。";
            mudslideDescribe = replacePlaceholders(mudslideDescribeOrg, map);
        }
        replaceMap.put("{{Disaster_MudslideDescribe}}", mudslideDescribe);

        String mountainTorrentDescribe = "";
        if (mountainTorrentData.length == 0 || !containsHighRisk(mountainTorrentData)) {
            mountainTorrentDescribe = "在本次评估中，未发现高风险的山洪隐患点。";
        } else {
            String mountainTorrentDescribeOrg = "山洪风险主要集中在{{Disaster_MountainTorrentMainCun}}附近区域。在持续降雨背景下共有{{Disaster_NumOfMountainTorrente}}处存在山洪骤发风险，需加强预警与应急准备。";
            mountainTorrentDescribe = replacePlaceholders(mountainTorrentDescribeOrg, map);
        }
        replaceMap.put("{{Disaster_MountainTorrentDescribe}}", mountainTorrentDescribe);

        String urbanFloodDescribe = "";
        if (urbanFloodData.length == 0 || !containsHighRisk(urbanFloodData)) {
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


    // 风险等级判断（保持不变）
    public static boolean containsHighRisk(String[][] data) {
        if (data == null || data.length == 0) {
            return false;
        }
        for (String[] row : data) {
            if (row.length > 4 && "高".equals(row[4])) {
                return true;
            }
        }
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


        // 6. 数据行（列数 = colWidths.length，不再写死）
        for (int i = 0; i < data.length; i++) {
            XWPFTableRow row = table.createRow();
            // 依次写每一列
            for (int c = 0; c < colWidths.length; c++) {
                String val = (c == 0) ? String.valueOf(i + 1) : data[i][c - 1];
                XWPFTableCell cell = row.getCell(c);
                if (cell.getParagraphs().size() == 0) {
                    cell.addParagraph(); // 确保单元格有段落
                }
                XWPFRun run = cell.getParagraphs().get(0).createRun();
                run.setText(val);
                run.setFontFamily("宋体"); // 设置字体为宋体
                run.setFontSize(11); // 设置字号为11号[^63^]
            }
            // 整行一次性：固定 1.06 cm 行高 + 上下左右居中 + 段前段后 0 磅
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

    public List<XianDem> calPeople(){
        List<Map<String, Object>> queryDisasterEstimation = xianFactorAnalysisMapper.queryDisasterEstimation(1L, DisasterType.RAINSTORM);

        List<Map<String, Integer>> peopleList = new ArrayList<>();

        LatLonDTO latLonDTO1 = new LatLonDTO();
        latLonDTO1.setLat(34.02667);
        latLonDTO1.setLon(108.04083);

        List<XianDem> xianDems1 = modelService.getPoliejiao(latLonDTO1);

        for(Map<String, Object> map : queryDisasterEstimation){
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

        Integer total = 0;
        for(Map<String, Integer> map : peopleList){
            total += map.get(map.keySet().iterator().next());
        }
        System.out.println("影响范围内的人口: " + total);

        return xianDems1;
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
                        if("高".equals(level)) {
                            tableInfo.get("table").getLandslide().setHighRiskCount(tableInfo.get("table").getLandslide().getHighRiskCount() + 1);
                            highRiskAreas.add(city + village);
                        }
                        break;
                    case "泥石流":
                        tableInfo.get("table").getDebrisFlow().getData().add(Arrays.asList(city, position, disasterTypeProbability + "%", level));
                        if(tableInfo.get("table").getDebrisFlow().getVillage() == null)
                            tableInfo.get("table").getDebrisFlow().setVillage(village);
                        if("高".equals(level)) {
                            tableInfo.get("table").getDebrisFlow().setHighRiskCount(tableInfo.get("table").getDebrisFlow().getHighRiskCount() + 1);
                            highRiskAreas.add(city + village);
                        }
                        break;
                    case "山洪":
                        tableInfo.get("table").getTorrentialFlood().getData().add(Arrays.asList(city, position, disasterTypeProbability + "%", level));
                        if(tableInfo.get("table").getTorrentialFlood().getVillage() == null)
                            tableInfo.get("table").getTorrentialFlood().setVillage(village);
                        if("高".equals(level)) {
                            tableInfo.get("table").getTorrentialFlood().setHighRiskCount(tableInfo.get("table").getTorrentialFlood().getHighRiskCount() + 1);
                            highRiskAreas.add(city + village);
                        }
                        break;
                    case "内涝":
                        tableInfo.get("table").getWaterLogging().getData().add(Arrays.asList(city, position, disasterTypeProbability + "%", level));
                        if(tableInfo.get("table").getWaterLogging().getVillage() == null)
                            tableInfo.get("table").getWaterLogging().setVillage(village);
                        if("高".equals(level)) {
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

            Integer total = 0;
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

            List<Map<String, Object>> queryDisasterEstimation = xianFactorAnalysisMapper.queryDisasterEstimation(1L, DisasterType.RAINSTORM);

            List<Map<String, Integer>> peopleList = new ArrayList<>();

            for(Map<String, Object> map : queryDisasterEstimation){
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
            int lowValue = range[0];
            int highValue = range[1];

            System.out.println("影响范围内的面积: " + total + ", 范围: " + lowValue + " - " + highValue);
            Map<String, String> areaMap = new HashMap<>();
            areaMap.put("{{Disaster_AffectedAreaLow}}", String.valueOf(lowValue/1000));
            areaMap.put("{{Disaster_AffectedAreaHigh}}", String.valueOf(highValue/1000));
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
