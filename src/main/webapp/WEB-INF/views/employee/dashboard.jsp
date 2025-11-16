<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tableau de Bord Employé</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block bg-success sidebar collapse">
                <div class="position-sticky pt-3">
                    <h5 class="text-white px-3">Employé</h5>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link text-white active" href="${pageContext.request.contextPath}/employee/dashboard">
                                <i class="fas fa-tachometer-alt"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/assignments?action=my-assignments">
                                <i class="fas fa-laptop"></i> Mes Équipements
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/equipments?action=my-equipment">
                                <i class="fas fa-list"></i> Liste de Mes Équipements
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
                    <h1 class="h2">Tableau de Bord Employé</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <span class="me-2">Connecté en tant que: <strong>${sessionScope.username}</strong></span>
                    </div>
                </div>

                <!-- Bienvenue -->
                <div class="row">
                    <div class="col-12">
                        <div class="alert alert-info">
                            <h4 class="alert-heading">
                                <i class="fas fa-user me-2"></i>Bienvenue ${sessionScope.username}!
                            </h4>
                            <p class="mb-0">Cet espace vous permet de gérer vos équipements attribués et de déclarer d'éventuels problèmes techniques.</p>
                        </div>
                    </div>
                </div>

                <!-- Mes statistiques -->
                <div class="row">
                    <div class="col-md-6 mb-4">
                        <div class="card text-white bg-primary">
                            <div class="card-body text-center">
                                <h1 class="display-4">${myAssignments}</h1>
                                <p class="card-text">Équipements Assignés</p>
                                <a href="${pageContext.request.contextPath}/assignments?action=my-assignments"
                                   class="btn btn-light btn-sm">
                                    Voir mes équipements
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 mb-4">
                        <div class="card text-white bg-warning">
                            <div class="card-body text-center">
                                <h1 class="display-4">
                                    <i class="fas fa-tools"></i>
                                </h1>
                                <p class="card-text">Déclarer une Panne</p>
                                <a href="${pageContext.request.contextPath}/equipments?action=my-equipment"
                                   class="btn btn-light btn-sm">
                                    Déclarer un problème
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Actions rapides -->
                <div class="row mt-4">
                    <div class="col-12">
                        <h4>Actions Disponibles</h4>
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <div class="card h-100">
                                    <div class="card-body text-center">
                                        <i class="fas fa-laptop fa-3x text-primary mb-3"></i>
                                        <h5 class="card-title">Mes Équipements</h5>
                                        <p class="card-text">Consulter la liste de tous mes équipements attribués</p>
                                        <a href="${pageContext.request.contextPath}/assignments?action=my-assignments"
                                           class="btn btn-primary">
                                            Accéder
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <div class="card h-100">
                                    <div class="card-body text-center">
                                        <i class="fas fa-exclamation-triangle fa-3x text-warning mb-3"></i>
                                        <h5 class="card-title">Déclarer une Panne</h5>
                                        <p class="card-text">Signaler un problème technique sur un équipement</p>
                                        <a href="${pageContext.request.contextPath}/equipments?action=my-equipment"
                                           class="btn btn-warning">
                                            Déclarer
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <div class="card h-100">
                                    <div class="card-body text-center">
                                        <i class="fas fa-history fa-3x text-info mb-3"></i>
                                        <h5 class="card-title">Historique</h5>
                                        <p class="card-text">Voir l'historique de mes affectations</p>
                                        <a href="${pageContext.request.contextPath}/assignments?action=history"
                                           class="btn btn-info">
                                            Consulter
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Instructions -->
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title">Comment utiliser cet espace</h5>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <h6><i class="fas fa-check text-success me-2"></i>Ce que vous pouvez faire :</h6>
                                        <ul>
                                            <li>Consulter vos équipements attribués</li>
                                            <li>Déclarer des pannes techniques</li>
                                            <li>Voir l'historique de vos affectations</li>
                                            <li>Suivre le statut de vos équipements</li>
                                        </ul>
                                    </div>
                                    <div class="col-md-6">
                                        <h6><i class="fas fa-info-circle text-primary me-2"></i>En cas de problème :</h6>
                                        <ul>
                                            <li>Déclarez la panne via le formulaire dédié</li>
                                            <li>Le technicien sera automatiquement notifié</li>
                                            <li>Vous serez informé de la réparation</li>
                                            <li>Contactez l'admin pour toute autre question</li>
                                        </ul>
                                    </div>
                                </div>
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