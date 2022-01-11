package ru.fusionsoft.iu.dereferencer.reference;

import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;

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
    protected void _parceUri(URI source) throws InvalidReferenceException {

        if (source.getQuery() == null) throw new InvalidReferenceException("Wrong query params.");

        String splitedPath[] = source.getPath().split("/");

        if (splitedPath.length < 8) throw new InvalidReferenceException("Wrong endpoint for api");

        projectId = Integer.parseInt(splitedPath[4]);
        pathToFileEncoded = splitedPath[7];

        try {
            pathToFile = URLDecoder.decode(pathToFileEncoded, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String splitedQuery[] = source.getQuery().split("&");

        for (String it : splitedQuery){
            if (it.contains("ref=")) branch = it.substring(it.lastIndexOf("=") + 1);
        }

    }

    @Override
    public Reference getRel(String relUri) throws InvalidReferenceException {
        return super.getRel(relUri);
    }

//    @Override
//    public Reference getRel(Reference ref) throws InvalidReferenceException {
//        return ReferenceFactory.makeReference();
//    }
}
