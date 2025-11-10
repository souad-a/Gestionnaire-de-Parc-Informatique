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
        :root {
            --sage-green: #87a96b;
            --dusty-blue: #6b8a96;
            --warm-taupe: #a96b6b;
            --soft-purple: #8a6b96;
            --sand-beige: #a9966b;
            --slate-gray: #6b7a96;
        }

        .stat-card {
            transition: transform 0.2s;
            border: none;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
            min-height: 140px;
            border-radius: 12px;
        }
        .stat-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.12);
        }
        .progress {
            height: 6px;
            border-radius: 3px;
        }
        .badge-stat {
            font-size: 0.75em;
        }
        .card-title {
            font-size: 0.9rem;
            font-weight: 600;
            letter-spacing: 0.5px;
        }
        .stat-number {
            font-size: 2rem;
            font-weight: 700;
        }
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
            margin: 1rem 0;
        }
        .stat-item {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 1.2rem;
            text-align: center;
            border-left: 4px solid var(--slate-gray);
            transition: all 0.3s ease;
        }
        .stat-item:hover {
            background: #ffffff;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .stat-item.available { border-left-color: var(--sage-green); }
        .stat-item.assigned { border-left-color: var(--dusty-blue); }
        .stat-item.maintenance { border-left-color: var(--sand-beige); }
        .stat-item.out-of-service { border-left-color: var(--warm-taupe); }

        /* Couleurs douces pour les cartes principales */
        .bg-employees {
            background: linear-gradient(135deg, var(--sage-green) 0%, #9abf88 100%) !important;
        }
        .bg-technicians {
            background: linear-gradient(135deg, var(--soft-purple) 0%, #9d88bf 100%) !important;
        }
        .bg-categories {
            background: linear-gradient(135deg, var(--dusty-blue) 0%, #88a9bf 100%) !important;
        }
        .bg-equipments {
            background: linear-gradient(135deg, var(--slate-gray) 0%, #8896bf 100%) !important;
        }

        /* Couleurs pour les boutons d'accès rapide */
        .btn-outline-sage {
            border-color: var(--sage-green);
            color: var(--sage-green);
        }
        .btn-outline-sage:hover {
            background-color: var(--sage-green);
            color: white;
        }

        .btn-outline-dusty {
            border-color: var(--dusty-blue);
            color: var(--dusty-blue);
        }
        .btn-outline-dusty:hover {
            background-color: var(--dusty-blue);
            color: white;
        }

        .btn-outline-taupe {
            border-color: var(--warm-taupe);
            color: var(--warm-taupe);
        }
        .btn-outline-taupe:hover {
            background-color: var(--warm-taupe);
            color: white;
        }

        .btn-outline-sand {
            border-color: var(--sand-beige);
            color: var(--sand-beige);
        }
        .btn-outline-sand:hover {
            background-color: var(--sand-beige);
            color: white;
        }

        /* Amélioration du header */
        .dashboard-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 12px;
            padding: 2rem;
            margin-bottom: 2rem;
        }

        /* Style pour les icônes */
        .stat-icon {
            opacity: 0.8;
            transition: opacity 0.3s ease;
        }
        .stat-card:hover .stat-icon {
            opacity: 1;
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
                <h2 class="mb-1" style="color: #2c3e50;">📊 Tableau de Bord</h2>
                <p class="text-muted mb-0">Bienvenue <strong>${sessionScope.username}</strong> - Statistiques en temps réel</p>
            </div>
            <div class="col-auto">
                <span class="badge bg-light text-dark">
                    <i class="fas fa-sync-alt"></i> Données actualisées
                </span>
            </div>
        </div>

        <!-- Cartes de statistiques principales -->
        <div class="row g-4">
            <!-- Employés -->
            <div class="col-xl-3 col-md-6">
                <div class="card stat-card text-white bg-employees">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 class="card-title mb-2"><i class="fas fa-users me-1"></i> EMPLOYÉS</h6>
                                <h2 class="stat-number mb-1">
                                    <c:out value="${not empty totalEmployees ? totalEmployees : 0}" />
                                </h2>
                                <small class="opacity-85">Enregistrés</small>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-users fa-2x stat-icon"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Techniciens -->
            <div class="col-xl-3 col-md-6">
                <div class="card stat-card text-white bg-technicians">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 class="card-title mb-2"><i class="fas fa-tools me-1"></i> TECHNICIENS</h6>
                                <h2 class="stat-number mb-1">
                                    <c:out value="${not empty totalTechnicians ? totalTechnicians : 0}" />
                                </h2>
                                <small class="opacity-85">Équipe technique</small>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-tools fa-2x stat-icon"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Catégories -->
            <div class="col-xl-3 col-md-6">
                <div class="card stat-card text-white bg-categories">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 class="card-title mb-2"><i class="fas fa-tags me-1"></i> CATÉGORIES</h6>
                                <h2 class="stat-number mb-1">
                                    <c:out value="${not empty totalCategories ? totalCategories : 0}" />
                                </h2>
                                <small class="opacity-85">Types d'équipements</small>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-tags fa-2x stat-icon"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Équipements -->
            <div class="col-xl-3 col-md-6">
                <div class="card stat-card text-white bg-equipments">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 class="card-title mb-2"><i class="fas fa-desktop me-1"></i> ÉQUIPEMENTS</h6>
                                <h2 class="stat-number mb-1">
                                    <c:out value="${not empty totalEquipments ? totalEquipments : 0}" />
                                </h2>
                                <small class="opacity-85">Total du parc</small>
                            </div>
                            <div class="align-self-center">
                                <i class="fas fa-desktop fa-2x stat-icon"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Section Statistiques Détaillées -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card border-0 shadow-sm">
                    <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
                        <h5 class="mb-0 text-dark">
                            <i class="fas fa-chart-bar me-2"></i> Statistiques Détaillées des Équipements
                        </h5>
                        <span class="badge bg-success">En temps réel</span>
                    </div>
                    <div class="card-body">
                        <!-- Grille de statistiques -->
                        <div class="stats-grid">
                            <div class="stat-item assigned">
                                <i class="fas fa-laptop fa-2x text-info mb-2"></i>
                                <h4 class="text-info">${stats.assignedCount}</h4>
                                <small class="text-muted">Assignés</small>
                            </div>
                            <div class="stat-item available">
                                <i class="fas fa-check-circle fa-2x text-success mb-2"></i>
                                <h4 class="text-success">${stats.availableCount}</h4>
                                <small class="text-muted">Disponibles</small>
                            </div>
                            <div class="stat-item">
                                <i class="fas fa-laptop text-primary fa-2x mb-2"></i>
                                <h4 class="text-primary"><c:out value="${not empty inUseEquipments ? inUseEquipments : 0}" /></h4>
                                <small class="text-muted">En utilisation</small>
                            </div>
                            <div class="stat-item maintenance">
                                <i class="fas fa-tools fa-2x text-warning mb-2"></i>
                                <h4 class="text-warning">${stats.maintenanceCount}</h4>
                                <small class="text-muted">En Maintenance</small>
                            </div>
                            <div class="stat-item out-of-service">
                                <i class="fas fa-ban fa-2x text-danger mb-2"></i>
                                <h4 class="text-danger">${stats.outOfServiceCount}</h4>
                                <small class="text-muted">Hors Service</small>
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
                        <h5 class="mb-0 text-dark">Accès Rapide</h5>
                        <span class="badge bg-primary">Gestion</span>
                    </div>
                    <div class="card-body">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <a href="${pageContext.request.contextPath}/categories"
                                   class="btn btn-outline-dusty w-100 text-start p-3">
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
                                   class="btn btn-outline-sage w-100 text-start p-3">
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
                                   class="btn btn-outline-taupe w-100 text-start p-3">
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
                                <a href="${pageContext.request.contextPath}/assignments"
                                   class="btn btn-outline-sand w-100 text-start p-3">
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
                        <c:set var="totalElements" value="${(not empty totalEquipments ? totalEquipments : 0) + (not empty totalEmployees ? totalEmployees : 0) + (not empty totalCategories ? totalCategories : 0) + (not empty totalTechnicians ? totalTechnicians : 0)}" />
                        <small class="text-muted"><c:out value="${totalElements}" /> éléments gérés</small>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Script pour actualisation automatique -->
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