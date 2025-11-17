package com.parcinformatique.controller;

import com.parcinformatique.model.User;
import com.parcinformatique.model.Role;
import com.parcinformatique.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/admin/employees")
public class EmployeeServlet extends HttpServlet {

    private UserService userService;

    private static final List<String> DEPARTMENTS = Arrays.asList(
            "IT", "RH", "FINANCE", "MARKETING", "PRODUCTION",
            "COMMERCIAL", "DIRECTION", "SUPPORT", "AUTRE"
    );

    @Override
    public void init() throws ServletException {
        System.out.println("🚀 EmployeeServlet initialisé - Gestion des Employés uniquement");
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("📥 GET /admin/employees - Action: " + request.getParameter("action"));

        // Vérifier la session admin
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"ADMIN".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        String action = request.getParameter("action");

        try {
            if (action == null) {
                listEmployees(request, response);
            } else {
                switch (action) {
                    case "new":
                        showNewEmployeeForm(request, response);
                        break;
                    case "edit":
                        showEditEmployeeForm(request, response);
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

        System.out.println("📥 POST /admin/employees - Action: " + request.getParameter("action"));

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"ADMIN".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "create":
                    createEmployee(request, response);
                    break;
                case "update":
                    updateEmployee(request, response);
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

    // === MÉTHODES POUR LES EMPLOYÉS UNIQUEMENT ===

    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("📋 Liste des employés (Users avec rôle EMPLOYEE)");

        List<User> employees = userService.getUsersByRole(Role.EMPLOYEE);

        request.setAttribute("employees", employees);
        request.setAttribute("pageTitle", "Gestion des Employés");
        request.setAttribute("departments", DEPARTMENTS);

        request.getRequestDispatcher("/WEB-INF/views/admin/employee-list.jsp").forward(request, response);
    }

    private void showNewEmployeeForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("➕ Formulaire nouvel employé");
        request.setAttribute("pageTitle", "Nouvel Employé");
        request.setAttribute("departments", DEPARTMENTS);
        request.getRequestDispatcher("/WEB-INF/views/admin/employee-form.jsp").forward(request, response);
    }

    private void createEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("🆕 Création d'un nouvel employé");

        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String department = request.getParameter("department");
            String phone = request.getParameter("phone");

            // Validation
            if (fullName == null || fullName.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {

                request.setAttribute("errorMessage", "Tous les champs obligatoires doivent être remplis");
                showNewEmployeeForm(request, response);
                return;
            }

            // Créer l'employé (User avec rôle EMPLOYEE)
            userService.createUser(username, password, Role.EMPLOYEE);

            request.setAttribute("successMessage", "Employé créé avec succès: " + fullName);
            listEmployees(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de la création: " + e.getMessage());
            showNewEmployeeForm(request, response);
        }
    }

    private void showEditEmployeeForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("✏️ Formulaire modification employé");

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            User employee = userService.getUserById(id);

            if (employee != null) {
                request.setAttribute("employee", employee);
                request.setAttribute("pageTitle", "Modifier l'Employé");
                request.setAttribute("departments", DEPARTMENTS);
                request.getRequestDispatcher("/WEB-INF/views/admin/employee-form.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Employé non trouvé");
                listEmployees(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID invalide");
            listEmployees(request, response);
        }
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("🔄 Mise à jour d'un employé");

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String username = request.getParameter("username");
            String department = request.getParameter("department");
            String phone = request.getParameter("phone");

            User employee = userService.getUserById(id);
            if (employee == null) {
                request.setAttribute("errorMessage", "Employé non trouvé");
                listEmployees(request, response);
                return;
            }

            // Mettre à jour
            employee.setFullName(fullName);
            employee.setEmail(email);
            employee.setUsername(username);
            employee.setDepartment(department != null && !department.trim().isEmpty() ? department : null);
            employee.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null);

            request.setAttribute("successMessage", "Employé mis à jour avec succès: " + fullName);
            listEmployees(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de la mise à jour: " + e.getMessage());
            showEditEmployeeForm(request, response);
        }
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("🗑️ Désactivation d'un employé");

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            User employee = userService.getUserById(id);

            if (employee != null) {
                userService.deactivateUser(id);
                request.setAttribute("successMessage", "Employé désactivé avec succès: " + employee.getFullName());
            } else {
                request.setAttribute("errorMessage", "Employé non trouvé");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de la désactivation: " + e.getMessage());
        }

        listEmployees(request, response);
    }

    private void searchEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("🔍 Recherche d'employés");

        String keyword = request.getParameter("keyword");
        String department = request.getParameter("department");

        List<User> employees = userService.getUsersByRole(Role.EMPLOYEE);

        // Filtrage
        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchTerm = keyword.toLowerCase().trim();
            employees = employees.stream()
                    .filter(user ->
                            (user.getFullName() != null && user.getFullName().toLowerCase().contains(searchTerm)) ||
                                    (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchTerm)) ||
                                    (user.getUsername() != null && user.getUsername().toLowerCase().contains(searchTerm)))
                    .toList();
        }

        if (department != null && !department.equals("all")) {
            employees = employees.stream()
                    .filter(user -> user.getDepartment() != null &&
                            user.getDepartment().equals(department))
                    .toList();
        }

        request.setAttribute("employees", employees);
        request.setAttribute("pageTitle", "Gestion des Employés");
        request.setAttribute("departments", DEPARTMENTS);
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("selectedDepartment", department);

        request.getRequestDispatcher("/WEB-INF/views/admin/employee-list.jsp").forward(request, response);
    }
}