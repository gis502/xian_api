package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.service.DownloadreportService;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:04
 * @description: 风险区实现类
 */

@Service
public class DownloadreportServiceImpl implements DownloadreportService {
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


        String pictitle = "灾情影响分布图";
        /* 读模板并替换 */
        try (InputStream template = getClass().getResourceAsStream("/reportTemplate/暴雨应急预评估报告模板.docx");
             XWPFDocument doc = new XWPFDocument(template)) {

            // 逐段替换
            for (XWPFParagraph p : doc.getParagraphs()) {
                replaceInParagraph(p, map);
            }

            //插入表格
            insertTableAfterTitle(doc, landslideTableName, landslideHead, landslideData,landslideColWidths);
            insertTableAfterTitle(doc, MudslideTableName, MudslideHead, MudslideData,MudslideColWidths);
            insertTableAfterTitle(doc, MountainTorrentTableName, MountainTorrentHead, MountainTorrentData,MountainTorrentColWidths);
            insertTableAfterTitle(doc, UrbanFloodTableName,  UrbanFloodHead,  UrbanFloodData, UrbanFloodColWidths);
            insertTableAfterTitle(doc, LifelineProjectTableName,   LifelineProjectHead,   LifelineProjectData,  LifelineProjectColWidths);
            //插入图片
            insertPicBeforeTitle(doc, pictitle, imgUrl);

            try (OutputStream os = Files.newOutputStream(wordPath)) {
                doc.write(os);
            }
        }
        return R.ok(wordName);
    }

    //   在段落内替换占位符
    private void replaceInParagraph(XWPFParagraph para, Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (XWPFRun r : para.getRuns()) {
            sb.append(r.text());
        }
        String fullText = sb.toString();

        // 替换占位符
        for (Map.Entry<String, String> e : map.entrySet()) {
            fullText = fullText.replace(e.getKey(), e.getValue());
        }

        // 清空原内容
        for (int i = para.getRuns().size() - 1; i >= 0; i--) {
            para.removeRun(i);
        }

        // 重新写入整个字符串，保持原样式
        if (!fullText.isEmpty()) {
            XWPFRun newRun = para.createRun();
            newRun.setText(fullText);
            // 拷贝原样式（可选）
            if (para.getRuns().size() > 0) {
                XWPFRun firstOld = para.createRun(); // 占位
                newRun.getCTR().setRPr(firstOld.getCTR().getRPr());
            }
        }
    }

    //插入表格
    private void insertTableAfterTitle(XWPFDocument doc, String title, String[] headers, String[][] data,int[] colWidths) {

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
        CTHeight  ht  = trPr.addNewTrHeight();
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

}
