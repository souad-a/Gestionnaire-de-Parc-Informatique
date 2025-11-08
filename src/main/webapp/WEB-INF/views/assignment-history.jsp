<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Historique des Affectations</title>
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
                <i class="fas fa-history"></i> Historique des Affectations
            </h2>
            <div class="btn-toolbar">
                <a href="${pageContext.request.contextPath}/assignments" class="btn btn-primary me-2">
                    <i class="fas fa-list"></i> Toutes les affectations
                </a>
                <a href="${pageContext.request.contextPath}/assignments/new" class="btn btn-success">
                    <i class="fas fa-plus"></i> Nouvelle Affectation
                </a>
            </div>
        </div>

        <!-- Filtres -->
        <div class="card mb-4">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-filter"></i> Filtres
                </h5>
            </div>
            <div class="card-body">
                <form method="get" action="${pageContext.request.contextPath}/assignments/history" class="row g-3">
                   <div class="col-md-6">
                       <label for="equipmentId" class="form-label">
                           <i class="fas fa-laptop"></i> Par Équipement
                       </label>
                       <select class="form-select" id="equipmentId" name="equipmentId">
                           <option value="">-- Tous les équipements --</option>
                           <c:forEach var="equip" items="${allEquipment}">
                               <option value="${equip.id}"
                                       ${param.equipmentId == equip.id ? 'selected' : ''}>
                                   ${equip.name} - ${equip.serialNumber}
                               </option>
                           </c:forEach>
                       </select>
                   </div>
                   <div class="col-md-6">
                       <label for="employeeId" class="form-label">
                           <i class="fas fa-user"></i> Par Employé
                       </label>
                       <select class="form-select" id="employeeId" name="employeeId">
                           <option value="">-- Tous les employés --</option>
                           <c:forEach var="emp" items="${allEmployees}">
                               <option value="${emp.id}"
                                       ${param.employeeId == emp.id ? 'selected' : ''}>
                                   ${emp.firstName} ${emp.lastName} - ${emp.department}
                               </option>
                           </c:forEach>
                       </select>
                   </div>
                    <div class="col-12">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-search"></i> Filtrer
                        </button>
                        <a href="${pageContext.request.contextPath}/assignments/history" class="btn btn-secondary">
                            <i class="fas fa-times"></i> Réinitialiser
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <!-- Tableau historique -->
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">
                    <i class="fas fa-clock-rotate-left"></i> Historique Complet
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
                                <th>Durée</th>
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
                                            <c:when test="${assignment.returnDate != null}">
                                                <!-- Calcul de la durée pourrait être fait côté Java -->
                                                <span class="badge bg-info">
                                                    <i class="fas fa-hourglass-half"></i> Calculer
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning text-dark">
                                                    <i class="fas fa-infinity"></i> En cours
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
                                        <button type="button" class="btn btn-info btn-sm"
                                                data-bs-toggle="modal" data-bs-target="#detailsModal${assignment.id}"
                                                title="Voir les détails">
                                            <i class="fas fa-eye"></i>
                                        </button>

                                        <!-- Modal Détails -->
                                        <div class="modal fade" id="detailsModal${assignment.id}" tabindex="-1">
                                            <div class="modal-dialog modal-lg">
                                                <div class="modal-content">
                                                    <div class="modal-header bg-primary text-white">
                                                        <h5 class="modal-title">
                                                            <i class="fas fa-info-circle"></i>
                                                            Détails de l'affectation #${assignment.id}
                                                        </h5>
                                                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="row mb-3">
                                                            <div class="col-md-6">
                                                                <h6 class="fw-bold">
                                                                    <i class="fas fa-laptop"></i> Équipement
                                                                </h6>
                                                                <p>${assignment.equipment.name}</p>
                                                                <p class="text-muted">
                                                                    ${assignment.equipment.brand} ${assignment.equipment.model}
                                                                    <br>SN: ${assignment.equipment.serialNumber}
                                                                </p>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <h6 class="fw-bold">
                                                                    <i class="fas fa-user"></i> Employé
                                                                </h6>
                                                                <p>${assignment.employee.firstName} ${assignment.employee.lastName}</p>
                                                                <p class="text-muted">
                                                                    ${assignment.employee.department}
                                                                    <br>${assignment.employee.email}
                                                                </p>
                                                            </div>
                                                        </div>

                                                        <hr>

                                                        <div class="row mb-3">
                                                            <div class="col-md-6">
                                                                <h6 class="fw-bold">
                                                                    <i class="fas fa-calendar-alt"></i> Date d'affectation
                                                                </h6>
                                                                <p>${assignment.assignmentDate}</p>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <h6 class="fw-bold">
                                                                    <i class="fas fa-calendar-check"></i> Date de retour
                                                                </h6>
                                                                <p>
                                                                    <c:choose>
                                                                        <c:when test="${assignment.returnDate != null}">
                                                                            ${assignment.returnDate}
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">Non retourné</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </p>
                                                            </div>
                                                        </div>

                                                        <h6 class="fw-bold">
                                                            <i class="fas fa-sticky-note"></i> Notes d'affectation
                                                        </h6>
                                                        <c:choose>
                                                            <c:when test="${not empty assignment.notes}">
                                                                <p class="text-muted" style="white-space: pre-wrap;">${assignment.notes}</p>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <p class="text-muted fst-italic">Aucune note</p>
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
                                    <td colspan="8" class="text-center text-muted py-5">
                                        <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                                        <h5>Aucun historique trouvé</h5>
                                        <p>Aucune affectation ne correspond à vos critères de recherche.</p>
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