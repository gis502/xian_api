package com.ruoyi.system.service.impl;

import com.ruoyi.system.service.DownloadreportService;
import lombok.var;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:04
 * @description: 风险区实现类
 */

@Service
public class DownloadreportServiceImpl implements DownloadreportService {
    @Override
    public void downloadrainreport() {
//        System.out.println("1111111");
        Path outPath = Paths.get("D:/report.docx");   // 任意目录

        try (XWPFDocument doc = new XWPFDocument()) {
            Path picpath = Paths.get("D:/testImg.png");
            // 2. 插入图片
            try (InputStream is = Files.newInputStream(picpath)){
                XWPFParagraph imgP = doc.createParagraph();
                XWPFRun imgR = imgP.createRun();
                imgR.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG,
                        "testlmg.png", Units.toEMU(400), Units.toEMU(250));
            }

            // 3. 写出到磁盘
            try (OutputStream os = Files.newOutputStream(outPath)) {
                doc.write(os);
            }
            System.out.println("Word 已生成：" + outPath.toAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("生成Word失败", e);
        }

    }
}
