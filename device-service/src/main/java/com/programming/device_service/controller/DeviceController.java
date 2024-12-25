package com.programming.device_service.controller;


import com.programming.device_service.dto.DeviceRequest;
import com.programming.device_service.dto.DeviceResponse;
import com.programming.device_service.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController  {

    private final DeviceService deviceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createDevice(@RequestBody DeviceRequest deviceRequest) {
        deviceService.createDevice(deviceRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DeviceResponse> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateDevice(@PathVariable UUID id, @RequestBody DeviceRequest deviceRequest) {
        deviceService.updateDevice(id, deviceRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
    }

    @DeleteMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content
    public void deleteAllByUserId(@PathVariable UUID userId) {
        deviceService.deleteAllByUserId(userId);
    }

    @GetMapping("/user/{userId}/devices")
    @ResponseStatus(HttpStatus.OK)
    public List<UUID> getDevicesIdsByUserId(@PathVariable UUID userId) {
        return deviceService.getDeviceIdsByUserId(userId);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<DeviceResponse> getDevicesByUserId(@PathVariable UUID userId) {
        return deviceService.getDevicesByUserId(userId);
    }

}
