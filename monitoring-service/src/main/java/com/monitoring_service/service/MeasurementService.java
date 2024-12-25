package com.monitoring_service.service;


import com.monitoring_service.dto.MeasurementDTO;
import com.monitoring_service.model.Measurement;
import com.monitoring_service.repository.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeasurementService {
    private final MeasurementRepository measurementRepository;


    public void createMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement=Measurement.builder()
                    .measurementValue(measurementDTO.getMeasurementValue())
                    .deviceId(measurementDTO.getDeviceId())
                    .timestamp(measurementDTO.getTimestamp())
                    .build();

        measurementRepository.save(measurement);
    }

}
