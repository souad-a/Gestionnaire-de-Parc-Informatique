package com.parcinformatique.model;

import java.util.List;

public class EquipmentStats {
    private long totalEquipments;
    private long availableCount;
    private long assignedCount;
    private long maintenanceCount;
    private long outOfServiceCount;
    private double availabilityRate;

    public EquipmentStats(List<Equipment> equipments) {
        this.totalEquipments = equipments.size();
        this.availableCount = equipments.stream()
                .filter(e -> e.getStatus() == EquipmentStatus.AVAILABLE)
                .count();
        this.assignedCount = equipments.stream()
                .filter(e -> e.getStatus() == EquipmentStatus.ASSIGNED)
                .count();
        this.maintenanceCount = equipments.stream()
                .filter(e -> e.getStatus() == EquipmentStatus.MAINTENANCE)
                .count();
        this.outOfServiceCount = equipments.stream()
                .filter(e -> e.getStatus() == EquipmentStatus.OUT_OF_SERVICE)
                .count();
        this.availabilityRate = totalEquipments > 0 ?
                (availableCount * 100.0) / totalEquipments : 0;
    }

    // Getters
    public long getTotalEquipments() { return totalEquipments; }
    public long getAvailableCount() { return availableCount; }
    public long getAssignedCount() { return assignedCount; }
    public long getMaintenanceCount() { return maintenanceCount; }
    public long getOutOfServiceCount() { return outOfServiceCount; }
    public double getAvailabilityRate() { return availabilityRate; }
}