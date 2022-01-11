package ru.fusionsoft.iu.dereferencer.enums;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RefTypeTest {

    @Test
    void checkINTERNAL_ANCHOR() throws URISyntaxException {
        RefType assertValue = RefType.INTERNAL_ANCHOR;
        RefType expectedValue = RefType.checkFragmentType(new URI("#fragment"));
        assertEquals(expectedValue, assertValue);
    }
    @Test
    void checkINTERNAL_PATH() throws URISyntaxException {
        RefType assertValue = RefType.INTERNAL_PATH;
        RefType expectedValue = RefType.checkFragmentType(new URI("#/defs/props"));
        assertEquals(expectedValue, assertValue);
    }
    @Test
    void checkLOCAL_DOCUMENT() throws URISyntaxException {
        RefType assertValue = RefType.LOCAL_DOCUMENT;
        RefType expectedValue = RefType.checkFragmentType(new URI("Nomenclature.yaml"));
        assertEquals(expectedValue, assertValue);
    }
    @Test
    void checkLOCAL_DOCUMENT_ANCHOR() throws URISyntaxException {
        RefType assertValue = RefType.LOCAL_DOCUMENT_ANCHOR;
        RefType expectedValue = RefType.checkFragmentType(new URI("Nomenclature.yaml#fragment"));
        assertEquals(expectedValue, assertValue);
    }
    @Test
    void checkLOCAL_DOCUMENT_PATH() throws URISyntaxException {
        RefType assertValue = RefType.LOCAL_DOCUMENT_PATH;
        RefType expectedValue = RefType.checkFragmentType(new URI("Nomenclature.yaml#/defs/props"));
        assertEquals(expectedValue, assertValue);
    }
    @Test
    void checkEXTERNAL_DOCUMENT() throws URISyntaxException {
        RefType assertValue = RefType.EXTERNAL_DOCUMENT;
        RefType expectedValue = RefType.checkFragmentType(new URI("https://gitlab.fusionsoft.ru/fusionsoft-ru/iu/info-universe/-/blob/master/iuData/Sample-univ/Nomenclature.yaml"));
        assertEquals(expectedValue, assertValue);
    }
    @Test
    void checkEXTERNAL_DOCUMENT_ANCHOR() throws URISyntaxException {
        RefType assertValue = RefType.EXTERNAL_DOCUMENT_ANCHOR;
        RefType expectedValue = RefType.checkFragmentType(new URI("https://gitlab.fusionsoft.ru/fusionsoft-ru/iu/info-universe/-/blob/master/iuData/Sample-univ/Nomenclature.yaml" +
                "#fragment"));
        assertEquals(expectedValue, assertValue);
    }
    @Test
    void checkEXTERNAL_DOCUMENT_PATH() throws URISyntaxException {
        RefType assertValue = RefType.EXTERNAL_DOCUMENT_PATH;
        RefType expectedValue = RefType.checkFragmentType(new URI("https://gitlab.fusionsoft.ru/fusionsoft-ru/iu/info-universe/-/blob/master/iuData/Sample-univ/Nomenclature.yaml" +
                "#/defs/props"));
        assertEquals(expectedValue, assertValue);
    }
    @Test
    void checkIsLocal(){
        assertTrue(RefType.isLocal(URI.create("W://info-universe")));
    }
}