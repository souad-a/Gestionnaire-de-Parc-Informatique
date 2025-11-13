package com.parcinformatique.service;

import com.parcinformatique.dao.EquipmentDAO;
import com.parcinformatique.model.Equipment;
import com.parcinformatique.model.EquipmentStatus;
import java.time.LocalDate;
import java.util.List;

public class EquipmentService {

    private EquipmentDAO equipmentDAO;

    public EquipmentService() {
        this.equipmentDAO = new EquipmentDAO();
    }

    public List<Equipment> getAllEquipment() {
        return equipmentDAO.findAll();
    }

    public Equipment getEquipmentById(Long id) {
        return equipmentDAO.findById(id);
    }

    public List<Equipment> getAvailableEquipment() {
        return equipmentDAO.findAvailableEquipment();
    }

    public void saveEquipment(Equipment equipment) {
        validateEquipment(equipment);
        equipmentDAO.save(equipment);
    }

    public void updateEquipment(Equipment equipment) {
        validateEquipment(equipment);
        equipmentDAO.save(equipment);
    }

    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentDAO.findById(id);
        if (equipment != null) {
            if (equipment.getStatus() == EquipmentStatus.ASSIGNED) {
                throw new RuntimeException("Impossible de supprimer un équipement assigné");
            }
            equipmentDAO.delete(id);
        }
    }

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


    }

