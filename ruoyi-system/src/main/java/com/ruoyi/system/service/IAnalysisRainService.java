package com.ruoyi.system.service;

import java.util.HashMap;
import java.util.List;

public interface IAnalysisRainService {

    public HashMap<String, List> getRainPreHours();

    public HashMap<String, List> getAdminCodeRain(int adminCode);
}
