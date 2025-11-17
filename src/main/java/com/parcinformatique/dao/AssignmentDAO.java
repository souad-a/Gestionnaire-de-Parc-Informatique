package com.parcinformatique.dao;

import com.parcinformatique.model.Assignment;
import java.time.LocalDate;
import java.util.List;

public interface AssignmentDAO {

    // Méthodes CRUD de base
    Assignment findById(Long id);
    List<Assignment> findAll();
    void save(Assignment assignment);
    void update(Assignment assignment);
    void delete(Long id);

    // Méthodes de recherche
    List<Assignment> findByEmployee(Long employeeId);
    List<Assignment> findByEquipment(Long equipmentId);
    List<Assignment> findByEquipmentAndEmployee(Long equipmentId, Long employeeId);
    List<Assignment> findByStatus(String status);
    List<Assignment> findAssignmentsBetweenDates(LocalDate startDate, LocalDate endDate);

    // Méthodes pour les affectations actives
    List<Assignment> findActiveAssignments();
    List<Assignment> findActiveAssignmentsByEmployee(Long employeeId);
    List<Assignment> findActiveAssignmentsByEquipment(Long equipmentId);

    // Méthodes de vérification
    boolean isEquipmentAvailable(Long equipmentId, LocalDate date);
    boolean hasActiveAssignment(Long employeeId);
    boolean hasActiveAssignmentForEquipment(Long equipmentId, Long employeeId);

    // Méthodes de comptage
    int countActiveAssignmentsByEmployee(Long employeeId);
    long countActiveAssignments();

    // Méthodes supplémentaires
    List<Assignment> findOverdueAssignments(LocalDate currentDate);
}