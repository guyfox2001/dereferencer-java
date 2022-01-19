package ru.fusionsoft.iu.dereferencer.reference.external;

import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.factories.ReferenceFactory;
import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;

import java.net.URI;

public class GitHubReference extends GitReference {

    protected String user = "";
    protected String repo = "";
    protected String branch = "";


    public GitHubReference(URI source, String TOKEN) throws InvalidReferenceException {
        super(source, TOKEN);
        if (!reference.getHost().equals("raw.githubusercontent.com"))
            throw new InvalidReferenceException("The host is specified incorrectly.");
        if (!reference.getScheme().equals("https"))
            throw new InvalidReferenceException("The scheme is specified incorrectly");
    }

    @Override
    protected void _parceUri(URI source) throws InvalidReferenceException {
        String[] splited =  source.getPath().split("/");

        if (splited.length < 5)
            throw new InvalidReferenceException("Wrong endpoint for api");

        user = splited[1];
        repo = splited[2];
        branch = splited[3];

        for (int i= 4; ; i++){
            if (i + 1 == splited.length) {
                fileName = splited[i];
                pathToFile += splited[i];
                break;
            }
            pathToFile+= splited[i] + "/";
        }
    }

    @Override
    public Reference getRel(String relUri) throws InvalidReferenceException {
        return super.getRel(relUri);
    }

    @Override
    public Reference getRel(Reference ref) throws InvalidReferenceException {
        return ReferenceFactory.makeReference(super.getRel(ref).getUri(), accessTOKEN);
    }

    public String getUser() {
        return user;
    }

    public String getRepo() {
        return repo;
    }

    public String getBranch() {
        return branch;
    }
}
