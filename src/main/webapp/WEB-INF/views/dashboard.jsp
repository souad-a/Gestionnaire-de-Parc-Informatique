<%-- 📁 src/main/webapp/WEB-INF/views/dashboard.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tableau de Bord - Gestionnaire de Parc</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .stat-card {
            transition: transform 0.2s;
            border: none;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            min-height: 140px;
        }
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(0,0,0,0.2);
        }
        .progress {
            height: 8px;
            border-radius: 4px;
        }
        .badge-stat {
            font-size: 0.75em;
        }
        .card-title {
            font-size: 0.9rem;
            font-weight: 600;
        }
        .stat-number {
            font-size: 2rem;
            font-weight: 700;
        }
    </style>
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
                    <i class="fas fa-user"></i> ${sessionScope.username}
                    <span class="badge bg-${sessionScope.role == 'ADMIN' ? 'warning' : 'info'}">${sessionScope.role}</span>
                </span>
                <a class="nav-link" href="${pageContext.request.contextPath}/auth?action=logout">
                    <i class="fas fa-sign-out-alt"></i> Déconnexion
                </a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <!-- En-tête -->
        <div class="row align-items-center mb-4">
            <div class="col">
                <h2 class="mb-1">📊 Tableau de Bord</h2>
                <p class="text-muted mb-0">Bienvenue <strong>${sessionScope.username}</strong> - Statistiques en temps réel</p>
            </div>
            <div class="col-auto">
                <span class="badge bg-light text-dark">
                    <i class="fas fa-sync-alt"></i> Données actualisées
                </span>
            </div>
        </div>

        <!-- Cartes de statistiques principales AVEC VALEURS PAR DÉFAUT -->
        <div class="row g-4">
            <!-- Équipements -->
            <div class="col-xl-3 col-md-6">
                <div class="card stat-card text-white bg-primary">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 class="card-title mb-2"><i class="fas fa-desktop me-1"></i> ÉQUIPEMENTS</h6>
                                <h2 class="stat-number mb-1">
                                    <c:out value="${not empty totalEquipments ? totalEquipments : 0}" />
                                </h2>
                                <small class="opacity-75">Total du parc</small>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-desktop fa-2x opacity-50"></i>
                            </div>
                        </div>
                        <div class="mt-3">
                            <div class="d-flex justify-content-between small mb-1">
                                <span>Disponibles: <strong><c:out value="${not empty availableEquipments ? availableEquipments : 0}" /></strong></span>
                                <span>Utilisés: <strong><c:out value="${not empty inUseEquipments ? inUseEquipments : 0}" /></strong></span>
                            </div>
                            <div class="progress mt-1 bg-dark bg-opacity-25">
                                <c:set var="availablePercent" value="${(not empty totalEquipments and totalEquipments > 0) ? (availableEquipments * 100 / totalEquipments) : 0}" />
                                <div class="progress-bar bg-success" style="width: ${availablePercent}%"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Employés -->
            <div class="col-xl-3 col-md-6">
                <div class="card stat-card text-white bg-success">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 class="card-title mb-2"><i class="fas fa-users me-1"></i> EMPLOYÉS</h6>
                                <h2 class="stat-number mb-1">
                                    <c:out value="${not empty totalEmployees ? totalEmployees : 0}" />
                                </h2>
                                <small class="opacity-75">Enregistrés</small>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-users fa-2x opacity-50"></i>
                            </div>
                        </div>
                        <div class="mt-3">
                            <c:if test="${not empty totalEmployees and totalEmployees > 0}">
                                <c:set var="ratio" value="${(not empty totalEquipments ? totalEquipments : 0) / totalEmployees}" />
                                <span class="badge bg-light text-dark badge-stat">
                                    Ratio: <fmt:formatNumber value="${ratio}" pattern="#.##" /> eq/emp
                                </span>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Catégories -->
            <div class="col-xl-3 col-md-6">
                <div class="card stat-card text-white bg-warning">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 class="card-title mb-2"><i class="fas fa-tags me-1"></i> CATÉGORIES</h6>
                                <h2 class="stat-number mb-1">
                                    <c:out value="${not empty totalCategories ? totalCategories : 0}" />
                                </h2>
                                <small class="opacity-75">Types d'équipements</small>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-tags fa-2x opacity-50"></i>
                            </div>
                        </div>
                        <div class="mt-3">
                            <c:if test="${(not empty totalCategories and totalCategories > 0) and (not empty totalEquipments and totalEquipments > 0)}">
                                <c:set var="moyenne" value="${totalEquipments / totalCategories}" />
                                <span class="badge bg-light text-dark badge-stat">
                                    Moy: <fmt:formatNumber value="${moyenne}" pattern="#.##" /> eq/cat
                                </span>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Taux d'utilisation -->
            <div class="col-xl-3 col-md-6">
                <div class="card stat-card text-white bg-info">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 class="card-title mb-2"><i class="fas fa-chart-line me-1"></i> UTILISATION</h6>
                                <h2 class="stat-number mb-1">
                                    <c:out value="${not empty utilizationRate ? utilizationRate : 0}" />%
                                </h2>
                                <small class="opacity-75">Taux d'utilisation</small>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-chart-line fa-2x opacity-50"></i>
                            </div>
                        </div>
                        <div class="mt-3">
                            <div class="progress bg-dark bg-opacity-25">
                                <div class="progress-bar bg-warning"
                                     style="width: <c:out value="${not empty utilizationRate ? utilizationRate : 0}" />%">
                                </div>
                            </div>
                            <small class="d-block mt-1 opacity-75">
                                Maintenance: <c:out value="${not empty maintenanceRate ? maintenanceRate : 0}" />%
                            </small>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Détails des équipements par statut -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card border-0 shadow-sm">
                    <div class="card-header bg-white border-0">
                        <h5 class="mb-0 text-dark"><i class="fas fa-chart-pie me-2"></i> Répartition des Équipements</h5>
                    </div>
                    <div class="card-body">
                        <div class="row text-center">
                            <div class="col-md-3">
                                <div class="border rounded p-3 bg-success bg-opacity-10">
                                    <i class="fas fa-check-circle text-success fa-2x mb-2"></i>
                                    <h4 class="text-success"><c:out value="${not empty availableEquipments ? availableEquipments : 0}" /></h4>
                                    <small class="text-muted">Disponibles</small>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="border rounded p-3 bg-primary bg-opacity-10">
                                    <i class="fas fa-laptop text-primary fa-2x mb-2"></i>
                                    <h4 class="text-primary"><c:out value="${not empty inUseEquipments ? inUseEquipments : 0}" /></h4>
                                    <small class="text-muted">En utilisation</small>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="border rounded p-3 bg-warning bg-opacity-10">
                                    <i class="fas fa-tools text-warning fa-2x mb-2"></i>
                                    <h4 class="text-warning"><c:out value="${not empty maintenanceEquipments ? maintenanceEquipments : 0}" /></h4>
                                    <small class="text-muted">En maintenance</small>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="border rounded p-3 bg-danger bg-opacity-10">
                                    <c:set var="horsService" value="${(not empty totalEquipments ? totalEquipments : 0) - (not empty availableEquipments ? availableEquipments : 0) - (not empty inUseEquipments ? inUseEquipments : 0) - (not empty maintenanceEquipments ? maintenanceEquipments : 0)}" />
                                    <i class="fas fa-ban text-danger fa-2x mb-2"></i>
                                    <h4 class="text-danger"><c:out value="${horsService}" /></h4>
                                    <small class="text-muted">Hors service</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Menu rapide et informations -->
        <div class="row mt-4 g-4">
            <!-- Menu rapide -->
            <div class="col-lg-8">
                <div class="card border-0 shadow-sm">
                    <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
                        <h5 class="mb-0 text-dark">🚀 Accès Rapide</h5>
                        <span class="badge bg-primary">Gestion</span>
                    </div>
                    <div class="card-body">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <a href="${pageContext.request.contextPath}/categories"
                                   class="btn btn-outline-primary w-100 text-start p-3">
                                    <i class="fas fa-tags me-2 fa-lg"></i>
                                    <div>
                                        <strong>Gérer les Catégories</strong>
                                        <br>
                                        <small class="text-muted">
                                            <c:out value="${not empty totalCategories ? totalCategories : 0}" /> catégories
                                        </small>
                                    </div>
                                </a>
                            </div>
                            <div class="col-md-6">
                                <a href="${pageContext.request.contextPath}/employees"
                                   class="btn btn-outline-success w-100 text-start p-3">
                                    <i class="fas fa-users me-2 fa-lg"></i>
                                    <div>
                                        <strong>Gérer les Employés</strong>
                                        <br>
                                        <small class="text-muted">
                                            <c:out value="${not empty totalEmployees ? totalEmployees : 0}" /> employés
                                        </small>
                                    </div>
                                </a>
                            </div>
                            <div class="col-md-6">
                                <a href="${pageContext.request.contextPath}/equipments"
                                   class="btn btn-outline-info w-100 text-start p-3">
                                    <i class="fas fa-desktop me-2 fa-lg"></i>
                                    <div>
                                        <strong>Gérer les Équipements</strong>
                                        <br>
                                        <small class="text-muted">
                                            <c:out value="${not empty totalEquipments ? totalEquipments : 0}" /> équipements
                                        </small>
                                    </div>
                                </a>
                            </div>
                            <div class="col-md-6">
                                <a href="${pageContext.request.contextPath}/affectations"
                                   class="btn btn-outline-warning w-100 text-start p-3">
                                    <i class="fas fa-handshake me-2 fa-lg"></i>
                                    <div>
                                        <strong>Gérer les Affectations</strong>
                                        <br>
                                        <small class="text-muted">
                                            <c:out value="${not empty inUseEquipments ? inUseEquipments : 0}" /> actives
                                        </small>
                                    </div>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Informations utilisateur -->
            <div class="col-lg-4">
                <div class="card border-0 shadow-sm">
                    <div class="card-header bg-white border-0">
                        <h5 class="mb-0 text-dark">👤 Votre Session</h5>
                    </div>
                    <div class="card-body">
                        <div class="d-flex align-items-center mb-3">
                            <div class="flex-shrink-0">
                                <i class="fas fa-user-circle fa-2x text-primary"></i>
                            </div>
                            <div class="flex-grow-1 ms-3">
                                <h6 class="mb-0">${sessionScope.username}</h6>
                                <span class="badge bg-${sessionScope.role == 'ADMIN' ? 'warning' : 'info'}">
                                    ${sessionScope.role}
                                </span>
                            </div>
                        </div>

                        <div class="mb-3">
                            <small class="text-muted">Dernière connexion</small>
                            <div>Maintenant</div>
                        </div>

                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/auth?action=logout"
                               class="btn btn-outline-danger btn-sm">
                                <i class="fas fa-sign-out-alt me-1"></i> Déconnexion
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Carte d'information système -->
                <div class="card mt-3 bg-light border-0">
                    <div class="card-body text-center py-3">
                        <i class="fas fa-database text-primary mb-2 fa-lg"></i>
                        <h6 class="mb-1">Système Actif</h6>
                        <c:set var="totalElements" value="${(not empty totalEquipments ? totalEquipments : 0) + (not empty totalEmployees ? totalEmployees : 0) + (not empty totalCategories ? totalCategories : 0)}" />
                        <small class="text-muted"><c:out value="${totalElements}" /> éléments gérés</small>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Script pour actualisation automatique (optionnel) -->
    <script>
        // Actualiser les données toutes les 30 secondes
        setTimeout(() => {
            window.location.reload();
        }, 30000);

        // Animation au chargement
        document.addEventListener('DOMContentLoaded', function() {
            const cards = document.querySelectorAll('.stat-card');
            cards.forEach((card, index) => {
                card.style.opacity = '0';
                card.style.transform = 'translateY(20px)';

                setTimeout(() => {
                    card.style.transition = 'all 0.5s ease';
                    card.style.opacity = '1';
                    card.style.transform = 'translateY(0)';
                }, index * 100);
            });
        });
    </script>
</body>
</html>