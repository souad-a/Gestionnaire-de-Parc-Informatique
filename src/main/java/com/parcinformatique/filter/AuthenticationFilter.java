package com.parcinformatique.filter;

import com.parcinformatique.model.Role;
import com.parcinformatique.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Pages publiques (pas besoin d'être connecté)
        if (path.startsWith("/auth") ||
                path.equals("/") ||
                path.startsWith("/resources/") ||
                path.contains(".css") ||
                path.contains(".js") ||
                path.contains(".jpg") ||
                path.contains(".png")) {

            chain.doFilter(request, response);
            return;
        }

        // Vérifier la session
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Non connecté → redirection vers login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth?action=login");
            return;
        }

        // Vérifier les autorisations selon le rôle
        User user = (User) session.getAttribute("user");
        if (!hasPermission(user.getRole(), path)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé - Vous n'avez pas les permissions nécessaires");
            return;
        }

        // Utilisateur connecté et autorisé → continuer
        chain.doFilter(request, response);
    }

    private boolean hasPermission(Role role, String path) {
        // Admin a accès à tout
        if (role == Role.ADMIN) {
            return true;
        }

        // Routes communes à tous les rôles connectés
        if (path.equals("/dashboard") || path.startsWith("/profile")) {
            return true;
        }

        // Permissions par rôle
        switch (role) {
            case TECHNICIAN:
                return path.startsWith("/equipments") ||
                        path.startsWith("/technician") ||
                        path.startsWith("/categories");

            case EMPLOYEE:
                return path.startsWith("/assignments") ||
                        path.startsWith("/employee") ||
                        (path.startsWith("/equipments") &&
                                (path.contains("?action=my-equipment") || path.equals("/equipments")));

            default:
                return false;
        }
    }
}