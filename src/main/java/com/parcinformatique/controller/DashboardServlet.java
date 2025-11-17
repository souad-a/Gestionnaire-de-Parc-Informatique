package com.parcinformatique.controller;

import com.parcinformatique.model.User;
import com.parcinformatique.model.Role;
import com.parcinformatique.service.EquipmentService;
import com.parcinformatique.service.UserService;
import com.parcinformatique.service.AssignmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet pour gérer les tableaux de bord selon les rôles
 * Calcule les statistiques principales et redirige vers la vue appropriée
 */
@WebServlet({"/dashboard", "/admin/dashboard", "/technician/dashboard", "/employee/dashboard"})
public class DashboardServlet extends HttpServlet {

    private EquipmentService equipmentService;
    private UserService userService;
    private AssignmentService assignmentService;

    @Override
    public void init() throws ServletException {
        this.equipmentService = new EquipmentService();
        this.userService = new UserService();
        this.assignmentService = new AssignmentService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        User user = (User) session.getAttribute("user");
        Role role = user.getRole();

        System.out.println("📊 Dashboard accédé par: " + user.getUsername() + " - Rôle: " + role);

        // Calculer les statistiques selon le rôle
        calculateStatistics(request, role);

        // Déterminer la page JSP selon le rôle
        String jspPage = determineDashboardPage(role);
        request.getRequestDispatcher(jspPage).forward(request, response);
    }

    /**
     * Calcule les statistiques principales selon le rôle de l'utilisateur
     */
    private void calculateStatistics(HttpServletRequest request, Role role) {
        try {
            // Statistiques communes
            long totalEquipment = equipmentService.getAllEquipment().size();
            long availableEquipment = equipmentService.getAvailableEquipment().size();
            long assignedEquipment = equipmentService.getAssignedEquipmentCount().size();
            long maintenanceEquipment = equipmentService.getMaintenanceEquipmentCount().size();
            long totalUsers = userService.getAllUsers().size();
            long activeAssignments = assignmentService.getActiveAssignments().size();

            // Passer les statistiques à la vue
            request.setAttribute("totalEquipment", totalEquipment);
            request.setAttribute("availableEquipment", availableEquipment);
            request.setAttribute("assignedEquipment", assignedEquipment);
            request.setAttribute("maintenanceEquipment", maintenanceEquipment);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("activeAssignments", activeAssignments);

            // Statistiques spécifiques selon le rôle
            switch (role) {
                case ADMIN:
                    // Statistiques complètes pour l'admin
                    long totalEmployees = userService.getUsersByRole(Role.EMPLOYEE).size();
                    long totalTechnicians = userService.getUsersByRole(Role.TECHNICIAN).size();
                    request.setAttribute("totalEmployees", totalEmployees);
                    request.setAttribute("totalTechnicians", totalTechnicians);
                    break;

                case TECHNICIAN:
                    // Statistiques pour le technicien (équipements en maintenance, pannes)
                    long equipmentWithIssues = equipmentService.getEquipmentWithIssues().size();
                    request.setAttribute("equipmentWithIssues", equipmentWithIssues);
                    break;

                case EMPLOYEE:
                    // Statistiques pour l'employé (ses équipements assignés)
                    // Note: nécessite l'ID de l'employé, à adapter selon votre modèle
                    // Pour l'instant, on passe 0 comme valeur par défaut
                    request.setAttribute("myAssignments", 0);
                    break;
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du calcul des statistiques: " + e.getMessage());
            e.printStackTrace();
            // Valeurs par défaut en cas d'erreur
            request.setAttribute("totalEquipment", 0);
            request.setAttribute("availableEquipment", 0);
            request.setAttribute("assignedEquipment", 0);
            request.setAttribute("maintenanceEquipment", 0);
            request.setAttribute("totalUsers", 0);
            request.setAttribute("activeAssignments", 0);
        }
    }

    /**
     * Détermine la page JSP à afficher selon le rôle
     */
    private String determineDashboardPage(Role role) {
        if (role == null) return "/WEB-INF/views/dashboard.jsp";

        switch (role) {
            case ADMIN:
                return "/WEB-INF/views/admin/dashboard.jsp";
            case TECHNICIAN:
                return "/WEB-INF/views/technician/dashboard.jsp";
            case EMPLOYEE:
                return "/WEB-INF/views/employee/dashboard.jsp";
            default:
                return "/WEB-INF/views/dashboard.jsp";
        }
    }
}
