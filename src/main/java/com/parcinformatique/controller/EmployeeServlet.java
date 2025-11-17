package com.parcinformatique.controller;

import com.parcinformatique.model.Employee;
import com.parcinformatique.service.EmployeeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {
    private EmployeeService employeeService;

    @Override
    public void init() {
        employeeService = new EmployeeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteEmployee(request, response);
                    break;
                case "search":
                    searchEmployees(request, response);
                    break;
                default:
                    listEmployees(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
            listEmployees(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("save".equals(action)) {
                saveEmployee(request, response);
            } else {
                listEmployees(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp")
                    .forward(request, response);
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Récupérer et afficher le message de succès depuis la session
        String successMessage = (String) request.getSession().getAttribute("successMessage");
        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
            request.getSession().removeAttribute("successMessage");
        }

        List<Employee> employees = employeeService.getAllEmployees();
        List<String> departments = employeeService.getAllDepartments();

        request.setAttribute("employees", employees);
        request.setAttribute("departments", departments);
        request.setAttribute("pageTitle", "Gestion des Employés");
        request.getRequestDispatcher("/WEB-INF/views/employee-list.jsp")
                .forward(request, response);
    }

    private void searchEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String department = request.getParameter("department");

        List<Employee> employees;

        if (department != null && !department.equals("all")) {
            // Filtrer par département
            employees = employeeService.getEmployeesByDepartment(department);
            request.setAttribute("selectedDepartment", department);

            // Si mot-clé aussi, filtrer les résultats
            if (keyword != null && !keyword.trim().isEmpty()) {
                final String searchTerm = keyword.trim().toLowerCase();
                employees = employees.stream()
                        .filter(e ->
                                (e.getFirstName() != null && e.getFirstName().toLowerCase().contains(searchTerm)) ||
                                        (e.getLastName() != null && e.getLastName().toLowerCase().contains(searchTerm)) ||
                                        (e.getEmail() != null && e.getEmail().toLowerCase().contains(searchTerm)) ||
                                        (e.getPhone() != null && e.getPhone().toLowerCase().contains(searchTerm))
                        )
                        .toList();
                request.setAttribute("searchKeyword", keyword);
            }
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            // Recherche par mot-clé uniquement
            employees = employeeService.searchEmployees(keyword);
            request.setAttribute("searchKeyword", keyword);
        } else {
            // Pas de filtre
            employees = employeeService.getAllEmployees();
        }

        // Charger tous les départements pour le filtre
        List<String> departments = employeeService.getAllDepartments();

        request.setAttribute("employees", employees);
        request.setAttribute("departments", departments);
        request.setAttribute("pageTitle", "Gestion des Employés");
        request.getRequestDispatcher("/WEB-INF/views/employee-list.jsp")
                .forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> departments = employeeService.getAllDepartments();
        request.setAttribute("departments", departments);
        request.setAttribute("pageTitle", "Nouvel Employé");
        request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp")
                .forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "ID de l'employé manquant");
            response.sendRedirect(request.getContextPath() + "/employees");
            return;
        }

        try {
            Long id = Long.parseLong(idParam);

            Employee employee = employeeService.getEmployeeById(id);
            List<String> departments = employeeService.getAllDepartments();

            request.setAttribute("departments", departments);
            request.setAttribute("employee", employee);
            request.setAttribute("pageTitle", "Modifier l'Employé");
            request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp")
                    .forward(request, response);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Format d'ID invalide");
            response.sendRedirect(request.getContextPath() + "/employees");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/employees");
        }
    }

    private void saveEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String department = request.getParameter("department");
            String phone = request.getParameter("phone");

            String successMessage;

            if (idParam != null && !idParam.trim().isEmpty()) {
                Long id = Long.parseLong(idParam);
                employeeService.updateEmployee(id, firstName, lastName, department, email, phone);
                successMessage = "Employé modifié avec succès";
            } else {
                employeeService.createEmployee(firstName, lastName, department, email, phone);
                successMessage = "Employé créé avec succès";
            }

            request.getSession().setAttribute("successMessage", successMessage);
            response.sendRedirect(request.getContextPath() + "/employees");

        } catch (IllegalArgumentException e) {
            // Erreurs de validation métier du service
            request.setAttribute("errorMessage", e.getMessage());

            // Recharger les départements pour le formulaire
            List<String> departments = employeeService.getAllDepartments();
            request.setAttribute("departments", departments);

            // Recharger l'employé si modification
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Long id = Long.parseLong(idParam);
                    Employee employee = employeeService.getEmployeeById(id);
                    request.setAttribute("employee", employee);
                    request.setAttribute("pageTitle", "Modifier l'Employé");
                } catch (Exception ex) {
                    request.setAttribute("pageTitle", "Nouvel Employé");
                }
            } else {
                request.setAttribute("pageTitle", "Nouvel Employé");
            }

            request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de la sauvegarde: " + e.getMessage());

            List<String> departments = employeeService.getAllDepartments();
            request.setAttribute("departments", departments);

            request.getRequestDispatcher("/WEB-INF/views/employee-form.jsp")
                    .forward(request, response);
        }
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                Long id = Long.parseLong(idParam);

                employeeService.deleteEmployee(id);
                request.getSession().setAttribute("successMessage", "Employé supprimé avec succès");
            } else {
                request.getSession().setAttribute("errorMessage", "ID de l'employé manquant");
            }
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/employees");
    }
}