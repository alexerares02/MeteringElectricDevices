package com.programming.device_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEvent {
    private UUID id;
    private Integer energyConsumption;
    private UUID userId;
}

