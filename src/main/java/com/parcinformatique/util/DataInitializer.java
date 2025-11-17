package com.parcinformatique.util;

import com.parcinformatique.dao.UserDAO;
import com.parcinformatique.model.User;
import com.parcinformatique.model.Role;

public class DataInitializer {

    public static void initializeTestData() {
        UserDAO userDAO = new UserDAO();

        // Vérifier si des utilisateurs existent déjà
        if (userDAO.findAll().isEmpty()) {
            // Créer un administrateur
            User admin = new User("admin", "admin123", Role.ADMIN);
            userDAO.save(admin);

            // Créer un technicien
            User tech = new User("technicien", "tech123", Role.TECHNICIAN);
            userDAO.save(tech);

            System.out.println(" Utilisateurs de test créés :");
            System.out.println("   - admin / admin123 (ADMIN)");
            System.out.println("   - technicien / tech123 (TECHNICIAN)");
        }
    }
}