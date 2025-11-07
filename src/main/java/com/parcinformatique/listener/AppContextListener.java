// 📁 src/main/java/com/parcinformatique/listener/AppContextListener.java
package com.parcinformatique.listener;

import com.parcinformatique.util.DataInitializer;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("🚀 Application Gestionnaire de Parc Informatique démarrée");

        // Initialiser les données de test
        DataInitializer.initializeTestData();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("🛑 Application arrêtée");
    }
}