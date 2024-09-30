Aléanne Camiré 20239733



## Test 1:
### ResponsePath.addPathDetails()

[Test](./web-api/src/test/java/com/graphhopper/ResponsePathTest.java) et 
[Main](./web-api/src/main/java/com/graphhopper/ResponsePath.java)

On ajoute des tests pour addPathDetails parce que la logique n'est pas exactement ce qui est attendu. Un add X aurais normalement le but d'ajouter les nouveaux Path Details au vieux path details. 
Par contre, cette implementatiton est un peut plus compliquer, car si il y a deja des pathDetails, il doit avoir le même nombre de novueaux details que de vieux, et ceci est un concept facile a manquer en essayer de modifier la fonction
Le programmer qui a crée la fonction a même pris le temps d'écrire une petite description pour s'assurer que les gens comprene. Les tests sont une autre barrière de protections contre une modification fautive

## Test 2:
### JsonFeature.isValidId()

[Test](./web-api/src/test/java/com/graphhopper/util/JsonFeatureTest.java) et
[Main](./web-api/src/main/java/com/graphhopper/util/JsonFeature.java)

On ajoute des tests car il est extremement important que le validateur des ids fonctionne. On ne veut pas accepter un ids qui est inaceptable.
Les tests on donc le but d'ajouter un autre niveaux de protection a tout modification qui pourrais être fait dans le code, qui briserais le code/les tests ajouter.
Donc, tout changement dans le code serais surement intentionnelle, car les tests devront être modifier aussi

## Test 3:
### Helper.toObject()

[Test](./web-api/src/test/java/com/graphhopper/util/HelperTest.java) et
[Main](./web-api/src/main/java/com/graphhopper/util/Helper.java)

On ajoute des tests a toObject car une fonction comme celle ci peut facilement avoir des erreurs d'implementation. Si on transforme une valeur qui devrait être un int en float, on pourrais accidentelement crée beaucoup de problème

*Note que pendant l'ajout du code, on a trouver un problème d'implementation. Les double sont mal implementer, et ceci cause tout fonction qui devrait être mis en double d'être mis en float, ce qui change le resultats

## Test 4:
### CustomModel.addAreas()
[Test](./web-api/src/test/java/com/graphhopper/util/CustomModelTest.java) et
[Main](./web-api/src/main/java/com/graphhopper/util/CustomModel.java)

On ajoute des tests à addAreas car on veut s'assurer que le code ajoute biens les areas, tout en s'assurant que les exception sont analyser et rejeter.
On veut s'assurer que tout les addAreas valid sont accepter, et ajouter a areas sans erreur.

## Test 5:
### CustomModel.getAreasAsMap()
[Test](./web-api/src/test/java/com/graphhopper/util/CustomModelTest.java) et
[Main](./web-api/src/main/java/com/graphhopper/util/CustomModel.java)

On ajoute des tests à getAreasAsMap car on veut s'assurer que le get, comme le add, est bien implementer, et rest bien implementer. 
On veut s'assurer que les exception sont traiter et que le resultats soit lui qui est attendue




