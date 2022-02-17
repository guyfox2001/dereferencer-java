package ru.fusionsoft.iu.dereferencer.reference.external;

import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.factories.ReferenceFactory;
import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class GitLabReference extends GitReference {

    protected String pathToFileEncoded;
    protected Integer projectId;
    protected String branch;

    public GitLabReference(URI source, String TOKEN) throws InvalidReferenceException {
        super(source, TOKEN);
    }

    @Override
    protected void _parseUri(URI source) throws InvalidReferenceException {

        if (source.getQuery() == null) throw new InvalidReferenceException("Wrong query params.");

        String[] splitedPath = source.getRawPath().split("/");

        if (splitedPath.length < 8) throw new InvalidReferenceException("Wrong endpoint for api");

        projectId = Integer.parseInt(splitedPath[4]);
        pathToFileEncoded = splitedPath[7];

        try {
            pathToFile = URLDecoder.decode(pathToFileEncoded, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        fileName = pathToFile.substring(pathToFile.lastIndexOf('/') + 1);

        String[] splitedQuery = source.getQuery().split("&");

        for (String it : splitedQuery){
            if (it.contains("ref=")) branch = it.substring(it.lastIndexOf("=") + 1);
        }

    }

    @Override
    public Reference getRel(String relUri) throws InvalidReferenceException {
        return getRel(ReferenceFactory.makeReference(relUri));
    }

    @Override
    public Reference getRel(Reference ref) throws InvalidReferenceException {
        if(ref.getUri().isAbsolute()) return ReferenceFactory.makeReference(ref.getUri(), null);

        try {
            return ReferenceFactory.makeGitLabReference(
                    reference.getHost(),
                    projectId,
                    branch,
                    URI.create(pathToFile).resolve(ref.getUri()).toString(),
                    ref.getInternal(),
                    accessTOKEN
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public String getBranch() {
        return branch;
    }
}
