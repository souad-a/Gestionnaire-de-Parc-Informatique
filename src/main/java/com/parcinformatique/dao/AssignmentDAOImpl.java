package com.parcinformatique.dao;

import com.parcinformatique.model.Assignment;
import com.parcinformatique.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class AssignmentDAOImpl implements AssignmentDAO {

    @Override
    public Assignment findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Assignment.class, id);
        }
    }

    @Override
    public List<Assignment> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Assignment a ORDER BY a.assignmentDate DESC", Assignment.class)
                    .getResultList();
        }
    }

    @Override
    public void save(Assignment assignment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(assignment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erreur lors de la sauvegarde de l'affectation", e);
        }
    }

    @Override
    public void update(Assignment assignment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(assignment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erreur lors de la mise à jour de l'affectation", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Assignment assignment = session.get(Assignment.class, id);
            if (assignment != null) {
                session.remove(assignment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erreur lors de la suppression de l'affectation", e);
        }
    }

    @Override
    public List<Assignment> findByEmployee(Long employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Assignment> query = session.createQuery(
                    "FROM Assignment a WHERE a.employee.id = :employeeId ORDER BY a.assignmentDate DESC",
                    Assignment.class
            );
            query.setParameter("employeeId", employeeId);
            return query.getResultList();
        }
    }

    @Override
    public List<Assignment> findByEquipment(Long equipmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Assignment> query = session.createQuery(
                    "FROM Assignment a WHERE a.equipment.id = :equipmentId ORDER BY a.assignmentDate DESC",
                    Assignment.class
            );
            query.setParameter("equipmentId", equipmentId);
            return query.getResultList();
        }
    }

    @Override
    public List<Assignment> findActiveAssignments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Assignment> query = session.createQuery(
                    "FROM Assignment a WHERE a.status = 'ACTIVE' ORDER BY a.assignmentDate DESC",
                    Assignment.class
            );
            return query.getResultList();
        }
    }

    //  LOGIQUE MÉTIER IMPORTANTE - Vérification disponibilité équipement
    @Override
    public boolean isEquipmentAvailable(Long equipmentId, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(a) FROM Assignment a WHERE a.equipment.id = :equipmentId " +
                            "AND a.status = 'ACTIVE' AND a.assignmentDate <= :date " +
                            "AND (a.returnDate IS NULL OR a.returnDate > :date)",
                    Long.class
            );
            query.setParameter("equipmentId", equipmentId);
            query.setParameter("date", date);
            Long count = query.uniqueResult();
            return count == 0; // Disponible si aucun assignment actif
        }
    }

    public List<Assignment> findByEquipmentAndEmployee(Long equipmentId, Long employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Assignment> query = session.createQuery(
                    "FROM Assignment a WHERE a.equipment.id = :equipmentId AND a.employee.id = :employeeId " +
                            "ORDER BY a.assignmentDate DESC",
                    Assignment.class
            );
            query.setParameter("equipmentId", equipmentId);
            query.setParameter("employeeId", employeeId);
            return query.getResultList();
        }
    }

    public boolean hasAnyAssignment(Long employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(a) FROM Assignment a WHERE a.employee.id = :employeeId",
                            Long.class
                    ).setParameter("employeeId", employeeId)
                    .uniqueResult();

            return count != null && count > 0;
        }
    }

}