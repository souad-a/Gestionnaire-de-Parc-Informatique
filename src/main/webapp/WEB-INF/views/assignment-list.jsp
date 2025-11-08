<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestion des Affectations</title>
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
        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center mb-4">
            <h2>
                <i class="fas fa-list-check"></i>
                <c:choose>
                    <c:when test="${isActiveView}">
                        Affectations Actives
                    </c:when>
                    <c:otherwise>
                        Gestion des Affectations
                    </c:otherwise>
                </c:choose>
            </h2>
            <div class="btn-toolbar">
                <a href="${pageContext.request.contextPath}/assignments/new" class="btn btn-success me-2">
                    <i class="fas fa-plus"></i> Nouvelle Affectation
                </a>
                <c:if test="${!isActiveView}">
                    <a href="${pageContext.request.contextPath}/assignments/active" class="btn btn-primary me-2">
                        <i class="fas fa-play-circle"></i> Affectations Actives
                    </a>
                </c:if>
                <a href="${pageContext.request.contextPath}/assignments/history" class="btn btn-info">
                    <i class="fas fa-history"></i> Historique
                </a>
            </div>
        </div>

        <!-- Messages -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle"></i> ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Statistiques rapides -->
        <c:if test="${!isActiveView}">
        <div class="row mb-4">
            <div class="col-md-4">
                <div class="card text-white bg-primary">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h4 class="card-title">${assignments.size()}</h4>
                                <p class="card-text">Total Affectations</p>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-list fa-3x"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-white bg-success">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h4 class="card-title">
                                    <c:set var="activeCount" value="0" />
                                    <c:forEach var="assignment" items="${assignments}">
                                        <c:if test="${assignment.status.toString() == 'ACTIVE'}">
                                            <c:set var="activeCount" value="${activeCount + 1}" />
                                        </c:if>
                                    </c:forEach>
                                    ${activeCount}
                                </h4>
                                <p class="card-text">Actives</p>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-play-circle fa-3x"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-white bg-secondary">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h4 class="card-title">
                                    <c:set var="returnedCount" value="0" />
                                    <c:forEach var="assignment" items="${assignments}">
                                        <c:if test="${assignment.status.toString() == 'RETURNED'}">
                                            <c:set var="returnedCount" value="${returnedCount + 1}" />
                                        </c:if>
                                    </c:forEach>
                                    ${returnedCount}
                                </h4>
                                <p class="card-text">Retournées</p>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-undo fa-3x"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </c:if>

        <!-- Tableau des affectations -->
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <i class="fas fa-table"></i>
                    <c:choose>
                        <c:when test="${isActiveView}">
                            Liste des Affectations Actives
                        </c:when>
                        <c:otherwise>
                            Liste des Affectations
                        </c:otherwise>
                    </c:choose>
                    <span class="badge bg-secondary">${assignments.size()}</span>
                </h5>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Équipement</th>
                                <th>Employé</th>
                                <th>Date Affectation</th>
                                <th>Date Retour</th>
                                <th>Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="assignment" items="${assignments}">
                                <tr>
                                    <td>#${assignment.id}</td>
                                    <td>
                                        <strong>${assignment.equipment.name}</strong>
                                        <br><small class="text-muted">
                                            <i class="fas fa-barcode"></i> ${assignment.equipment.serialNumber}
                                        </small>
                                    </td>
                                    <td>
                                        <i class="fas fa-user"></i> ${assignment.employee.firstName} ${assignment.employee.lastName}
                                        <br><small class="text-muted">
                                            <i class="fas fa-building"></i> ${assignment.employee.department}
                                        </small>
                                    </td>
                                    <td>
                                        <i class="fas fa-calendar-alt"></i> ${assignment.assignmentDate}
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${assignment.returnDate != null}">
                                                <i class="fas fa-calendar-check"></i> ${assignment.returnDate}
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning text-dark">
                                                    <i class="fas fa-clock"></i> En cours
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${assignment.status.toString() == 'ACTIVE'}">
                                                <span class="badge bg-success">
                                                    <i class="fas fa-check-circle"></i> Active
                                                </span>
                                            </c:when>
                                            <c:when test="${assignment.status.toString() == 'RETURNED'}">
                                                <span class="badge bg-secondary">
                                                    <i class="fas fa-arrow-rotate-left"></i> Retournée
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-info">${assignment.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="btn-group" role="group">
                                            <c:if test="${assignment.status.toString() == 'ACTIVE'}">
                                                <a href="${pageContext.request.contextPath}/assignments/return?id=${assignment.id}"
                                                   class="btn btn-warning btn-sm" title="Retourner l'équipement">
                                                    <i class="fas fa-undo"></i>
                                                </a>
                                            </c:if>
                                            <button type="button" class="btn btn-info btn-sm"
                                                    data-bs-toggle="modal" data-bs-target="#notesModal${assignment.id}"
                                                    title="Voir les notes">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                        </div>

                                        <!-- Modal Notes -->
                                        <div class="modal fade" id="notesModal${assignment.id}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header bg-primary text-white">
                                                        <h5 class="modal-title">
                                                            <i class="fas fa-sticky-note"></i>
                                                            Notes de l'affectation #${assignment.id}
                                                        </h5>
                                                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <h6 class="fw-bold">Notes d'affectation :</h6>
                                                        <c:choose>
                                                            <c:when test="${not empty assignment.notes}">
                                                                <p class="text-muted" style="white-space: pre-wrap;">${assignment.notes}</p>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <p class="text-muted fst-italic">
                                                                    <i class="fas fa-info-circle"></i>
                                                                    Aucune note d'affectation
                                                                </p>
                                                            </c:otherwise>
                                                        </c:choose>


                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>

                            <c:if test="${empty assignments}">
                                <tr>
                                    <td colspan="7" class="text-center text-muted py-5">
                                        <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                                        <c:choose>
                                            <c:when test="${isActiveView}">
                                                <h5>Aucune affectation active</h5>
                                                <p>Toutes les affectations ont été retournées ou il n'y a pas encore d'affectations.</p>
                                            </c:when>
                                            <c:otherwise>
                                                <h5>Aucune affectation trouvée</h5>
                                                <p>Commencez par créer une nouvelle affectation.</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Retour au Dashboard
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>