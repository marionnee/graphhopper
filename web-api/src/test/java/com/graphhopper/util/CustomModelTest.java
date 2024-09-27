/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.graphhopper.util;

import com.graphhopper.json.Statement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.util.Iterator;
import java.util.Map;

import static com.graphhopper.json.Statement.*;
import static com.graphhopper.json.Statement.Op.MULTIPLY;

public class CustomModelTest {
    private CustomModel underTest = new CustomModel(); // Assuming your class is called AreaManager


    @Test
    public void testMergeComparisonKeys() {
        CustomModel truck = new CustomModel();
        truck.addToPriority(If("max_width < 3", MULTIPLY, "0"));
        CustomModel car = new CustomModel();
        car.addToPriority(If("max_width<2", MULTIPLY, "0"));
        CustomModel bike = new CustomModel();
        bike.addToPriority(If("max_weight<0.02", MULTIPLY, "0"));

        assertEquals(2, CustomModel.merge(bike, car).getPriority().size());
        assertEquals(1, bike.getPriority().size());
        assertEquals(1, car.getPriority().size());
    }

    @Test
    public void testMergeElse() {
        CustomModel truck = new CustomModel();
        truck.addToPriority(If("max_width < 3", MULTIPLY, "0"));

        CustomModel car = new CustomModel();
        car.addToPriority(If("max_width < 2", MULTIPLY, "0"));

        CustomModel merged = CustomModel.merge(truck, car);
        assertEquals(2, merged.getPriority().size());
        assertEquals(1, car.getPriority().size());
    }

    @Test
    public void testMergeEmptyModel() {
        CustomModel emptyCar = new CustomModel();
        CustomModel car = new CustomModel();
        car.addToPriority(If("road_class==primary", MULTIPLY, "0.5"));
        car.addToPriority(ElseIf("road_class==tertiary", MULTIPLY, "0.8"));

        Iterator<Statement> iter = CustomModel.merge(emptyCar, car).getPriority().iterator();
        assertEquals("0.5", iter.next().value());
        assertEquals("0.8", iter.next().value());

        iter = CustomModel.merge(car, emptyCar).getPriority().iterator();
        assertEquals("0.5", iter.next().value());
        assertEquals("0.8", iter.next().value());
    }

    //The 5 following tests are for addAreas

    //Ce test a comme but de regarder si le addAreas marche correctement dans des circonstance normal
    @Test
    public void given2Areas_whenAddAreas_ReturnsTwoAreas() {
        JsonFeatureCollection externalAreas = new JsonFeatureCollection();
        externalAreas.features.add(new JsonFeature("1", "1",null,null,null));
        externalAreas.features.add(new JsonFeature("2", "2", null,null,null));
        underTest.addAreas(externalAreas);

        assertEquals(2, underTest.getAreas().getFeatures().size());
    }

    //Ce test a comme but de voir si la fonction renvoye une erreur quand le id est invalid
    @Test
    public void givenInvalidID_whenAddAreas_ReturnIllegalArgument() {
        JsonFeatureCollection externalAreas = new JsonFeatureCollection();
        externalAreas.features.add(new JsonFeature("invalid-id!","1",null,null,null));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            underTest.addAreas(externalAreas);
        });

        assertEquals("The area 'invalid-id!' has an invalid id. Only letters, numbers and underscore are allowed.", exception.getMessage());
    }

    //Ce test a comme but de voir si la fonction renvoye une erreur quand 2 area pareil sont envoyer en même temps
    @Test
    public void given2ExactAreas_whenAddAreas_returnsIllegalArgumentException() {
        JsonFeatureCollection externalAreas = new JsonFeatureCollection();
        externalAreas.features.add(new JsonFeature("1", "1",null,null,null));
        externalAreas.features.add(new JsonFeature("1", "1",null,null,null));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            underTest.addAreas(externalAreas);
        });

        assertEquals("area 1 already exists", exception.getMessage());
    }

    //Ce test a comme but de voir si 1 id valid et 1 id non-valid renvoye une erreur
    @Test
    public void given1ValidIdAndOneInvalidId_whenAddAreas_returnIllegalArgument() {
        JsonFeatureCollection externalAreas = new JsonFeatureCollection();
        externalAreas.features.add(new JsonFeature("valid_id","1",null,null,null));
        externalAreas.features.add(new JsonFeature("invalid-id!", "1",null,null,null));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            underTest.addAreas(externalAreas);
        });

        assertEquals("The area 'invalid-id!' has an invalid id. Only letters, numbers and underscore are allowed.", exception.getMessage());
    }

    //Ce test a comme but de voir si on renvoye une erreur quand deux areas pareil sont envoyer a different moments
    @Test
    public void givenNewAreasExactlyLikePastOne_whenAddAreas_ReturnIllegalArgument() {
        JsonFeature feature = new JsonFeature("1","1",null,null,null);
        underTest.getAreas().getFeatures().add(feature);
        JsonFeatureCollection externalAreas = new JsonFeatureCollection();
        externalAreas.features.add(new JsonFeature("1","1",null,null,null));


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            underTest.addAreas(externalAreas);
        });

        assertEquals("area 1 already exists", exception.getMessage());
    }

    //The 3 following tests are for getAreasAsMap

    //Ce test a comme but de voir si getAreasAsMap fonctionne dans des cironstance normal
    @Test
    public void givenUniqueIds_whenGetAreasAsMap_ReturnsUniqueAreas() {
        JsonFeatureCollection areas = new JsonFeatureCollection();
        areas.features.add(new JsonFeature("1", "", null,null,null));
        areas.features.add(new JsonFeature("2", "", null,null,null));
        areas.features.add(new JsonFeature("3", "", null,null,null));

        Map<String, JsonFeature> result = underTest.getAreasAsMap(areas);

        assertEquals(3, result.size());
        assertNotNull(result.get("1"));
        assertNotNull(result.get("2"));
        assertNotNull(result.get("3"));
    }

    //Ce test a comme but de voir si une erreur est renvoyer quand on trouve un duplicate area
    @Test
    public void givenDuplicateIds_whenGetAreasAsMap_ReturnIllegalArgument() {
        JsonFeatureCollection areas = new JsonFeatureCollection();
        areas.features.add(new JsonFeature("1", "", null,null,null));
        areas.features.add(new JsonFeature("1", "",null,null,null));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            underTest.getAreasAsMap(areas);
        });

        assertEquals("Cannot handle duplicate area 1", exception.getMessage());
    }

    //Ce test a comme but de voir si le resultats est vide quand le areas est vide
    @Test
    public void givenEmptyList_whenGetAreasAsMap_ReturnEmptyMap() {
        JsonFeatureCollection areas = new JsonFeatureCollection();

        Map<String, JsonFeature> result = underTest.getAreasAsMap(areas);

        assertEquals(Map.of(), result);
    }
}

