package com.parcinformatique.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    //var statiques accessibles à toute l'app
    private static StandardServiceRegistry registry;//configuration de base
    private static SessionFactory sessionFactory;//usine qui crée les connexions
    //methode principale
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {//sic'est la première connexion
            try {
                // Créer le registry
                registry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")//lis ce fichier (url, username et passeword)
                        .build();

                // Créer MetadataSources
                MetadataSources sources = new MetadataSources(registry);//conteneur des entitées JPA

                // Ajouter les entités MANUELLEMENT (important !)
                sources.addAnnotatedClass(com.parcinformatique.model.Category.class);
                sources.addAnnotatedClass(com.parcinformatique.model.Employee.class);

                //analyses les classes (correspondances entre champs/colonnes, relations entre tables et ORM)
                Metadata metadata = sources.getMetadataBuilder().build();

                //l'objet le plus important,lusine à connexion qu'on utilise à chaque interaction à la base
                sessionFactory = metadata.getSessionFactoryBuilder().build();

                System.out.println("✅ Hibernate SessionFactory créée avec succès !");

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {//si ne se passe pas bien nettoie les ressources
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}