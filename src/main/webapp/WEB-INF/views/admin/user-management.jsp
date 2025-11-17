<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Fonction pour formater le rôle --%>
<c:set var="roleLabels">
    {
        "ADMIN": "👑 Administrateur",
        "TECHNICIAN": "🔧 Technicien",
        "EMPLOYEE": "👨‍💼 Employé"
    }
</c:set>

<!DOCTYPE html>
<html>
<head>
    <title>Gestion des Utilisateurs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .required::after {
            content: " *";
            color: red;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <nav class="col-md-3 col-lg-2 d-md-block bg-dark sidebar collapse">
                <div class="position-sticky pt-3">
                    <h5 class="text-white px-3">Administration</h5>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/dashboard">
                                <i class="fas fa-tachometer-alt"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/employees">
                                <i class="fas fa-users"></i> Gestion Employés
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin/employees?action=users">
                                <i class="fas fa-user-cog"></i> Gestion Utilisateurs
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

            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Gestion des Utilisateurs</h1>
                </div>

                <!-- Messages -->
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Formulaire création utilisateur -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-user-plus me-2"></i>Créer un Nouvel Utilisateur
                        </h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/admin/user-management" method="post" id="userForm">
                            <input type="hidden" name="action" value="createUser">

                            <!-- Informations personnelles -->
                            <div class="row mb-4">
                                <div class="col-12">
                                    <h6 class="border-bottom pb-2">
                                        <i class="fas fa-user me-2"></i>Informations Personnelles
                                    </h6>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="fullName" class="form-label required">Nom complet</label>
                                        <input type="text" class="form-control" id="fullName" name="fullName"
                                               placeholder="Ex: Jean Dupont" required>
                                        <div class="form-text">Le nom complet de l'utilisateur</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="email" class="form-label required">Email</label>
                                        <input type="email" class="form-control" id="email" name="email"
                                               placeholder="exemple@entreprise.com" required>
                                        <div class="form-text">L'adresse email professionnelle</div>
                                    </div>
                                </div>
                            </div>

                            <!-- Informations de connexion -->
                            <div class="row mb-4">
                                <div class="col-12">
                                    <h6 class="border-bottom pb-2">
                                        <i class="fas fa-key me-2"></i>Informations de Connexion
                                    </h6>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label for="username" class="form-label required">Nom d'utilisateur</label>
                                        <input type="text" class="form-control" id="username" name="username"
                                               placeholder="Ex: j.dupont" required>
                                        <div class="form-text">Identifiant unique pour la connexion</div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label for="password" class="form-label required">Mot de passe</label>
                                        <div class="input-group">
                                            <input type="password" class="form-control" id="password" name="password"
                                                   placeholder="Minimum 6 caractères" required minlength="6">
                                            <button type="button" class="btn btn-outline-secondary"
                                                    onclick="togglePasswordVisibility()">
                                                <i class="fas fa-eye" id="passwordIcon"></i>
                                            </button>
                                        </div>
                                        <div class="form-text">Le mot de passe doit contenir au moins 6 caractères</div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label for="confirmPassword" class="form-label required">Confirmer le mot de passe</label>
                                        <input type="password" class="form-control" id="confirmPassword"
                                               placeholder="Répétez le mot de passe" required>
                                        <div class="invalid-feedback" id="passwordError">
                                            Les mots de passe ne correspondent pas
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Rôle et département -->
                            <div class="row mb-4">
                                <div class="col-12">
                                    <h6 class="border-bottom pb-2">
                                        <i class="fas fa-user-tag me-2"></i>Rôle et Département
                                    </h6>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="role" class="form-label required">Rôle</label>
                                        <select class="form-select" id="role" name="role" required>
                                            <option value="">Sélectionner un rôle</option>
                                            <option value="EMPLOYEE">EMPLOYEE</option>
                                            <option value="RTECHNICIAN">TECHNICIAN</option>
                                        </select>
                                        <div class="form-text">Définit les permissions de l'utilisateur</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="department" class="form-label">Département</label>
                                        <select class="form-select" id="department" name="department">
                                            <option value="">Sélectionner un département (optionnel)</option>
                                            <option value="IT">Informatique (IT)</option>
                                            <option value="RH">Ressources Humaines</option>
                                            <option value="FINANCE">Finance</option>
                                            <option value="MARKETING">Marketing</option>
                                            <option value="PRODUCTION">Production</option>
                                            <option value="COMMERCIAL">Commercial</option>
                                            <option value="DIRECTION">Direction</option>
                                            <option value="SUPPORT">Support Technique</option>
                                            <option value="AUTRE">Autre</option>
                                        </select>
                                        <div class="form-text">Département auquel appartient l'utilisateur</div>
                                    </div>
                                </div>
                            </div>

                            <!-- Informations supplémentaires -->
                            <div class="row">
                                <div class="col-12">
                                    <div class="mb-3 form-check">
                                        <input type="checkbox" class="form-check-input" id="sendWelcomeEmail" name="sendWelcomeEmail" checked>
                                        <label class="form-check-label" for="sendWelcomeEmail">
                                            Envoyer un email de bienvenue avec les informations de connexion
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-save me-2"></i>Créer l'utilisateur
                                </button>
                                <button type="reset" class="btn btn-outline-secondary">
                                    <i class="fas fa-undo me-2"></i>Réinitialiser
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Liste des utilisateurs -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">
                            <i class="fas fa-list me-2"></i>Liste des Utilisateurs
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Nom d'utilisateur</th>
                                        <th>Nom complet</th>
                                        <th>Email</th>
                                        <th>Rôle</th>
                                        <th>Département</th>
                                        <th>Statut</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="user" items="${users}">
                                        <tr>
                                            <td>
                                                <strong>${user.username}</strong>
                                            </td>
                                            <td>${user.fullName}</td>
                                            <td>${user.email}</td>
                                            <td>
                                                <span class="badge
                                                    <c:choose>
                                                        <c:when test="${user.role == 'ADMIN'}">bg-danger</c:when>
                                                        <c:when test="${user.role == 'TECHNICIAN'}">bg-warning</c:when>
                                                        <c:otherwise>bg-info</c:otherwise>
                                                    </c:choose>">
                                                    <c:choose>
                                                        <c:when test="${user.role == 'ADMIN'}">👑 Admin</c:when>
                                                        <c:when test="${user.role == 'TECHNICIAN'}">🔧 Technicien</c:when>
                                                        <c:when test="${user.role == 'EMPLOYEE'}">👨‍💼 Employé</c:when>
                                                        <c:otherwise>${user.role}</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty user.department}">
                                                        <span class="badge bg-secondary">${user.department}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">Non défini</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge ${user.active ? 'bg-success' : 'bg-secondary'}">
                                                    ${user.active ? 'Actif' : 'Inactif'}
                                                </span>
                                            </td>
                                            <td>
                                                <div class="btn-group btn-group-sm">
                                                    <button class="btn btn-outline-primary btn-sm"
                                                            onclick="editUser(${user.id})"
                                                            title="Modifier">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <c:if test="${user.active}">
                                                        <button class="btn btn-outline-warning btn-sm"
                                                                onclick="deactivateUser(${user.id})"
                                                                title="Désactiver">
                                                            <i class="fas fa-pause"></i>
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${not user.active}">
                                                        <button class="btn btn-outline-success btn-sm"
                                                                onclick="activateUser(${user.id})"
                                                                title="Activer">
                                                            <i class="fas fa-play"></i>
                                                        </button>
                                                    </c:if>
                                                    <button class="btn btn-outline-info btn-sm"
                                                            onclick="changePassword(${user.id})"
                                                            title="Changer mot de passe">
                                                        <i class="fas fa-key"></i>
                                                    </button>
                                                    <button class="btn btn-outline-danger btn-sm"
                                                            onclick="deleteUser(${user.id})"
                                                            title="Supprimer">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validation des mots de passe
        document.getElementById('userForm').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const passwordError = document.getElementById('passwordError');

            if (password !== confirmPassword) {
                e.preventDefault();
                passwordError.style.display = 'block';
                document.getElementById('confirmPassword').classList.add('is-invalid');
            } else {
                passwordError.style.display = 'none';
                document.getElementById('confirmPassword').classList.remove('is-invalid');
            }
        });

        // Affichage/masquage du mot de passe
        function togglePasswordVisibility() {
            const passwordInput = document.getElementById('password');
            const confirmInput = document.getElementById('confirmPassword');
            const icon = document.getElementById('passwordIcon');

            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                confirmInput.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                passwordInput.type = 'password';
                confirmInput.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        }

        // Fonctions de gestion des utilisateurs
        function editUser(userId) {
            alert('Modification de l\'utilisateur ID: ' + userId);
            // Implémenter la modification
        }

        function deactivateUser(userId) {
            if (confirm('Êtes-vous sûr de vouloir désactiver cet utilisateur ?')) {
                // Implémenter la désactivation via AJAX ou formulaire
                console.log('Désactivation user ID:', userId);
            }
        }

        function activateUser(userId) {
            if (confirm('Êtes-vous sûr de vouloir activer cet utilisateur ?')) {
                // Implémenter l'activation via AJAX ou formulaire
                console.log('Activation user ID:', userId);
            }
        }

        function changePassword(userId) {
            const newPassword = prompt('Entrez le nouveau mot de passe (min. 6 caractères):');
            if (newPassword && newPassword.length >= 6) {
                // Implémenter le changement de mot de passe
                console.log('Changement mot de passe user ID:', userId);
            } else if (newPassword) {
                alert('Le mot de passe doit contenir au moins 6 caractères');
            }
        }

        function deleteUser(userId) {
            if (confirm('Êtes-vous sûr de vouloir supprimer définitivement cet utilisateur ?')) {
                // Implémenter la suppression
                console.log('Suppression user ID:', userId);
            }
        }

        // Validation en temps réel
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const password = document.getElementById('password').value;
            const confirmPassword = this.value;
            const passwordError = document.getElementById('passwordError');

            if (confirmPassword && password !== confirmPassword) {
                passwordError.style.display = 'block';
                this.classList.add('is-invalid');
            } else {
                passwordError.style.display = 'none';
                this.classList.remove('is-invalid');
            }
        });
    </script>
</body>
</html>