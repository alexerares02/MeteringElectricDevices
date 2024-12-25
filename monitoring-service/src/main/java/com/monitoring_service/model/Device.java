package com.monitoring_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name="device")
public class Device {
    @Id
    private UUID id;

    @NotNull(message = "Energy consumption is mandatory")
    @Min(value = 0, message = "Energy consumption must be a positive value")
    private Integer energyConsumption;

    @NotNull(message = "User ID is mandatory")
    private UUID userId;
}
