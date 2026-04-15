# Citizen Platform - Plateforme Participative Citoyenne

Ce projet est une application web moderne structurée en deux parties distinctes : un **Backend Spring Boot** et un **Frontend Tailwind CSS**.

## 📂 Structure du Projet

- **/backend** : API REST développée avec Spring Boot 3.2.4.
- **/frontend** : Interface utilisateur moderne utilisant Tailwind CSS et le design Glassmorphism.

---

## ⚙️ Backend (Spring Boot)

### Fonctionnalités
- API REST pour la gestion des initiatives, votes et commentaires
- Authentification JWT avec Spring Security
- Gestion des rôles (ADMIN, CITOYEN, MODERATEUR)
- Persistance des données avec Spring Data JPA et MySQL
- Base de données existante : `cityzen`

### Configuration de la base de données
- Nom de la base de données : `cityzen`
- Utilisateur : `root`
- Mot de passe : (vide)
- Port : `3306`

### Installation
1. Assurez-vous que MySQL est installé et que la base de données `cityzen` existe
2. Accédez au dossier : `cd backend`
3. La configuration est déjà dans `src/main/resources/application.properties`
4. Lancez l'application :
   ```bash
   mvn spring-boot:run
   ```
L'API sera disponible sur `http://localhost:8080`.

### Endpoints API
- **Authentification** : `/api/auth/login`, `/api/auth/register`, `/api/auth/profile/{id}`
- **Initiatives** : `/api/initiatives` (CRUD complet)
- **Commentaires** : `/api/comments` (CRUD + modération)
- **Votes** : `/api/votes`
- **Tableau de bord** : `/api/dashboard/stats`
- **Catégories** : `/api/categories`
- **Zones** : `/api/zones`

---

## 🎨 Frontend (Tailwind CSS)

### Fonctionnalités
- Design **Glassmorphism** avec fond bleu sombre
- Interface responsive et animations fluides sur les boutons et navigation
- Présentation des initiatives sous forme de réseau social (post, comment, share, soutien)
- Pages : Accueil, Connexion, Inscription, Initiatives, Détail, Création, Tableau de bord, Profil, Admin

### Installation
1. Accédez au dossier : `cd frontend`
2. Ouvrez `index.html` dans votre navigateur ou utilisez un serveur local (ex: Live Server)
3. Le frontend est configuré pour communiquer avec l'API sur le port 8080

### Pages disponibles
- `index.html` - Page d'accueil avec aperçu des initiatives
- `login.html` - Page de connexion
- `register.html` - Page d'inscription
- `initiatives.html` - Liste des initiatives avec filtres
- `initiative-detail.html` - Détail d'une initiative avec commentaires
- `create-initiative.html` - Création d'une nouvelle initiative
- `dashboard.html` - Tableau de bord avec statistiques
- `profile.html` - Gestion du profil utilisateur
- `admin.html` - Panneau d'administration

---

## 🛠️ Technologies Utilisées

| Partie | Technologies |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3.2.4, Spring Security, Spring Data JPA, Hibernate, MySQL, JWT, Lombok |
| **Frontend** | HTML5, Tailwind CSS (CDN), JavaScript (ES6+), Glassmorphism Design |
| **Outils** | Maven, Git |

---

## 🌟 Caractère Innovant
L'application transforme l'engagement citoyen en une expérience sociale interactive, permettant une priorisation collective des projets locaux par le vote et le débat direct. Les initiatives sont présentées sous forme de réseau social avec possibilité de commenter, partager et soutenir.

---

## 📝 Instructions de démarrage rapide

1. **Démarrer le backend** :
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. **Démarrer le frontend** :
   - Ouvrir `frontend/index.html` dans un navigateur
   - Ou utiliser un serveur local comme Live Server dans VS Code

3. **Premiers pas** :
   - Créer un compte sur la page d'inscription
   - Explorer les initiatives existantes
   - Proposer votre propre initiative
   - Voter et commenter sur les initiatives

---

## 🔐 Sécurité
- Authentification par JWT token
- Rôles utilisateurs : CITOYEN, MODERATEUR, ADMIN
- Mot de passe haché avec BCrypt
- CORS configuré pour le frontend
