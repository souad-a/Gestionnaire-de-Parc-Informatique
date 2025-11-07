<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${empty category ? 'Nouvelle Catégorie' : 'Modifier Catégorie'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h4 class="mb-0">
                            <i class="fas ${empty category ? 'fa-plus' : 'fa-edit'}"></i>
                            ${empty category ? 'Nouvelle Catégorie' : 'Modifier la Catégorie'}
                        </h4>
                    </div>
                    <div class="card-body">
                        <%-- Message d'erreur --%>
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show">
                                <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <form action="categories" method="post">
                            <input type="hidden" name="action" value="${empty category ? 'create' : 'update'}">
                            <c:if test="${not empty category}">
                                <input type="hidden" name="id" value="${category.id}">
                            </c:if>

                            <div class="mb-3">
                                <label for="name" class="form-label">
                                    Nom de la catégorie <span class="text-danger">*</span>
                                </label>
                                <input type="text"
                                       class="form-control ${not empty errorMessage ? 'is-invalid' : ''}"
                                       id="name"
                                       name="name"
                                       value="${category.name}"
                                       required
                                       maxlength="100"
                                       placeholder="Ex: Ordinateurs Portables, Écrans, Périphériques...">
                                <div class="form-text">
                                    Le nom doit être unique et ne peut pas dépasser 100 caractères.
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control"
                                          id="description"
                                          name="description"
                                          rows="4"
                                          maxlength="255"
                                          placeholder="Description optionnelle de la catégorie...">${category.description}</textarea>
                                <div class="form-text">
                                    Maximum 255 caractères. Cette description aide à identifier l'usage de la catégorie.
                                </div>
                            </div>

                            <div class="d-flex justify-content-between align-items-center">
                                <a href="categories" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left"></i> Retour à la liste
                                </a>
                                <div>
                                    <button type="reset" class="btn btn-outline-secondary me-2">
                                        <i class="fas fa-undo"></i> Réinitialiser
                                    </button>
                                    <button type="submit" class="btn btn-success">
                                        <i class="fas ${empty category ? 'fa-save' : 'fa-check'}"></i>
                                        ${empty category ? 'Créer la catégorie' : 'Mettre à jour'}
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <%-- Informations supplémentaires --%>
                <div class="mt-3">
                    <div class="alert alert-info">
                        <h6><i class="fas fa-info-circle"></i> Bonnes pratiques :</h6>
                        <ul class="mb-0">
                            <li>Utilisez des noms clairs et descriptifs</li>
                            <li>Évitez les doublons de noms</li>
                            <li>La description aide à clarifier l'usage de la catégorie</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <%-- Script pour améliorer l UX --%>
    <script>
        // Focus sur le champ nom au chargement
        document.addEventListener('DOMContentLoaded', function() {
            document.getElementById('name').focus();
        });

        // Validation côté client basique
        document.querySelector('form').addEventListener('submit', function(e) {
            const nameField = document.getElementById('name');
            if (nameField.value.trim().length === 0) {
                e.preventDefault();
                nameField.classList.add('is-invalid');
                alert('Le nom de la catégorie est obligatoire.');
                nameField.focus();
            }
        });
    </script>
</body>
</html>