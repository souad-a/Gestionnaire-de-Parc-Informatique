package com.parcinformatique.dao;

import com.parcinformatique.model.Assignment;
import java.time.LocalDate;
import java.util.List;

public interface AssignmentDAO {
    // CRUD de base
    Assignment findById(Long id);
    List<Assignment> findAll();
    void save(Assignment assignment);
    void update(Assignment assignment);
    void delete(Long id);

    // Logique métier spécifique
    List<Assignment> findByEmployee(Long employeeId);
    List<Assignment> findByEquipment(Long equipmentId);
    List<Assignment> findActiveAssignments();
    List<Assignment> findByEquipmentAndEmployee(Long equipmentId, Long employeeId);

    // Vérifications métier
    boolean isEquipmentAvailable(Long equipmentId, LocalDate date);
}