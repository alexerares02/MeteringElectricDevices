package com.monitoring_service.controller;

import com.monitoring_service.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitoring/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;
}
