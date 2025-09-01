package com.ruoyi.system.service.impl;

import com.ruoyi.common.config.DocumentConfig;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.TypesOfSecondaryDisasters;
import com.ruoyi.common.utils.file.DocumentUtils;
import com.ruoyi.system.domain.entity.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.DownloadreportService;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private XianFactorAnalysisMapper xianFactorAnalysisMapper;

    // word保存路径
    @Value("${document.path.rain.report}")
    private String wordPath;

    public DownloadreportServiceImpl(XianDisasterRainMapper xianDisasterRainMapper) {
        this.xianDisasterRainMapper = xianDisasterRainMapper;
    }

    //生成报告
    @Override
    public R<String> generateRainReport(Integer disasterId) throws IOException {
        // 获取报告数据
        RainReportEntity rainReportEntity = new RainReportEntity();

        // 生成 Word 路径
        Path wordDir = Paths.get(wordPath);
        if (!Files.exists(wordDir)) {
            Files.createDirectories(wordDir);
        }
        String wordName = "report_" + System.currentTimeMillis() + ".docx";
        String wordPath = wordDir.resolve(wordName).toAbsolutePath().toString();

        // 表头宽度
        // int[] widths = {1500, 3000, 8000, 3000};
        try {
            new CreateRainReport().createRainReport(wordPath, rainReportEntity);
        }catch (IOException e) {
            e.printStackTrace();
        }

        return R.ok(wordName);
    }

    public static void main(String[] args) {
        try {
            RainReportEntity rainReportEntity = new RainReportEntity();
            rainReportEntity.setReportTime("08月30日16时24分");
            rainReportEntity.setRainTime("2025年08月30日17时20分");
            rainReportEntity.setRainAreaPosition(Arrays.asList("蓝田县", "周至县"));
            rainReportEntity.setRainAreaQuantity(Arrays.asList("120", "300"));
            rainReportEntity.setConcentratedAreaPosition("蓝田县");
            rainReportEntity.setConcentratedAreaQuantity("500");
            rainReportEntity.setConcentratedAreaAverageQuantity("300");
            rainReportEntity.setConcentratedAreaDetailStreet(Arrays.asList("第一街道", "第二街道"));
            rainReportEntity.setConcentratedAreaDetailQuantity("234");
            rainReportEntity.setConcentratedAreaDetailGrade(10);
            rainReportEntity.setExtremelyHeavyRainstormStreet(Arrays.asList("x街道", "y街道"));
            rainReportEntity.setExtremelyHeavyRainQuantity(Arrays.asList("100", "300.6"));
            rainReportEntity.setRainstormStreet(Arrays.asList("z街道", "d街道"));
            rainReportEntity.setRainstormQuantity(Arrays.asList("78.89"));
            rainReportEntity.setRiskAreaQuantity(596);
            rainReportEntity.setHideAreaQuantity(296);
            rainReportEntity.setHazards(Arrays.asList("高程", "坡度", "植被覆盖率"));
            rainReportEntity.setSignificantIncreaseArea("周至县");
            rainReportEntity.setSignificantIncreaseAreaStreet(Arrays.asList("a街道", "b街道"));
            rainReportEntity.setSignificantIncreaseAreaRiskQuantity(123);
            rainReportEntity.setSignificantIncreaseAreaHideQuantity(78);

            TypesOfSecondaryDisasters[] typesOfSecondaryDisasters = {
                    TypesOfSecondaryDisasters.LANDSLIDE,
                    TypesOfSecondaryDisasters.DEBRIS_FLOW,
                    TypesOfSecondaryDisasters.TORRENTIAL_FLOOD,
                    TypesOfSecondaryDisasters.WATER_LOGGING
            };
            for (int i = 0; i < 4; i++) {
                RainReportEntity.SecondaryDisasterReportEntity secondaryDisasterReportEntity =
                        new RainReportEntity().new SecondaryDisasterReportEntity();
                secondaryDisasterReportEntity.setDisasterType(typesOfSecondaryDisasters[i]);
                secondaryDisasterReportEntity.setRiskStreet("aaa街道");
                secondaryDisasterReportEntity.setRiskPointName("www");
                secondaryDisasterReportEntity.setRiskPointProbability(
                        (Math.random() * 100) + "%"
                );
                secondaryDisasterReportEntity.setInfluencePeopleQuantity(
                        (long) Math.ceil(Math.random() * 300)
                );
                secondaryDisasterReportEntity.setSeriousArea(Arrays.asList("agu", "ijis"));
                secondaryDisasterReportEntity.setExtraLargeArea("长安区");
                secondaryDisasterReportEntity.setExtraLargeAreaPoint("aaa");
                secondaryDisasterReportEntity.setExtraLargeAreaRiskQuantity(100);
                secondaryDisasterReportEntity.setExtraLargeAreaRiskPeopleQuantity(30);
                secondaryDisasterReportEntity.setSmallArea("蓝田县");
                secondaryDisasterReportEntity.setSmallAreaPoint("bbb");
                secondaryDisasterReportEntity.setSmallAreaRiskPeopleQuantity(44);
                rainReportEntity.getSecondaryDisasterReport().add(secondaryDisasterReportEntity);

                // 创建表格数据
                for (int j = 0; j < 5; j++) {
                    RainReportEntity.SecondaryDisasterReportEntity.SecondaryDisasterTableData secondaryDisasterTableData =
                            new RainReportEntity().new SecondaryDisasterReportEntity().new SecondaryDisasterTableData();
                    secondaryDisasterTableData.setPosition("dshsdid");
                    secondaryDisasterTableData.setProbability(Math.random() * 100 + "%");
                    secondaryDisasterTableData.setGrade("高");
                    secondaryDisasterReportEntity.getDisasterTableData().add(secondaryDisasterTableData);
                }
            }
            rainReportEntity.setWorkScheduleArea(Arrays.asList("长安区", "蓝田县"));
            rainReportEntity.setEvacuateTheCrowdArea(Arrays.asList("喂子坪村", "沣峪村"));
            rainReportEntity.setFocusArea(Arrays.asList("长安区靖宁路与西部大道十字交汇区域", "朱雀市场等"));
            new CreateRainReport().createRainReport("D:/test.docx", rainReportEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //下载报告
    @Override
    public void downloadReport(String fileName, HttpServletResponse resp) throws IOException {
        Path file = Paths.get(wordPath).resolve(fileName).normalize();

        System.out.println("尝试下载文件: {}" + file.toString());
        System.out.println("文件是否存在: {}" + Files.exists(file));

        if (!Files.exists(file)) {
            System.out.println("文件不存在: {}" + file.toString());
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
}

/**
 * word文档生成
 */
class CreateRainReport {
    /**
     * 创建word文件
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
            // 第三部分，应急处置建议
            createEmergencyResponseSuggestions(document, rainReportEntity);
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

        String content = String.format("%s，显示%s分别达到%s降水。" +
                        "根据最新实时气象监测数据，降雨主要集中在%s一带，预计总降雨量达%s毫米，平均降雨量可达%s毫米，其中%s的降雨量%s毫米，达到%s级。" +
                        "其中达到特大暴雨的监测站点有%s，达到大暴雨\\暴雨监测站点有%s。",
                rainReportEntity.getRainTime(),
                DocumentUtils.list2Str(rainReportEntity.getRainAreaPosition(), null),
                DocumentUtils.list2Str(rainReportEntity.getRainAreaQuantity(), "毫米"),
                rainReportEntity.getConcentratedAreaPosition(),
                rainReportEntity.getConcentratedAreaQuantity(),
                rainReportEntity.getConcentratedAreaAverageQuantity(),
                DocumentUtils.list2Str(rainReportEntity.getConcentratedAreaDetailStreet(), null),
                rainReportEntity.getConcentratedAreaDetailQuantity(),
                rainReportEntity.getConcentratedAreaDetailGrade(),
                DocumentUtils.streetPlusRainfall(rainReportEntity.getExtremelyHeavyRainstormStreet(), rainReportEntity.getExtremelyHeavyRainQuantity()),
                DocumentUtils.streetPlusRainfall(rainReportEntity.getRainstormStreet(), rainReportEntity.getRainstormQuantity())
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

        String content1 = String.format("受持续强降雨影响，灾害风险评估模型在%d个地质灾害风险区、%d个地质灾害在测隐患点的范围内，结合了%s这些致灾因子进行评估，" +
                        "评估得到%s%s的地质灾害风险显著上升，需高度警惕其中的%d个地质风险区和%d个地质灾害在测隐患点发生山洪、泥石流等次生灾害发生的可能性。",
                rainReportEntity.getRiskAreaQuantity(),
                rainReportEntity.getHideAreaQuantity(),
                DocumentUtils.list2Str(rainReportEntity.getHazards(), null),
                rainReportEntity.getSignificantIncreaseArea(),
                DocumentUtils.list2Str(rainReportEntity.getSignificantIncreaseAreaStreet(), null),
                rainReportEntity.getSignificantIncreaseAreaRiskQuantity(),
                rainReportEntity.getSignificantIncreaseAreaHideQuantity()
        );
        DocumentUtils.addRegularRun(paragraph1, content1);

        // 第二段，滑坡、泥石流、山洪、内涝分别处理
       for (int i = 0; i < rainReportEntity.getSecondaryDisasterReport().size(); i++) {
            RainReportEntity.SecondaryDisasterReportEntity disasterReport = rainReportEntity.getSecondaryDisasterReport().get(i);

            XWPFParagraph paragraph2 = DocumentUtils.addRegularParagraph(doc, null);
            StringBuilder contentBuilder = new StringBuilder();
            if (i == 0) {
                contentBuilder.append("测算结果根据降雨集中区域结合地形地势因素分析得出，");
            }

            // 灾害类型
            contentBuilder.append(
                    String.format("%s风险主要集中在%s附近区域，其中%s点位滑坡发生概率达%s。" +
                                    "预计受影响的人数为%d人，灾害等级将达特大型，为本轮强降雨期间%s风险最高区域，需要提前疏散居民，严加防范。" +
                                    "除此之外，在持续性降雨背景下，%s可能发生的灾害等级将达到大型、中型。",
                            disasterReport.getDisasterType().getDisasterName(),
                            disasterReport.getRiskStreet(),
                            disasterReport.getRiskPointName(),
                            disasterReport.getRiskPointProbability(),
                            disasterReport.getInfluencePeopleQuantity(),
                            disasterReport.getDisasterType().getDisasterName(),
                            DocumentUtils.list2Str(disasterReport.getSeriousArea(), null)
                    )
            );
            DocumentUtils.addAnnotationRun(paragraph2, contentBuilder.toString());

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

            // 第三段
            String text = String.format(
                    "其中，%s%s可能的特大型/大型灾害附近涉及%d个风险区（村庄），预计影响%d人，附近居民和风险影响区域居民必须撤离。" +
                            "%s%s可能的中/小型灾害，预计影响%d人，建议附近居民做好防护，风险影响区域居民建议撤离。",
                    disasterReport.getExtraLargeArea(),
                    disasterReport.getExtraLargeAreaPoint(),
                    disasterReport.getExtraLargeAreaRiskQuantity(),
                    disasterReport.getExtraLargeAreaRiskPeopleQuantity(),
                    disasterReport.getSmallArea(),
                    disasterReport.getSmallAreaPoint(),
                    disasterReport.getSmallAreaRiskPeopleQuantity()
            );
            XWPFParagraph paragraph3 = DocumentUtils.addRegularParagraph(doc, text);
            paragraph3.setIndentationFirstLine(0);      // 首行不缩进
        }
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


}