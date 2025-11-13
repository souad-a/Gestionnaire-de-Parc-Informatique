package com.parcinformatique.service;

import com.parcinformatique.dao.EmployeeDAO;
import com.parcinformatique.model.Employee;

import java.util.List;
import java.util.regex.Pattern;

public class EmployeeService {
    private EmployeeDAO employeeDAO;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9\\s\\-\\.\\(\\)]+$");

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
    }


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
        validateEmployeeData(firstName, lastName, department, email, phone);

        if (!employeeDAO.isEmailUnique(email, null)) {
            throw new IllegalArgumentException("Un employé avec l'email '" + email + "' existe déjà");
        }

        Employee employee = new Employee(firstName, lastName, department, email, phone);
        employeeDAO.save(employee);
        return employee;
    }

    public Employee updateEmployee(Long id, String firstName, String lastName, String department, String email, String phone) {
        validateEmployeeData(firstName, lastName, department, email, phone);
        Employee existingEmployee = getEmployeeById(id);

        if (!employeeDAO.isEmailUnique(email, id)) {
            throw new IllegalArgumentException("Un autre employé avec l'email '" + email + "' existe déjà");
        }

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

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Format d'email invalide");
        }

        if (firstName.length() > 50) throw new IllegalArgumentException("Le prénom ne peut pas dépasser 50 caractères");
        if (lastName.length() > 50) throw new IllegalArgumentException("Le nom ne peut pas dépasser 50 caractères");
        if (department.length() > 100) throw new IllegalArgumentException("Le département ne peut pas dépasser 100 caractères");
        if (email.length() > 100) throw new IllegalArgumentException("L'email ne peut pas dépasser 100 caractères");

        if (phone != null && !phone.trim().isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Format de téléphone invalide");
        }
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
}