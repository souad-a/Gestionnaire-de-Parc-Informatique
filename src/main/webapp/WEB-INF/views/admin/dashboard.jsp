<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tableau de Bord Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block bg-dark sidebar collapse">
                <div class="position-sticky pt-3">
                    <h5 class="text-white px-3">Administration</h5>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin/dashboard">
                                <i class="fas fa-tachometer-alt"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/employees">
                                <i class="fas fa-users"></i> Gestion Employés
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/employees?action=users">
                                <i class="fas fa-user-cog"></i> Gestion Utilisateurs
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/equipments">
                                <i class="fas fa-laptop"></i> Équipements
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
                    <h1 class="h2">Tableau de Bord Administrateur</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <span class="me-2">Connecté en tant que: <strong>${sessionScope.username}</strong></span>
                    </div>
                </div>

                <!-- Statistiques -->
                <div class="row">
                    <div class="col-md-3 mb-4">
                        <div class="card text-white bg-primary">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h4>${totalEquipment}</h4>
                                        <p>Équipements Total</p>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-laptop fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 mb-4">
                        <div class="card text-white bg-success">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h4>${availableEquipment}</h4>
                                        <p>Disponibles</p>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-check-circle fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 mb-4">
                        <div class="card text-white bg-warning">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h4>${assignedEquipment}</h4>
                                        <p>Assignés</p>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-user-check fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3 mb-4">
                        <div class="card text-white bg-info">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h4>${totalUsers}</h4>
                                        <p>Utilisateurs</p>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-users fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Actions rapides -->
                <div class="row mt-4">
                    <div class="col-12">
                        <h4>Actions Rapides</h4>
                        <div class="d-grid gap-2 d-md-flex">
                            <a href="${pageContext.request.contextPath}/equipments?action=new"
                               class="btn btn-primary me-2">
                                <i class="fas fa-plus"></i> Nouvel Équipement
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/employees?action=new"
                               class="btn btn-success me-2">
                                <i class="fas fa-user-plus"></i> Nouvel Employé
                            </a>
                            <a href="${pageContext.request.contextPath}/assignments?action=new"
                               class="btn btn-warning me-2">
                                <i class="fas fa-tasks"></i> Nouvelle Affectation
                            </a>
                            <a href="${pageContext.request.contextPath}/admin/employees?action=users"
                               class="btn btn-info">
                                <i class="fas fa-user-cog"></i> Gérer Utilisateurs
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Alertes -->
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Alertes et Notifications</h5>
                            </div>
                            <div class="card-body">
                                <c:if test="${maintenanceEquipment > 0}">
                                    <div class="alert alert-warning">
                                        <i class="fas fa-tools"></i>
                                        ${maintenanceEquipment} équipement(s) en maintenance
                                    </div>
                                </c:if>
                                <c:if test="${activeAssignments > 0}">
                                    <div class="alert alert-info">
                                        <i class="fas fa-tasks"></i>
                                        ${activeAssignments} affectation(s) active(s)
                                    </div>
                                </c:if>
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