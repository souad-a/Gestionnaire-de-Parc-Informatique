<%-- 📁 src/main/webapp/WEB-INF/views/dashboard.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tableau de Bord</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-laptop-house"></i> Gestionnaire de Parc
            </a>

            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3">
                    <i class="fas fa-user"></i> ${username}
                    <span class="badge bg-secondary">${role}</span>
                </span>
                <a class="nav-link" href="${pageContext.request.contextPath}/auth?action=logout">
                    <i class="fas fa-sign-out-alt"></i> Déconnexion
                </a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h2>📊 Tableau de Bord</h2>
        <p class="text-muted">Bienvenue dans votre espace de gestion</p>

        <!-- Cartes de statistiques -->
        <div class="row mt-4">
            <div class="col-md-3">
                <div class="card text-white bg-primary">
                    <div class="card-body">
                        <h5><i class="fas fa-desktop"></i> Équipements</h5>
                        <h3>0</h3>
                        <small>À gérer</small>
                    </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card text-white bg-success">
                    <div class="card-body">
                        <h5><i class="fas fa-users"></i> Employés</h5>
                        <h3>0</h3>
                        <small>Enregistrés</small>
                    </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card text-white bg-warning">
                    <div class="card-body">
                        <h5><i class="fas fa-tags"></i> Catégories</h5>
                        <h3>0</h3>
                        <small>Disponibles</small>
                    </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card text-white bg-info">
                    <div class="card-body">
                        <h5><i class="fas fa-handshake"></i> Affectations</h5>
                        <h3>0</h3>
                        <small>Actives</small>
                    </div>
                </div>
            </div>
        </div>

        <!-- Menu rapide -->
        <div class="row mt-5">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5>📋 Gestion du Parc</h5>
                    </div>
                    <div class="card-body">
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/categories" class="btn btn-outline-primary">
                                <i class="fas fa-tags"></i> Gérer les Catégories
                            </a>
                            <a href="${pageContext.request.contextPath}/employees" class="btn btn-outline-primary">
                                <i class="fas fa-users"></i> Gérer les Employés
                            </a>
                            <a href="#" class="btn btn-outline-secondary">
                                <i class="fas fa-desktop"></i> Gérer les Équipements
                            </a>
                            <a href="#" class="btn btn-outline-secondary">
                                <i class="fas fa-handshake"></i> Gérer les Affectations
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5>👤 Votre Compte</h5>
                    </div>
                    <div class="card-body">
                        <p><strong>Nom :</strong> ${username}</p>
                        <p><strong>Rôle :</strong> ${role}</p>
                        <p><strong>Session :</strong> Active</p>

                        <div class="d-grid gap-2 mt-3">
                            <a href="${pageContext.request.contextPath}/auth?action=logout"
                               class="btn btn-outline-danger">
                                <i class="fas fa-sign-out-alt"></i> Déconnexion
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>