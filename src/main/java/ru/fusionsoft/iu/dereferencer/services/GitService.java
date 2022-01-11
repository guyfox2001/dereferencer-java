package ru.fusionsoft.iu.dereferencer.services;


import com.fasterxml.jackson.databind.JsonNode;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.reference.GitReference;

import java.io.IOException;

public interface GitService {
    JsonNode get(GitReference target) throws InvalidReferenceException, IOException;
}
