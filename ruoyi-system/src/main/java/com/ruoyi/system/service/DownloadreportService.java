package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.DisasterType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DownloadreportService {

    public R<String> generateRainReport(Integer disasterId) throws IOException, InvalidFormatException;

    public void downloadReport(String fileName, HttpServletResponse resp) throws IOException;
}
