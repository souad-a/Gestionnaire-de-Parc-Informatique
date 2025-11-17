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

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Pages publiques
        if (path.startsWith("/auth") || path.equals("/") || path.startsWith("/resources/") ||
                path.contains(".css") || path.contains(".js") || path.contains(".jpg") || path.contains(".png")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/auth?action=login");
            return;
        }

        User user = (User) session.getAttribute("user");
        Role role = user.getRole();

        // Debug
        System.out.println("🔑 Filtre: User=" + user.getUsername() + ", Role=" + role + ", Path=" + path);

        if (!hasPermission(role, path)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé - permissions insuffisantes");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean hasPermission(Role role, String path) {
        if (role == Role.ADMIN) return true;

        if (path.equals("/dashboard") || path.startsWith("/profile")) return true;

        switch (role) {
            case TECHNICIAN:
                return path.startsWith("/equipments") || path.startsWith("/technician") || path.startsWith("/categories");
            case EMPLOYEE:
                return path.startsWith("/assignments") || path.startsWith("/employee") ||
                        (path.startsWith("/equipments") && (path.contains("?action=my-equipment") || path.equals("/equipments")));
            default:
                return false;
        }
    }
}
