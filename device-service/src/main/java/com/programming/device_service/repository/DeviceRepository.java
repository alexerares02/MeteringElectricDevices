package com.programming.device_service.repository;

import com.programming.device_service.dto.DeviceResponse;
import com.programming.device_service.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    void deleteAllByUserId(UUID userId);

    @Query("SELECT d.id FROM Device d WHERE d.userId = :userId")
    List<UUID> findDeviceIdsByUserId(UUID userId);

    List<Device> findAllByUserId(UUID userId);
}

