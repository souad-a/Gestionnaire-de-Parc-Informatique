<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${pageTitle}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h4 class="mb-0"><i class="fas fa-user"></i> ${pageTitle}</h4>
                    </div>
                    <div class="card-body">
                        <%-- Message d'erreur --%>
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show">
                                <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <form action="employees" method="post">
                            <input type="hidden" name="action" value="save">
                            <c:if test="${not empty employee}">
                                <input type="hidden" name="id" value="${employee.id}">
                            </c:if>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="firstName" class="form-label">Prénom *</label>
                                    <input type="text" class="form-control" id="firstName" name="firstName"
                                           value="${employee.firstName}"
                                           required maxlength="50">
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="lastName" class="form-label">Nom *</label>
                                    <input type="text" class="form-control" id="lastName" name="lastName"
                                           value="${employee.lastName}"
                                           required maxlength="50">
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="department" class="form-label">Département *</label>
                                <select class="form-select" id="department" name="department" required>
                                    <option value="">Sélectionnez un département</option>

                                    <c:choose>
                                        <c:when test="${not empty departments}">
                                            <c:forEach var="dept" items="${departments}">
                                                <option value="${dept}" ${employee.department == dept ? 'selected' : ''}>${dept}</option>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Départements par défaut si la liste est vide -->
                                            <option value="IT" ${employee.department == 'IT' ? 'selected' : ''}>IT</option>
                                            <option value="Ressources Humaines" ${employee.department == 'Ressources Humaines' ? 'selected' : ''}>Ressources Humaines</option>
                                            <option value="Finance" ${employee.department == 'Finance' ? 'selected' : ''}>Finance</option>
                                            <option value="Marketing" ${employee.department == 'Marketing' ? 'selected' : ''}>Marketing</option>
                                            <option value="Production" ${employee.department == 'Production' ? 'selected' : ''}>Production</option>
                                            <option value="Commercial" ${employee.department == 'Commercial' ? 'selected' : ''}>Commercial</option>
                                        </c:otherwise>
                                    </c:choose>

                                    <option value="__nouveau__" style="font-weight: bold; color: #0d6efd;">➕ Ajouter un nouveau département</option>
                                </select>
                                <input type="text" class="form-control mt-2" id="newDepartment" name="newDepartment"
                                       style="display: none;" placeholder="Nom du nouveau département" maxlength="50">
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label">Email *</label>
                                <input type="email" class="form-control" id="email" name="email"
                                       value="${employee.email}"
                                       required maxlength="100">
                                <div class="form-text">Format: exemple@entreprise.com</div>
                            </div>

                            <div class="mb-3">
                                <label for="phone" class="form-label">Téléphone</label>
                                <input type="tel" class="form-control" id="phone" name="phone"
                                       value="${employee.phone}"
                                       maxlength="20">
                                <div class="form-text">Format: 0123456789 ou +33 1 23 45 67 89</div>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="employees" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left"></i> Retour à la liste
                                </a>
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-save"></i>
                                    <c:choose>
                                        <c:when test="${empty employee}">Créer l'employé</c:when>
                                        <c:otherwise>Mettre à jour</c:otherwise>
                                    </c:choose>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        // Gestion de l ajout d un nouveau département
        document.getElementById('department').addEventListener('change', function() {
            const newDeptInput = document.getElementById('newDepartment');
            if (this.value === '__nouveau__') {
                newDeptInput.style.display = 'block';
                newDeptInput.required = true;
                newDeptInput.focus();
            } else {
                newDeptInput.style.display = 'none';
                newDeptInput.required = false;
                newDeptInput.value = '';
            }
        });

        // Validation du formulaire
        document.querySelector('form').addEventListener('submit', function(e) {
            const deptSelect = document.getElementById('department');
            const newDeptInput = document.getElementById('newDepartment');

            if (deptSelect.value === '__nouveau__') {
                const newDeptValue = newDeptInput.value.trim();

                if (newDeptValue === '') {
                    e.preventDefault();
                    alert('Veuillez saisir le nom du nouveau département');
                    newDeptInput.focus();
                    return false;
                }

                // Créer une nouvelle option et la sélectionner
                const newOption = document.createElement('option');
                newOption.value = newDeptValue;
                newOption.text = newDeptValue;
                newOption.selected = true;

                // Insérer avant l'option "Ajouter nouveau"
                const addNewOption = deptSelect.options[deptSelect.options.length - 1];
                deptSelect.insertBefore(newOption, addNewOption);

                // Masquer le champ texte
                newDeptInput.style.display = 'none';

                console.log('Nouveau département ajouté:', newDeptValue);
            }

            // Vérifier que le département n'est pas vide
            if (!deptSelect.value || deptSelect.value === '__nouveau__') {
                e.preventDefault();
                alert('Veuillez sélectionner un département');
                return false;
            }
        });
    </script>
</body>
</html>