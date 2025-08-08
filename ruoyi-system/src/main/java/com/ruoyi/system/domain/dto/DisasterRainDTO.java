package com.ruoyi.system.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DisasterRainDTO {

    private String rainfall;
    private String duration;
    private Long longitude;
    private Long latitude;
    private String position;
    private String disasterName;
    private LocalDateTime occurrenceTime;



}
