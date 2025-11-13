// 📁 src/main/java/com/parcinformatique/model/EquipmentStatus.java
package com.parcinformatique.model;

public enum EquipmentStatus {
    AVAILABLE,
    ASSIGNED,
    MAINTENANCE,
    OUT_OF_ORDER,
    RESERVED, OUT_OF_SERVICE;

    @Override
    public String toString() {
        switch (this) {
            case AVAILABLE: return "Disponible";
            case ASSIGNED: return "Assigné";
            case MAINTENANCE: return "En maintenance";
            case OUT_OF_ORDER: return "Hors service";
            case RESERVED: return "Réservé";
            default: return name();
        }
    }
}