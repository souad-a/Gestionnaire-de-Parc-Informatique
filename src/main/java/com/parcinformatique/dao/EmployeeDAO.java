package com.parcinformatique.dao;

import com.parcinformatique.model.Employee;
import com.parcinformatique.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class EmployeeDAO {

    public Employee findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Employee.class, id);
        } finally {
            session.close();
        }
    }

    public List<Employee> findByDepartment(String department) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                            "FROM Employee WHERE department = :department ORDER BY lastName, firstName",
                            Employee.class
                    )
                    .setParameter("department", department)
                    .list();

        } finally {
            session.close();
        }
    }

    public List<Employee> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Employee ORDER BY lastName, firstName", Employee.class).list();
        } finally {
            session.close();
        }
    }

    public List<String> findAllDepartments() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                    "SELECT DISTINCT e.department FROM Employee e WHERE e.department IS NOT NULL ORDER BY e.department",
                    String.class
            ).list();
        } finally {
            session.close();
        }
    }

    public void save(Employee employee) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(employee);
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
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.delete(employee);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public boolean isEmailUnique(String email, Long excludeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "SELECT COUNT(e) FROM Employee e WHERE e.email = :email";
            if (excludeId != null) {
                hql += " AND e.id != :excludeId";
            }

            var query = session.createQuery(hql);
            query.setParameter("email", email);
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