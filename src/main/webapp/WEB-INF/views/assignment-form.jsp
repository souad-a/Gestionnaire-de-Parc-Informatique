<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nouvelle Affectation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                <i class="fas fa-laptop-house"></i> Gestionnaire de Parc
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3">
                    <i class="fas fa-user"></i> ${sessionScope.username} (${sessionScope.role})
                </span>
                <a class="nav-link" href="${pageContext.request.contextPath}/auth?action=logout">
                    <i class="fas fa-sign-out-alt"></i> Déconnexion
                </a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h4 class="mb-0">
                            <i class="fas fa-plus-circle"></i> Nouvelle Affectation d'Équipement
                        </h4>
                    </div>
                    <div class="card-body">
                        <!-- Messages d'erreur -->
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/assignments/assign" method="post">
                            <div class="mb-4">
                                <label for="equipmentId" class="form-label">
                                    <i class="fas fa-laptop"></i> Équipement à affecter *
                                </label>
                                <select class="form-select" id="equipmentId" name="equipmentId" required>
                                    <option value="">-- Sélectionnez un équipement --</option>
                                    <c:forEach var="equipment" items="${availableEquipment}">
                                        <option value="${equipment.id}">
                                            ${equipment.name} - ${equipment.brand} ${equipment.model}
                                            (SN: ${equipment.serialNumber})
                                        </option>
                                    </c:forEach>
                                </select>
                                <c:if test="${empty availableEquipment}">
                                    <div class="form-text text-warning">
                                        <i class="fas fa-exclamation-triangle"></i>
                                        Aucun équipement disponible pour le moment
                                    </div>
                                </c:if>
                            </div>

                            <div class="mb-4">
                                <label for="employeeId" class="form-label">
                                    <i class="fas fa-user"></i> Employé *
                                </label>
                                <select class="form-select" id="employeeId" name="employeeId" required>
                                    <option value="">-- Sélectionnez un employé --</option>
                                    <c:forEach var="employee" items="${employees}">
                                        <option value="${employee.id}">
                                            ${employee.firstName} ${employee.lastName} - ${employee.department}
                                        </option>
                                    </c:forEach>
                                </select>
                                <c:if test="${empty employees}">
                                    <div class="form-text text-warning">
                                        <i class="fas fa-exclamation-triangle"></i>
                                        Aucun employé enregistré
                                    </div>
                                </c:if>
                            </div>

                            <div class="mb-4">
                                <label for="assignmentDate" class="form-label">
                                    <i class="fas fa-calendar-alt"></i> Date d'affectation *
                                </label>
                                <input type="date"
                                       class="form-control"
                                       id="assignmentDate"
                                       name="assignmentDate"
                                       value="${today}"
                                       required>
                                <div class="form-text">
                                    <i class="fas fa-info-circle"></i>
                                    Date à laquelle l'équipement est remis à l'employé
                                </div>
                            </div>

                            <div class="mb-4">
                                <label for="notes" class="form-label">
                                    <i class="fas fa-sticky-note"></i> Notes / Commentaires
                                </label>
                                <textarea class="form-control"
                                          id="notes"
                                          name="notes"
                                          rows="4"
                                          maxlength="500"
                                          placeholder="Ajoutez des notes sur cette affectation (état de l'équipement, accessoires fournis, etc.)"></textarea>
                                <div class="form-text">
                                    <i class="fas fa-info-circle"></i>
                                    Optionnel - Maximum 500 caractères
                                </div>
                            </div>

                            <div class="alert alert-info">
                                <i class="fas fa-lightbulb"></i>
                                <strong>Information :</strong> L'équipement sera automatiquement marqué comme "Assigné"
                                et ne sera plus disponible pour d'autres affectations.
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/assignments" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left"></i> Annuler
                                </a>
                                <button type="submit"
                                        class="btn btn-success"
                                        ${empty availableEquipment || empty employees ? 'disabled' : ''}>
                                    <i class="fas fa-check"></i> Créer l'affectation
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>