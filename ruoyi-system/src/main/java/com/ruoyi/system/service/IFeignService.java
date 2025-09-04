package com.ruoyi.system.service;


import com.ruoyi.system.domain.dto.OutputDTO;
import com.ruoyi.system.domain.dto.TriggerDTO;
import com.ruoyi.system.domain.params.ThematicQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IFeignService {

    public ThematicQuery trigger(TriggerDTO triggerDTO);

    public List<OutputDTO> thematicMap(ThematicQuery query);

    public void downloadReport(String eqId, String eqqueueId, HttpServletResponse resp) throws IOException;
}
