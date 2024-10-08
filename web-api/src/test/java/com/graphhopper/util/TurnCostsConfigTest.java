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
    static Stream<List<String>> validRestrictionsProvider() {
        return Stream.of(
                List.of("carpool", "bus", "atv"),
                List.of("minibus", "mofa", "moped", "motorcar", "motorcycle", "motor_vehicle", "motorhome"),
                List.of("foot")
        );
    }
    @Test
        public void TurnCosts_check_Null() {
            TurnCostsConfig turnCostsClass = new TurnCostsConfig();
            IllegalArgumentException thrown = assertThrows( IllegalArgumentException.class,
                    () -> turnCostsClass.check(null)
            );
            assertEquals("turn_costs cannot have empty vehicle_types", thrown.getMessage());
        }

        @Test
        public void turnCosts_Check_EmptyList() {
            TurnCostsConfig turnCostsClass = new TurnCostsConfig();
            IllegalArgumentException thrown = assertThrows(
                    IllegalArgumentException.class,
                    () -> turnCostsClass.check(Arrays.asList())
            );
            assertEquals("turn_costs cannot have empty vehicle_types", thrown.getMessage());
        }

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


    @Test
    public void turnCosts_Check_Invalid_Last() {
        TurnCostsConfig turnCostsClass = new TurnCostsConfig();

        List<String> input = List.of("bus", "carpool", "carosse");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> turnCostsClass.check(input)
        );

        assertEquals("Currently we do not support the restriction: carosse",
                thrown.getMessage());
    }

    @ParameterizedTest
        @MethodSource("validRestrictionsProvider")
        public void turnCosts_Check_valid(List<String> input) {
            TurnCostsConfig turnCostsClass = new TurnCostsConfig();
            assertEquals(input, turnCostsClass.check(input));
        }
    }
