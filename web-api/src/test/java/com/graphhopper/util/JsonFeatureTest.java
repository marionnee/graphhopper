package com.graphhopper.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonFeatureTest {

    //Test a comme but de s'assurer que validation est rejeter si la valeur est trop petit
    @Test
    public void givenShortIdName_WhenValidateId_ReturnsFalse(){
       boolean answer = JsonFeature.isValidId("a");
       assertEquals(answer, false);
    }

    //Ce test a comme but de s'assurer que la validation est rejeter si le id commence pas par in_
    @Test
    public void givenNameDoesntStartWithIN_WhenValidateId_ReturnsFalse(){
        boolean answer = JsonFeature.isValidId("answer");
        assertEquals(answer, false);
    }

    //Ce test a comme but de s'assurer que la validation est rejeter si il a un underscore après in_
    @Test
    public void givenNameHasUnderscores_WhenValidateId_ReturnsFalse(){
        boolean answer = JsonFeature.isValidId("in__");
        assertEquals(answer, false);
    }

    //Ce test a comme but de s'assurer que la validation est rejeter si il y a un keyword
    @Test
    public void givenNameHasKeyword_WhenValidateId_ReturnsFalse(){
        boolean answer = JsonFeature.isValidId("in_&");
        assertEquals(answer, false);
    }

    //Ce test a comme but de s'assurer que la validation passe quand tout les critère sont accepter
    @Test
    public void givenAcceptableName_WhenValidateId_ReturnsTrue(){
        boolean answer = JsonFeature.isValidId("in_test");
        assertEquals(answer, true);
    }
}
