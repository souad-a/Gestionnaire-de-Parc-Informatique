package com.parcinformatique.service;

import com.parcinformatique.dao.UserDAO;
import com.parcinformatique.model.User;
import com.parcinformatique.model.Role;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // ✅ CRÉATION DE COMPTE
    public User createUser(String username, String password, Role role) {
        // Validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur est obligatoire");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }

        // Vérifier unicité
        if (userDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("Ce nom d'utilisateur existe déjà");
        }

        User user = new User(username, password, role);
        userDAO.save(user);
        return user;
    }

    // ✅ LISTER TOUS LES UTILISATEURS
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    // ✅ NOUVELLE MÉTHODE : Lister les utilisateurs par rôle (pour les employés)
    public List<User> getUsersByRole(Role role) {
        return userDAO.findByRole(role);
    }

    // ✅ RECHERCHER UTILISATEUR PAR ID
    public User getUserById(Long id) {
        return userDAO.findById(id);
    }

    // ✅ ACTIVER/DÉSACTIVER COMPTE
    public void activateUser(Long userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setActive(true);
            userDAO.save(user);
        }
    }

    public void deactivateUser(Long userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setActive(false);
            userDAO.save(user);
        }
    }

    // ✅ CHANGER MOT DE PASSE
    public void changePassword(Long userId, String newPassword) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setPassword(newPassword);
            userDAO.save(user);
        }
    }

    // ✅ METTRE À JOUR LE RÔLE
    public void updateUserRole(Long userId, Role newRole) {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setRole(newRole);
            userDAO.save(user);
        }
    }

    // ✅ AUTHENTIFICATION
    public User authenticate(String username, String password) {
        User user = userDAO.findByUsernameAndPassword(username, password);
        if (user != null && user.isActive()) {
            return user;
        }
        return null;
    }

    // ✅ VÉRIFIER SI USERNAME EXISTE
    public boolean isUsernameAvailable(String username) {
        return userDAO.findByUsername(username) == null;
    }

    // ✅ COMPTER UTILISATEURS PAR RÔLE
    public long countUsersByRole(Role role) {
        return userDAO.countByRole(role);
    }

}