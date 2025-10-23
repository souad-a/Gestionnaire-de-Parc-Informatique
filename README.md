# Gestionnaire-de-Parc-Informatique
# 🖥️ Gestionnaire de Parc Informatique

## 📋 Description
Application web de gestion de parc informatique développée en JEE avec Servlet, JSP et Hibernate pour le compte d'un service informatique d'entreprise.

## 🎯 Objectif
Permettre au service IT de gérer efficacement le parc d'équipements informatiques, les employés et les affectations.

## 🛠️ Stack Technique
- **Backend:** Java EE, Servlets, Hibernate
- **Frontend:** JSP, JSTL, Bootstrap 5
- **Base de données:** MySQL
- **Build:** Maven
- **Serveur:** Apache Tomcat 9+

## 👥 Équipe de Développement
| Membre | Rôle | Modules |
|--------|------|---------|
| [Agourar Souad] | Backend & Sécurité | Authentification, Utilisateurs |
| [Mesad El Ayam Hafida] | Frontend & Data | Catégories, Employés, Équipements |

## 📁 Structure du Projet




📊 Fonctionnalités

✅ Phase 1 - MVP (Minimum Viable Product)
Système d'authentification sécurisé
Gestion des rôles (Administrateur, Technicien)
CRUD Catégories d'équipements
CRUD Employés avec informations détaillées
Interface responsive avec Bootstrap

🔄 Phase 2 - En Développement
Gestion des équipements (CRUD complet)
Système d'affectation équipement/employé
Historique des affectations
Tableau de bord avec statistiques

🎯 Phase 3 - Futures Améliorations
Rapports PDF automatiques
Notifications par email

🗃️ Modèle de Données

Entités Principales
User: Gestion des comptes et authentification
Employee: Informations des employés
Category: Catégories d'équipements
Equipment: Équipements informatiques
Assignment: Historique des affectations

Relations
User (1) → (*) Equipment - Gestion des équipements
Employee (1) ←→ (*) Equipment - Via Assignment (ManyToMany)
Category (1) → (*) Equipment - Classification
## 🤝 Guide de Contribution

### 📝 Convention des commits
bash
git commit -m "feat: ajout gestion des catégories"
git commit -m "fix: correction bug suppression employé"
git commit -m "docs: mise à jour README"

Types de commits :
feat: Nouvelle fonctionnalité
fix: Correction de bug
docs: Documentation
style: Formatage (sans changement fonctionnel)
refactor: Restructuration du code
test: Ajout de tests

🔄 Notre Workflow de travail
1. Chaque membre travaille sur sa branche
bash
# Membre 1 - Authentification
git checkout -b feature/auth-users

# Membre 2 - Catégories & Employés  
git checkout -b feature/categories-employees
2. Commits réguliers (au moins 1x par jour)
bash
git add .
git commit -m "feat: entité Category avec validation"
git push origin feature/categories-employees
3. Synchronisation quotidienne
bash
# Récupérer les modifications des autres
git fetch origin
git merge origin/develop
4. Revue de code mutuelle
Chaque Pull Request est revue par l'autre membre
Validation avant merge
Résolution des conflits ensemble

📋 Notre Processus de Revue de Code
Checklist de review :
Le code compile sans erreurs
Les fonctionnalités marchent comme prévu
Pas de code dupliqué
Les conventions de nommage sont respectées
La documentation est à jour
