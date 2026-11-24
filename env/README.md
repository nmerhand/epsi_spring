# Environnement de TP — Atelier Spring Boot

La base `compta` du module Accès aux données, dans un conteneur MariaDB 12.3, avec un Adminer.
Même schéma, mêmes données de départ : les entités du TP 09 d'Accès aux données la mappent telle quelle.

## Démarrer

```bash
cd env
docker compose up -d
docker compose exec db mariadb -uapp -papp compta -e "SELECT COUNT(*) FROM fournisseur"
```

La dernière commande doit afficher `4`. Si elle échoue alors que le conteneur est `healthy`, le
premier démarrage s'est interrompu : repartez de zéro avec `docker compose down -v` puis
`docker compose up -d`.

- **Adminer** : <http://localhost:8080>, serveur `db`, utilisateur `root`, mot de passe `root`.
  C'est lui qui occupe le port 8080 : compta-api écoute sur 8081.
- **Ligne de commande** : `docker compose exec db mariadb -uapp -papp compta`

## Si l'environnement d'Accès aux données tourne encore

Les deux environnements prennent les mêmes ports : 3306 pour la base, 8080 pour Adminer. Arrêtez
l'ancien depuis son dossier `env/` (s'il date d'avant son nom de projet, son README dit comment) :

```bash
docker compose down
```

Ou lancez celui-ci sur d'autres ports. Exportez-les d'abord, dans le terminal où vous lancerez
aussi compta-api, qui lit le même `DB_PORT` :

```bash
export DB_PORT=3307 ADMINER_PORT=8090
docker compose up -d
```

Sous PowerShell : `$env:DB_PORT=3307; $env:ADMINER_PORT=8090`. Adminer est alors sur
<http://localhost:8090>, un port qu'aucun TP de l'atelier n'utilise.

Ces variables ne valent que pour ce terminal. Oublier `ADMINER_PORT` ne gêne pas la base : seul
Adminer refuse de démarrer, sur `port is already allocated`. Lancer compta-api d'un autre terminal,
sans `DB_PORT`, est plus traître : il se connecte à la base de l'ancien environnement, qui a le même
nom et le même compte, sans aucun message d'erreur.

## Remettre la base à neuf

```bash
docker compose down -v
docker compose up -d
```

`-v` supprime le volume : toutes vos insertions disparaissent, et les données de départ sont
rechargées.

## Ce que le script crée aussi

Hérités d'Accès aux données, et inutilisés ici : la base `compta_ref`, copie de référence, et un
compte `migrator`. compta-api ne se sert que de `compta` et du compte `app`.

## D'où viennent les données

`data/01-compta.sql` est une copie octet pour octet de celle d'Accès aux données. Ne la modifiez
pas ici : une correction se fait à la source, puis se recopie.
