package ru.fusionsoft.iu.dereferencer.managers;

import com.fasterxml.jackson.databind.JsonNode;
import ru.fusionsoft.iu.dereferencer.reference.Reference;

import java.io.File;
import java.io.IOException;

public interface Manager {

    public JsonNode getDocument(Reference ref) throws IOException;

    public File findFile(Reference path);
    public File getLastUsedFile();
}
