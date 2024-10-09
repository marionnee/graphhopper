package com.graphhopper.util;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class TurnCostsConfigTest {

    // Ici nous allons tester la fonction check qui vérifie les types de transports (restrictions)
    static Stream<List<String>> validRestrictionsProvider() {
        return Stream.of(
                List.of("carpool", "bus", "atv"),
                List.of("minibus", "mofa", "moped", "motorcar", "motorcycle", "motor_vehicle",
                        "motorhome"),
                List.of("foot")
        );
    }

    // Cas null qui doit renvoyer une exception
    @Test
        public void TurnCosts_check_Null() {
            TurnCostsConfig turnCostsClass = new TurnCostsConfig();
            IllegalArgumentException thrown = assertThrows( IllegalArgumentException.class,
                    () -> turnCostsClass.check(null)
            );
            assertEquals("turn_costs cannot have empty vehicle_types", thrown.getMessage());
        }

        // Cas liste vide qui doit renvoyer une exception
        @Test
        public void turnCosts_Check_EmptyList() {
            TurnCostsConfig turnCostsClass = new TurnCostsConfig();
            IllegalArgumentException thrown = assertThrows(
                    IllegalArgumentException.class,
                    () -> turnCostsClass.check(Arrays.asList())
            );
            assertEquals("turn_costs cannot have empty vehicle_types", thrown.getMessage());
        }

        // On s'assure que les types de transports invalides ne soient pas acceptés
    @Test
    public void turnCosts_Check_Invalid_First() {
        TurnCostsConfig turnCostsClass = new TurnCostsConfig();

        List<String> input = List.of("batmobile", "bus");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> turnCostsClass.check(input)
        );

        assertEquals("Currently we do not support the restriction: batmobile",
                thrown.getMessage());
    }


    // On vérifie qu'une exception est renvoyée si un des types de transport est invalide
    // meme si une partie des types de transport est valide
    @Test
    public void turnCosts_Check_Invalid_Last() {
        TurnCostsConfig turnCostsClass = new TurnCostsConfig();

        List<String> input = List.of("bus", "carpool", "carosse_enchante");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> turnCostsClass.check(input)
        );

        assertEquals("Currently we do not support the restriction: carosse_enchante",
                thrown.getMessage());
    }

    // On s'assure du comportement normal de la fonction, peut importe la longueur des restrictions
    @ParameterizedTest
        @MethodSource("validRestrictionsProvider")
        public void turnCosts_Check_valid(List<String> input) {
            TurnCostsConfig turnCostsClass = new TurnCostsConfig();
            assertEquals(input, turnCostsClass.check(input));
        }


        // Ici on teste la methode qui va regarder l'état des couts de tournants (turnCost)
    static Stream<List<Integer>> validTurnsProvider() {
        return Stream.of(
                List.of(25, 36),
                List.of(-89, 11),
                List.of(15, -14),
                List.of(-515, -114),
                List.of(0, 264),
                List.of(-267, 0)
        );
    }

    // On teste le comportement normal de la fonction avec des entrées qui ont un cout de Turn
    @ParameterizedTest
    @MethodSource("validTurnsProvider")
    public void hasLeftRightStraightCosts(List<Integer> input) {
        TurnCostsConfig turnCostsClass = new TurnCostsConfig();
        turnCostsClass.setLeftCosts(input.get(0));
        turnCostsClass.setRightCosts(input.get(1));
        assertTrue(turnCostsClass.hasLeftRightStraightCosts());
    }


    // On teste lorsqu'il n'y a pas de cout de turn
    @Test
    public void hasLeftRightStraightCosts_False() {
        TurnCostsConfig turnCostsClass = new TurnCostsConfig();
        turnCostsClass.setLeftCosts(0);
        turnCostsClass.setRightCosts(0);
        turnCostsClass.setStraightCosts(0);
        assertFalse(turnCostsClass.hasLeftRightStraightCosts());
    }
    }
