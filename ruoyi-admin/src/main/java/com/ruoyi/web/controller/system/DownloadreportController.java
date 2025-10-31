package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.domain.params.RainParams;
import com.ruoyi.system.domain.params.RainQuery;
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
@RequestMapping("/admins/downloadReport")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class DownloadreportController {

    private static final Logger log = LoggerFactory.getLogger(DownloadreportController.class);

    @Resource
    private DownloadreportService downloadreportService;

    @PostMapping("/generateRainReport")
    public R<String> generateRainReport(@RequestBody RainParams rainQuery) throws IOException, InvalidFormatException{
        return downloadreportService.generateRainReport(rainQuery.getRainId(),rainQuery.getRainQueueId(),rainQuery.getRainDisasterId());
    }
    @GetMapping("/file/{fileName}")
    @CrossOrigin(origins = "*")
    public void downloadReport(@PathVariable String fileName, HttpServletResponse resp) throws IOException {
        downloadreportService.downloadReport(fileName,resp);
    }
//    @GetMapping("/test")
//    public ResponseEntity<?> test(@RequestParam Integer disasterId){
//        downloadreportService.generateRainReportEntity(disasterId);
//        return ResponseEntity.ok("ckw");
//    }
}
