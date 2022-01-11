package ru.fusionsoft.iu.dereferencer.reference;

import ru.fusionsoft.iu.dereferencer.MD5;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;

import java.net.URI;

public abstract class GitReference extends LocalReference {

    protected String accessTOKEN;
    protected String pathToFile = "";
    protected String hashFileName = "";

    public GitReference(URI source, String TOKEN) throws InvalidReferenceException {
        super(source);
        accessTOKEN = TOKEN;
        hashFileName = MD5.getMD5(reference.toString()) + ".yaml";
    }

    public String getHashFileName() {
        return hashFileName;
    }

    public String getAccessTOKEN() {
        return accessTOKEN;
    }

    public void setAccessTOKEN(String accessTOKEN) {
        this.accessTOKEN = accessTOKEN;
    }

    public String getPathToFile() {
        return pathToFile;
    }


}
