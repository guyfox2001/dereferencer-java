package ru.fusionsoft.iu.dereferencer.mappers.json;

import org.junit.jupiter.api.Test;
import ru.fusionsoft.iu.dereferencer.Dereferencer;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.factories.ReferenceFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public class JsonMapperTest {

    @Test
    public void RunSimplestJsonFile() throws InvalidReferenceException, IOException, URISyntaxException, CloneNotSupportedException {
        Dereferencer.getResolve(ReferenceFactory.makeReference("/DataHolder/generated-jsons/1.json"));
    }
}
