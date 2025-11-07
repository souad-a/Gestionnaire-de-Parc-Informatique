// 📁 src/main/java/com/parcinformatique/controller/EquipmentServlet.java
package com.parcinformatique.controller;

import com.parcinformatique.dao.EquipmentDAO;
import com.parcinformatique.dao.CategoryDAO;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/equipments")
public class EquipmentServlet extends HttpServlet {
    private EquipmentDAO equipmentDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        equipmentDAO = new EquipmentDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) action = "list";

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
            }
        } catch (Exception e) {
            throw new ServletException(e);
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
            throw new ServletException(e);
        }
    }

    private void listEquipment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Equipment> equipmentList = equipmentDAO.findAll();
        request.setAttribute("equipmentList", equipmentList);
        request.getRequestDispatcher("/WEB-INF/views/equipment-list.jsp").forward(request, response);
    }

    private void listAvailableEquipment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Equipment> availableEquipment = equipmentDAO.findAvailableEquipment();
        request.setAttribute("equipmentList", availableEquipment);
        request.setAttribute("showOnlyAvailable", true);
        request.getRequestDispatcher("/WEB-INF/views/equipment-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);
        request.setAttribute("statusValues", EquipmentStatus.values());
        request.getRequestDispatcher("/WEB-INF/views/equipment-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Equipment equipment = equipmentDAO.findById(id);
        List<Category> categories = categoryDAO.findAll();

        request.setAttribute("equipment", equipment);
        request.setAttribute("categories", categories);
        request.setAttribute("statusValues", EquipmentStatus.values());
        request.getRequestDispatcher("/WEB-INF/views/equipment-form.jsp").forward(request, response);
    }

    private void createEquipment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            Equipment equipment = extractEquipmentFromRequest(request);

            // Validation numéro de série unique
            if (!equipmentDAO.isSerialNumberUnique(equipment.getSerialNumber(), null)) {
                request.setAttribute("errorMessage", "Un équipement avec ce numéro de série existe déjà.");
                showNewForm(request, response);
                return;
            }

            equipmentDAO.save(equipment);
            response.sendRedirect("equipments?successMessage=Équipement créé avec succès");

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Erreur lors de la création: " + e.getMessage());
            showNewForm(request, response);
        }
    }

    private void updateEquipment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Equipment equipment = extractEquipmentFromRequest(request);
            equipment.setId(id);

            // Validation numéro de série unique
            if (!equipmentDAO.isSerialNumberUnique(equipment.getSerialNumber(), id)) {
                request.setAttribute("errorMessage", "Un équipement avec ce numéro de série existe déjà.");
                showEditForm(request, response);
                return;
            }

            equipmentDAO.save(equipment);
            response.sendRedirect("equipments?successMessage=Équipement modifié avec succès");

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Erreur lors de la modification: " + e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteEquipment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        equipmentDAO.delete(id);
        response.sendRedirect("equipments?successMessage=Équipement supprimé avec succès");
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
            Category category = new Category();
            category.setId(Long.parseLong(categoryIdParam));
            equipment.setCategory(category);
        }

        return equipment;
    }
}