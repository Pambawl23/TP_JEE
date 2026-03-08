# Application de Gestion de Commandes - Spring Boot

Ce projet est une application Spring Boot qui implémente une API REST pour la gestion de commandes clients. Elle permet de gérer les produits, les clients et les commandes, avec une architecture en couches conforme aux standards industriels.

## 📦 Fonctionnalités

- **Gestion des produits** : CRUD complet pour les produits
- **Gestion des clients** : CRUD complet pour les clients  
- **Gestion des commandes** : Création, validation, annulation et suivi des commandes
- **Règles métier** : 
  - Vérification du stock avant création de commande
  - Calcul automatique du total de la commande
  - Mise à jour du stock après validation
- **Documentation API** : OpenAPI/Swagger intégré
- **Multi-environnements** : Support des profils dev, test et prod

## 🏗️ Architecture

```
com.polytech.commandes
├── entity          # Entités JPA
├── repository      # Interfaces Spring Data JPA
├── service         # Couche service (interfaces + implémentations)
├── controller      # Contrôleurs REST
├── exception       # Gestion des exceptions
├── config          # Configuration Spring
└── CommandesApplication.java
```

## 🚀 Lancement de l'application

### Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- Base de données PostgreSQL/MySQL/H2 (selon configuration)

### Étapes d'installation

1. **Cloner le dépôt**
   ```bash
   git clone https://github.com/Pambawl23/TP_JEE.git
   cd commandes-app
   ```

2. **Configurer les variables d'environnement**
   ```bash
   export SPRING_PROFILES_ACTIVE=dev
   export DB_URL=jdbc:mysql://localhost:8080/commandes_db
   export DB_USERNAME=username
   export DB_PASSWORD=password
   export SERVER_PORT=8080
   ```

3. **Compiler et exécuter avec Maven**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Alternative : exécuter le JAR**
   ```bash
   mvn clean package
   java -jar target/commandes-application.jar
   ```

## 🔧 Activation des profils

L'application supporte trois profils Spring :

| Profil | Usage | Fichier de configuration |
|--------|-------|--------------------------|
| `dev`  | Développement local | `application-dev.yml` |
| `test` | Tests automatisés | `application-test.yml` |
| `prod` | Production | `application-prod.yml` |

### Méthodes d'activation

1. **Variable d'environnement** (recommandé) :
   ```bash
   export SPRING_PROFILES_ACTIVE=dev
   ```

2. **Paramètre de ligne de commande** :
   ```bash
   java -jar commandes-application.jar --spring.profiles.active=dev
   ```

3. **Dans le fichier `application.yml`** :
   ```yaml
   spring:
     profiles:
       active: dev
   ```

## 📚 Accès à Swagger/OpenAPI

Une fois l'application démarrée, la documentation interactive de l'API est disponible :

- **URL Swagger UI** : http://localhost:8080/swagger-ui.html
- **URL OpenAPI JSON** : http://localhost:8080/v3/api-docs

La page Swagger affiche :
- Tous les endpoints disponibles regroupés par ressource (tags)
- La description de chaque opération
- Les modèles de requêtes/réponses
- La possibilité de tester les endpoints directement depuis l'interface

## 🔐 Authentification par token (JWT)

L'API est sécurisée par JWT. Avant d'appeler les endpoints protégés (`/api/**`), il faut d'abord obtenir un token.

### 1. Obtenir un token

Endpoint public :
- `POST /securite/token`

Corps de la requête :

```json
{
  "email": "dupont@email.com",
  "nom": "Dupont"
}
```

Réponse :

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 2. Utiliser le token dans les appels API

Ajouter l'en-tête HTTP suivant :

```http
Authorization: Bearer <votre_token>
```

Exemple :

```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." http://localhost:8080/api/c1/clients
```

### 3. Via Swagger UI

1. Exécuter `POST /securite/token` pour récupérer le token.
2. Cliquer sur le bouton **Authorize** dans Swagger.
3. Coller le token dans le format `Bearer <token>`.
4. Tester les endpoints sécurisés.

### 4. Durée de validité du token

La durée d'expiration est configurable via :
- `security.jwt.expiration` (en secondes, valeur par défaut : `3600`).

## 📞 Exemples d'appels API

### Produits

**1. Récupérer tous les produits**
```bash
GET http://localhost:8080/api/produits
```

**2. Créer un nouveau produit**
```bash
POST http://localhost:8080/api/produits
Content-Type: application/json

{
  "nom": "Ordinateur Portable",
  "prix": 1299.99,
  "stock": 50
}
```

**3. Récupérer un produit par ID**
```bash
GET http://localhost:8080/api/produits/1
```

### Clients

**1. Créer un client**
```bash
POST http://localhost:8080/api/clients
Content-Type: application/json

{
  "nom": "Dupont",
  "email": "dupont@email.com"
}
```

**2. Récupérer les commandes d'un client**
```bash
GET http://localhost:8080/api/clients/1/commandes
```

### Commandes

**1. Créer une commande**
```bash
POST http://localhost:8080/api/commandes
Content-Type: application/json

{
  "clientId": 1,
  "lignesCommande": [
    {
      "produitId": 1,
      "quantite": 2
    },
    {
      "produitId": 2,
      "quantite": 1
    }
  ]
}
```

**2. Valider une commande**
```bash
PUT http://localhost:8080/api/commandes/1/valider
```

**3. Annuler une commande**
```bash
PUT http://localhost:8080/api/commandes/1/annuler
```

**4. Récupérer les commandes par statut**
```bash
GET http://localhost:8080/api/commandes?status=VALIDATED
```

## ⚙️ Configuration

### Fichiers de configuration

- `application.yml` : Configuration principale
- `application-dev.yml` : Configuration développement (initialisation des données automatique)
- `application-test.yml` : Configuration pour les tests
- `application-prod.yml` : Configuration production

### Variables d'environnement requises

| Variable | Description | Exemple |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Profil Spring actif | `dev` |
| `DB_URL` | URL de la base de données | `jdbc:postgresql://localhost:5432/commandes` |
| `DB_USERNAME` | Utilisateur de la BD | `admin` |
| `DB_PASSWORD` | Mot de passe de la BD | `secret` |
| `SERVER_PORT` | Port d'écoute de l'application | `8080` |

## 📊 Initialisation des données (profil dev uniquement)

Lors du démarrage avec le profil `dev`, un `CommandLineRunner` insère automatiquement :
- 3 clients de test
- 5 produits de test  
- 2 commandes complètes avec leurs lignes de commande

## 🧪 Tests

Exécuter les tests :
```bash
# Tous les tests
mvn test

# Tests avec le profil test
SPRING_PROFILES_ACTIVE=test mvn test
```

## 📁 Structure du projet

Le projet suit l'architecture imposée avec une séparation stricte des responsabilités :
- **Controllers** : Gestion des requêtes HTTP et des réponses
- **Services** : Implémentation de la logique métier
- **Repositories** : Accès aux données avec Spring Data JPA
- **Entities** : Modèles de données JPA
- **Config** : Configuration Spring (OpenAPI, sécurité, etc.)



## 📄 Licence

Ce projet est réalisé dans le cadre pédagogique de l'Institut Polytechnique de Saint Louis.
