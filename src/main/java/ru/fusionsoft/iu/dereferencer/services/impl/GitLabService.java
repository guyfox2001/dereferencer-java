package ru.fusionsoft.iu.dereferencer.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.fusionsoft.iu.dereferencer.managers.impl.ManagerImpl;
import ru.fusionsoft.iu.dereferencer.reference.external.GitLabReference;
import ru.fusionsoft.iu.dereferencer.reference.external.GitReference;
import ru.fusionsoft.iu.dereferencer.services.GitService;
import ru.fusionsoft.iu.dereferencer.utils.MapperUtil;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

public class GitLabService implements GitService {
    public GitLabService(){}
    @Override
    public JsonNode get(GitReference target) throws IOException {
        if (!(target instanceof GitLabReference)) throw new IllegalArgumentException();

        GitLabReference targetCast = (GitLabReference) target;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(targetCast.getUri().toURL())
                .build();
        if(target.getAccessTOKEN() != null && !target.getAccessTOKEN().equals("") ){
            request =  new Request.Builder(request).addHeader("PRIVATE-TOKEN", targetCast.getAccessTOKEN()).build();
        }

        Response response = client.newCall(request).execute();

        if (response.code() == 404) {
            throw new ConnectException("The connection cannot be established. Invalid token or link. Check:\nbranch - " +
                    targetCast.getBranch()  + "\nproject ID - " +
                    targetCast.getProjectId() + "\npath to file - " + targetCast.getPathToFile() + "\nTOKEN - " +
                    targetCast.getAccessTOKEN() + "\nmessage error: " + response.message());
        }

        JsonNode jsonNode = MapperUtil.getMapperInstance(target).readTree(response.body().string());
        MapperUtil.getMapperInstance(target).writeValue(new File(ManagerImpl.getDownloadDirectory() + "/" + target.getHashFileName()) , jsonNode);
        return jsonNode;
    }
}
