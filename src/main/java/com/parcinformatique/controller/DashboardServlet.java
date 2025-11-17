package com.parcinformatique.controller;

import com.parcinformatique.dao.*;
import com.parcinformatique.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private EquipmentDAO equipmentDAO;
    private EmployeeDAO employeeDAO;
    private CategoryDAO categoryDAO;
    private AssignmentDAO assignmentDAO;

    @Override
    public void init() throws ServletException {
        equipmentDAO = new EquipmentDAO();
        employeeDAO = new EmployeeDAO();
        categoryDAO = new CategoryDAO();
        assignmentDAO = new AssignmentDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Récupérer toutes les données
            List<Equipment> allEquipments = equipmentDAO.findAll();
            List<Employee> allEmployees = employeeDAO.findAll();
            List<Category> allCategories = categoryDAO.findAll();
            List<Assignment> activeAssignments = assignmentDAO.findActiveAssignments();

            // Calculer les statistiques des équipements
            long totalEquipments = allEquipments.size();
            long availableEquipments = allEquipments.stream()
                    .filter(e -> e.getStatus() == EquipmentStatus.AVAILABLE)
                    .count();
            long inUseEquipments = allEquipments.stream()
                    .filter(e -> e.getStatus() == EquipmentStatus.ASSIGNED)
                    .count();
            long maintenanceEquipments = allEquipments.stream()
                    .filter(e -> e.getStatus() == EquipmentStatus.MAINTENANCE)
                    .count();
            long outOfServiceEquipments = allEquipments.stream()
                    .filter(e -> e.getStatus() == EquipmentStatus.OUT_OF_SERVICE)
                    .count();

            // Calculer les taux
            double utilizationRate = totalEquipments > 0 ?
                    (inUseEquipments * 100.0) / totalEquipments : 0;
            double maintenanceRate = totalEquipments > 0 ?
                    (maintenanceEquipments * 100.0) / totalEquipments : 0;
            double availabilityRate = totalEquipments > 0 ?
                    (availableEquipments * 100.0) / totalEquipments : 0;

            // Préparer les attributs pour la JSP
            request.setAttribute("totalEquipments", totalEquipments);
            request.setAttribute("availableEquipments", availableEquipments);
            request.setAttribute("inUseEquipments", inUseEquipments);
            request.setAttribute("maintenanceEquipments", maintenanceEquipments);
            request.setAttribute("outOfServiceEquipments", outOfServiceEquipments);
            request.setAttribute("utilizationRate", Math.round(utilizationRate * 100.0) / 100.0);
            request.setAttribute("maintenanceRate", Math.round(maintenanceRate * 100.0) / 100.0);
            request.setAttribute("availabilityRate", Math.round(availabilityRate * 100.0) / 100.0);

            request.setAttribute("totalEmployees", allEmployees.size());
            request.setAttribute("totalCategories", allCategories.size());
            request.setAttribute("activeAssignments", activeAssignments.size());

            // Ajouter les statistiques détaillées
            EquipmentStats stats = new EquipmentStats(allEquipments);
            request.setAttribute("stats", stats);

            // Log pour debug
            System.out.println("📊 Dashboard stats - Équipements: " + totalEquipments +
                    ", Disponibles: " + availableEquipments +
                    ", Utilisés: " + inUseEquipments);

            // Forward vers la page dashboard
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement du dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}