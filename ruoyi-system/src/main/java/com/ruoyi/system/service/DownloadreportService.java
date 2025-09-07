package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.DisasterType;
import com.ruoyi.system.domain.entity.RainReportEntity;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DownloadreportService {

    public R<String> generateRainReport(String rainId,String rainQueueId,Integer rainDisasterId) throws IOException, InvalidFormatException;

    public void downloadReport(String fileName, HttpServletResponse resp) throws IOException;

//    public RainReportEntity generateRainReportEntity(Integer disasterId);
}
