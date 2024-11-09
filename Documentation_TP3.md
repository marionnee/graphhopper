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

## Flag 3:
### Abort VM on compilation failure = true
[Line 97-114](./.github/workflows/test.yml)

En activant cette flag, on interrompt l'exécution de la machine virtuelle en cas d'échec
à la compilation. Cela permet d'assurer que la machine virtuelle s'arrête lorsqu'une erreur de
compilation est détectée, garantissant ainsi que les tests ne sont exécutés que si le code est
dans un état valide.

## Flag 4:
### Eliminate allocations
[Line 116-133](./.github/workflows/test.yml)

En activant cette flag, on demande à la JVM d'éliminer les allocations inutiles d'objets pendant
l'exécution du programme. Cela peut réduire les coûts de performance tout en améliorant l'efficacité
du programme.

## Flag 5:
### BackgroundCompilation = true
[Line 135-152](./.github/workflows/test.yml)

En activant cette flag, on permet à la machine virtuelle de compiler en arrière-plan sans perturber
l'exécution principale de l'application. Cela améliore en particulier la réactivité des programmes
comme graphhoper qui traitent de nombreux calculs en temps réel.
