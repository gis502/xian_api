package com.ruoyi.system.service;

import com.ruoyi.system.domain.params.DisasterParam;

import java.util.HashMap;
import java.util.List;

public interface IHistoricalSimilarityAnalysisService {
    HashMap<String, List> getRainAffectPoints (DisasterParam disasterParam);
}
