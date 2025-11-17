package com.parcinformatique.dao;

import com.parcinformatique.model.User;
import com.parcinformatique.model.Role;
import com.parcinformatique.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private EntityManager entityManager;

    public UserDAO() {
        this.entityManager = Persistence.createEntityManagerFactory("votre-persistence-unit")
                .createEntityManager();
    }

    // ✅ TROUVER PAR USERNAME ET PASSWORD
    public User findByUsernameAndPassword(String username, String password) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
                    User.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // ✅ TROUVER PAR USERNAME
    public User findByUsername(String username) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username",
                    User.class
            );
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // ✅ TROUVER PAR ID
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    // ✅ LISTER TOUS LES UTILISATEURS
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u ORDER BY u.username",
                User.class
        );
        return query.getResultList();
    }

    // ✅ SAUVEGARDER (CREATE/UPDATE)
    public void save(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (user.getId() == null) {
                entityManager.persist(user);
            } else {
                entityManager.merge(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // ✅ SUPPRIMER
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // ✅ COMPTER PAR RÔLE
    public long countByRole(Role role) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.role = :role",
                Long.class
        );
        query.setParameter("role", role);
        return query.getSingleResult();
    }

    // ✅ LISTER UTILISATEURS ACTIFS
    public List<User> findActiveUsers() {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.active = true ORDER BY u.username",
                User.class
        );
        return query.getResultList();
    }
    /*public List<User> findByRole(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User u WHERE u.role = :role AND u.active = true";
            return session.createQuery(hql, User.class)
                    .setParameter("role", role)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }*/
    public List<User> findByRole(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User u WHERE u.role = :role AND u.active = true ORDER BY u.fullName";
            return session.createQuery(hql, User.class)
                    .setParameter("role", role)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}