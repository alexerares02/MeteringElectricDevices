package com.programming.device_service.model;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 100, message = "Description should not exceed 100 characters")
    private String description;

    @NotBlank(message = "Address is mandatory")
    @Size(max = 150, message = "Address should not exceed 150 characters")
    private String address;

    @NotNull(message = "Energy consumption is mandatory")
    @Min(value = 0, message = "Energy consumption must be a positive value")
    private Integer energyConsumption;

    @NotNull(message = "User ID is mandatory")
    private UUID userId;
}
