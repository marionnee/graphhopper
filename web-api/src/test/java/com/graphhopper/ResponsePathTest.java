package com.graphhopper;

import com.graphhopper.util.details.PathDetail;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponsePathTest {
    ResponsePath underTest = new ResponsePath();
    List<PathDetail> PATH_DETAILS = List.of(new PathDetail(1));
    Map<String, List<PathDetail>> MAP_DETAILS_1 = Map.of("1", PATH_DETAILS);
    Map<String, List<PathDetail>> MAP_DETAILS_2 = Map.of("2", PATH_DETAILS);

    Map<String, List<PathDetail>> MAP_DETAILS_3 = Map.of("1", PATH_DETAILS, "2", PATH_DETAILS);
    //Ce test a comme but de s'assurer que addPathDetails resort le bon map si c'étais null avait
    @Test
    public void givenDetailsIsAcceptable_WhenAddPathDatails_ThenAddPathDetails(){
        underTest.addPathDetails(MAP_DETAILS_3);

        assertEquals(underTest.getPathDetails(),MAP_DETAILS_3);
    }

    //Ce test a comme but de lncer une erreur si la grandeur du nouveau PathDetails et du vieux PathDetails n'est pas égal
    @Test
    public void givenDetailsIsNotEqualInSizeTooCurrentPathDetails_WhenAddPathDetails_ThenReturnIllegalStatementException() {
        underTest.addPathDetails(MAP_DETAILS_3);
        try {
            underTest.addPathDetails(MAP_DETAILS_1);
            assertEquals(1,  "error not thrown");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(),"Details have to be the same size");
        }
    }

    //Ce test a comme but de s'assurer que le resultats de deux details qui se font ajouter, avec la même grandeur, est une addition desdeux
    @Test
    public void givenDetailsTwice_whenAddDetails_ThenReturnsDetailsTwice(){
        underTest.addPathDetails(MAP_DETAILS_1);
        underTest.addPathDetails(MAP_DETAILS_2);

        assertEquals(underTest.getPathDetails(),MAP_DETAILS_3);
    }
}
