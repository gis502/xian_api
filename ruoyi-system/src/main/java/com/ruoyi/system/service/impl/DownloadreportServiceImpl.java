package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.DisasterType;
import com.ruoyi.system.mapper.XianDisasterRainMapper;
import com.ruoyi.system.mapper.XianFactorAnalysisMapper;
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

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:04
 * @description: 风险区实现类
 */

@Service
public class DownloadreportServiceImpl implements DownloadreportService {
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


        // 生成 Word
        Path wordDir = Paths.get("D:/report");
        if (!Files.exists(wordDir)) {
            Files.createDirectories(wordDir);
        }

        String wordName = "report_" + System.currentTimeMillis() + ".docx";
        Path wordPath = wordDir.resolve(wordName);

        /* 占位符内容 */
//        Map<String, String> map = new HashMap<>();
//        map.put("{{ReportDate}}", "2023年08月11日15时51分");
//        map.put("{{OverView_ReportDate}}", "2023年8月11日15时51分");
//        map.put("{{OverView_RainCoveredQuXian}}", "长安区、临潼区、蓝田县");
//        map.put("{{OverView_mainRainQuXian}}", "长安区");
//        map.put("{{Disaster_MainRainQuXian}}", "长安区");
//        map.put("{{Disaster_LandslideMainCun}}", "喂子坪村");
//        map.put("{{Disaster_LandslideMostHigh}}", "鸡窝子组上鸡窝");
//        map.put("{{Disaster_LandslideMostHighProbability}}", "83%");
//        map.put("{{Disaster_NumOfLandslide}}", "7");
//        map.put("{{Disaster_MudslideMainCun}}", "沣峪村、喂子坪村");
//        map.put("{{Disaster_MudslideMostHigh}}", "大门村组红草河以东");
//        map.put("{{Disaster_MudslideMostHighProbability}}", "81%");
//        map.put("{{Disaster_NumOfMudslide}}", "8");
//        map.put("{{Disaster_MountainTorrentMainCun}}", "沣峪村");
//        map.put("{{Disaster_MountainTorrentMostHigh}}", "大门村组红草河以东");
//        map.put("{{Disaster_MountainTorrentMostHighProbability}}", "83%");
//        map.put("{{Disaster_NumOfMountainTorrente}}", "2");
//        map.put("{{Disaster_UrbanFloodMainCun}}", "长安区、雁塔区");
//        map.put("{{Disaster_UrbanFloodMostHigh}}", "靖宁路与西部大道十字");
//        map.put("{{Disaster_UrbanFloodMostHighProbability}}", "73%");
//        map.put("{{Disaster_NumOfUrbanFlood}}", "2");
//        map.put("{{Disaster_ProtectAreas}}", "长安区喂子坪村、沣峪村，以及靖宁路与西部大道十字交汇区域、朱雀市场");
//        map.put("{{Disaster_AffectedAreaLow}}", "xx");
//        map.put("{{Disaster_AffectedAreaHigh}}", "xx");
//        map.put("{{Disaster_AffectedPeopleLow}}", "xx");
//        map.put("{{Disaster_AffectedPeopleHigh}}", "xx");
//        map.put("{{Disposal_AffectedByFloodAndSlide}}", "喂子坪村、沣峪村");
//        map.put("{{Disposal_AffectedByUrbanFlood}}", "长安区靖宁路与西部大道十字交汇区域、朱雀市场等");


        //表格内容
        String landslideTableName = "滑坡灾害预测概率统计表";
        String[] landslideHead = {"序号", "位置", "滑坡发生概率", "风险等级"};
        int[] landslideColWidths = {2540, 15480, 6300, 3780}; // 序号 位置 概率 等级
        String[][] landslideData = {
                {"喂子坪村鸡窝子组上鸡窝(B2)", "83%", "高"},
                {"喂子坪村北石槽组原北石槽村(B10)", "78%", "高"},
                {"喂子坪村北石槽组南石槽沟西口(B9)", "76%", "高"},
                {"喂子坪村鸡窝子组龙窝子-凤凰咀(B3)", "76%", "高"},
                {"喂子坪村北石槽组南石槽沟内(B7)", "75%", "高"},
                {"喂子坪村青岗树组夭佛岩(B4)", "74%", "高"},
                {"喂子坪村大坪组大坪(B10)", "74%", "高"},
                {"沣峪村石峡沟组原石峡沟村(B6)", "60%", "中"},
                {"沣峪村大门村组红草河以东(B3)", "62%", "中"},
                {"上王村六组翠微宫园(C2)", "22%", "低"}
        };

        String MudslideTableName = "泥石流灾害预测概率统计表";
        String[] MudslideHead = {"序号", "位置", "泥石流发生概率", "风险等级"};
        int[] MudslideColWidths = {2540, 15480, 6300, 3780}; // 序号 位置 概率 等级
        String[][] MudslideData = {
                {"沣峪村大门村组红草河以东(B3)", "81%", "高"},
                {"沣峪村石峡沟组原石峡沟村(B6)", "78%", "高"},
                {"喂子坪村鸡窝子组上鸡窝(B2)", "76%", "高"},
                {"喂子坪村北石槽组原北石槽村(B10)", "74%", "高"},
                {"喂子坪村北石槽组南石槽沟西口(B9)", "74%", "高"},
                {"喂子坪村鸡窝子组龙窝子-凤凰咀(B3)", "73%", "高"},
                {"喂子坪村北石槽组南石槽沟内(B7)", "72%", "高"},
                {"喂子坪村青岗树组夭佛岩(B4)", "70%", "高"},
                {"喂子坪村大坪组大坪(B10)", "68%", "中"},
                {"上王村六组翠微宫园(C2)", "32%", "低"}
        };

        String MountainTorrentTableName = "山洪灾害预测概率统计表";
        String[] MountainTorrentHead = {"序号", "位置", "山洪发生概率", "风险等级"};
        int[] MountainTorrentColWidths = {2540, 15480, 6300, 3780}; // 序号 位置 概率 等级
        String[][] MountainTorrentData = {
                {"沣峪村大门村组红草河以东(B3)", "84%", "高"},
                {"沣峪村石峡沟组原石峡沟村(B6)", "82%", "高"},
                {"喂子坪村鸡窝子组上鸡窝(B2)", "63%", "中"},
                {"喂子坪村北石槽组原北石槽村(B10)", "62%", "中"},
                {"喂子坪村北石槽组南石槽沟西口(B9)", "62%", "中"},
                {"喂子坪村鸡窝子组龙窝子-凤凰咀(B3)", "61%", "中"},
                {"喂子坪村北石槽组南石槽沟内(B7)", "61%", "中"},
                {"喂子坪村青岗树组夭佛岩(B4)", "60%", "中"},
                {"喂子坪村大坪组大坪(B10)", "58%", "中"},
                {"上王村六组翠微宫园(C2)", "25%", "低"}
        };

        String UrbanFloodTableName = "城市内涝灾害预测概率统计表";
        String[] UrbanFloodHead = {"序号", "位置", "城市内涝发生概率", "风险等级"};
        int[] UrbanFloodColWidths = {2540, 14570, 7210, 3780}; // 序号 位置 概率 等级
        String[][] UrbanFloodData = {
                {"长安区靖宁路与西部大道十字", "73%", "高"},
                {"长安区朱雀市场", "71%", "高"},
                {"长安区西部大道积水点", "68%", "中"},
                {"长安区学府大街西段", "66%", "中"},
                {"雁塔区含光路崇业路", "65%", "中"},
                {"雁塔区小寨十字", "64%", "中"},
                {"雁塔区永城路下穿", "64%", "中"},
                {"雁塔区西影路阳光小区", "59%", "中"},
                {"雁塔区咸宁东路恒大绿洲", "58%", "中"},
                {"高新区西三环丈八立交", "22%", "低"}
        };

        String LifelineProjectTableName = "生命线工程影响统计表";
        String[] LifelineProjectHead = {"序号", "类型", "位置"};
        int[] LifelineProjectColWidths = {2540, 7210, 18350}; // 序号 位置 概率 等级
        String[][] LifelineProjectData = {
                {"道路", "G210 国道（沣峪村段）"},
                {"道路", "喂子坪村通村公路"},
                {"输电线路", "35千伏输电线路（沿红草河沟谷段）"},
                {"通信设施", "移动通信基站（鸡窝子组）"},
                {"输水管道", "镇级饮用水主管线（经大门村组）"}
        };

        Map<String, String> replaceMap = writeDescibe(landslideData, MudslideData, MountainTorrentData, UrbanFloodData);
        String pictitle = "灾情影响分布图";
        /* 读模板并替换 */
        try (InputStream template = getClass().getResourceAsStream("/reportTemplate/暴雨应急预评估报告模板.docx");
             XWPFDocument doc = new XWPFDocument(template)) {

            // 逐段替换
            for (XWPFParagraph p : doc.getParagraphs()) {
                replaceInParagraph(p, replaceMap);
            }

            //插入表格
            insertTableAfterTitle(doc, landslideTableName, landslideHead, landslideData, landslideColWidths);
            insertTableAfterTitle(doc, MudslideTableName, MudslideHead, MudslideData, MudslideColWidths);
            insertTableAfterTitle(doc, MountainTorrentTableName, MountainTorrentHead, MountainTorrentData, MountainTorrentColWidths);
            insertTableAfterTitle(doc, UrbanFloodTableName, UrbanFloodHead, UrbanFloodData, UrbanFloodColWidths);
            insertTableAfterTitle(doc, LifelineProjectTableName, LifelineProjectHead, LifelineProjectData, LifelineProjectColWidths);
            //插入图片
            insertPicBeforeTitle(doc, pictitle, imgUrl);

            try (OutputStream os = Files.newOutputStream(wordPath)) {
                doc.write(os);
            }
        }
        return R.ok(wordName);
    }

    //描述文段
    private static Map<String, String> writeDescibe(String[][] landslideData, String[][] MudslideData, String[][] MountainTorrentData, String[][] UrbanFloodData) {
        Map<String, String> map = new HashMap<>();
        map.put("{{ReportDate}}", "2023年08月11日15时51分");
        map.put("{{OverView_ReportDate}}", "2023年8月11日15时51分");
        map.put("{{OverView_RainCoveredQuXian}}", "长安区、临潼区、蓝田县");
        map.put("{{OverView_mainRainQuXian}}", "长安区");
        map.put("{{Disaster_MainRainQuXian}}", "长安区");
        map.put("{{Disaster_LandslideMainCun}}", "喂子坪村");
        map.put("{{Disaster_LandslideMostHigh}}", "鸡窝子组上鸡窝");
        map.put("{{Disaster_LandslideMostHighProbability}}", "83%");
        map.put("{{Disaster_NumOfLandslide}}", "7");
        map.put("{{Disaster_MudslideMainCun}}", "沣峪村、喂子坪村");
        map.put("{{Disaster_MudslideMostHigh}}", "大门村组红草河以东");
        map.put("{{Disaster_MudslideMostHighProbability}}", "81%");
        map.put("{{Disaster_NumOfMudslide}}", "8");
        map.put("{{Disaster_MountainTorrentMainCun}}", "沣峪村");
        map.put("{{Disaster_MountainTorrentMostHigh}}", "大门村组红草河以东");
        map.put("{{Disaster_MountainTorrentMostHighProbability}}", "83%");
        map.put("{{Disaster_NumOfMountainTorrente}}", "2");
        map.put("{{Disaster_UrbanFloodMainCun}}", "长安区、雁塔区");
        map.put("{{Disaster_UrbanFloodMostHigh}}", "靖宁路与西部大道十字");
        map.put("{{Disaster_UrbanFloodMostHighProbability}}", "73%");
        map.put("{{Disaster_NumOfUrbanFlood}}", "2");
        map.put("{{Disaster_ProtectAreas}}", "长安区喂子坪村、沣峪村，以及靖宁路与西部大道十字交汇区域、朱雀市场");
        map.put("{{Disaster_AffectedAreaLow}}", "xx");
        map.put("{{Disaster_AffectedAreaHigh}}", "xx");
        map.put("{{Disaster_AffectedPeopleLow}}", "xx");
        map.put("{{Disaster_AffectedPeopleHigh}}", "xx");
        map.put("{{Disposal_AffectedByFloodAndSlide}}", "喂子坪村、沣峪村");
        map.put("{{Disposal_AffectedByUrbanFlood}}", "长安区靖宁路与西部大道十字交汇区域、朱雀市场等");

        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("{{ReportDate}}", "2023年08月11日15时51分");

        String DisasterOverAllOrg = "受持续强降雨影响，根据灾害风险评估模型测算结果，{{Disaster_MainRainQuXian}}多个村（组）地质灾害风险显著上升，需高度警惕滑坡、泥石流等次生灾害发生可能。";
        String DisasterOverAll = replacePlaceholders(DisasterOverAllOrg, map);
        replaceMap.put("{{Disaster_OverAll}}", DisasterOverAll);

        String OverViewOrg = "{{OverView_ReportDate}}，西安市部分区域（包括{{OverView_RainCoveredQuXian}}）已出现50毫米以上降水。根据最新气象监测数据，暴雨主要集中在{{OverView_mainRainQuXian}}一带，区域内山体含水饱和风险增加，具备诱发滑坡、泥石流、山洪和城市内涝等次生灾害的典型触发条件。";
        String OverView = replacePlaceholders(OverViewOrg, map);
        replaceMap.put("{{OverView}}", OverView);


        String LandslideDescribe = "";
        if (!containsHighRisk(landslideData)) {
            LandslideDescribe = "在本次评估中，多个隐患点的滑坡发生概率处于低风险。但仍需采取适当的预防措施，以应对可能的滑坡事件。";
        } else {
            String LandslideDescribeOrg = "{{Disaster_LandslideMainCun}}为滑坡高风险区域。{{Disaster_LandslideMostHigh}}滑坡概率达{{Disaster_LandslideMostHighProbability}}，为当前评估区域内滑坡风险最高点。在本轮强降雨影响下，共有{{Disaster_NumOfLandslide}}处滑坡隐患点被评估为高风险，存在失稳可能，需立即加强防范。";
            LandslideDescribe = replacePlaceholders(LandslideDescribeOrg, map);
        }
        replaceMap.put("{{Disaster_LandslideDescribe}}", LandslideDescribe);


        String MudslideDescribe = "";
        if (!containsHighRisk(MudslideData)) {
            MudslideDescribe = "在本次评估中，多个隐患点的泥石流发生概率处于低风险。但仍需采取适当的预防措施，以应对可能的泥石流事件。";
        } else {
            String MudslideDescribeOrg = "泥石流风险主要集中在{{Disaster_MudslideMainCun}}一带。其中{{Disaster_MudslideMostHigh}}点位泥石流发生概率高达{{Disaster_MudslideMostHighProbability}}，为目前模型评估中泥石流风险最高区域。在持续强降雨影响下，共有{{Disaster_NumOfMudslide}}处存在较高的泥石流触发风险。";
            MudslideDescribe = replacePlaceholders(MudslideDescribeOrg, map);
        }
        replaceMap.put("{{Disaster_MudslideDescribe}}", MudslideDescribe);


        String MountainTorrentDescribeOrg = "山洪风险主要集中在{{Disaster_MountainTorrentMainCun}}附近区域。其中{{Disaster_MountainTorrentMostHigh}}点位山洪发生概率达{{Disaster_MountainTorrentMostHighProbability}}，为本轮强降雨期间山洪风险最高区域。在持续降雨背景下共有{{Disaster_NumOfMountainTorrente}}处存在山洪骤发风险，需加强预警与应急准备。";
        String MountainTorrentDescribe = replacePlaceholders(MountainTorrentDescribeOrg, map);
        replaceMap.put("{{Disaster_MountainTorrentDescribe}}", MountainTorrentDescribe);

        String UrbanFloodDescribeOrg = "城市内涝风险主要集中在{{Disaster_UrbanFloodMainCun}}低洼区域及部分老旧排水片区，其中{{Disaster_UrbanFloodMostHigh}}内涝发生概率为{{Disaster_UrbanFloodMostHighProbability}}，为本轮强降雨期间城市内涝风险最高区域。短时强降雨下共有{{Disaster_NumOfUrbanFlood}}处易出现道路积水和排涝不畅等问题，需提前做好排水疏导和交通应对措施。";
        String UrbanFloodDescribe = replacePlaceholders(UrbanFloodDescribeOrg, map);
        replaceMap.put("{{Disaster_UrbanFloodDescribe}}", UrbanFloodDescribe);

        String DisasterAllInAllOrg = "综合研判，{{Disaster_ProtectAreas}}周边等地为本轮强降雨期间次生灾害重点防范区域。建议有关单位强化动态监测和预警信息发布，提前做好人员转移安置及应急物资准备，切实提升应对突发地质灾害的处置能力。";
        String DisasterAllInAll = replacePlaceholders(DisasterAllInAllOrg, map);
        replaceMap.put("{{Disaster_AllInAll}}", DisasterAllInAll);

        String DisasterAffectedAreaAndPeopleOrg = "根据滑坡破裂角模型计算结果，结合区域地形坡向与沟谷汇水条件综合分析，当前在持续强降雨影响下，一旦发生次生灾害，初步预测其可能影响范围在{{Disaster_AffectedAreaLow}}至{{Disaster_AffectedAreaHigh}}平方公里之间。经对区域建筑密度与人口分布数据进行叠加分析，预计受影响人口在{{Disaster_AffectedPeopleLow}}至{{Disaster_AffectedPeopleHigh}}人之间，主要集中在地势低洼、沟谷下游及滑坡堆积方向所覆盖区域。";
        String DisasterAffectedAreaAndPeople = replacePlaceholders(DisasterAffectedAreaAndPeopleOrg, map);
        replaceMap.put("{{Disaster_AffectedAreaAndPeople}}", DisasterAffectedAreaAndPeople);

        String DisasterLifeLine = "其中，多处道路存在中断风险，沿线输电线路和通信基站可能受损，导致局部供电和通信中断；下游加油站及部分工业厂房等重点危险源或引发燃气泄漏和火灾爆炸等次生灾害。";
        replaceMap.put("{{Disaster_LifeLine}}", DisasterLifeLine);

        String DisposalEvacuationOrg = "人员疏散方面，建议优先组织{{Disposal_AffectedByFloodAndSlide}}中紧邻河道的民房、农家乐等高风险区域居民转移，此类区域靠近水体，受山洪和泥石流突发影响最为显著。同时，应重点关注{{Disposal_AffectedByUrbanFlood}}城市内涝易发的低洼积水区域，确保上述重点区域人员能够及时、安全撤离，最大限度保障群众生命安全。";
        String DisposalEvacuation = replacePlaceholders(DisposalEvacuationOrg, map);
        replaceMap.put("{{Disposal_Evacuation}}", DisposalEvacuation);

        return replaceMap;

    }

    public static boolean containsHighRisk(String[][] Data) {
        for (String[] data : Data) {
            // 检查风险等级是否为“高”
            if ("高".equals(data[2])) {
                return true; // 存在高风险
            }
        }
        return false; // 不存在高风险
    }

    public static String replacePlaceholders(String originalText, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            originalText = originalText.replace(entry.getKey(), entry.getValue());
        }
        return originalText;
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