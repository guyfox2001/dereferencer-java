package ru.fusionsoft.iu.dereferencer.managers;

import com.fasterxml.jackson.databind.JsonNode;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;

import java.io.File;
import java.io.IOException;

public interface Manager {

    JsonNode getDocument(Reference ref) throws IOException, InvalidReferenceException;

    File findFile(Reference path);
    File getLastUsedFile();
}
