package ru.fusionsoft.iu.dereferencer.factories;

import org.junit.jupiter.api.Test;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.reference.internal.LocalReference;
import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;

import java.net.URI;

class ReferenceFactoryTest {


    @Test
    void  makeGitHubReference(){

    }

    @Test
    void makeLocalReference () throws InvalidReferenceException {
        Reference assertValue = new LocalReference(URI.create("W:/info-universe"));
        Reference expectingValue = ReferenceFactory.makeReference("W:/info-universe");
    }
}