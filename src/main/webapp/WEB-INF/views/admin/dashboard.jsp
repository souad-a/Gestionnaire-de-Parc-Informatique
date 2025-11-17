<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tableau de Bord Admin</title>
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
        .bg-equipments-total {
            background: linear-gradient(135deg, var(--slate-gray) 0%, #8896bf 100%) !important;
        }
        .bg-equipments-available {
            background: linear-gradient(135deg, var(--sage-green) 0%, #9abf88 100%) !important;
        }
        .bg-equipments-assigned {
            background: linear-gradient(135deg, var(--soft-purple) 0%, #9d88bf 100%) !important;
        }
        .bg-users {
            background: linear-gradient(135deg, var(--dusty-blue) 0%, #88a9bf 100%) !important;
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

        /* Sidebar améliorée */
        .sidebar {
            background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%) !important;
        }
        .sidebar .nav-link {
            border-radius: 8px;
            margin: 2px 0;
            transition: all 0.3s ease;
        }
        .sidebar .nav-link:hover {
            background: rgba(255,255,255,0.1);
            transform: translateX(5px);
        }
        .sidebar .nav-link.active {
            background: var(--sage-green);
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse" style="background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%);">
                <div class="position-sticky pt-3">
                    <h5 class="text-white px-3 mb-4">
                        <i class="fas fa-cogs me-2"></i>Administration
                    </h5>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin/dashboard">
                                <i class="fas fa-tachometer-alt me-2"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/employees">
                                <i class="fas fa-users me-2"></i> Gestion Employés
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/employees?action=users">
                                <i class="fas fa-user-cog me-2"></i> Gestion Utilisateurs
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/equipments">
                                <i class="fas fa-laptop me-2"></i> Équipements
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/assignments">
                                <i class="fas fa-tasks me-2"></i> Affectations
                            </a>
                        </li>
                        <li class="nav-item mt-4">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/auth?action=logout">
                                <i class="fas fa-sign-out-alt me-2"></i> Déconnexion
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <!-- En-tête -->
                <div class="row align-items-center mb-4 mt-3">
                    <div class="col">
                        <h2 class="mb-1" style="color: #2c3e50;">📊 Tableau de Bord Administrateur</h2>
                        <p class="text-muted mb-0">Bienvenue <strong>${sessionScope.username}</strong> - Gestion complète du parc</p>
                    </div>
                    <div class="col-auto">
                        <span class="badge bg-light text-dark">
                            <i class="fas fa-user-shield"></i> Administrateur
                        </span>
                    </div>
                </div>

                <!-- Cartes de statistiques principales -->
                <div class="row g-4">
                    <!-- Équipements Total -->
                    <div class="col-xl-3 col-md-6">
                        <div class="card stat-card text-white bg-equipments-total">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div>
                                        <h6 class="card-title mb-2"><i class="fas fa-laptop me-1"></i> ÉQUIPEMENTS TOTAL</h6>
                                        <h2 class="stat-number mb-1">${totalEquipment}</h2>
                                        <small class="opacity-85">Parc complet</small>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-laptop fa-2x stat-icon"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Équipements Disponibles -->
                    <div class="col-xl-3 col-md-6">
                        <div class="card stat-card text-white bg-equipments-available">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div>
                                        <h6 class="card-title mb-2"><i class="fas fa-check-circle me-1"></i> DISPONIBLES</h6>
                                        <h2 class="stat-number mb-1">${availableEquipment}</h2>
                                        <small class="opacity-85">Prêts à l'emploi</small>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-check-circle fa-2x stat-icon"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Équipements Assignés -->
                    <div class="col-xl-3 col-md-6">
                        <div class="card stat-card text-white bg-equipments-assigned">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div>
                                        <h6 class="card-title mb-2"><i class="fas fa-user-check me-1"></i> ASSIGNÉS</h6>
                                        <h2 class="stat-number mb-1">${assignedEquipment}</h2>
                                        <small class="opacity-85">En utilisation</small>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-user-check fa-2x stat-icon"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Utilisateurs -->
                    <div class="col-xl-3 col-md-6">
                        <div class="card stat-card text-white bg-users">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div>
                                        <h6 class="card-title mb-2"><i class="fas fa-users me-1"></i> UTILISATEURS</h6>
                                        <h2 class="stat-number mb-1">${totalUsers}</h2>
                                        <small class="opacity-85">Comptes actifs</small>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-users fa-2x stat-icon"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Actions rapides -->
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card border-0 shadow-sm">
                            <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
                                <h5 class="mb-0 text-dark">Accès Rapide</h5>
                                <span class="badge bg-primary">Administration</span>
                            </div>
                            <div class="card-body">
                                <div class="row g-3">
                                    <div class="col-md-3">
                                        <a href="${pageContext.request.contextPath}/equipments?action=new"
                                           class="btn btn-outline-taupe w-100 text-start p-3">
                                            <i class="fas fa-plus me-2 fa-lg"></i>
                                            <div>
                                                <strong>Nouvel Équipement</strong>
                                                <br>
                                                <small class="text-muted">Ajouter un équipement</small>
                                            </div>
                                        </a>
                                    </div>
                                   <div class="col-md-3">
                                       <a href="${pageContext.request.contextPath}/categories?action=new"
                                          class="btn btn-outline-sage w-100 text-start p-3">
                                           <i class="fas fa-tags me-2 fa-lg"></i>
                                           <div>
                                               <strong>Nouvelle Catégorie</strong>
                                               <br>
                                               <small class="text-muted">Créer une catégorie</small>
                                           </div>
                                       </a>
                                   </div>
                                    <div class="col-md-3">
                                        <a href="${pageContext.request.contextPath}/assignments?action=new"
                                           class="btn btn-outline-sand w-100 text-start p-3">
                                            <i class="fas fa-tasks me-2 fa-lg"></i>
                                            <div>
                                                <strong>Nouvelle Affectation</strong>
                                                <br>
                                                <small class="text-muted">Assigner un équipement</small>
                                            </div>
                                        </a>
                                    </div>
                                    <div class="col-md-3">
                                        <a href="${pageContext.request.contextPath}/admin/employees?action=users"
                                           class="btn btn-outline-dusty w-100 text-start p-3">
                                            <i class="fas fa-user-cog me-2 fa-lg"></i>
                                            <div>
                                                <strong>Gérer Utilisateurs</strong>
                                                <br>
                                                <small class="text-muted">${totalUsers} utilisateurs</small>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Alertes et Informations -->
                <div class="row mt-4 g-4">
                    <!-- Alertes -->
                    <div class="col-lg-8">
                        <div class="card border-0 shadow-sm">
                            <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
                                <h5 class="mb-0 text-dark">
                                    <i class="fas fa-bell me-2"></i> Alertes et Notifications
                                </h5>
                                <span class="badge bg-warning">Surveillance</span>
                            </div>
                            <div class="card-body">
                                <c:if test="${maintenanceEquipment > 0}">
                                    <div class="alert alert-warning d-flex align-items-center">
                                        <i class="fas fa-tools fa-lg me-3"></i>
                                        <div>
                                            <strong>Maintenance requise</strong><br>
                                            <small>${maintenanceEquipment} équipement(s) en maintenance</small>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${activeAssignments > 0}">
                                    <div class="alert alert-info d-flex align-items-center">
                                        <i class="fas fa-tasks fa-lg me-3"></i>
                                        <div>
                                            <strong>Affectations actives</strong><br>
                                            <small>${activeAssignments} affectation(s) en cours</small>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${maintenanceEquipment == 0 && activeAssignments == 0}">
                                    <div class="alert alert-success d-flex align-items-center">
                                        <i class="fas fa-check-circle fa-lg me-3"></i>
                                        <div>
                                            <strong>Tout est en ordre</strong><br>
                                            <small>Aucune alerte en ce moment</small>
                                        </div>
                                    </div>
                                </c:if>
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
                                        <i class="fas fa-user-shield fa-2x text-primary"></i>
                                    </div>
                                    <div class="flex-grow-1 ms-3">
                                        <h6 class="mb-0">${sessionScope.username}</h6>
                                        <span class="badge bg-warning">ADMINISTRATEUR</span>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <small class="text-muted">Dernière activité</small>
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
                                <i class="fas fa-cogs text-primary mb-2 fa-lg"></i>
                                <h6 class="mb-1">Système Admin Actif</h6>
                                <c:set var="totalElements" value="${totalEquipment + totalUsers}" />
                                <small class="text-muted"><c:out value="${totalElements}" /> éléments gérés</small>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Script pour l'animation -->
    <script>
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