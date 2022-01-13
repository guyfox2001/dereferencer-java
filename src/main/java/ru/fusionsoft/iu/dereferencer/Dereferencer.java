package ru.fusionsoft.iu.dereferencer;

import com.fasterxml.jackson.databind.JsonNode;
import ru.fusionsoft.iu.dereferencer.builders.impl.FragmentBuilder;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;

import java.io.IOException;
import java.net.URISyntaxException;

public class Dereferencer {
    private static FragmentBuilder fragmentBuilder;
    public static JsonNode getResolve(Reference ref) throws IOException, URISyntaxException, InvalidReferenceException, CloneNotSupportedException {
        if (fragmentBuilder == null){
            fragmentBuilder = new FragmentBuilder(ref);
        }
        return fragmentBuilder.build();
    }

}
