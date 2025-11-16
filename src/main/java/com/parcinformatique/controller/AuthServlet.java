// 📁 src/main/java/com/parcinformatique/controller/AuthServlet.java
package com.parcinformatique.controller;

import com.parcinformatique.model.User;
import com.parcinformatique.model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("✅ AuthServlet initialisé avec succès");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("📥 GET /auth appelé");
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            // Déconnexion
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/auth");
        } else {
            // Afficher la page de login
            System.out.println("🔄 Affichage de login.jsp");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("🔐 Tentative de connexion: " + username);

        // Authentification simple sans base de données
        User user = authenticateSimple(username, password);

        if (user != null) {
            // Connexion réussie
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole().name());

            System.out.println("✅ Connexion réussie pour: " + username + " - Rôle: " + user.getRole());

            // Redirection selon le rôle
            String redirectPath = getDashboardPath(user.getRole(), request);
            System.out.println("🔄 Redirection vers: " + redirectPath);
            response.sendRedirect(redirectPath);

        } else {
            // Échec connexion
            System.out.println("❌ Échec connexion pour: " + username);
            request.setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    private User authenticateSimple(String username, String password) {
        // Utilisateurs de test
        if ("admin".equals(username) && "admin123".equals(password)) {
            User user = new User("admin", "admin123", Role.ADMIN);
            user.setId(1L);
            user.setActive(true);
            return user;
        }
        if ("technicien".equals(username) && "tech123".equals(password)) {
            User user = new User("technicien", "tech123", Role.TECHNICIAN);
            user.setId(2L);
            user.setActive(true);
            return user;
        }
        if ("employe".equals(username) && "emp123".equals(password)) {
            User user = new User("employe", "emp123", Role.EMPLOYEE);
            user.setId(3L);
            user.setActive(true);
            return user;
        }
        return null;
    }

    private String getDashboardPath(Role role, HttpServletRequest request) {
        switch (role) {
            case ADMIN:
                return request.getContextPath() + "/admin/dashboard";
            case TECHNICIAN:
                return request.getContextPath() + "/technician/dashboard";
            case EMPLOYEE:
                return request.getContextPath() + "/employee/dashboard";
            default:
                return request.getContextPath() + "/dashboard";
        }
    }
}