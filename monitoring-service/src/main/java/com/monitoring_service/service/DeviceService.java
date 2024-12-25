package com.monitoring_service.service;

import com.monitoring_service.dto.DeviceEvent;
import com.monitoring_service.model.Device;
import com.monitoring_service.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public Device findById(UUID id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @RabbitListener(queues = "deviceCreateQueue")
    public void handleCreate(DeviceEvent deviceEvent) {
        Device device = new Device(
                deviceEvent.getId(),
                deviceEvent.getEnergyConsumption(),
                deviceEvent.getUserId()
        );
        log.info("Device created: " + device);

        deviceRepository.save(device);
    }

    @RabbitListener(queues = "deviceUpdateQueue")
    public void handleUpdate(DeviceEvent deviceEvent) {
        Device existingDevice = deviceRepository.findById(deviceEvent.getId())
                .orElseThrow(() -> new RuntimeException("Device not found"));
        existingDevice.setEnergyConsumption(deviceEvent.getEnergyConsumption());
        existingDevice.setUserId(deviceEvent.getUserId());
        System.out.println(deviceEvent.getId() + "update");
        log.info("Device updated: " + existingDevice);

        deviceRepository.save(existingDevice);
    }

    @RabbitListener(queues = "deviceDeleteQueue")
    public void handleDelete(DeviceEvent deviceEvent) {
        log.info("Device sters: ");

        deviceRepository.deleteById(deviceEvent.getId());
    }

    @Transactional
    @RabbitListener(queues = "deviceDeleteAllByUserIdQueue")
    public void handleDeleteAllByUserId(DeviceEvent deviceEvent) {
        UUID userId = deviceEvent.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null in deleteAllByUserId event");
        }

        try {
            deviceRepository.deleteAllByUserId(userId);
            log.info("Deleted all devices for userId: {}", userId);
        } catch (Exception e) {
            log.error("Failed to delete devices for userId: {}", userId, e);
            throw e; // Ensure the exception propagates to trigger retries
        }
    }

}
