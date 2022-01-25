package ru.fusionsoft.iu.dereferencer.factories;

import ru.fusionsoft.iu.dereferencer.enums.RefType;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.reference.external.GitHubReference;
import ru.fusionsoft.iu.dereferencer.reference.external.GitLabReference;
import ru.fusionsoft.iu.dereferencer.reference.internal.LocalReference;
import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ReferenceFactory {


    public static GitLabReference makeGitLabReference(String host, Integer projectId, String branch, String path, Boolean raw, String fragment, String TOKEN) throws
            UnsupportedEncodingException, InvalidReferenceException {
        if (projectId == null || branch == null || path == null || branch.equals("") || path.equals("")) throw new NullPointerException();
        if (host.equals("")) host = "gitlab.ru";
        String uri = "https://" + host +"/api/v4/projects/" + projectId + "/repository/files/" + URLEncoder.encode(path, StandardCharsets.UTF_8.name());
        if (raw) uri+= "/raw";
        uri += "?ref=" + branch;
        if(!fragment.equals("")) uri += "#" + fragment;
        return new GitLabReference(URI.create(uri), TOKEN);
    }

    public static GitHubReference makeGitHubReference(String host, String user, String branch, String repository, String path, String fragment, String TOKEN)
            throws InvalidReferenceException {
        if (user == null || branch == null || repository == null || path == null) throw new NullPointerException();

        if(host.equals("")) host = "raw.githubusercontent.com";

        String uri  ="https://" + host + "/" + user + "/" + repository + "/" + branch + "/" + path;
        if (!fragment.equals("")) uri += "#" + fragment;

        return new GitHubReference(URI.create(uri), TOKEN);
    }

    public static Reference makeReference(String ref) throws InvalidReferenceException {
        return makeReference(URI.create(ref));
    }

    public static  Reference makeReference(String ref, String TOKEN) throws InvalidReferenceException {
        return makeReference(URI.create(ref), TOKEN);
    }

    public static Reference makeReference(URI source) throws InvalidReferenceException {
        if (RefType.isLocal(source) || RefType.isInternalPath(source) || RefType.isInternalAnchor(source)) return new LocalReference(source);
        if (RefType.isExternal(source)){
            if (RefType.isGitHub(source)) return new GitHubReference(source, "");
            if  (RefType.isGitLab(source)) return new GitHubReference(source, "");
        }
        return null;
    }

    public static Reference makeReference(URI source, String TOKEN) throws InvalidReferenceException {
        if (RefType.isGitHub(source)) return new GitHubReference(source, TOKEN);
        if (RefType.isGitLab(source)) return new GitLabReference(source, TOKEN);
        return null;
    }


}
