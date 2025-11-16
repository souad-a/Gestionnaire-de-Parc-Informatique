package com.parcinformatique.controller;

import com.parcinformatique.service.EquipmentService;
import com.parcinformatique.service.CategoryService;
import com.parcinformatique.model.Equipment;
import com.parcinformatique.model.EquipmentStatus;
import com.parcinformatique.model.Category;
import com.parcinformatique.model.User;
import com.parcinformatique.model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/equipments")
public class EquipmentServlet extends HttpServlet {
    private EquipmentService equipmentService;
    private CategoryService categoryService;

    @Override
    public void init() {
        try {
            this.equipmentService = new EquipmentService();
            this.categoryService = new CategoryService();
            System.out.println("EquipmentServlet initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing EquipmentServlet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        try {
            // Vérifier les permissions selon l'action
            if (!hasPermission(request, action)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès non autorisé");
                return;
            }

            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteEquipment(request, response);
                    break;
                case "available":
                    listAvailableEquipment(request, response);
                    break;
                case "my-equipment": // Nouvelle action pour les employés
                    showMyEquipment(request, response);
                    break;
                case "technician-view": // Nouvelle action pour les techniciens
                    showTechnicianView(request, response);
                    break;
                case "report-issue":
                    showReportIssueForm(request, response);
                    break;
                default:
                    listEquipment(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
            listEquipment(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            // Vérifier les permissions
            if (!hasPermission(request, action)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès non autorisé");
                return;
            }

            if ("create".equals(action)) {
                createEquipment(request, response);
            } else if ("update".equals(action)) {
                updateEquipment(request, response);
            } else if ("update-status".equals(action)) {
                updateEquipmentStatus(request, response);
            } else if ("report-issue".equals(action)) {
                reportEquipmentIssue(request, response);
            } else {
                listEquipment(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/equipment-form.jsp")
                    .forward(request, response);
        }
    }

    private boolean hasPermission(HttpServletRequest request, String action) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        User user = (User) session.getAttribute("user");
        if (user == null) return false;

        Role role = user.getRole();

        // Admin a tous les droits
        if (role == Role.ADMIN) return true;

        switch (action) {
            case "new":
            case "edit":
            case "delete":
            case "create":
            case "update":
                // Seul admin peut créer/modifier/supprimer
                return false;

            case "update-status":
                // Technicien peut changer le statut
                return role == Role.TECHNICIAN;

            case "report-issue":
            case "my-equipment":
                // Employé peut déclarer des pannes et voir son équipement
                return role == Role.EMPLOYEE;

            case "technician-view":
                // Technicien peut voir la vue spéciale
                return role == Role.TECHNICIAN;

            default:
                // Liste et consultation pour tous
                return true;
        }
    }

    // ✅ NOUVELLE MÉTHODE - Équipement de l'employé connecté
    private void showMyEquipment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Ici vous devrez récupérer les équipements assignés à cet employé
        // Via AssignmentService.getAssignedEquipmentForEmployee(user.getEmployee().getId())

        List<Equipment> myEquipment = equipmentService.getAvailableEquipment(); // À adapter
        request.setAttribute("equipmentList", myEquipment);
        request.setAttribute("isMyEquipmentView", true);
        request.getRequestDispatcher("/WEB-INF/views/employee/my-equipment.jsp").forward(request, response);
    }

    // ✅ NOUVELLE MÉTHODE - Vue technicien
    private void showTechnicianView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Equipment> equipmentWithIssues = equipmentService.getEquipmentWithIssues();
        List<Equipment> maintenanceEquipment = equipmentService.getEquipmentInMaintenance();

        request.setAttribute("equipmentWithIssues", equipmentWithIssues);
        request.setAttribute("maintenanceEquipment", maintenanceEquipment);
        request.setAttribute("isTechnicianView", true);
        request.getRequestDispatcher("/WEB-INF/views/technician/equipment-view.jsp").forward(request, response);
    }

    // ✅ NOUVELLE MÉTHODE - Formulaire déclaration panne
    private void showReportIssueForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String equipmentId = request.getParameter("id");
        if (equipmentId != null) {
            Equipment equipment = equipmentService.getEquipmentById(Long.parseLong(equipmentId));
            request.setAttribute("equipment", equipment);
        }
        request.getRequestDispatcher("/WEB-INF/views/employee/report-issue.jsp").forward(request, response);
    }

    // ✅ NOUVELLE MÉTHODE - Mise à jour statut (technicien)
    private void updateEquipmentStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String equipmentIdParam = request.getParameter("equipmentId");
            String statusParam = request.getParameter("status");

            Long equipmentId = Long.parseLong(equipmentIdParam);
            EquipmentStatus newStatus = EquipmentStatus.valueOf(statusParam);

            equipmentService.updateEquipmentStatus(equipmentId, newStatus);

            request.getSession().setAttribute("successMessage", "Statut de l'équipement mis à jour avec succès");
            response.sendRedirect(request.getContextPath() + "/equipments?action=technician-view");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de la mise à jour: " + e.getMessage());
            showTechnicianView(request, response);
        }
    }

    // ✅ NOUVELLE MÉTHODE - Déclaration panne (employé)
    private void reportEquipmentIssue(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String equipmentIdParam = request.getParameter("equipmentId");
            String issueDescription = request.getParameter("issueDescription");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            // Ici vous appelleriez AssignmentService.reportEquipmentIssue()
            Long equipmentId = Long.parseLong(equipmentIdParam);
            equipmentService.updateEquipmentStatus(equipmentId, EquipmentStatus.PANNE);

            request.getSession().setAttribute("successMessage", "Panne déclarée avec succès");
            response.sendRedirect(request.getContextPath() + "/equipments?action=my-equipment");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de la déclaration: " + e.getMessage());
            showReportIssueForm(request, response);
        }
    }


    private void listEquipment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Récupérer et afficher le message de succès depuis la session
        String successMessage = (String) request.getSession().getAttribute("successMessage");
        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
            request.getSession().removeAttribute("successMessage");
        }

        // Récupérer le message d'erreur depuis la session
        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            request.getSession().removeAttribute("errorMessage");
        }

        List<Equipment> equipmentList = equipmentService.getAllEquipment();
        request.setAttribute("equipmentList", equipmentList);
        request.getRequestDispatcher("/WEB-INF/views/equipment-list.jsp").forward(request, response);
    }

    private void listAvailableEquipment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Equipment> availableEquipment = equipmentService.getAvailableEquipment();
        request.setAttribute("equipmentList", availableEquipment);
        request.setAttribute("showOnlyAvailable", true);
        request.getRequestDispatcher("/WEB-INF/views/equipment-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = categoryService.getAllCategories();
        request.setAttribute("categories", categories);
        request.setAttribute("statusValues", EquipmentStatus.values());
        request.getRequestDispatcher("/WEB-INF/views/equipment-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "ID de l'équipement manquant");
            response.sendRedirect(request.getContextPath() + "/equipments");
            return;
        }

        try {
            Long id = Long.parseLong(idParam);
            Equipment equipment = equipmentService.getEquipmentById(id);
            List<Category> categories = categoryService.getAllCategories();

            if (equipment == null) {
                request.getSession().setAttribute("errorMessage", "Équipement non trouvé");
                response.sendRedirect(request.getContextPath() + "/equipments");
                return;
            }

            request.setAttribute("equipment", equipment);
            request.setAttribute("categories", categories);
            request.setAttribute("statusValues", EquipmentStatus.values());
            request.getRequestDispatcher("/WEB-INF/views/equipment-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Format d'ID invalide");
            response.sendRedirect(request.getContextPath() + "/equipments");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/equipments");
        }
    }

    private void createEquipment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            Equipment equipment = extractEquipmentFromRequest(request);
            equipmentService.saveEquipment(equipment);
            request.getSession().setAttribute("successMessage", "Équipement créé avec succès");
            response.sendRedirect(request.getContextPath() + "/equipments");

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            List<Category> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);
            request.setAttribute("statusValues", EquipmentStatus.values());
            request.getRequestDispatcher("/WEB-INF/views/equipment-form.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de la création: " + e.getMessage());
            showNewForm(request, response);
        }
    }

    private void updateEquipment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "ID de l'équipement manquant");
                response.sendRedirect(request.getContextPath() + "/equipments");
                return;
            }

            Long id = Long.parseLong(idParam);
            Equipment equipment = extractEquipmentFromRequest(request);
            equipment.setId(id);

            equipmentService.updateEquipment(equipment);
            request.getSession().setAttribute("successMessage", "Équipement modifié avec succès");
            response.sendRedirect(request.getContextPath() + "/equipments");

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Long id = Long.parseLong(idParam);
                    Equipment equipment = equipmentService.getEquipmentById(id);
                    request.setAttribute("equipment", equipment);
                } catch (Exception ex) {
                    // Ignore
                }
            }
            List<Category> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);
            request.setAttribute("statusValues", EquipmentStatus.values());
            request.getRequestDispatcher("/WEB-INF/views/equipment-form.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de la modification: " + e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteEquipment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.trim().isEmpty()) {
                Long id = Long.parseLong(idParam);
                equipmentService.deleteEquipment(id);
                request.getSession().setAttribute("successMessage", "Équipement supprimé avec succès");
            } else {
                request.getSession().setAttribute("errorMessage", "ID de l'équipement manquant");
            }
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/equipments");
    }

    private Equipment extractEquipmentFromRequest(HttpServletRequest request) {
        Equipment equipment = new Equipment();

        equipment.setName(request.getParameter("name"));
        equipment.setBrand(request.getParameter("brand"));
        equipment.setModel(request.getParameter("model"));
        equipment.setSerialNumber(request.getParameter("serialNumber"));
        equipment.setDescription(request.getParameter("description"));

        // Gestion du statut
        String statusParam = request.getParameter("status");
        if (statusParam != null && !statusParam.isEmpty()) {
            equipment.setStatus(EquipmentStatus.valueOf(statusParam));
        }

        // Gestion de la date d'achat
        String purchaseDateParam = request.getParameter("purchaseDate");
        if (purchaseDateParam != null && !purchaseDateParam.isEmpty()) {
            equipment.setPurchaseDate(LocalDate.parse(purchaseDateParam));
        }

        // Gestion de la catégorie
        String categoryIdParam = request.getParameter("categoryId");
        if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
            Category category = categoryService.getCategoryById(Long.parseLong(categoryIdParam));
            equipment.setCategory(category);
        }

        return equipment;
    }
}