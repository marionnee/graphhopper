package com.graphhopper.util;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
public class RoundaboutInstructionTest {
    // Ici on va tester la fonction qui va recupérer et transmettre les instructions roundabout
    static Stream<RoundaboutInstruction> roundaboutProvider() {
        return Stream.of(
                new RoundaboutInstruction(2, "test", new PointList(3, false)),
                new RoundaboutInstruction(3, "leTest", new PointList(5, true)),
                new RoundaboutInstruction(140, "t@ÉestLe", new PointList(4685, false)),
                new RoundaboutInstruction(6, "tout8Test", new PointList(78, true))
        );
    }

    // On vérifie que la fonction fonctionne avec des valeurs différentes
    @ParameterizedTest
    @MethodSource("roundaboutProvider")
    public void getExtraInfoJSON_Entries(RoundaboutInstruction input) {

        input.setExitNumber(1);
        input.setExited();

        Map<String, Object> result = input.getExtraInfoJSON();

        assertNotNull(result);
        assertEquals(1, result.get("exit_number"));
        assertTrue((Boolean) result.get("exited"));
    }
}

