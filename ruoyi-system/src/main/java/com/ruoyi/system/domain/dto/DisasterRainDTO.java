package com.ruoyi.system.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DisasterRainDTO {

    private Long disasterId;
    private String rainfall;
    private String duration;
    private Double longitude;
    private Double latitude;
    private String position;
    private String disasterName;
    private LocalDateTime occurrenceTime;



}
