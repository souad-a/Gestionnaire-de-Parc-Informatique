# 🖥️ Gestionnaire de Parc Informatique

## 🎯 Objectif du Projet

Développer une **application web complète** permettant au service IT de gérer efficacement le parc informatique de l'entreprise.

### 📋 Fonctionnalités Principales
- **Gestion centralisée** des équipements informatiques 🖥️
- **Suivi précis** des affectations aux employés 👥
- **Tableaux de bord** pour la prise de décision 📊
- **Historique complet** des mouvements d'équipements 📝

## 🛠️ Stack Technique

| Couche | Technologies |
|--------|--------------|
| **Backend** | Java EE, Servlets, Hibernate ORM, JAX-RS |
| **Frontend** | JSP, JSTL, Bootstrap 5, Chart.js, DataTables |
| **Base de données** | MySQL 8.0+ |
| **Build Tool** | Apache Maven 3.6+ |
| **Serveur** | Apache Tomcat 9.x |
| **Sécurité** | Filters Java, BCrypt, Session Management |
| **Logging** | Log4j2 |

## 👥 Équipe de Développement

| Membre | Rôle Principal | Modules Responsables |
|--------|----------------|---------------------|
| **Agourar Souad** | Backend & Sécurité | Authentification, Gestion des Utilisateurs, Sécurité API |
| **Mesad El Ayam Hafida** | Frontend & Data | Catégories, Employés, Équipements, Dashboard |

## 📁 Architecture du Projet
gestionnaire-parc-informatique/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/parcinformatique/
│ │ │ ├── controller/ # Servlets & Contrôleurs
│ │ │ ├── service/ # Logique métier
│ │ │ ├── repository/ # Couche d'accès aux données
│ │ │ ├── entity/ # Entités JPA
│ │ │ ├── dto/ # Data Transfer Objects
│ │ │ ├── filter/ # Filtres de sécurité
│ │ │ └── config/ # Configuration Hibernate
│ │ ├── resources/
│ │ │ ├── hibernate.cfg.xml
│ │ │ ├── log4j2.xml
│ │ │ └── database/ # Scripts SQL
│ │ └── webapp/
│ │ ├── WEB-INF/
│ │ │ ├── web.xml
│ │ │ └── views/ # Pages JSP sécurisées
│ │ ├── assets/
│ │ │ ├── css/ # Styles Bootstrap
│ │ │ ├── js/ # JavaScript custom
│ │ │ ├── images/ # Images & icônes
│ │ │ └── lib/ # Dépendances frontend
│ │ ├── index.jsp
│ │ └── login.jsp
│ └── test/
│ ├── java/ # Tests unitaires
│ └── resources/ # Configurations de test
├── pom.xml
├── README.md
├── .gitignore
└── requirements.md

text

## 🚀 Roadmap de Développement

### ✅ Phase 1 – MVP (Minimum Viable Product)
- [x] Système d'authentification sécurisé
- [x] Gestion des rôles (Admin / Technicien)
- [x] CRUD complet des catégories d'équipements
- [x] CRUD complet des employés
- [x] Interface responsive avec Bootstrap 5

### 🔄 Phase 2 – En Cours de Développement
- [ ] CRUD complet des équipements informatiques
- [ ] Système d'affectation Équipement ↔ Employé
- [ ] Historique détaillé des affectations
- [ ] Tableau de bord avec indicateurs clés

### 📅 Phase 3 – Planifiée
- [ ] Génération automatique de rapports PDF
- [ ] Système de notifications par email
- [ ] Recherche avancée et filtres
- [ ] Application mobile companion

## 🗃️ Modèle de Données

### Entités Principales

| Entité | Description | Attributs Clés |
|--------|-------------|----------------|
| **User** | Comptes d'accès | id, username, password, role, active |
| **Employee** | Informations employés | id, firstName, lastName, email, department |
| **Category** | Classification équipements | id, name, description, specifications |
| **Equipment** | Équipements informatiques | id, serialNumber, brand, model, status |
| **Assignment** | Historique affectations | id, assignmentDate, returnDate, notes |

### Relations entre Entités
- User (1) → (*) Equipment
- Employee (1) ←→ (*) Equipment via Assignment
- Category (1) → (*) Equipment

## 🤝 Guide de Contribution

### Conventions de Commits

| Type | Usage |
|------|-------|
| **feat** | Nouvelle fonctionnalité |
| **fix** | Correction de bug |
| **docs** | Documentation |
| **style** | Formatage / CSS |
| **refactor** | Refactorisation |

**Exemples :**
bash
git commit -m "feat: ajout gestion des catégories avec validation"
git commit -m "fix: correction bug suppression employé"
Workflow de Développement
Création de branche :

bash
git checkout -b feature/nom-fonctionnalite
Commits réguliers :

bash
git add .
git commit -m "feat: implémentation entité Category"
git push origin feature/nom-fonctionnalite
Revue de code obligatoire avant merge

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
