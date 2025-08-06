package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.service.DownloadreportService;
import lombok.var;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 22:04
 * @description: 风险区实现类
 */

@Service
public class DownloadreportServiceImpl implements DownloadreportService {

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

    @Override
    public  R<String> generateRainReport(String imgUrl) throws IOException, InvalidFormatException {
        // 1. 读取图片
        Path tempImgPath = Paths.get(imgUrl);
        if (!Files.exists(tempImgPath)) return R.fail("图片不存在");

        // 生成 Word
        Path wordDir = Paths.get("D:/report");
        if (!Files.exists(wordDir)) {
            Files.createDirectories(wordDir);
        }

        String wordName = "report_" + System.currentTimeMillis() + ".docx";
        Path wordPath = wordDir.resolve(wordName);

        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph p = doc.createParagraph();
            XWPFRun run = p.createRun();
            try (InputStream is = Files.newInputStream(tempImgPath)) {
                run.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG,
                        tempImgPath.getFileName().toString(),
                        Units.toEMU(400), Units.toEMU(250));
            }
            try (OutputStream os = Files.newOutputStream(wordPath)) {
                doc.write(os);
            }
        }

        return R.ok(wordName);

//        Path outPath = Paths.get("D:/report.docx");   // 任意目录

//        try (XWPFDocument doc = new XWPFDocument()) {
//            Path picpath = Paths.get("D:/testImg.png");
//            // 2. 插入图片
//            try (InputStream is = Files.newInputStream(picpath)) {
//                XWPFParagraph imgP = doc.createParagraph();
//                XWPFRun imgR = imgP.createRun();
//                imgR.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG,
//                        "testImg.png", Units.toEMU(400), Units.toEMU(250));
//            }
//
//            // 3. 写出到磁盘
//            try (OutputStream os = Files.newOutputStream(outPath)) {
//                doc.write(os);
//            }
//            System.out.println("Word 已生成：" + outPath.toAbsolutePath());
//        } catch (Exception e) {
//            throw new RuntimeException("生成Word失败", e);
//        }

    }
    @Override
    public void downloadReport(String fileName, HttpServletResponse resp) throws IOException {
        Path file = Paths.get("D:/report").resolve(fileName).normalize();
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition","attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
        Files.copy(file, resp.getOutputStream());
    }

}
