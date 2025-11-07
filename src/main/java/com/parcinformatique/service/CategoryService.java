package com.parcinformatique.service;

import com.parcinformatique.dao.CategoryDAO;
import com.parcinformatique.model.Category;

import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    // 📊 MÉTHODES MÉTIER

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public Category getCategoryById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de catégorie invalide");
        }
        return categoryDAO.findById(id);
    }

    public Category createCategory(String name, String description) {
        // Validation métier
        validateCategoryData(name);

        // Vérification unicité
        if (!categoryDAO.isNameUnique(name, null)) {
            throw new IllegalArgumentException("Une catégorie avec le nom '" + name + "' existe déjà");
        }

        Category category = new Category(name, description);
        categoryDAO.save(category);
        return category;
    }

    public Category updateCategory(Long id, String name, String description) {
        // Validation
        validateCategoryData(name);
        Category existingCategory = getCategoryById(id);

        if (existingCategory == null) {
            throw new IllegalArgumentException("Catégorie non trouvée avec l'ID: " + id);
        }

        // Vérification unicité (exclure l'actuel)
        if (!categoryDAO.isNameUnique(name, id)) {
            throw new IllegalArgumentException("Une autre catégorie avec le nom '" + name + "' existe déjà");
        }

        // Mise à jour
        existingCategory.setName(name);
        existingCategory.setDescription(description);
        categoryDAO.save(existingCategory);

        return existingCategory;
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        if (category == null) {
            throw new IllegalArgumentException("Catégorie non trouvée avec l'ID: " + id);
        }

        // 🔍  RÈGLES MÉTIER à AJOUTER PLUS TARD
        //  Vérifier qu'aucun équipement n'utilise cette catégorie
        // if (!category.getEquipments().isEmpty()) {
        //     throw new IllegalStateException("Impossible de supprimer : catégorie utilisée par des équipements");
        // }

        categoryDAO.delete(id);
    }

    // ✅ VALIDATION MÉTIER
    private void validateCategoryData(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie est obligatoire");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Le nom ne peut pas dépasser 100 caractères");
        }
        if (!name.matches("^[a-zA-Z0-9\\s\\-éèêëàâäôöûüçÉÈÊËÀÂÄÔÖÛÜÇ]+$")) {
            throw new IllegalArgumentException("Le nom contient des caractères non autorisés");
        }
    }

    // 🔍 MÉTHODES MÉTIER SPÉCIFIQUES
    public boolean isCategoryNameUnique(String name, Long excludeId) {
        return categoryDAO.isNameUnique(name, excludeId);
    }

    public List<Category> searchCategories(String keyword) {
        // Implémentation future pour la recherche
        // Pour l'instant, filtre manuellement
        List<Category> allCategories = getAllCategories();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allCategories;
        }

        String lowerKeyword = keyword.toLowerCase();
        return allCategories.stream()
                .filter(c -> c.getName().toLowerCase().contains(lowerKeyword) ||
                        (c.getDescription() != null && c.getDescription().toLowerCase().contains(lowerKeyword)))
                .toList();
    }
}