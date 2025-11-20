package com.parcinformatique.model;

public enum EquipmentStatus {
    AVAILABLE,
    ASSIGNED,
    MAINTENANCE,
    OUT_OF_SERVICE,
    RESERVED;

    @Override
    public String toString() {
        switch (this) {
            case AVAILABLE: return "Disponible";
            case ASSIGNED: return "Assigné";
            case MAINTENANCE: return "En maintenance";
            case OUT_OF_SERVICE: return "Hors service";
            case RESERVED: return "Réservé";
            default: return name();
        }
    }
}