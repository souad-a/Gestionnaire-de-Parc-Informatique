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

@WebServlet("/admin/user-management")
public class UserManagementServlet extends HttpServlet {

    private UserService userService;

    private static final List<String> DEPARTMENTS = Arrays.asList(
            "IT", "RH", "FINANCE", "MARKETING", "PRODUCTION",
            "COMMERCIAL", "DIRECTION", "SUPPORT", "AUTRE"
    );

    @Override
    public void init() throws ServletException {
        System.out.println("🚀 UserManagementServlet initialisé");
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("📥 GET /admin/user-management");

        // Vérifier la session admin
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"ADMIN".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        // Afficher la page de gestion des utilisateurs
        showUserManagement(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("📥 POST /admin/user-management");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"ADMIN".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("createUser".equals(action)) {
                createUser(request, response);
            } else if ("activateUser".equals(action)) {
                activateUser(request, response);
            } else if ("deactivateUser".equals(action)) {
                deactivateUser(request, response);
            } else {
                showUserManagement(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
            showUserManagement(request, response);
        }
    }

    private void showUserManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("👥 Affichage gestion des utilisateurs");

        List<User> users = userService.getAllUsers();

        request.setAttribute("users", users);
        request.setAttribute("pageTitle", "Gestion des Utilisateurs");
        request.setAttribute("departments", DEPARTMENTS);
        request.setAttribute("roles", Role.values());

        request.getRequestDispatcher("/WEB-INF/views/admin/user-management.jsp").forward(request, response);
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("👤 Création d'un nouvel utilisateur");

        try {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String roleStr = request.getParameter("role");
            String department = request.getParameter("department");

            // Validation
            if (fullName == null || fullName.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty() ||
                    roleStr == null || roleStr.trim().isEmpty()) {

                request.setAttribute("errorMessage", "Tous les champs obligatoires doivent être remplis");
                showUserManagement(request, response);
                return;
            }

            Role role = Role.valueOf(roleStr);
            userService.createUser(username, password, role);

            request.setAttribute("successMessage", "Utilisateur créé avec succès: " + username);
            showUserManagement(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de la création: " + e.getMessage());
            showUserManagement(request, response);
        }
    }

    private void activateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long userId = Long.parseLong(request.getParameter("id"));
            userService.activateUser(userId);
            request.setAttribute("successMessage", "Utilisateur activé avec succès");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Erreur lors de l'activation: " + e.getMessage());
        }
        showUserManagement(request, response);
    }

    private void deactivateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long userId = Long.parseLong(request.getParameter("id"));
            userService.deactivateUser(userId);
            request.setAttribute("successMessage", "Utilisateur désactivé avec succès");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Erreur lors de la désactivation: " + e.getMessage());
        }
        showUserManagement(request, response);
    }
}