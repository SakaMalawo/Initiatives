# Citizen Platform - Plateforme Participative Citoyenne

Ce projet est une application web moderne structurée en deux parties distinctes : un **Backend Spring Boot** et un **Frontend Tailwind CSS**.

## 📂 Structure du Projet

- **/backend** : API REST développée avec Spring Boot 3.4.0 et Java 24
- **/frontend** : Interface utilisateur moderne utilisant Tailwind CSS et le design Glassmorphism
- **Base de données** : MySQL avec tables `categories` et `geographic_zones` pré-remplies automatiquement

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

### Prérequis
- **Java 24** (compatible avec Spring Boot 3.4.0)
- **Maven 3.9+**
- **MySQL** avec base de données `cityzen`
- **Python** (pour serveur frontend) ou tout serveur HTTP local

### Installation Backend
1. Assurez-vous que MySQL est installé et que la base de données `cityzen` existe
2. Configurez `src/main/resources/application.properties` avec vos identifiants MySQL
3. Lancez l'application :
   ```bash
   cd backend
   mvn spring-boot:run
   ```
4. L'API sera disponible sur `http://localhost:8080`
5. **Les catégories et zones géographiques sont créées automatiquement au démarrage**

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

### Installation Frontend
**Important** : Le frontend doit être servi via un serveur HTTP (pas directement en fichier local) pour que l'API fonctionne correctement.

#### Option 1 : Python (recommandé)
```bash
cd frontend
python -m http.server 5500
```
Accédez à : `http://localhost:5500`

#### Option 2 : Node.js
```bash
cd frontend
npx serve -p 5500
```

#### Option 3 : Live Server (VS Code)
Installez l'extension "Live Server" et cliquez sur "Go Live"

Le frontend communiquera avec l'API sur le port 8080.

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
| **Backend** | Java 24, Spring Boot 3.4.0, Spring Security, Spring Data JPA, Hibernate, MySQL, JWT (Lombok supprimé) |
| **Frontend** | HTML5, Tailwind CSS (CDN), JavaScript (ES6+), Glassmorphism Design |
| **Outils** | Maven, Git |

---

## 🌟 Caractère Innovant
L'application transforme l'engagement citoyen en une expérience sociale interactive, permettant une priorisation collective des projets locaux par le vote et le débat direct. Les initiatives sont présentées sous forme de réseau social avec possibilité de commenter, partager et soutenir.

---

## 📝 Instructions de démarrage rapide

### 1. Démarrer le Backend
```bash
cd backend
mvn spring-boot:run
```
L'API démarre sur `http://localhost:8080`

### 2. Démarrer le Frontend (dans un autre terminal)
```bash
cd frontend
python -m http.server 5500
```
Accédez à `http://localhost:5500`

### 3. Premiers pas
1. **Inscription** : Créez un compte sur `http://localhost:5500/register.html`
2. **Connexion** : Connectez-vous avec vos identifiants
3. **Explorer** : Parcourez les initiatives existantes
4. **Créer** : Proposez votre propre initiative (nécessite connexion)
5. **Voter** : Votez et commentez sur les initiatives

**Note** : Le rôle "CITOYEN" est créé automatiquement si inexistant.

---

## ⚠️ Notes Importantes

### Suppression de Lombok
Ce projet ne utilise **plus Lombok**. Tous les getters, setters et constructeurs sont écrits explicitement.

### Changements majeurs récents
- ✅ Java 24 support (Spring Boot 3.4.0)
- ✅ Lombok complètement supprimé
- ✅ Initialisation automatique des catégories/zones
- ✅ Protection des pages frontend par authentification
- ✅ Navbar fixe et responsive
- ✅ Menu adaptatif selon l'état de connexion

---

## 🔐 Sécurité et Authentification

### Mécanisme d'authentification
- **JWT Token** : Stocké dans `localStorage`, valable pour toutes les requêtes API
- **Protection frontend** : Les pages protégées redirigent vers `login.html` si non connecté
- **Protection backend** : Endpoints sécurisés avec Spring Security
- **Mot de passe** : Haché avec BCrypt

### Pages protégées (nécessitent connexion)
| Page | Protection |
|------|-----------|
| `create-initiative.html` | ✅ Redirection vers login |
| `dashboard.html` | ✅ Redirection vers login |
| `profile.html` | ✅ Redirection vers login |
| `admin.html` | ✅ Redirection vers login |
| `initiatives.html` | ❌ Accès public (lecture seule) |
| `index.html` | ❌ Accès public |

### Rôles utilisateurs
- **CITOYEN** : Proposer des initiatives, voter, commenter
- **MODERATEUR** : Modérer les commentaires
- **ADMIN** : Gérer les utilisateurs et toutes les initiatives

### Flux d'authentification
1. Inscription → Création compte avec rôle CITOYEN
2. Connexion → Stockage du token JWT dans `localStorage`
3. Navigation → Menu adaptatif (Connexion/Inscription vs Profil/Déconnexion)
4. Déconnexion → Suppression du token et redirection

---

## 🛠️ Dépannage

### Problème : Frontend ne communique pas avec le backend
**Solution** : Assurez-vous que :
1. Le backend tourne sur `http://localhost:8080`
2. Le frontend est servi via HTTP (pas `file://`)
3. Les CORS sont correctement configurés

### Problème : Tables categories/zones vides
**Solution** : Redémarrez le backend. Les données sont initialisées automatiquement au démarrage par `DataInitializer`.

### Problème : Erreur "Rôle CITOYEN non trouvé"
**Solution** : Ce problème est corrigé. Le rôle est créé automatiquement s'il n'existe pas.

### Problème : "Unsupported class file major version"
**Solution** : Utilisez Java 24 et Spring Boot 3.4.0 comme indiqué dans les prérequis.

### Ports utilisés
| Service | Port | URL |
|---------|------|-----|
| Backend API | 8080 | `http://localhost:8080/api` |
| Frontend | 5500 | `http://localhost:5500` |
| MySQL | 3306 | - |
