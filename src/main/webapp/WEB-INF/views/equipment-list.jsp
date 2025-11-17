<%-- 📁 src/main/webapp/WEB-INF/views/equipment-list.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.parcinformatique.model.EquipmentStatus" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestion des Équipements</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
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
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-laptop"></i> Gestion des Équipements</h2>
            <div>
                <a href="${pageContext.request.contextPath}/equipments?action=available" class="btn btn-outline-success me-2">
                    <i class="fas fa-check-circle"></i> Voir disponibles
                </a>
                <a href="${pageContext.request.contextPath}/equipments?action=new" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Nouvel équipement
                </a>
            </div>
        </div>

        <!-- Messages -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show">
                ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show">
                ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Tableau des équipements -->
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-list"></i> Liste des Équipements
                    <c:if test="${showOnlyAvailable}">
                        <span class="badge bg-success">Disponibles seulement</span>
                    </c:if>
                </h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty equipmentList}">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Nom</th>
                                        <th>Marque/Modèle</th>
                                        <th>N° Série</th>
                                        <th>Catégorie</th>
                                        <th>Statut</th>
                                        <th>Date d'achat</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="equipment" items="${equipmentList}">
                                        <tr>
                                            <td><strong>${equipment.name}</strong></td>
                                            <td>${equipment.brand} ${equipment.model}</td>
                                            <td><code>${equipment.serialNumber}</code></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty equipment.category}">
                                                        <span class="badge bg-secondary">${equipment.category.name}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-light text-dark">Non catégorisé</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>

                                            <td>
                                                <c:choose>
                                                    <c:when test="${equipment.status == EquipmentStatus.AVAILABLE}">
                                                        <span class="badge bg-success">
                                                            <i class="fas fa-check-circle"></i> Disponible
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${equipment.status == EquipmentStatus.ASSIGNED}">
                                                        <span class="badge bg-primary">
                                                            <i class="fas fa-user-check"></i> Assigné
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${equipment.status == EquipmentStatus.MAINTENANCE}">
                                                        <span class="badge bg-warning text-dark">
                                                            <i class="fas fa-tools"></i> Maintenance
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${equipment.status == EquipmentStatus.OUT_OF_ORDER}">
                                                        <span class="badge bg-danger">
                                                            <i class="fas fa-times-circle"></i> Hors service
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${equipment.status == EquipmentStatus.RESERVED}">
                                                        <span class="badge bg-info text-dark">
                                                            <i class="fas fa-bookmark"></i> Réservé
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">${equipment.status}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${not empty equipment.purchaseDate}">
                                                    ${equipment.purchaseDate}
                                                </c:if>
                                            </td>
                                            <td>
                                                <a href="equipments?action=edit&id=${equipment.id}"
                                                   class="btn btn-warning btn-sm" title="Modifier">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <a href="equipments?action=delete&id=${equipment.id}"
                                                   class="btn btn-danger btn-sm"
                                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet équipement ?')"
                                                   title="Supprimer">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info text-center">
                            <i class="fas fa-info-circle"></i> Aucun équipement n'a été créé pour le moment.
                        </div>
                    </c:otherwise>
                </c:choose>
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