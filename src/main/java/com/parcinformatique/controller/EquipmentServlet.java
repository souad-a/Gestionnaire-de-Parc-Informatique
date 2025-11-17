package com.parcinformatique.controller;

import com.parcinformatique.service.EquipmentService;
import com.parcinformatique.service.CategoryService;
import com.parcinformatique.model.Equipment;
import com.parcinformatique.model.EquipmentStatus;
import com.parcinformatique.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            if ("create".equals(action)) {
                createEquipment(request, response);
            } else if ("update".equals(action)) {
                updateEquipment(request, response);
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