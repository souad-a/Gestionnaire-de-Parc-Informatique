import com.parcinformatique.dao.CategoryDAO;
import com.parcinformatique.dao.EmployeeDAO;
import com.parcinformatique.model.Category;
import com.parcinformatique.model.Employee;

import java.util.List;

public class TestDAOs {
    public static void main(String[] args) {
        CategoryDAO categoryDAO = new CategoryDAO();
        EmployeeDAO employeeDAO = new EmployeeDAO();

        // 1. Ajouter des catégories
        Category[] categoriesToAdd = {
                new Category("Réseau", "Routeurs et Switchs"),
                new Category("Informatique", "PC, Laptops, Imprimantes"),
                new Category("Sécurité", "Caméras, Alarmes"),
                new Category("Logiciels", "Systèmes, Antivirus")
        };

        for (Category cat : categoriesToAdd) {
            if (categoryDAO.isNameUnique(cat.getName(), null)) {
                categoryDAO.save(cat);
                System.out.println(" Catégorie ajoutée : " + cat.getName());
            } else {
                System.out.println(" Catégorie déjà existante : " + cat.getName());
            }
        }

        // 2. Ajouter des employés (département AVANT email)
        Employee[] employeesToAdd = {
                new Employee("Alice", "Martin", "Informatique", "alice.martin@example.com", "0612345678"),
                new Employee("Bob", "Durand", "Réseau", "bob.durand@example.com", "0698765432"),
                new Employee("Charlie", "Dupont", "Sécurité", "charlie.dupont@example.com", "0622334455"),
                new Employee("David", "Bernard", "Logiciels", "david.bernard@example.com", "0655443322")
        };

        for (Employee emp : employeesToAdd) {
            if (employeeDAO.isEmailUnique(emp.getEmail(), null)) {
                employeeDAO.save(emp);
                System.out.println("Employé ajouté : " + emp.getFirstName() + " " + emp.getLastName());
            } else {
                System.out.println(" Email déjà utilisé : " + emp.getEmail());
            }
        }

        //  3. Affichage final
        System.out.println("\n Catégories en base :");
        categoryDAO.findAll().forEach(System.out::println);

        System.out.println("\n Employés en base :");
        employeeDAO.findAll().forEach(System.out::println);
    }
}
