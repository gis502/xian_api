package com.ruoyi.system.domain.dto;

import java.util.List;

import com.ruoyi.system.domain.entity.*;

import lombok.Data;

@Data
public class EffactAreaDTO {

    private List<Bridge> bridgeList;

    private List<Reservoir> reservoirList;

    private List<Highway>  highwayList;

    private List<Road>  roadList;

    private List<WaterPipe>  waterPipeList;

    private List<People> peopleList;

    private List<Crops> cropsList;

}
