# V-V-TP4

## Adaptation du code

En vue de rationnaliser et généraliser la connexion et la gestion de l'authentification, j'ai rajouté deux classes helper dans le packages utils.
Celles-ci se chargent de gérer le driver et produises une méthode de login et de logout.
Avant chaque test un logout est fait afin de garantir un état non authentifié avant chaque passe de test.
Login doit etre appelé en début de méthode pour bénéficier de l'authentification.

Les tests ont été découpés en deux classes, apparences et droits sur authentification.

## Jmeter

Jmeter n'est pas concluant avec la configuration imposée, les requetes aboutissent toutes mais pas entièrement avec le temps de réponse max de 30 millisec.
Le fichier de configuration et els résultats bruts se trouve dans le répertoire jmeter.