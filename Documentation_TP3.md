Aléanne Camiré 20239733,
Marion Absalon 20211423

# Cas d'étude : Graphhopper

## Flag 1:
### AllowVectorizeOnDemand = false
[Line 58-75](./.github/workflows/test.yml)

On ajoute cette flag, on s'assure que le code peut fonctioner, même si la vectorization ne fonctionne pas.
Ceci à donc pour but de s'assurer que le code est fonctionnelle dans tout environnement.

## Flag 2:
### UseAdaptiveSizePolicy = false
[Line 77-94](./.github/workflows/test.yml)

En ajoutant cette flag, on s'assure que le heap size est asser grand, même quand on n'ajuste pas la grandeur du heap dynamiquement
Ceci à donc pour but de s'assurer que le code est fonctionnelle même quand l'ajustement dynamique du heap ne fonctionne pas




