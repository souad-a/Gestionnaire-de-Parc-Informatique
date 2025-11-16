// 📁 src/main/java/com/parcinformatique/controller/EmployeeServlet.java
package com.parcinformatique.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/employees")
public class EmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("📥 GET /admin/employees appelé");

        // Vérifier la session
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || !"ADMIN".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

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
                case "users":
                    showUserManagement(request, response);
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

    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("📋 Affichage liste des employés");

        // Données simulées pour le test
        request.setAttribute("pageTitle", "Gestion des Employés");
        request.getRequestDispatcher("/WEB-INF/views/admin/employee-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("➕ Affichage formulaire nouvel employé");
        request.setAttribute("pageTitle", "Nouvel Employé");
        request.getRequestDispatcher("/WEB-INF/views/admin/employee-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("✏️ Affichage formulaire modification employé");
        request.setAttribute("pageTitle", "Modifier l'Employé");
        request.getRequestDispatcher("/WEB-INF/views/admin/employee-form.jsp").forward(request, response);
    }

    private void showUserManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("👥 Affichage gestion des utilisateurs");
        request.setAttribute("pageTitle", "Gestion des Utilisateurs");
        request.getRequestDispatcher("/WEB-INF/views/admin/user-management.jsp").forward(request, response);
    }
}