package ru.fusionsoft.iu.dereferencer.factories;

import ru.fusionsoft.iu.dereferencer.enums.RefType;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.reference.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ReferenceFactory {


    public static GitLabReference makeGitLabReference(String host, Integer projectId, String branch, String path, Boolean raw, String TOKEN) throws
            UnsupportedEncodingException, InvalidReferenceException {
        if (projectId == null || branch == null || path == null || branch.equals("") || path.equals("")) throw new NullPointerException();
        if (host.equals("") || host == null) host = "https://gitlab.ru";
        String uri = host +"/api/v4/projects/" + projectId + "/repository/files/" + URLEncoder.encode(path, StandardCharsets.UTF_8.name());
        if (raw) uri+= "/raw";
        uri += "?ref=" + branch;
        return new GitLabReference(URI.create(uri), TOKEN);
    }

    public static GitHubReference makeGitHubReference(String user, String branch, String repository, String path, String fragment, String TOKEN)
            throws InvalidReferenceException {
        if (user == null || branch == null || repository == null || path == null) throw new NullPointerException();
        String uri  = "https://raw.githubusercontent.com" + "/" + user + "/" + repository + "/" + branch + "/" + path;
        if (!fragment.equals("")) uri += "#" + fragment;

        return new GitHubReference(URI.create(uri), TOKEN);
    }

    public static Reference makeReference(String ref) throws InvalidReferenceException {
        return makeReference(URI.create(ref));
    }

    public static  Reference makeReference(String ref, String TOKEN) throws InvalidReferenceException {
        return makeReference(URI.create(ref), TOKEN);
    }

    public static Reference copy(Reference ref) throws InvalidReferenceException {
        if (ref instanceof GitReference) return makeReference(ref.get(), ((GitReference) ref).getAccessTOKEN());
        return new LocalReference(ref.get());
    }

    public static Reference makeReference(URI source) throws InvalidReferenceException {
        if (RefType.isLocal(source) || RefType.isInternalPath(source) || RefType.isInternalAnchor(source)) return new LocalReference(source);
        if (RefType.isExternal(source)){
            if (RefType.isGitHub(source)) return new GitHubReference(source, "");
        }
        return null;
    }

    public static Reference makeReference(URI source, String TOKEN) throws InvalidReferenceException {
        if (RefType.isGitHub(source)) return new GitHubReference(source, TOKEN);
        return null;
    }


}
