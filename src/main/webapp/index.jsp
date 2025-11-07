<%-- 📁 src/main/webapp/index.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionnaire de Parc Informatique</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .hero-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .feature-card {
            transition: transform 0.3s ease;
        }
        .feature-card:hover {
            transform: translateY(-5px);
        }
    </style>
</head>
<body>
    <div class="hero-section d-flex align-items-center">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 mx-auto text-center text-white">
                    <h1 class="display-3 fw-bold mb-4">
                        🖥️ Gestionnaire de Parc Informatique
                    </h1>
                    <p class="lead mb-5">
                        Solution complète pour la gestion et le suivi de votre parc informatique d'entreprise
                    </p>

                    <div class="d-grid gap-3 d-sm-flex justify-content-sm-center mb-5">
                        <a href="<%= request.getContextPath() %>/auth" class="btn btn-light btn-lg px-5 shadow">
                            🔐 Se connecter
                        </a>
                        <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-outline-light btn-lg px-5">
                            📊 Tableau de bord
                        </a>
                    </div>

                    <!-- Fonctionnalités -->
                    <div class="row mt-5 g-4">
                        <div class="col-md-4">
                            <div class="feature-card p-4 bg-white bg-opacity-10 rounded shadow">
                                <div class="fs-1 mb-3">💻</div>
                                <h5>Gestion du matériel</h5>
                                <p class="small mb-0">Suivez vos équipements informatiques en temps réel</p>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="feature-card p-4 bg-white bg-opacity-10 rounded shadow">
                                <div class="fs-1 mb-3">🔧</div>
                                <h5>Maintenance</h5>
                                <p class="small mb-0">Planifiez et suivez les interventions techniques</p>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="feature-card p-4 bg-white bg-opacity-10 rounded shadow">
                                <div class="fs-1 mb-3">📈</div>
                                <h5>Rapports</h5>
                                <p class="small mb-0">Générez des statistiques détaillées</p>
                            </div>
                        </div>
                    </div>

                    <!-- Comptes de test -->
                    <div class="mt-5 p-4 bg-white bg-opacity-10 rounded">
                        <h6 class="mb-3">🔑 Comptes de démonstration</h6>
                        <div class="row text-start">
                            <div class="col-md-6">
                                <strong>Administrateur :</strong><br>
                                <code class="text-warning">admin / admin123</code>
                            </div>
                            <div class="col-md-6">
                                <strong>Technicien :</strong><br>
                                <code class="text-warning">technicien / tech123</code>
                            </div>
                        </div>
                    </div>

                    <!-- Footer -->
                    <div class="mt-5 pt-4 border-top border-white border-opacity-25">
                        <p class="text-white-50 small mb-0">
                            Développé avec Java EE, Hibernate, MySQL et Bootstrap 5
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>