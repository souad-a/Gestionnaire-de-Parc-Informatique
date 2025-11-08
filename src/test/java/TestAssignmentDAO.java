import com.parcinformatique.dao.AssignmentDAO;
import com.parcinformatique.dao.AssignmentDAOImpl;

public class TestAssignmentDAO {
    public static void main(String[] args) {
        AssignmentDAO assignmentDAO = new AssignmentDAOImpl();

        // Test 1: Récupérer toutes les affectations
        System.out.println("=== TEST findAll() ===");
        assignmentDAO.findAll().forEach(System.out::println);

        // Test 2: Vérifier méthodes de logique métier
        System.out.println("=== TEST logique métier ===");
        System.out.println("Assignments actifs: " + assignmentDAO.findActiveAssignments().size());
    }
}