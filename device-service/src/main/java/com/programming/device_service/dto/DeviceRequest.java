package com.programming.device_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequest {
    private String description;
    private String address;
    private Integer energyConsumption;
    private UUID userId;
}
