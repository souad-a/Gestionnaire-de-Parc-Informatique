package com.parcinformatique.service;

import com.parcinformatique.dao.EquipmentDAO;
import com.parcinformatique.model.Equipment;
import com.parcinformatique.model.EquipmentStatus;
import com.parcinformatique.model.Category;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EquipmentService {

    private EquipmentDAO equipmentDAO;

    public EquipmentService() {
        this.equipmentDAO = new EquipmentDAO();
    }

    // ✅ CRUD METHODS
    public List<Equipment> getAllEquipment() {
        return equipmentDAO.findAll();
    }

    public Equipment getEquipmentById(Long id) {
        return equipmentDAO.findById(id);
    }

    public List<Equipment> getEquipmentByStatus(EquipmentStatus status) {
        return equipmentDAO.findByStatus(status);
    }

    public List<Equipment> getEquipmentByCategory(Long categoryId) {
        return equipmentDAO.findByCategory(categoryId);
    }

    // ✅ MÉTHODE SPÉCIFIQUE POUR ASSIGNMENT
    public List<Equipment> getAvailableEquipment() {
        return equipmentDAO.findAvailableEquipment();
    }

    // ✅ GESTION ÉQUIPEMENT
    public void saveEquipment(Equipment equipment) {
        validateEquipment(equipment);
        equipmentDAO.save(equipment);
    }

    public void updateEquipment(Equipment equipment) {
        validateEquipment(equipment);
        equipmentDAO.save(equipment); // Utilise save() qui fait merge()
    }

    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentDAO.findById(id);
        if (equipment != null) {
            // Vérifier si l'équipement est assigné
            if (equipment.getStatus() == EquipmentStatus.ASSIGNED) {
                throw new RuntimeException("Impossible de supprimer un équipement assigné");
            }
            equipmentDAO.delete(id);
        }
    }

    // ✅ VALIDATION
    private void validateEquipment(Equipment equipment) {
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new RuntimeException("Le nom de l'équipement est obligatoire");
        }

        if (equipment.getSerialNumber() != null && !equipment.getSerialNumber().trim().isEmpty()) {
            boolean isUnique = equipmentDAO.isSerialNumberUnique(
                    equipment.getSerialNumber(),
                    equipment.getId()
            );
            if (!isUnique) {
                throw new RuntimeException("Le numéro de série doit être unique");
            }
        }

        if (equipment.getPurchaseDate() != null &&
                equipment.getPurchaseDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("La date d'achat ne peut pas être dans le futur");
        }
    }

    // ✅ STATISTIQUES
    public long getAvailableEquipmentCount() {
        return equipmentDAO.countByStatus(EquipmentStatus.AVAILABLE);
    }

    public long getAssignedEquipmentCount() {
        return equipmentDAO.countByStatus(EquipmentStatus.ASSIGNED);
    }

    public long getMaintenanceEquipmentCount() {
        return equipmentDAO.countByStatus(EquipmentStatus.MAINTENANCE);
    }

    // ✅ VÉRIFICATIONS MÉTIER
    public boolean isEquipmentAvailable(Long equipmentId) {
        Equipment equipment = equipmentDAO.findById(equipmentId);
        return equipment != null && equipment.getStatus() == EquipmentStatus.AVAILABLE;
    }

    public Optional<Equipment> findBySerialNumber(String serialNumber) {
        return equipmentDAO.findBySerialNumber(serialNumber);
    }

    // ✅ CHANGEMENT DE STATUT
    public void markAsAssigned(Long equipmentId) {
        Equipment equipment = equipmentDAO.findById(equipmentId);
        if (equipment != null) {
            equipment.setStatus(EquipmentStatus.ASSIGNED);
            equipmentDAO.save(equipment);
        }
    }

    public void markAsAvailable(Long equipmentId) {
        Equipment equipment = equipmentDAO.findById(equipmentId);
        if (equipment != null) {
            equipment.setStatus(EquipmentStatus.AVAILABLE);
            equipmentDAO.save(equipment);
        }
    }

    public void markAsMaintenance(Long equipmentId) {
        Equipment equipment = equipmentDAO.findById(equipmentId);
        if (equipment != null) {
            equipment.setStatus(EquipmentStatus.MAINTENANCE);
            equipmentDAO.save(equipment);
        }
    }
}