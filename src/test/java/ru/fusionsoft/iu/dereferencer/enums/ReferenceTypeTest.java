package ru.fusionsoft.iu.dereferencer.enums;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReferenceTypeTest {

    @Test
    void checkIsLocal(){
        assertTrue(ReferenceType.isLocal(URI.create("W://info-universe")));
    }
}