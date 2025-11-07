// 📁 src/main/java/com/parcinformatique/dao/UserDAO.java
package com.parcinformatique.dao;

import com.parcinformatique.model.User;
import com.parcinformatique.model.Role;
import com.parcinformatique.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class UserDAO {

    // Trouver par username
    public User findByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    // Authentification
    public User findByUsernameAndPassword(String username, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM User WHERE username = :username AND password = :password AND active = true", User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    // Lister tous les utilisateurs
    public List<User> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM User ORDER BY username", User.class).list();
        } finally {
            session.close();
        }
    }

    // Sauvegarder
    public void save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    // Vérifier si username est unique
    public boolean isUsernameUnique(String username, Long excludeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT COUNT(u) FROM User u WHERE u.username = :username";
            if (excludeId != null) {
                hql += " AND u.id != :excludeId";
            }

            var query = session.createQuery(hql);
            query.setParameter("username", username);
            if (excludeId != null) {
                query.setParameter("excludeId", excludeId);
            }

            Long count = (Long) query.uniqueResult();
            return count == 0;
        } finally {
            session.close();
        }
    }
}