package ru.fusionsoft.iu.dereferencer.managers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
import ru.fusionsoft.iu.dereferencer.managers.Manager;
import ru.fusionsoft.iu.dereferencer.reference.external.GitHubReference;
import ru.fusionsoft.iu.dereferencer.reference.external.GitLabReference;
import ru.fusionsoft.iu.dereferencer.reference.external.GitReference;
import ru.fusionsoft.iu.dereferencer.reference.internal.LocalReference;
import ru.fusionsoft.iu.dereferencer.reference.internal.Reference;
import ru.fusionsoft.iu.dereferencer.utils.GitServiceUtil;
import ru.fusionsoft.iu.dereferencer.utils.MapperUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;


public class ManagerImpl implements Manager {

    private static File startDirectory = new File(System.getProperty("user.dir") + "/.dereferencer");
    private File lastUsed;
    private Reference lastReference;
    private static final File downloadDirectory = new File(System.getProperty("user.home") + "/.download-fragments");

    public ManagerImpl(){
        lastUsed = null;
        if (!startDirectory.exists()) startDirectory.mkdir();
    }

    public ManagerImpl(URI path){
        lastUsed = new File(path.toString());
        startDirectory = new File(path.toString());
        if (!startDirectory.exists()) startDirectory.mkdir();
    }


    @Override
    public File findFile(Reference path) {
        lastUsed = new File(path.toString());
        if (lastUsed.exists()) return lastUsed;
        return null;
    }

    @Override
    public JsonNode getDocument(Reference ref) throws IOException, InvalidReferenceException {
        File downloaded = null;
        lastReference = ref;
        if (!downloadDirectory.exists()) downloadDirectory.mkdir();

        if (ref instanceof LocalReference){
            File target = new File(ref.getUri().toString());
            if (target.exists()) {
                lastUsed = target;
                return MapperUtil.getMapperInstance(target).readTree(target);
            }
        }

        if (ref instanceof GitHubReference){
            downloaded = new File(downloadDirectory +"/" + ((GitHubReference) ref).getHashFileName());
        }
        if(ref instanceof GitLabReference){
            downloaded = new File(downloadDirectory +"/" + ((GitLabReference) ref).getHashFileName());
        }


        if (downloaded.exists()) return MapperUtil.getMapperInstance(downloaded).readTree(downloaded);
        return GitServiceUtil.getGitService((GitReference) ref).get((GitReference) ref);
    }

    @Override
    public File getLastUsedFile() {
        return lastUsed;
    }

    public Reference getLastReference() {
        return lastReference;
    }

    public static File getStartDirectory(){return  startDirectory;}

    public static File getDownloadDirectory() { return downloadDirectory; }
}
