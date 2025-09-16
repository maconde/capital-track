# CapitalTrack

Application Spring Boot pour la gestion et le calcul des participations directes et indirectes des actionnaires (personnes ou sociétés) dans une entreprise.

## Fonctionnalités

**Contrat MUST HAVE**: il s’agit de la priorité du MVP, elle doit donc être livrée pour valider le
fonctionnement de la gestion des bénéficiaires effectifs par le métier

- **Calcul des bénéficiaires**: Obtenez la liste des bénéficiaires d’une société, avec filtrage
- **Calcul des participations**: Déterminez les pourcentages de détention.
- **Gestion des erreurs**: Retourne des codes HTTP adaptés (404 si la société n’existe pas, 204 si aucun bénéficiaire).
- **Documentation OpenAPI**: Endpoints documentés via Swagger (http://localhost:8080/swagger-ui/index.html)
- **Tests unitaires**: Couverture des cas métiers principaux.

## Prérequis

- Java 21 ou supérieur
- Spring Boot 3.5+
- Maven 3.8+

## Installation

1. Cloner le dépôt:
   ```bash 
    git clone https://github.com/maconde/capital-track.git
    ```
   
2. Se placer dans le dossier du projet:
   ```bash
   cd capital-track
   ```
3. Construire le projet avec Maven:
   ```bash
   mvn clean install
   ```
4. Lancer l'application:
   ```bash
   mvn spring-boot:run
   ```

L’API sera accessible sur : `http://localhost:8080`


## Améliorations futures
- **Authentification et Autorisation**: Intégrer OAuth2 ou JWT pour sécuriser les endpoints.
- **Pagination**: Ajouter la pagination pour les listes de bénéficiaires.
- **Cache**: Implémenter un système de cache pour améliorer les performances des requêtes
- **Utilisation de la programmation réactive**: Utiliser Spring WebFlux pour une meilleure gestion des ressources.