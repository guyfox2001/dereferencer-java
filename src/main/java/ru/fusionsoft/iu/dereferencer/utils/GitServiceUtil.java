package ru.fusionsoft.iu.dereferencer.utils;

import ru.fusionsoft.iu.dereferencer.reference.external.GitHubReference;
import ru.fusionsoft.iu.dereferencer.reference.external.GitLabReference;
import ru.fusionsoft.iu.dereferencer.reference.external.GitReference;
import ru.fusionsoft.iu.dereferencer.services.impl.GitHubService;
import ru.fusionsoft.iu.dereferencer.services.impl.GitLabService;
import ru.fusionsoft.iu.dereferencer.services.GitService;

public class GitServiceUtil {
    public static GitService getGitService(GitReference ref) {

        if (ref instanceof GitLabReference) return new GitLabService();

        if (ref instanceof GitHubReference) return new GitHubService();

        return null;

    }
}
