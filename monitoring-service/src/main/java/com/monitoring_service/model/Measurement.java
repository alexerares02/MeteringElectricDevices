package com.monitoring_service.model;

import jakarta.persistence.*;
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
@Table(name="measurement")
public class Measurement {
    private long timestamp;
    @Id
    private UUID deviceId;
    private Double measurementValue;
}
