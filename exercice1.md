# TP Exercice 1 : Analysez l’application

## Tâche 1 : Questions

### Quels sont les principaux domaines métiers de l'application Order flow ?

Les principaux domaines métiers de l'application Order flow sont : 
- les domaines Support
- les dommaines Générique

### Comment les microservices sont-ils conçus pour implémenter les domaines métiers ?

Les microservices sont conçus pour implémenter les domaines métiers selon un modèle CQRS et piloté par les événements.

### Quelles sont les responsabilités des conteneurs de code apps/of-api-gateway, apps/of-product-registry-microservices/product.registry, apps/of-product-registry-microservices/product.registry.read, libs/event-sourcing, libs/published-language ?

apps/of-api-gateway -> point d'entrée de l'application 
apps/of-product-registry-microservices/product.registry -> écriture des produits
apps/of-product-registry-microservices/product.registry.read -> lecture des produits
libs/event-sourcing -> gestion des événements de l'application
libs/published-language -> 

## Tâche 2 : Questions

### Quels sont les concepts principaux utilisés dans l'application Order flow ?

Les services sont séparés en différents domaines de métiers

### Comment les concepts principaux sont-ils implémentés dans les microservices ?

- MongoDB (stockage des données, persistance)
- Quarkus (Java) (Gestion des microservices (couche applicative))
- Message broker (communication entre les microservices)
- Event sourcing (gestion des événements)
- Gradle (gestion des dépendances)

### Que fait la bibliothèque libs/event-sourcing ? Comment est-elle utilisée dans les microservices (relation entre métier et structure du code) ?

Event sourcing (Gestion des événements), est utilisée pour la gestion des événements dans les microservices.

### Comment l'implémentation actuelle de l'event-sourcing assure-t-elle la fiabilité des états internes de l'application ?

L'agrégat id est utilisé pour garantir la cohérence des données.

### Tâche 3 : Identifier les problèmes de qualité

- Attribut "public" -> il faut faire des getters setters
- throw new RuntimeException -> pas une bonne pratique (pas précis)
- Pas de javadoc (ex : ProductRegistryEventEntity.java)
- Pas énormément de tests unitaires et d'intégration
- Problème metier CQRS (pas de séparation des commandes et des requêtes)
- Utilisation d'exception technique pour les logiques metiers
    il faut avoir une gestion d'erreur metier et technique séparée
        erreur technique -> logique de retry 
        erreur metier -> retourne un message au client