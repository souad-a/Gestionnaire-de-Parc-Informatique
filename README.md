# Gestionnaire-de-Parc-Informatique

🎯 Objectif

Permettre au service IT de :

Gérer les catégories d’équipements 🖥️

Suivre les employés et leurs affectations 👥

Visualiser les statistiques et l’état du parc 📊

🛠️ Stack Technique
Type	Technologies
Backend	Java EE, Servlets, Hibernate
Frontend	JSP, JSTL, Bootstrap 5
Base de données	MySQL
Build Tool	Maven
Serveur d’application	Apache Tomcat 9+
👥 Équipe de Développement
Membre	Rôle	Modules
🧩 Agourar Souad	Backend & Sécurité	Authentification, Utilisateurs
🎨 Mesad El Ayam Hafida	Frontend & Data	Catégories, Employés, Équipements
📁 Structure du Projet
├── src/
│   ├── main/
│   │   ├── java/           # Classes Java métier (controllers, services, entities, etc.)
│   │   ├── resources/      # Fichiers de configuration (hibernate.cfg.xml, etc.)
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       ├── index.jsp
│   │       └── assets/     # JSP, HTML, CSS, JS, images
│   └── test/
│       ├── java/           # Tests unitaires et d’intégration
│       └── resources/      # Configurations de test
├── pom.xml                 # Configuration Maven (dépendances, plugins)
├── README.md               # Documentation du projet
└── .gitignore              # Fichiers/dossiers ignorés par Git

📊 Fonctionnalités
✅ Phase 1 – MVP (Minimum Viable Product)

🔐 Authentification sécurisée

👥 Gestion des rôles (Admin / Technicien)

🗂️ CRUD Catégories d’équipements

👤 CRUD Employés

💎 Interface responsive (Bootstrap 5)

🔄 Phase 2 – En développement

💻 Gestion des équipements (CRUD complet)

🔗 Système d’affectation Équipement ↔ Employé

🕓 Historique des affectations

📈 Tableau de bord statistiques

🚀 Phase 3 – Futures Améliorations

🧾 Génération de rapports PDF automatiques

✉️ Notifications par email

🗃️ Modèle de Données
Entités principales
Entité	Description
User	Gestion des comptes et authentification
Employee	Informations des employés
Category	Catégories d’équipements
Equipment	Données sur les équipements informatiques
Assignment	Historique des affectations entre employés et équipements
Relations

User (1) → (*) Equipment : Gestion des équipements

Employee (1) ←→ (*) Equipment via Assignment (ManyToMany)

Category (1) → (*) Equipment : Classification des matériels

🤝 Guide de Contribution
🧩 Conventions de commits
git commit -m "feat: ajout gestion des catégories"
git commit -m "fix: correction bug suppression employé"
git commit -m "docs: mise à jour README"

Types de commits
Type	Signification
feat	Nouvelle fonctionnalité
fix	Correction de bug
docs	Mise à jour de la documentation
style	Formatage / indentation
refactor	Amélioration du code sans changement fonctionnel
test	Ajout ou modification de tests
🔄 Workflow de Développement

Création de branche pour chaque fonctionnalité :

# Membre 1 - Authentification
git checkout -b feature/auth-users

# Membre 2 - Catégories & Employés
git checkout -b feature/categories-employees


Commits réguliers (au moins 1 par jour) :

git add .
git commit -m "feat: entité Category avec validation"
git push origin feature/categories-employees


Synchronisation quotidienne :

git fetch origin
git merge origin/develop


Revue de code mutuelle :

Chaque Pull Request est revue par l’autre membre.

Validation avant merge.

Résolution des conflits en collaboration.

🧾 Checklist de Revue de Code

✅ Le code compile sans erreurs

✅ Les fonctionnalités marchent comme prévu

✅ Pas de code dupliqué

✅ Les conventions de nommage sont respectées

✅ La documentation est à jour

🧠 Auteur·es & Contact

👩‍💻 Agourar Souad — [GitHub](https://github.com/souad-a)

👩‍💻 Mesad El Ayam Hafida — [GitHub](https://github.com/Hafidamesad)

💡 Projet académique réalisé dans le cadre du module “Développement Web JEE”
