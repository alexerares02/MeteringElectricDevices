package com.monitoring_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementDTO {
    private long timestamp;
    private UUID deviceId;
    private Double measurementValue;
}
