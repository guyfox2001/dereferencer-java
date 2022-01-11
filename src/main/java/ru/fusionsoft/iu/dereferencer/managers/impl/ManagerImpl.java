package ru.fusionsoft.iu.dereferencer.managers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import ru.fusionsoft.iu.dereferencer.managers.Manager;
import ru.fusionsoft.iu.dereferencer.reference.GitHubReference;
import ru.fusionsoft.iu.dereferencer.reference.GitReference;
import ru.fusionsoft.iu.dereferencer.reference.LocalReference;
import ru.fusionsoft.iu.dereferencer.reference.Reference;
import ru.fusionsoft.iu.dereferencer.services.GitHubService;
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
    public JsonNode getDocument(Reference ref) throws IOException {
        if (!downloadDirectory.exists()) downloadDirectory.mkdir();

        lastReference = ref;
        if (ref instanceof GitHubReference){
            File downloaded = new File(downloadDirectory +"/" + ((GitHubReference) ref).getHashFileName());

            if (downloaded.exists()) return MapperUtil.getMapperInstance(downloaded).readTree(downloaded);

            return new GitHubService().get((GitReference) ref);
        }

        if (ref instanceof LocalReference){
            File target = new File(ref.get().toString());
            if (target.exists()) {
                lastUsed = target;
                return MapperUtil.getMapperInstance(target).readTree(target);
            }
        }
        return null;
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
