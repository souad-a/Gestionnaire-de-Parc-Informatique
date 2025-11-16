// 📁 src/main/java/com/parcinformatique/controller/DashboardServlet.java
package com.parcinformatique.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/dashboard", "/admin/dashboard", "/technician/dashboard", "/employee/dashboard"})
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        System.out.println("📊 Dashboard accédé par: " + username + " - Rôle: " + role);

        // Déterminer quelle page afficher selon le rôle
        String jspPage = determineDashboardPage(role);
        request.getRequestDispatcher(jspPage).forward(request, response);
    }

    private String determineDashboardPage(String role) {
        if (role == null) return "/WEB-INF/views/dashboard.jsp";

        switch (role) {
            case "ADMIN":
                return "/WEB-INF/views/admin/dashboard.jsp";
            case "TECHNICIAN":
                return "/WEB-INF/views/technician/dashboard.jsp";
            case "EMPLOYEE":
                return "/WEB-INF/views/employee/dashboard.jsp";
            default:
                return "/WEB-INF/views/dashboard.jsp";
        }
    }
}