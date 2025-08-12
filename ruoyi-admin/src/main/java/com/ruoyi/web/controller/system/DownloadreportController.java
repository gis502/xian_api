package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.service.DownloadreportService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/downloadReport")
public class DownloadreportController {

    private static final Logger log = LoggerFactory.getLogger(DownloadreportController.class);

    @Resource
    private DownloadreportService downloadreportService;

    @PostMapping("/saveCanvas")
    public R<String> saveCanvas(@RequestParam("file") MultipartFile file) throws IOException {
//        log.info(">>> 收到文件：{}，大小：{}", file.getOriginalFilename(), file.getSize());
        return downloadreportService.saveCanvas(file);
    }

    @PostMapping("/generateRainReport")
    public R<String> generateRainReport(@RequestParam String imgUrl) throws IOException, InvalidFormatException{
        return downloadreportService.generateRainReport(imgUrl);
    }
    @GetMapping("/file/{fileName}")
    public void downloadReport(@PathVariable String fileName, HttpServletResponse resp) throws IOException {
        downloadreportService.downloadReport(fileName,resp);
    }
    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok(downloadreportService.calPeople());
    }
}
