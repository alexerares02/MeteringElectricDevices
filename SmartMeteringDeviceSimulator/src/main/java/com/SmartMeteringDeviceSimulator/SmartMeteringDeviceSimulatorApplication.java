package com.SmartMeteringDeviceSimulator;

import com.SmartMeteringDeviceSimulator.dto.MeasurementDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SmartMeteringDeviceSimulatorApplication implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;

	@Value("${sensor.csv.path}")
	private String csvFilePath;

	@Value("${rabbitmq.queue.name}")
	private String queueName;

	private UUID deviceId = UUID.fromString("e5eca34e-603c-441b-8390-9acefb9b635a");;

	public SmartMeteringDeviceSimulatorApplication(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(SmartMeteringDeviceSimulatorApplication.class, args);
	}

	@Bean
	public Queue queue() {
		return new Queue(queueName, true);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Simulator started with device_id: " + deviceId);

		try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
			String line;
			Double prev = 0.0,act;
			if((line = reader.readLine()) != null){
				prev = Double.parseDouble(line);
			}
			while ((line = reader.readLine()) != null) {
				act = Double.parseDouble(line.trim());
				Double measurementValue = act-prev;
				prev=act;
				long timestamp = System.currentTimeMillis();

				// Create DTO
				MeasurementDTO dto = new MeasurementDTO();
				dto.setTimestamp(timestamp);
				dto.setDeviceId(deviceId);
				dto.setMeasurementValue(measurementValue);

				// Send DTO to RabbitMQ
				rabbitTemplate.convertAndSend(queueName, dto);
				System.out.println("Sent: " + dto);

				// Wait 5 seconds before sending the next reading
				TimeUnit.SECONDS.sleep(5);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
