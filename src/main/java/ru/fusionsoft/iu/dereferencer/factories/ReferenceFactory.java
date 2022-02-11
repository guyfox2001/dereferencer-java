package ru.fusionsoft.iu.dereferencer.factories;

import ru.fusionsoft.iu.dereferencer.enums.ReferenceType;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.reference.external.GitHubReference;
import ru.fusionsoft.iu.dereferencer.reference.external.GitLabReference;
import ru.fusionsoft.iu.dereferencer.reference.internal.LocalReference;
import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;
import ru.fusionsoft.iu.dereferencer.utils.GitTokensUtill;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ReferenceFactory {



    public static GitLabReference makeGitLabReference(String host, Integer projectId, String branch, String path, String fragment, String TOKEN) throws
            UnsupportedEncodingException, InvalidReferenceException {
        if (projectId == null || branch == null || path == null || branch.equals("") || path.equals("")) throw new NullPointerException();

        if (!GitTokensUtill.getGitTokensInstance().containsKey(projectId.toString()))
            GitTokensUtill.getGitTokensInstance().put(projectId.toString(), TOKEN);

        if (host.equals("")) host = "gitlab.ru";

        String uri = "https://" + host +"/api/v4/projects/" + projectId + "/repository/files/" + URLEncoder.encode(path, StandardCharsets.UTF_8.name()) + "/raw"  + "?ref=" + branch;

        if( fragment != null && !fragment.equals("")) uri += "#" + fragment;

        return new GitLabReference(URI.create(uri), TOKEN);
    }

    public static GitHubReference makeGitHubReference(String host, String user, String branch, String repository, String path, String fragment, String TOKEN)
            throws InvalidReferenceException {
        if (user == null || branch == null || repository == null || path == null) throw new NullPointerException();

        if (!GitTokensUtill.getGitTokensInstance().containsKey(user))
            GitTokensUtill.getGitTokensInstance().put(user, TOKEN);

        if(host.equals("")) host = "raw.githubusercontent.com";

        String uri  ="https://" + host + "/" + user + "/" + repository + "/" + branch + "/" + path;
        if (fragment != null && !fragment.equals("")) uri += "#" + fragment;

        return new GitHubReference(URI.create(uri), TOKEN);
    }

    public static Reference makeReference(String ref) throws InvalidReferenceException {
        return makeReference(URI.create(ref));
    }

    public static  Reference makeReference(String ref, String TOKEN) throws InvalidReferenceException {
        return makeReference(URI.create(ref), TOKEN);
    }

    public static Reference makeReference(URI source) throws InvalidReferenceException {
        if (ReferenceType.isLocal(source) || ReferenceType.isInternalPath(source) || ReferenceType.isInternalAnchor(source)) return new LocalReference(source);
        if (ReferenceType.isExternal(source)){
            if (ReferenceType.isGitHub(source)) return new GitHubReference(source, "");
            if  (ReferenceType.isGitLab(source)) return new GitLabReference(source, "");
        }
        return null;
    }

    public static Reference makeReference(URI source, String TOKEN) throws InvalidReferenceException {

        if (ReferenceType.isGitHub(source)){
            GitHubReference ghr  = new GitHubReference(source, TOKEN);
            if (TOKEN == null || TOKEN.equals(""))
                ghr.setAccessTOKEN(GitTokensUtill.getGitTokensInstance().get(ghr.getUser()));
            return ghr;
        }
        if (ReferenceType.isGitLab(source)){
            GitLabReference glr = new GitLabReference(source, TOKEN);
            if (TOKEN == null || TOKEN.equals(""))
                glr.setAccessTOKEN(GitTokensUtill.getGitTokensInstance().get(glr.getProjectId().toString()));
            return glr;
        }
        return null;
    }


}
