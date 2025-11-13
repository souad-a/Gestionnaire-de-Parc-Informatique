package com.parcinformatique.dao;

import com.parcinformatique.model.Equipment;
import com.parcinformatique.model.EquipmentStatus;
import com.parcinformatique.model.Category;
import com.parcinformatique.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class EquipmentDAO {

    public Equipment findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Equipment.class, id);
        } finally {
            session.close();
        }
    }

    public List<Equipment> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                    "FROM Equipment e LEFT JOIN FETCH e.category ORDER BY e.name",
                    Equipment.class
            ).list();
        } finally {
            session.close();
        }
    }

    public List<Equipment> findByStatus(EquipmentStatus status) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Equipment> query = session.createQuery(
                    "FROM Equipment WHERE status = :status ORDER BY name", Equipment.class);
            query.setParameter("status", status);
            return query.list();
        } finally {
            session.close();
        }
    }

    public List<Equipment> findByCategory(Long categoryId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Equipment> query = session.createQuery(
                    "FROM Equipment e WHERE e.category.id = :categoryId ORDER BY e.name",
                    Equipment.class);
            query.setParameter("categoryId", categoryId);
            return query.list();
        } finally {
            session.close();
        }
    }


    public List<Equipment> findAvailableEquipment() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // ✅ CORRECTION : Ajouter LEFT JOIN FETCH pour charger les catégories
            Query<Equipment> query = session.createQuery(
                    "FROM Equipment e LEFT JOIN FETCH e.category WHERE e.status = :status ORDER BY e.name",
                    Equipment.class
            );
            query.setParameter("status", EquipmentStatus.AVAILABLE);
            return query.list();
        } finally {
            session.close();
        }
    }

    public Optional<Equipment> findBySerialNumber(String serialNumber) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Equipment> query = session.createQuery(
                    "FROM Equipment WHERE serialNumber = :serialNumber", Equipment.class);
            query.setParameter("serialNumber", serialNumber);
            return Optional.ofNullable(query.uniqueResult());
        } finally {
            session.close();
        }
    }

    public void save(Equipment equipment) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(equipment); // Utilise merge pour gérer détaché/attaché
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
            Equipment equipment = session.get(Equipment.class, id);
            if (equipment != null) {
                session.remove(equipment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean isSerialNumberUnique(String serialNumber, Long excludeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT COUNT(e) FROM Equipment e WHERE e.serialNumber = :serialNumber";
            if (excludeId != null) {
                hql += " AND e.id != :excludeId";
            }

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("serialNumber", serialNumber);
            if (excludeId != null) {
                query.setParameter("excludeId", excludeId);
            }

            Long count = query.uniqueResult();
            return count == 0;
        } finally {
            session.close();
        }
    }

    public long countByStatus(EquipmentStatus status) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(e) FROM Equipment e WHERE e.status = :status", Long.class);
            query.setParameter("status", status);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
}