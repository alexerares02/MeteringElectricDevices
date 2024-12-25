package com.monitoring_service.repository;

import com.monitoring_service.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    void deleteAllByUserId(UUID userId);
}
