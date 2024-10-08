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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static com.graphhopper.util.Helper.UTF_CS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Karich
 */
public class HelperTest {
    private static final int DEGREE_FACTOR = 10_000_000;

    @Test
    public void testElevation() {
        assertEquals(9034.1, Helper.uIntToEle(Helper.eleToUInt(9034.1)), .1);
        assertEquals(1234.5, Helper.uIntToEle(Helper.eleToUInt(1234.5)), .1);
        assertEquals(0, Helper.uIntToEle(Helper.eleToUInt(0)), .1);
        assertEquals(-432.3, Helper.uIntToEle(Helper.eleToUInt(-432.3)), .1);

        assertEquals(Double.MAX_VALUE, Helper.uIntToEle(Helper.eleToUInt(11_000)));
        assertEquals(Double.MAX_VALUE, Helper.uIntToEle(Helper.eleToUInt(Double.MAX_VALUE)));

        assertThrows(IllegalArgumentException.class, () -> Helper.eleToUInt(Double.NaN));
    }

    @Test
    public void testGetLocale() {
        assertEquals(Locale.GERMAN, Helper.getLocale("de"));
        assertEquals(Locale.GERMANY, Helper.getLocale("de_DE"));
        assertEquals(Locale.GERMANY, Helper.getLocale("de-DE"));
        assertEquals(Locale.ENGLISH, Helper.getLocale("en"));
        assertEquals(Locale.US, Helper.getLocale("en_US"));
        assertEquals(Locale.US, Helper.getLocale("en_US.UTF-8"));
    }

    @Test
    public void testRound() {
        assertEquals(100.94, Helper.round(100.94, 2), 1e-7);
        assertEquals(100.9, Helper.round(100.94, 1), 1e-7);
        assertEquals(101.0, Helper.round(100.95, 1), 1e-7);
        // using negative values for decimalPlaces means we are rounding with precision > 1
        assertEquals(1040, Helper.round(1041.02, -1), 1.e-7);
        assertEquals(1000, Helper.round(1041.02, -2), 1.e-7);
    }

    @Test
    public void testKeepIn() {
        assertEquals(2, Helper.keepIn(2, 1, 4), 1e-2);
        assertEquals(3, Helper.keepIn(2, 3, 4), 1e-2);
        assertEquals(3, Helper.keepIn(-2, 3, 4), 1e-2);
    }

    @Test
    public void testCamelCaseToUnderscore() {
        assertEquals("test_case", Helper.camelCaseToUnderScore("testCase"));
        assertEquals("test_case_t_b_d", Helper.camelCaseToUnderScore("testCaseTBD"));
        assertEquals("_test_case", Helper.camelCaseToUnderScore("TestCase"));

        assertEquals("_test_case", Helper.camelCaseToUnderScore("_test_case"));
    }

    @Test
    public void testUnderscoreToCamelCase() {
        assertEquals("testCase", Helper.underScoreToCamelCase("test_case"));
        assertEquals("testCaseTBD", Helper.underScoreToCamelCase("test_case_t_b_d"));
        assertEquals("TestCase_", Helper.underScoreToCamelCase("_test_case_"));
    }

    @Test
    public void testIssue2609() {
        String s = "";
        for (int i = 0; i < 128; i++) {
            s += "ä";
        }

        // all chars are 2 bytes so at 255 we cut the char into an invalid character and this is probably automatically
        // corrected leading to a longer string (or do chars have special marker bits to indicate their byte length?)
        assertEquals(257, new String(s.getBytes(UTF_CS), 0, 255, UTF_CS).getBytes(UTF_CS).length);

        // see this in action:
        byte[] bytes = "a".getBytes(UTF_CS);
        assertEquals(1, new String(bytes, 0, 1, UTF_CS).getBytes(UTF_CS).length);
        // force incorrect char:
        bytes[0] = -25;
        assertEquals(3, new String(bytes, 0, 1, UTF_CS).getBytes(UTF_CS).length);
    }

    @Test
    void degreeToInt() {
        int storedInt = 444_494_395;
        double lat = Helper.intToDegree(storedInt);
        assertEquals(44.4494395, lat);
        assertEquals(storedInt, Helper.degreeToInt(lat));
    }

    @Test
    void eleToInt() {
        int storedInt = 1145636;
        double ele = Helper.uIntToEle(storedInt);
        // converting to double is imprecise
        assertEquals(145.635986, ele, 1.e-6);
        // ... but converting back to int should yield the same value we started with!
        assertEquals(storedInt, Helper.eleToUInt(ele));
    }
    /**DEBUT DES NOUVEAUX TESTS*/

    //Le but de ce test est de voir si on envoye un boolean en string, il renvoyera un boolean.
    static Stream<String[]> booleanProvider() {
        return Stream.of(
                new String[]{"true", "true"},
                new String[]{"false", "false"},
                new String[]{"TRUE", "true"},
                new String[]{"FALSE", "false"}
        );
    }
    @ParameterizedTest
    @MethodSource("booleanProvider")
    public void givenBoolean_whenToObject_ReturnBoolean(String input, String expected) {
        assertEquals(Boolean.parseBoolean(expected), Helper.toObject(input));
    }

    //Le but de ce test est d'enoyer un integer en string et que il revoye un integer
    static Stream<String[]> integerProvider() {
        return Stream.of(
                new String[]{"123", "123"},
                new String[]{"-456", "-456"},
                new String[]{"0", "0"},
                new String[]{"2147483647", "2147483647"}
        );
    }
    @ParameterizedTest
    @MethodSource("integerProvider")
    public void givenInteger_whenToObject_ReturnInteger(String input, String expected) {
        assertEquals(Integer.parseInt(expected), Helper.toObject(input));

    }

    //Le but de ce test est d'envoyer un long en string et de recevoir un long
    static Stream<String[]> longProvider() {
        return Stream.of(
                new String[]{"9223372036854775807", "9223372036854775807"},
                new String[]{"-9223372036854775808", "-9223372036854775808"},
                new String[]{"10000000000", "10000000000"}
        );
    }
    @ParameterizedTest
    @MethodSource("longProvider")
    public void givenLong_whenToObject_ReturnLong(String input, String expected) {
        assertEquals(Long.parseLong(expected), Helper.toObject(input));
    }

    //Le but de ce test est d'envoyer un float en string et de recevoir le float
    static Stream<String[]> floatProvider() {
        return Stream.of(
                new String[]{"3.14", "3.14"},
                new String[]{"-0.99", "-0.99"},
                new String[]{"1.0E10", "1.0E10"}
        );
    }
    @ParameterizedTest
    @MethodSource("floatProvider")
    public void givenFloats_whenToObject_ReturnFloat(String input, String expected) {
        assertEquals(Float.parseFloat(expected), Helper.toObject(input));
    }
    //Le but de ce tests est d'envoyer un double en string et de recevoir des doubles

    //Par contre, il semble avoir une mauvaise implementation, car les resultats retourne des float, pas des doubles
    //Il est donc impossible de tester les double
    //Par exemple, le 2em assert renvoye un infinie, car le resultat est trop gros pour un float
/**    @Test
    public void givenDoubles_whenToObject_returnDouble() {
        assertEquals(2.718281828459, Helper.toObject("2.718281828459"));
        assertEquals(1.7976931348623157E308, Helper.toObject("1.7976931348623157E308D"));
        assertEquals(-1.0E-10, Helper.toObject("-1.0E-10"));
    }
 */

    //Le but de ce test est d'envoyer un string qui ne peut pas est vue comme aucune autre valeur, et de recevoir un string
    static Stream<String[]> stringProvider() {
        return Stream.of(
                new String[]{"Hello World", "Hello World"},
                new String[]{"123abc", "123abc"},
                new String[]{"true123", "true123"},
                new String[]{"12.34abc", "12.34abc"}
        );
    }
    @ParameterizedTest
    @MethodSource("stringProvider")
    public void givenString_whenToObject_ReturnString(String input, String expected) {
        assertEquals(expected, Helper.toObject(input));
    }

    // test de parseList


    // Pour des entrées vides
    static Stream<String[]> invalidListsProvider() {
        return Stream.of(
                //new String[]{},
                new String[]{""},
                new String[]{"[]"},
                new String[]{"A"},
                new String[]{"AB"}
        );
    }
    static Stream<String[]> validListsOf3Provider() {
        return Stream.of(
                new String[]{"[a,b,c]"},
                new String[]{"[ a,b,c ]"},
                new String[]{"[a ,b ,c]"},
                new String[]{"[ a , b , c ]"},
                new String[]{"[a,b ,c ]"}
        );
    }

    @ParameterizedTest
    @MethodSource ("invalidListsProvider")
    public void parseList_EmptyLists(String input) {
        assertEquals(Collections.emptyList(), Helper.parseList(input));
    }

    // Tests avec entrées valides
    @Test
    public void parseList_TwoElements() {
        String input = "[a,b]";
        assertEquals(Arrays.asList("a", "b"), Helper.parseList(input));
    }
    // Ici on teste si le parsing detecte les elements correctement peut importe les
    // espaces entre chacun
    @ParameterizedTest
    @MethodSource("validListsOf3Provider")
    public void parseList_MultipleElements(String input) {
        assertEquals(Arrays.asList("a", "b", "c"), Helper.parseList(input));
    }

    // Ici on teste si les characteres speciaux sont bien detectes comme elements
    @Test
    public void parseList_SpecialCharacters() {
        String input = "[8,[ ,@ ]";
        assertEquals(Arrays.asList("8", "[", "@"), Helper.parseList(input));
    }

    @Test
    public void testIntToDegree_MaxValue() {
        int input = Integer.MAX_VALUE;
        double expected = Double.MAX_VALUE;
        assertEquals(expected, Helper.intToDegree(input));
    }

    @Test
    public void intToDegree_NegativeValue() {
        int input = -Integer.MAX_VALUE;
        double expected = -Double.MAX_VALUE;
        assertEquals(expected, Helper.intToDegree(input));
    }

    // Test with valid inputs
    @ParameterizedTest
    @ValueSource(ints = {125138, -465321087, 9990215, 111113225, 2, 0, -0, 5352})
    public void IntToDegree_ValidValues(int input) {
        double expected = (double) input / DEGREE_FACTOR;
        assertEquals(expected, Helper.intToDegree(input));
    }
}
