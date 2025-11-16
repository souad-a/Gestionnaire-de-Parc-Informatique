package com.parcinformatique.service;

import com.parcinformatique.dao.AssignmentDAO;
import com.parcinformatique.dao.AssignmentDAOImpl;
import com.parcinformatique.dao.EquipmentDAO;
import com.parcinformatique.dao.EmployeeDAO;
import com.parcinformatique.model.Assignment;
import com.parcinformatique.model.Equipment;
import com.parcinformatique.model.Employee;
import com.parcinformatique.model.EquipmentStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentService {

    private AssignmentDAO assignmentDAO;
    private EquipmentDAO equipmentDAO;
    private EmployeeDAO employeeDAO;

    public AssignmentService() {
        this.assignmentDAO = new AssignmentDAOImpl();
        this.equipmentDAO = new EquipmentDAO();
        this.employeeDAO = new EmployeeDAO();
    }

    // ✅ CRUD METHODS
    public List<Assignment> getAllAssignments() {
        return assignmentDAO.findAll();
    }

    public Assignment getAssignmentById(Long id) {
        return assignmentDAO.findById(id);
    }

    public List<Assignment> getActiveAssignments() {
        return assignmentDAO.findActiveAssignments();
    }

    public List<Assignment> getAssignmentsByEmployee(Long employeeId) {
        return assignmentDAO.findByEmployee(employeeId);
    }

    public List<Assignment> getAssignmentsByEquipment(Long equipmentId) {
        return assignmentDAO.findByEquipment(equipmentId);
    }

    // ✅ CORRECTION dans la méthode assignEquipment()
    public Assignment assignEquipment(Long equipmentId, Long employeeId, LocalDate assignmentDate, String notes) {
        // Vérifier l'existence des entités
        Equipment equipment = equipmentDAO.findById(equipmentId);
        if (equipment == null) {
            throw new RuntimeException("Équipement non trouvé");
        }

        Employee employee = employeeDAO.findById(employeeId);
        if (employee == null) {
            throw new RuntimeException("Employé non trouvé");
        }

        // ✅ VÉRIFICATION DISPONIBILITÉ ÉQUIPEMENT
        if (!assignmentDAO.isEquipmentAvailable(equipmentId, assignmentDate)) {
            throw new RuntimeException("L'équipement n'est pas disponible à la date spécifiée");
        }

        // Créer l'affectation
        Assignment assignment = new Assignment();
        assignment.setEquipment(equipment);
        assignment.setEmployee(employee);
        assignment.setAssignmentDate(assignmentDate);
        assignment.setStatus("ACTIVE");
        assignment.setNotes(notes);

        // ✅ CORRECTION : Utiliser l'Enum EquipmentStatus
        equipment.setStatus(EquipmentStatus.ASSIGNED);
        equipmentDAO.save(equipment);

        // Sauvegarder l'affectation
        assignmentDAO.save(assignment);

        return assignment;
    }

    // ✅ CORRECTION dans la méthode returnEquipment()
    public void returnEquipment(Long assignmentId, LocalDate returnDate, String returnNotes) {
        Assignment assignment = assignmentDAO.findById(assignmentId);
        if (assignment == null) {
            throw new RuntimeException("Affectation non trouvée");
        }

        if (!"ACTIVE".equals(assignment.getStatus())) {
            throw new RuntimeException("Cette affectation est déjà retournée");
        }

        if (returnDate.isBefore(assignment.getAssignmentDate())) {
            throw new RuntimeException("La date de retour ne peut pas être avant la date d'affectation");
        }

        // Mettre à jour l'affectation
        assignment.setReturnDate(returnDate);
        assignment.setStatus("RETURNED");
        if (returnNotes != null && !returnNotes.trim().isEmpty()) {
            String currentNotes = assignment.getNotes() != null ? assignment.getNotes() + "\n" : "";
            assignment.setNotes(currentNotes + "Retour: " + returnNotes);
        }

        // ✅ CORRECTION : Utiliser l'Enum EquipmentStatus
        Equipment equipment = assignment.getEquipment();
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        equipmentDAO.save(equipment);

        // Sauvegarder les modifications
        assignmentDAO.update(assignment);
    }

    // ✅ VÉRIFICATIONS MÉTIER
    public boolean isEquipmentAvailable(Long equipmentId, LocalDate date) {
        return assignmentDAO.isEquipmentAvailable(equipmentId, date);
    }

    public boolean hasActiveAssignment(Long employeeId) {
        return assignmentDAO.hasActiveAssignment(employeeId);
    }

    public int countActiveAssignmentsByEmployee(Long employeeId) {
        return assignmentDAO.countActiveAssignmentsByEmployee(employeeId);
    }

    public List<Assignment> getAssignmentHistory(LocalDate startDate, LocalDate endDate) {
        return assignmentDAO.findAssignmentsBetweenDates(startDate, endDate);
    }
    public List<Assignment> getAssignmentsByEquipmentAndEmployee(Long equipmentId, Long employeeId) {
        return assignmentDAO.findByEquipmentAndEmployee(equipmentId, employeeId);
    }
    // ✅ MÉTHODES EMPLOYÉ
    public List<Equipment> getAssignedEquipmentForEmployee(Long employeeId) {
        List<Assignment> assignments = assignmentDAO.findActiveAssignmentsByEmployee(employeeId);
        return assignments.stream()
                .map(Assignment::getEquipment)
                .collect(Collectors.toList());
    }

    public void reportEquipmentIssue(Long equipmentId, Long employeeId, String issueDescription) {
        // Vérifier que l'équipement est bien assigné à cet employé
        boolean isAssigned = assignmentDAO.hasActiveAssignmentForEquipment(equipmentId, employeeId);
        if (!isAssigned) {
            throw new RuntimeException("Cet équipement n'est pas assigné à vous");
        }

        Equipment equipment = equipmentDAO.findById(equipmentId);
        if (equipment != null) {
            equipment.setStatus(EquipmentStatus.PANNE);
            // Vous pouvez ajouter un champ pour stocker la description de la panne
            equipmentDAO.save(equipment);
        }
    }

    public List<Assignment> getEmployeeAssignmentHistory(Long employeeId) {
        return assignmentDAO.findByEmployee(employeeId);
    }
}