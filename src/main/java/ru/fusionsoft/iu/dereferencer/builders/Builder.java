package ru.fusionsoft.iu.dereferencer.builders;

import org.eclipse.jgit.api.errors.GitAPIException;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Builder {
    public Object build() throws URISyntaxException, IOException, GitAPIException, InvalidReferenceException, CloneNotSupportedException;
}
