<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Retour d'Équipement</title>
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
                    <div class="card-header bg-warning text-dark">
                        <h4 class="mb-0">
                            <i class="fas fa-undo"></i> Retour d'Équipement
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

                        <!-- Informations sur l'affectation -->
                        <div class="alert alert-info mb-4">
                            <h5 class="alert-heading">
                                <i class="fas fa-info-circle"></i> Informations sur l'affectation
                            </h5>
                            <hr>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="mb-2">
                                        <strong><i class="fas fa-laptop"></i> Équipement :</strong><br>
                                        ${assignment.equipment.name}
                                    </p>
                                    <p class="mb-2">
                                        <strong><i class="fas fa-barcode"></i> N° Série :</strong><br>
                                        ${assignment.equipment.serialNumber}
                                    </p>
                                </div>
                                <div class="col-md-6">
                                    <p class="mb-2">
                                        <strong><i class="fas fa-user"></i> Employé :</strong><br>
                                        ${assignment.employee.firstName} ${assignment.employee.lastName}
                                    </p>
                                    <p class="mb-2">
                                        <strong><i class="fas fa-calendar-alt"></i> Date d'affectation :</strong><br>
                                        ${assignment.assignmentDate}
                                    </p>
                                </div>
                            </div>
                            <c:if test="${not empty assignment.notes}">
                                <hr>
                                <p class="mb-0">
                                    <strong><i class="fas fa-sticky-note"></i> Notes d'affectation :</strong><br>
                                    <span class="text-muted" style="white-space: pre-wrap;">${assignment.notes}</span>
                                </p>
                            </c:if>
                        </div>

                        <form action="${pageContext.request.contextPath}/assignments/return" method="post">
                            <input type="hidden" name="assignmentId" value="${assignment.id}">

                            <div class="mb-4">
                                <label for="returnDate" class="form-label">
                                    <i class="fas fa-calendar-check"></i> Date de retour *
                                </label>
                                <input type="date"
                                       class="form-control"
                                       id="returnDate"
                                       name="returnDate"
                                       value="${today}"
                                       required>
                                <div class="form-text">
                                    <i class="fas fa-info-circle"></i>
                                    Date à laquelle l'équipement est retourné
                                </div>
                            </div>

                            <div class="mb-4">
                                <label for="returnNotes" class="form-label">
                                    <i class="fas fa-clipboard-check"></i> Notes de retour / État de l'équipement
                                </label>
                                <textarea class="form-control"
                                          id="returnNotes"
                                          name="returnNotes"
                                          rows="4"
                                          maxlength="500"
                                          placeholder="Décrivez l'état de l'équipement au retour (dommages, accessoires manquants, etc.)"></textarea>
                                <div class="form-text">
                                    <i class="fas fa-info-circle"></i>
                                    Optionnel - Maximum 500 caractères
                                </div>
                            </div>

                            <div class="alert alert-warning">
                                <i class="fas fa-exclamation-triangle"></i>
                                <strong>Attention :</strong> Une fois le retour validé, l'équipement sera de nouveau
                                marqué comme "Disponible" et pourra être réaffecté.
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/assignments/active" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left"></i> Annuler
                                </a>
                                <button type="submit" class="btn btn-warning">
                                    <i class="fas fa-check"></i> Confirmer le retour
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