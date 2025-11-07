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
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-users"></i> ${pageTitle}</h2>
            <a href="employees?action=new" class="btn btn-primary">
                <i class="fas fa-plus"></i> Nouvel Employé
            </a>
        </div>

        <%-- Messages --%>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fas fa-check-circle"></i> ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show">
                <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <%-- Barre de recherche et filtres --%>
        <div class="card mb-4">
            <div class="card-body">
                <form action="employees" method="get" class="row g-3">
                    <input type="hidden" name="action" value="search">
                    <div class="col-md-6">
                        <label class="form-label">Recherche</label>
                        <div class="input-group">
                            <input type="text" class="form-control" name="keyword"
                                   value="${searchKeyword}" placeholder="Nom, prénom, email...">
                            <button type="submit" class="btn btn-outline-primary">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Département</label>
                        <select name="department" class="form-select" onchange="this.form.submit()">
                            <option value="all" ${empty selectedDepartment or selectedDepartment == 'all' ? 'selected' : ''}>Tous les départements</option>
                            <c:forEach var="dept" items="${departments}">
                                <option value="${dept}" ${selectedDepartment == dept ? 'selected' : ''}>${dept}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <a href="employees" class="btn btn-outline-secondary">Réinitialiser</a>
                    </div>
                </form>
            </div>
        </div>

        <%-- Tableau des employés --%>
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0"><i class="fas fa-list"></i> Liste des Employés</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty employees}">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Nom Complet</th>
                                        <th>Département</th>
                                        <th>Email</th>
                                        <th>Téléphone</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="employee" items="${employees}">
                                        <tr>
                                            <td>${employee.id}</td>
                                            <td>
                                                <strong>${employee.fullName}</strong>
                                            </td>
                                            <td>
                                                <span class="badge bg-primary">${employee.department}</span>
                                            </td>
                                            <td>${employee.email}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty employee.phone}">
                                                        ${employee.phone}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">-</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="employees?action=edit&id=${employee.id}"
                                                   class="btn btn-warning btn-sm" title="Modifier">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <a href="employees?action=delete&id=${employee.id}"
                                                   class="btn btn-danger btn-sm"
                                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer l\'employé ${employee.fullName} ?')"
                                                   title="Supprimer">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info text-center">
                            <i class="fas fa-info-circle"></i> Aucun employé trouvé.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="mt-3">
            <a href="categories" class="btn btn-outline-primary">
                <i class="fas fa-arrow-left"></i> Gestion des Catégories
            </a>
            <a href="dashboard" class="btn btn-secondary">
                <i class="fas fa-home"></i> Tableau de Bord
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>