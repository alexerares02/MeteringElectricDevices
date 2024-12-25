package com.programming.device_service.service;

import com.programming.device_service.config.MessageProducer;
import com.programming.device_service.dto.DeviceEvent;
import com.programming.device_service.dto.DeviceRequest;
import com.programming.device_service.dto.DeviceResponse;
import com.programming.device_service.model.Device;
import com.programming.device_service.repository.DeviceRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final MessageProducer messageProducer;

    private static final String EXCHANGE_NAME = "deviceExchange";
    private static final String CREATE_ROUTING_KEY = "device.create";
    private static final String UPDATE_ROUTING_KEY = "device.update";
    private static final String DELETE_ROUTING_KEY = "device.delete";

    public void createDevice(DeviceRequest deviceRequest) {
        Device device = Device.builder()
                .description(deviceRequest.getDescription())
                .address(deviceRequest.getAddress())
                .energyConsumption(deviceRequest.getEnergyConsumption())
                .userId(deviceRequest.getUserId())
                .build();

        deviceRepository.save(device);
        DeviceEvent deviceEvent = new DeviceEvent(
                device.getId(),
                device.getEnergyConsumption(),
                device.getUserId()
        );
        messageProducer.sendMessage(EXCHANGE_NAME, CREATE_ROUTING_KEY, deviceEvent);
        log.info("Device created: {}", device.getId());
    }

    public List<DeviceResponse> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream().map(this::mapToDeviceResponse).toList();
    }

    private DeviceResponse mapToDeviceResponse(Device device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .description(device.getDescription())
                .address(device.getAddress())
                .energyConsumption(device.getEnergyConsumption())
                .userId(device.getUserId())
                .build();
    }

    // public Long getOneDevice(Long userId){
    //
    // return device.getid;
    //
    // }
    //
    @Transactional
    public void updateDevice(UUID id, DeviceRequest deviceRequest) {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + id));

        if(deviceRequest.getDescription() != null)
            device.setDescription(deviceRequest.getDescription());

        if(deviceRequest.getAddress() != null)
            device.setAddress(deviceRequest.getAddress());

        if(deviceRequest.getEnergyConsumption() != null)
            device.setEnergyConsumption(deviceRequest.getEnergyConsumption());

        if(deviceRequest.getUserId() != null)
            device.setUserId(deviceRequest.getUserId());

        deviceRepository.save(device);

        DeviceEvent deviceEvent = new DeviceEvent(
                device.getId(),
                device.getEnergyConsumption(),
                device.getUserId()
        );
        messageProducer.sendMessage(EXCHANGE_NAME, UPDATE_ROUTING_KEY, deviceEvent);
    }

    @Transactional
    public void deleteDevice(UUID id) {
        if (!deviceRepository.existsById(id)) {
            throw new RuntimeException("Device not found with id: " + id);
        }
        deviceRepository.deleteById(id);
        DeviceEvent deviceEvent = new DeviceEvent(
                id,
                null, // Energy consumption is not relevant for deletion
                null
        );
        messageProducer.sendMessage(EXCHANGE_NAME, DELETE_ROUTING_KEY, deviceEvent);
    }

    @Transactional
    public void deleteAllByUserId(UUID userId) {
        deviceRepository.deleteAllByUserId(userId);

        DeviceEvent deviceEvent = new DeviceEvent(
                null,
                null, // Energy consumption is not relevant for deletion
                userId
        );
        messageProducer.sendMessage(EXCHANGE_NAME, "device.deleteAllByUserId", deviceEvent);

    }

    public List<UUID> getDeviceIdsByUserId(UUID userId) {
        return deviceRepository.findDeviceIdsByUserId(userId);
    }

    public List<DeviceResponse> getDevicesByUserId(UUID userId) {
        List<Device> devices = deviceRepository.findAllByUserId(userId);
        return devices.stream().map(this::mapToDeviceResponse).toList();
    }
}
