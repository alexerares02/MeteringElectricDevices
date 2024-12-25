package com.monitoring_service.controller;

import com.monitoring_service.dto.MeasurementDTO;
import com.monitoring_service.model.Device;
import com.monitoring_service.service.DeviceService;
import com.monitoring_service.service.MeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class QueueListener {

    private final DeviceService deviceService;
    private final MeasurementService measurementService;


    @Autowired
    private SimpMessagingTemplate messageT;

    @RabbitListener(queues = "energy_measurements")
    public void receiveMessage(MeasurementDTO message) {
        System.out.println("Received: " + message);
        Device device = deviceService.findById(message.getDeviceId());
        System.out.println(device);

        if (message.getMeasurementValue() > device.getEnergyConsumption()) {
            String alertMessage = "Alert! Device " + device.getId() +
                    " has exceeded its energy consumption limit.";
            sendWebs(alertMessage, String.valueOf(device.getUserId()));
            System.out.println("NiggasInParis!!!\n"+device.getUserId());
        }

        measurementService.createMeasurement(message);
    }

    void sendWebs(String alertMessage, String userId) {
        messageT.convertAndSend("/topic/reply/"+userId, alertMessage);
    }
}
