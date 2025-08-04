package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.R;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DownloadreportService {
    public R<String> saveCanvas(MultipartFile file) throws IOException;
    public  R<String> generateRainReport(String imgUrl) throws IOException, InvalidFormatException;

     public void downloadReport(String fileName, HttpServletResponse resp) throws IOException;

}
