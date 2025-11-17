package com.parcinformatique.service;

import com.parcinformatique.dao.EmployeeDAO;
import com.parcinformatique.dao.UserDAO;
import com.parcinformatique.model.Employee;
import com.parcinformatique.model.User;
import com.parcinformatique.model.Role;

import java.util.List;
import java.util.regex.Pattern;

public class EmployeeService {
    private EmployeeDAO employeeDAO;
    private UserDAO userDAO; // DÉCLARATION

    // Regex pour validation email
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9\\s\\-\\.\\(\\)]+$");

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
        this.userDAO = new UserDAO(); // INITIALISATION
    }

    // 📊 MÉTHODES MÉTIER EXISTANTES
    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    public Employee getEmployeeById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID d'employé invalide");
        }
        Employee employee = employeeDAO.findById(id);
        if (employee == null) {
            throw new IllegalArgumentException("Employé non trouvé avec l'ID: " + id);
        }
        return employee;
    }

    public Employee createEmployee(String firstName, String lastName, String department, String email, String phone) {
        // Validation métier
        validateEmployeeData(firstName, lastName, department, email, phone);

        // Vérification unicité email
        if (!employeeDAO.isEmailUnique(email, null)) {
            throw new IllegalArgumentException("Un employé avec l'email '" + email + "' existe déjà");
        }

        Employee employee = new Employee(firstName, lastName, department, email, phone);
        employeeDAO.save(employee);
        return employee;
    }

    public Employee updateEmployee(Long id, String firstName, String lastName, String department, String email, String phone) {
        // Validation
        validateEmployeeData(firstName, lastName, department, email, phone);
        Employee existingEmployee = getEmployeeById(id);

        // Vérification unicité email (exclure l'actuel)
        if (!employeeDAO.isEmailUnique(email, id)) {
            throw new IllegalArgumentException("Un autre employé avec l'email '" + email + "' existe déjà");
        }

        // Mise à jour
        existingEmployee.setFirstName(firstName);
        existingEmployee.setLastName(lastName);
        existingEmployee.setDepartment(department);
        existingEmployee.setEmail(email);
        existingEmployee.setPhone(phone);
        employeeDAO.save(existingEmployee);

        return existingEmployee;
    }

    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeDAO.delete(id);
    }

    public List<String> getAllDepartments() {
        return employeeDAO.findAllDepartments();
    }

    // ✅ VALIDATION MÉTIER
    private void validateEmployeeData(String firstName, String lastName, String department, String email, String phone) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Le département est obligatoire");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }

        // Validation format email
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Format d'email invalide");
        }

        // Validation longueurs
        if (firstName.length() > 50) throw new IllegalArgumentException("Le prénom ne peut pas dépasser 50 caractères");
        if (lastName.length() > 50) throw new IllegalArgumentException("Le nom ne peut pas dépasser 50 caractères");
        if (department.length() > 100) throw new IllegalArgumentException("Le département ne peut pas dépasser 100 caractères");
        if (email.length() > 100) throw new IllegalArgumentException("L'email ne peut pas dépasser 100 caractères");

        // Validation optionnelle du téléphone
        if (phone != null && !phone.trim().isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Format de téléphone invalide");
        }
    }

    // 🔍 MÉTHODES MÉTIER SPÉCIFIQUES
    public boolean isEmployeeEmailUnique(String email, Long excludeId) {
        return employeeDAO.isEmailUnique(email, excludeId);
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            return getAllEmployees();
        }
        return employeeDAO.findByDepartment(department);
    }

    public List<Employee> searchEmployees(String keyword) {
        List<Employee> allEmployees = getAllEmployees();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allEmployees;
        }

        String lowerKeyword = keyword.toLowerCase();
        return allEmployees.stream()
                .filter(e -> e.getFirstName().toLowerCase().contains(lowerKeyword) ||
                        e.getLastName().toLowerCase().contains(lowerKeyword) ||
                        e.getDepartment().toLowerCase().contains(lowerKeyword) ||
                        e.getEmail().toLowerCase().contains(lowerKeyword))
                .toList();
    }

    // ✅ MÉTHODES ADMIN - Gestion des utilisateurs
    // ✅ MÉTHODES ADMIN - Gestion des utilisateurs
    // Dans EmployeeService.java - Version sans relation directe
    // ✅ MÉTHODES ADMIN - Gestion des utilisateurs - CORRECTION
    // Dans EmployeeService.java - CORRECTION
    public User createUserAccount(String username, String password, Role role, Employee employee) {
        // Vérifier l'unicité du username
        if (userDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("Un utilisateur avec ce nom d'utilisateur existe déjà");
        }

        // Créer l'utilisateur
        User user = new User(username, password, role);
        userDAO.save(user);

        // Associer l'employé si fourni
        if (employee != null) {
            employee.setUser(user);
            employeeDAO.save(employee);
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public void deactivateUser(Long userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setActive(false);
            userDAO.save(user);
        }
    }

    public void activateUser(Long userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setActive(true);
            userDAO.save(user);
        }
    }
}