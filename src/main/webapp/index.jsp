<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestionnaire de Parc Informatique</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid bg-dark text-white vh-100 d-flex align-items-center justify-content-center">
        <div class="text-center">
            <h1 class="display-4 mb-4">🖥️ Gestionnaire de Parc Informatique</h1>
            <p class="lead mb-4">Application de gestion du parc informatique d'entreprise</p>
            <div class="d-grid gap-2 d-sm-flex justify-content-sm-center">
                <a href="auth?action=login" class="btn btn-primary btn-lg px-4 gap-3">Se connecter</a>
                <a href="dashboard" class="btn btn-outline-light btn-lg px-4">Tableau de bord</a>
            </div>
            <div class="mt-5">
                <p class="text-muted">Développé avec Java EE, Hibernate et Bootstrap</p>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 
