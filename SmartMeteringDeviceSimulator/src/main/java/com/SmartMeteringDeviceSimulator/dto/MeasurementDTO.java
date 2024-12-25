package com.SmartMeteringDeviceSimulator.dto;

import java.io.Serializable;
import java.util.UUID;

public class MeasurementDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private long timestamp;
    private UUID deviceId;
    private Double measurementValue;

    // Getters and setters
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public Double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(Double measurementValue) {
        this.measurementValue = measurementValue;
    }

    @Override
    public String toString() {
        return "{" +
                "timestamp:" + timestamp +
                ", deviceId:'" + deviceId + '\'' +
                ", measurementValue:" + measurementValue +
                '}';
    }
}
