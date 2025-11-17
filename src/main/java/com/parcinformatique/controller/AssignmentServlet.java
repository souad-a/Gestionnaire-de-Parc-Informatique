package com.parcinformatique.controller;

import com.parcinformatique.model.Assignment;
import com.parcinformatique.model.Equipment;
import com.parcinformatique.model.Employee;
import com.parcinformatique.model.User;
import com.parcinformatique.model.Role;
import com.parcinformatique.service.AssignmentService;
import com.parcinformatique.service.EquipmentService;
import com.parcinformatique.service.EmployeeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/assignments/*")
public class AssignmentServlet extends HttpServlet {

    private AssignmentService assignmentService;
    private EquipmentService equipmentService;
    private EmployeeService employeeService;

    @Override
    public void init() {
        try {
            this.assignmentService = new AssignmentService();
            this.equipmentService = new EquipmentService();
            this.employeeService = new EmployeeService();
            System.out.println("AssignmentServlet initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing AssignmentServlet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getPathInfo();

        try {
            // Vérifier les permissions
            if (!hasPermission(request, action)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès non autorisé");
                return;
            }

            switch (action == null ? "list" : action) {
                case "/new":
                    showAssignmentForm(request, response);
                    break;
                case "/history":
                    showAssignmentHistory(request, response);
                    break;
                case "/active":
                    showActiveAssignments(request, response);
                    break;
                case "/return":
                    showReturnForm(request, response);
                    break;
                case "/my-assignments": // Nouvelle action pour les employés
                    showMyAssignments(request, response);
                    break;
                default:
                    listAssignments(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
            listAssignments(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getPathInfo();

        try {
            // Vérifier les permissions
            if (!hasPermission(request, action)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès non autorisé");
                return;
            }

            switch (action) {
                case "/assign":
                    assignEquipment(request, response);
                    break;
                case "/return":
                    returnEquipment(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
            showAssignmentForm(request, response);
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
            case "/new":
            case "/assign":
            case "/return":
                // Seul admin peut assigner/retourner
                return false;

            case "/my-assignments":
                // Employé peut voir ses propres affectations
                return role == Role.EMPLOYEE;

            case "/history":
            case "/active":
            case "list":
                // Technicien et Employé peuvent consulter (avec restrictions)
                return role == Role.TECHNICIAN || role == Role.EMPLOYEE;

            default:
                return true;
        }
    }

    // ✅ NOUVELLE MÉTHODE - Affectations de l'employé connecté
    private void showMyAssignments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Récupérer l'ID de l'employé connecté (à adapter selon votre modèle)
        Long employeeId = getEmployeeIdFromUser(user);

        if (employeeId != null) {
            List<Assignment> myAssignments = assignmentService.getAssignmentsByEmployee(employeeId);
            List<Assignment> activeAssignments = assignmentService.getActiveAssignments().stream()
                    .filter(assignment -> assignment.getEmployee().getId().equals(employeeId))
                    .toList();

            request.setAttribute("assignments", myAssignments);
            request.setAttribute("activeAssignments", activeAssignments);
            request.setAttribute("isMyAssignmentsView", true);
        }

        request.getRequestDispatcher("/WEB-INF/views/employee/my-assignments.jsp")
                .forward(request, response);
    }

    // Méthode utilitaire pour récupérer l'ID employé
    private Long getEmployeeIdFromUser(User user) {
        // À adapter selon votre modèle de données
        // Si User a une référence à Employee, retournez user.getEmployee().getId()
        // Sinon, vous devrez peut-être faire une requête pour trouver l'employé associé
        return null; // Temporaire - à implémenter
    }
    private void listAssignments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Récupérer le message de succès depuis la session
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

        List<Assignment> assignments = assignmentService.getAllAssignments();
        request.setAttribute("assignments", assignments);
        request.getRequestDispatcher("/WEB-INF/views/assignment-list.jsp")
                .forward(request, response);
    }

    private void showActiveAssignments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String successMessage = (String) request.getSession().getAttribute("successMessage");
        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
            request.getSession().removeAttribute("successMessage");
        }

        List<Assignment> activeAssignments = assignmentService.getActiveAssignments();
        request.setAttribute("assignments", activeAssignments);
        request.setAttribute("isActiveView", true);
        request.getRequestDispatcher("/WEB-INF/views/assignment-list.jsp")
                .forward(request, response);
    }

    private void showAssignmentHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String equipmentId = request.getParameter("equipmentId");
        String employeeId = request.getParameter("employeeId");

        List<Assignment> assignments;

        // Gérer toutes les combinaisons
        boolean hasEquipmentFilter = equipmentId != null && !equipmentId.isEmpty();
        boolean hasEmployeeFilter = employeeId != null && !employeeId.isEmpty();

        if (hasEquipmentFilter && hasEmployeeFilter) {
            // CAS 1 : Filtre par ÉQUIPEMENT ET EMPLOYÉ
            assignments = assignmentService.getAssignmentsByEquipmentAndEmployee(
                    Long.parseLong(equipmentId),
                    Long.parseLong(employeeId)
            );
        }
        else if (hasEquipmentFilter) {
            // CAS 2 : Filtre par ÉQUIPEMENT seulement
            assignments = assignmentService.getAssignmentsByEquipment(Long.parseLong(equipmentId));
        }
        else if (hasEmployeeFilter) {
            // CAS 3 : Filtre par EMPLOYÉ seulement
            assignments = assignmentService.getAssignmentsByEmployee(Long.parseLong(employeeId));
        }
        else {
            // CAS 4 : Aucun filtre
            assignments = assignmentService.getAllAssignments();
        }

        // Récupérer les données pour les listes déroulantes
        List<Equipment> allEquipment = equipmentService.getAllEquipment();
        List<Employee> allEmployees = employeeService.getAllEmployees();

        request.setAttribute("assignments", assignments);
        request.setAttribute("allEquipment", allEquipment);
        request.setAttribute("allEmployees", allEmployees);
        request.setAttribute("isHistoryView", true);

        request.getRequestDispatcher("/WEB-INF/views/assignment-history.jsp")
                .forward(request, response);
    }

    private void showAssignmentForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Equipment> availableEquipment = equipmentService.getAvailableEquipment();
        List<Employee> employees = employeeService.getAllEmployees();

        request.setAttribute("availableEquipment", availableEquipment);
        request.setAttribute("employees", employees);
        request.setAttribute("today", LocalDate.now());

        request.getRequestDispatcher("/WEB-INF/views/assignment-form.jsp")
                .forward(request, response);
    }

    private void showReturnForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String assignmentId = request.getParameter("id");
        if (assignmentId == null || assignmentId.isEmpty()) {
            request.getSession().setAttribute("errorMessage", "ID d'affectation manquant");
            response.sendRedirect(request.getContextPath() + "/assignments/active");
            return;
        }

        try {
            Assignment assignment = assignmentService.getAssignmentById(Long.parseLong(assignmentId));
            if (assignment == null || !"ACTIVE".equals(assignment.getStatus())) {
                request.getSession().setAttribute("errorMessage",
                        "Affectation non trouvée ou déjà retournée");
                response.sendRedirect(request.getContextPath() + "/assignments/active");
                return;
            }

            request.setAttribute("assignment", assignment);
            request.setAttribute("today", LocalDate.now());

            request.getRequestDispatcher("/WEB-INF/views/assignment-return.jsp")
                    .forward(request, response);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Format d'ID invalide");
            response.sendRedirect(request.getContextPath() + "/assignments/active");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage",
                    "Erreur lors de la récupération de l'affectation: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/assignments/active");
        }
    }

    private void assignEquipment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {
            String equipmentIdParam = request.getParameter("equipmentId");
            String employeeIdParam = request.getParameter("employeeId");
            String assignmentDateParam = request.getParameter("assignmentDate");

            if (equipmentIdParam == null || employeeIdParam == null || assignmentDateParam == null) {
                request.setAttribute("errorMessage", "Tous les champs obligatoires doivent être remplis");
                showAssignmentForm(request, response);
                return;
            }

            Long equipmentId = Long.parseLong(equipmentIdParam);
            Long employeeId = Long.parseLong(employeeIdParam);
            LocalDate assignmentDate = LocalDate.parse(assignmentDateParam);
            String notes = request.getParameter("notes");

            // Vérification disponibilité
            if (!assignmentService.isEquipmentAvailable(equipmentId, assignmentDate)) {
                request.setAttribute("errorMessage", "L'équipement n'est pas disponible à cette date");
                showAssignmentForm(request, response);
                return;
            }

            Assignment assignment = assignmentService.assignEquipment(
                    equipmentId, employeeId, assignmentDate, notes
            );

            request.getSession().setAttribute("successMessage",
                    "Équipement affecté avec succès à l'employé");
            response.sendRedirect(request.getContextPath() + "/assignments");

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Format de données invalide");
            showAssignmentForm(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            showAssignmentForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors de l'affectation: " + e.getMessage());
            showAssignmentForm(request, response);
        }
    }

    private void returnEquipment(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {
            String assignmentIdParam = request.getParameter("assignmentId");
            String returnDateParam = request.getParameter("returnDate");

            if (assignmentIdParam == null || returnDateParam == null) {
                request.setAttribute("errorMessage", "Tous les champs obligatoires doivent être remplis");
                showReturnForm(request, response);
                return;
            }

            Long assignmentId = Long.parseLong(assignmentIdParam);
            LocalDate returnDate = LocalDate.parse(returnDateParam);
            String returnNotes = request.getParameter("returnNotes");

            assignmentService.returnEquipment(assignmentId, returnDate, returnNotes);

            request.getSession().setAttribute("successMessage",
                    "Équipement retourné avec succès");
            response.sendRedirect(request.getContextPath() + "/assignments/active");

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Format de données invalide");
            showReturnForm(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            showReturnForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erreur lors du retour: " + e.getMessage());
            showReturnForm(request, response);
        }
    }
}