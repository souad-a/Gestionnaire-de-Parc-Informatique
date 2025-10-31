// 📁 src/main/java/com/parcinformatique/util/TestHibernate.java

import com.parcinformatique.model.Category;
import com.parcinformatique.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TestHibernate {
    public static void main(String[] args) {
        Session session = null;
        Transaction transaction = null;

        try {
            // 1. Obtenir une session
            session = HibernateUtil.getSessionFactory().openSession();
            System.out.println("✅ Session Hibernate ouverte !");

            // 2. Démarrer une transaction
            transaction = session.beginTransaction();

            // 3. Créer et sauvegarder une catégorie de test
            Category testCategory = new Category("Ordinateurs Portables", "Laptops et ultrabooks");
            session.save(testCategory);
            System.out.println("✅ Catégorie sauvegardée : " + testCategory);

            // 4. Commit de la transaction
            transaction.commit();
            System.out.println("✅ Transaction commitée !");

            // 5. Vérifier en relisant
            Category savedCategory = session.get(Category.class, testCategory.getId());
            System.out.println("✅ Catégorie relue depuis la BDD : " + savedCategory);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ ERREUR : " + e.getMessage());
            e.printStackTrace();

        } finally {
            if (session != null) {
                session.close();
            }
        }

        // Fermer Hibernate
        HibernateUtil.shutdown();
    }
}