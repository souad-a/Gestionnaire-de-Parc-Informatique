<%-- 📁 src/main/webapp/WEB-INF/views/equipment-form.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${empty equipment ? 'Nouvel Équipement' : 'Modifier Équipement'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                <i class="fas fa-laptop-house"></i> Gestionnaire de Parc
            </a>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h4 class="mb-0">
                            <i class="fas ${empty equipment ? 'fa-plus' : 'fa-edit'}"></i>
                            ${empty equipment ? 'Nouvel Équipement' : 'Modifier l\'Équipement'}
                        </h4>
                    </div>
                    <div class="card-body">
                        <!-- Message d'erreur -->
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger alert-dismissible fade show">
                                ${errorMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <form action="equipments" method="post">
                            <input type="hidden" name="action" value="${empty equipment ? 'create' : 'update'}">
                            <c:if test="${not empty equipment}">
                                <input type="hidden" name="id" value="${equipment.id}">
                            </c:if>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="name" class="form-label">Nom de l'équipement *</label>
                                        <input type="text" class="form-control" id="name" name="name"
                                               value="${equipment.name}" required maxlength="100">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="serialNumber" class="form-label">Numéro de série *</label>
                                        <input type="text" class="form-control" id="serialNumber" name="serialNumber"
                                               value="${equipment.serialNumber}" required maxlength="100">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="brand" class="form-label">Marque</label>
                                        <input type="text" class="form-control" id="brand" name="brand"
                                               value="${equipment.brand}" maxlength="50">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="model" class="form-label">Modèle</label>
                                        <input type="text" class="form-control" id="model" name="model"
                                               value="${equipment.model}" maxlength="50">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="categoryId" class="form-label">Catégorie</label>
                                        <select class="form-select" id="categoryId" name="categoryId">
                                            <option value="">-- Sélectionnez une catégorie --</option>
                                            <c:forEach var="category" items="${categories}">
                                                <option value="${category.id}"
                                                    ${equipment.category.id == category.id ? 'selected' : ''}>
                                                    ${category.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="status" class="form-label">Statut *</label>
                                        <select class="form-select" id="status" name="status" required>
                                            <option value="">-- Sélectionnez un statut --</option>
                                            <c:forEach var="status" items="${statusValues}">
                                                <option value="${status}"
                                                    ${equipment.status == status ? 'selected' : ''}>
                                                    ${status}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="purchaseDate" class="form-label">Date d'achat</label>
                                        <input type="date" class="form-control" id="purchaseDate" name="purchaseDate"
                                               value="${equipment.purchaseDate}">
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description"
                                          rows="3" maxlength="500">${equipment.description}</textarea>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="equipments" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left"></i> Retour à la liste
                                </a>
                                <button type="submit" class="btn btn-success">
                                    <i class="fas ${empty equipment ? 'fa-save' : 'fa-edit'}"></i>
                                    ${empty equipment ? 'Créer l\'équipement' : 'Modifier l\'équipement'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>