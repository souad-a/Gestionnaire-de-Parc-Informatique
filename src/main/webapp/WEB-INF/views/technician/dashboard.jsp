<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tableau de Bord Technicien</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block bg-primary sidebar collapse">
                <div class="position-sticky pt-3">
                    <h5 class="text-white px-3">Technicien</h5>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link text-white active" href="${pageContext.request.contextPath}/technician/dashboard">
                                <i class="fas fa-tachometer-alt"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/equipments?action=technician-view">
                                <i class="fas fa-laptop-medical"></i> Équipements à Réparer
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/equipments">
                                <i class="fas fa-laptop"></i> Tous les Équipements
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/assignments">
                                <i class="fas fa-tasks"></i> Affectations
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/auth?action=logout">
                                <i class="fas fa-sign-out-alt"></i> Déconnexion
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Tableau de Bord Technicien</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <span class="me-2">Connecté en tant que: <strong>${sessionScope.username}</strong></span>
                    </div>
                </div>

                <!-- Statistiques -->
                <div class="row">
                    <div class="col-md-4 mb-4">
                        <div class="card text-white bg-danger">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h4>${equipmentWithIssues}</h4>
                                        <p>Équipements en Panne</p>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-exclamation-triangle fa-2x"></i>
                                    </div>
                                </div>
                                <a href="${pageContext.request.contextPath}/equipments?action=technician-view"
                                   class="text-white text-decoration-none">
                                    Voir les détails <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-4">
                        <div class="card text-white bg-warning">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h4>${maintenanceEquipment}</h4>
                                        <p>En Maintenance</p>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-tools fa-2x"></i>
                                    </div>
                                </div>
                                <a href="${pageContext.request.contextPath}/equipments?action=technician-view"
                                   class="text-white text-decoration-none">
                                    Voir les détails <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 mb-4">
                        <div class="card text-white bg-success">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h4>${totalEquipment}</h4>
                                        <p>Total Équipements</p>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-laptop fa-2x"></i>
                                    </div>
                                </div>
                                <a href="${pageContext.request.contextPath}/equipments"
                                   class="text-white text-decoration-none">
                                    Voir tous <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Actions rapides -->
                <div class="row mt-4">
                    <div class="col-12">
                        <h4>Interventions Requises</h4>
                        <c:choose>
                            <c:when test="${equipmentWithIssues > 0}">
                                <div class="alert alert-danger">
                                    <i class="fas fa-exclamation-circle"></i>
                                    <strong>Attention!</strong> Vous avez ${equipmentWithIssues} équipement(s) en panne nécessitant une intervention.
                                </div>
                                <a href="${pageContext.request.contextPath}/equipments?action=technician-view"
                                   class="btn btn-danger btn-lg">
                                    <i class="fas fa-tools"></i> Gérer les Pannes
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-success">
                                    <i class="fas fa-check-circle"></i>
                                    <strong>Excellent!</strong> Aucun équipement en panne pour le moment.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Instructions -->
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Guide du Technicien</h5>
                            </div>
                            <div class="card-body">
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item">
                                        <i class="fas fa-check text-success me-2"></i>
                                        Consulter la liste des équipements en panne
                                    </li>
                                    <li class="list-group-item">
                                        <i class="fas fa-check text-success me-2"></i>
                                        Mettre à jour le statut des équipements après réparation
                                    </li>
                                    <li class="list-group-item">
                                        <i class="fas fa-check text-success me-2"></i>
                                        Suivre l'historique des affectations
                                    </li>
                                    <li class="list-group-item">
                                        <i class="fas fa-check text-success me-2"></i>
                                        Marquer les équipements comme disponibles après réparation
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>