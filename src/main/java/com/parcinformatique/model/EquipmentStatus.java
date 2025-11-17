// 📁 src/main/java/com/parcinformatique/model/EquipmentStatus.java
package com.parcinformatique.model;

public enum EquipmentStatus {
    AVAILABLE,      // Disponible
    ASSIGNED,       // Assigné à un employé
    MAINTENANCE,    // En maintenance
    OUT_OF_ORDER,
    PANNE, // Hors service
    RESERVED, OUT_OF_SERVICE;       // Réservé

    @Override
    public String toString() {
        switch (this) {
            case AVAILABLE: return "Disponible";
            case ASSIGNED: return "Assigné";
            case MAINTENANCE: return "En maintenance";
            case OUT_OF_ORDER: return "Hors service";
            case PANNE: return "En panne";
            case RESERVED: return "Réservé";
            default: return name();
        }
    }
}