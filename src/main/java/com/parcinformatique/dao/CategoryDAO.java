package com.parcinformatique.dao;

import com.parcinformatique.model.Category;
import com.parcinformatique.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class CategoryDAO {

    // Trouver par ID
    public Category findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Category.class, id);
        } finally {
            session.close();
        }
    }

    // Lister toutes les catégories
    public List<Category> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Category ORDER BY name", Category.class).list();
        } finally {
            session.close();
        }
    }

    // Sauvegarder (créer ou modifier)
    public void save(Category category) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(category);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void delete(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Category category = session.get(Category.class, id);
            if (category != null) {
                session.delete(category);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    // Vérifier si le nom est unique
    public boolean isNameUnique(String name, Long excludeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT COUNT(c) FROM Category c WHERE c.name = :name";
            if (excludeId != null) {
                hql += " AND c.id != :excludeId";// En cas de modification, vérifier qu’aucune autre catégorie ne possède
                                                // le même nom avec un ID différent.

            }

            var query = session.createQuery(hql);
            query.setParameter("name", name);
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
