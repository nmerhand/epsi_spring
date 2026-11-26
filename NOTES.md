# Java
## QUESTIONS TP01

> Ouvrez le pom.xml. Combien de dépendances concernent JDBC ? Combien concernent l'API JDBC (java.sql) ?  

1 dépendance concerne JDBC 
3 dépendances concernent l'API JDBC

> Écrivez dans `NOTES.md` le nom de classe que le programme affichera, avant de lancer. Faites le changement, lancez, notez la classe obtenue et expliquez l'écart s'il y en a un.

Db 
=> Hikari

## QUESTIONS TP02

> Quel constructeur sert à un fournisseur **pas encore enregistré**, et pourquoi son identifiant est-il un `Long` et non un `long` ?

Le deuxième constructeur sert à un fournisseur pas encore enregistré :
```java
public Fournisseur(Long id, String nom, Adresse adresse) {  
    this(nom, adresse);  
    this.id = id;  
}
```

Son identifiant est un Long car ...

> Écrivez dans `NOTES.md` ce que vous pensez obtenir de `ps.getGeneratedKeys()` si l'on oublie `Statement.RETURN_GENERATED_KEYS`

rs.next serait faux
=> ![[Pasted image 20261022153535.png]]
Le fournisseur a bien été créé dans la bdd mais rs.next était bien faux donc la méthode n'a pas été plus loin

> **Prédiction.** Pour le bon 7 (`delai IS NULL`), qu'affichent `rs.getInt("delai")` puis `rs.getObject("delai", Integer.class)` ? Écrivez votre réponse, exécutez les deux lectures depuis `app.Brouillon`, notez les deux résultats. Gardez la seconde dans `delai`

`rs.getInt("delai")` => Optional[0]
![[Pasted image 20261022164418.png|416]]

`rs.getObject("delai", Integer.class)` => null 
![[Pasted image 20261022164646.png|463]]

> En deux phrases dans `NOTES.md` : pourquoi la première lecture est-elle un bug de livraison, et pas un détail d'arrondi ?

En renvoyant Optional[0] on pourrait croire que le contenu du champ est 0 alors que c'est Null. Mais vu que le Null d'un int est 0, il peut y avoir méprise.

> Recopiez dans `NOTES.md` l'identifiant que `creer` a renvoyé à votre **premier** lancement.

=> 5

## QUESTIONS TP05

> Quel type déclare `createEntityManagerFactory()` ? Quelle dépendance apporte le paquet `jakarta.persistence` ? Lancez `mvn dependency:tree -Dincludes=jakarta.persistence` et recopiez les lignes utiles.

- `createEntityManagerFactory()` déclare un type `EntityManager`. 
- `import org.hibernate.jpa.HibernatePersistenceConfiguration;`. 

```shell
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------------< org.sebsy:demo-data-access >---------------------
[INFO] Building demo-data-access 1.0.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- dependency:3.7.0:tree (default-cli) @ demo-data-access ---

[INFO] org.sebsy:demo-data-access:jar:1.0.0-SNAPSHOT
[INFO] \- org.hibernate.orm:hibernate-core:jar:7.4.11.Final:compile
[INFO]    \- jakarta.persistence:jakarta.persistence-api:jar:3.2.0:compile
```

> Recopiez dans `NOTES.md` le fragment qui commence par `Schema validation`, mot pour mot

Il n'y a pas de fragment qui commence par `Schema validation` 

> En une phrase : pourquoi la correction se fait-elle chez le propriétaire ?

Car Adresse n'est pas obligatoirement utilisable dans Fournisseur et la nomination "code_postal" est propre à la table fournisseur, pour cela, la correction doit se faire chez le propriétaire.

> Notez la requête affichée par logback, et ce que renvoie `estComplete()` pour le fournisseur 4.

```SQL
select f1_0.id,f1_0.rue,f1_0.code_postal,f1_0.ville,f1_0.nom from fournisseur f1_0 where f1_0.id=?
```

fournisseur4.getAdresse().estComplete() => false

> **Prédiction datée.** Avant toute exécution, écrivez dans `NOTES.md`, avec la date et l'heure, ce que fera `validate` face à cette entité de test, mappée sur la table `article`, qu'aucune entité de l'unité ne porte encore :
```java
package org.sebsy.compta.essai;

@Entity(name = "ArticleCourt")
@Table(name = "article")
public class ArticleCourt {
    @Id
    Integer id;     // la colonne est un BIGINT
    String ref;
}
```

23.10.2026 - 11:15

Il va pas accepter parce qu'on n'a pas toutes les colonnes.
Et le type de id est pas bon.

Constat : ça fonctionne 

> Passez ensuite `id` en `String`, relancez et recopiez le fragment `Schema validation`

Pas de fragment `Schema validation` :

```zsh
Unable to build Hibernate SessionFactory  [persistence unit: compta]
```

> Qu'est-ce que ces deux résultats disent de ce que `validate` compare ?

`validate` compare les types.

> Quel risque reste-t-il avec un `Integer` sur cette colonne ? 

jsp

> Renommez `@Table(name = "fournisseurs")`. Relancez, recopiez le fragment `Schema validation`, remettez le bon nom.

```zsh
Unable to build Hibernate SessionFactory  [persistence unit: compta]
```

> L'application démarre-t-elle ? 

Oui

> Recopiez la première commande SQL qu'Hibernate tente d'exécuter et la réponse de MariaDB (la ligne qui contient `Error:`). 

```zsh
WARN  o.h.t.s.i.ExceptionHandlerLoggedImpl - GenerationTarget encountered exception accepting command : Error executing DDL "alter table if exists fournisseur modify column ville varchar(255)" via JDBC [(conn=1672) 
```

```zsh
WARN  o.m.j.m.server.ErrorPacket - Error: 1142-42000: ALTER command denied to user 'app'@'172.18.0.1' for table `compta`.`fournisseur`
```

> Qu'est-ce qui vous a protégé ? 

Je n'ai pas les droit pour modifier la table.

> Que se serait-il passé avec un compte qui a les droits ?

La colonne ville aurait été modifiée

> Comptez les requêtes par `factory().getStatistics().getPrepareStatementCount()`, après un `clear()` des statistiques.

![[Pasted image 20261023120136.png]]

>Concluez en deux phrases : qu'est-ce que le contexte de persistance, et combien de temps vit-il ici ?

Il vit jusqu'à un close().

## QUESTIONS TP06

> Recopiez le message que renvoie MariaDB. Pourquoi `validate` a-t-il accepté une entité qu'on ne peut pas insérer ?


```zsh
Exception in thread "main" org.hibernate.exception.ConstraintViolationException: could not execute statement [(conn=2044) Field 'id_fou' doesn't have a default value] [insert into article (actif,designation,prix,ref) values (?,?,?,?)]
```

-> Parce que le constructeur est bon, c'est la relation entre Article et Fournisseur qui n'est pas instaurée 

> Passez `delai` en `int`, relancez, recopiez le message. Quel bon le provoque, et pourquoi ? Revenez à `Integer`.

```zsh
Exception in thread "main" org.hibernate.PropertyAccessException: Null value was assigned to a property [class org.sebsy.compta.domain.Bon.delai] of primitive type: 'org.sebsy.compta.domain.Bon.delai' (setter)
```

Le bon qui provoque ce message d'erreur est celui dont le `délai = null`.
Il le provoque car `int` n'accepte pas les `null`. 

> Passez `statut` en `EnumType.ORDINAL`, relancez, recopiez la ligne `Schema validation`

```
 Schema validation: wrong column type encountered in column [statut] in table [bon]; found [varchar (Types#VARCHAR)], but expecting [tinyint (Types#TINYINT)]
```

> En deux phrases : pourquoi `Adresse` est-elle un objet-valeur et pas une entité ? Citez une autre donnée du domaine `compta` qui mériterait un `@Embeddable`, et dites pourquoi.

`Adresse` ne fait pas référence à une table directement mais à des champs.
`PrixTtc` mériterait un `@Embeddable` pour pouvoir gérer cela plus facilement.

## QUESTIONS TP08



---
# Spring

## QUESTIONS TP01 

> Copiez dans `NOTES.md` la ligne `Started ComptaApiApplication in … seconds` de votre terminal.

```zsh
Started ComptaApiApplication in 5.527 seconds (process running for 5.982)
```

> Interrogez la santé de l'application et copiez la réponse :


```bash
{"groups":["liveness","readiness"],"status":"UP"}
```


> **Prédiction** : l'application tourne, la base s'arrête. Que répond la même requête, code compris ? Écrivez-le, puis vérifiez :

Ça va plus marcher.

```bash
HTTP/1.1 503   
Content-Type: application/vnd.spring-boot.actuator.v3+json  
Transfer-Encoding: chunked  
Date: Tue, 24 Nov 2026 14:23:51 GMT  
Connection: close  
  
{"groups":["liveness","readiness"],"status":"DOWN"}
```


> Copiez **le premier** message qui donne la cause, pas le dernier, et dites en une phrase pourquoi le dernier égare.

```bash
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:4.1.2:run (default-cli) on project compta-api: Process terminated with exit code: 1 -> [Help 1]
```
> Copiez la ligne `Schema validation: …`. Dites qui a refusé, et pourquoi c'est une bonne nouvelle.

```bash
Schema validation: missing column [date_commande] in table [bon
```
C'est l'application qui a refusée. Parce que ça évite d'avoir des erreurs de bdd.

## QUESTIONS TP02 

> Lancez `./mvnw test` et notez combien de tests échouent.

5 tests sur 6 ont échoué.

> Relancez l'application et exécutez les trois appels de la démo du cours ; copiez les réponses :

```bash
C:\EPSI\B3\Java\compta-api>curl "localhost:8081/api/devis?prixHt=2.25&quantite=10"  
{"quantite":10,"ht":22.50,"ttc":27.00}  
C:\EPSI\B3\Java\compta-api>curl "localhost:8081/api/devis/3?prixHt=74.99"  
{"quantite":3,"ht":224.97,"ttc":269.96}  
C:\EPSI\B3\Java\compta-api>curl "localhost:8081/api/devis?quantite=3"  
{"timestamp":"2026-11-26T08:13:16.741Z","status":400,"error":"Bad Request","path":"/api/devis"}  
```

> **Prédiction** : que se passe-t-il au prochain démarrage ? Écrivez-le, relancez, puis copiez la ligne `Parameter 0 of constructor …` et les deux noms de beans qui la suivent.

Il ne va pas savoir quelle bean utiliser (RemiseAucune ou RemiseVolume). Il va falloir lui donner une priorité.

```bash
Parameter 0 of constructor in org.sebsy.compta.devis.DevisService required a single bean, but 2 were found:
	- remiseAucune: defined in file [C:\EPSI\B3\Java\compta-api\target\classes\org\sebsy\compta\devis\RemiseAucune.class]
	- remiseVolume: defined in file [C:\EPSI\B3\Java\compta-api\target\classes\org\sebsy\compta\devis\RemiseVolume.class]
```

> Rejouez le premier appel de l'étape 4 et copiez la réponse.

```zsh
C:\EPSI\B3\Java\compta-api>curl "localhost:8081/api/devis?prixHt=2.25&quantite=10"  
{"quantite":10,"ht":21.38,"ttc":25.66}
```

> **Prédiction** : combien d'éléments reçoit-il ? Vérifiez et notez.

Il reçoit deux éléments : RemiseAucune et RemiseVolume.

![[Pasted image 20261126095228.png]]

> Copiez le message, dites ce que le compilateur n'a pas vu

J'ai utilisé IntelliJ pour renommer, il n'y a eu aucun soucis.

> **Prédiction** : que donne le lancement sans profil, avec `dev`, avec `prod`, avec `dev,prod` ?

Sans profil : peut pas choisir, erreur
dev : Remise nulle
prod : RemiseVolume
dev, prod : peut pas choisir, erreur 

-> Sans profil 
```zsh
Parameter 0 of constructor in org.sebsy.compta.devis.DevisPublicService required a bean of type 'org.sebsy.compta.devis.remise.Remise' that could not be found.

The injection point has the following annotations:
	- @org.springframework.beans.factory.annotation.Qualifier("sansRemise")
```

-> dev
```zsh
curl "localhost:8081/api/devis?prixHt=2.25&quantite=10"  
{"quantite":10,"ht":22.50,"ttc":27.00}
```

-> prod
```zsh
Error creating bean with name 'devisPublicService' defined in file [C:\EPSI\B3\Java\compta-api\target\classes\org\sebsy\compta\devis\DevisPublicService.class]: Unsatisfied dependency expressed through constructor parameter 0: No qualifying bean of type 'org.sebsy.compta.devis.remise.Remise' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Qualifier("sansRemise")}
```
Enfaite il veut pas.

-> dev, prod
```zsh
Parameter 0 of constructor in org.sebsy.compta.devis.DevisService required a single bean, but 2 were found:
	- remiseVolume: defined in file [C:\EPSI\B3\Java\compta-api\target\classes\org\sebsy\compta\devis\remise\RemiseVolume.class]
	- sansRemise: defined in file [C:\EPSI\B3\Java\compta-api\target\classes\org\sebsy\compta\devis\remise\SansRemise.class]
```

