package ru.fusionsoft.iu.dereferencer.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.fusionsoft.iu.dereferencer.exceptions.InvalidReferenceException;
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
                .addHeader("PRIVATE-TOKEN", targetCast.getAccessTOKEN())
                .url(targetCast.get().toString())
                .build();
        Response response = client.newCall(request).execute();

        if (response.code() == 404) {
            throw new ConnectException("The connection cannot be established. Invalid token or link. Check:\nbranch - " +
                    targetCast.getBranch()  + "\nproject ID - " +
                    targetCast.getProjectId() + "\npath to file - " + targetCast.getPathToFile() + "\nTOKEN - " +
                    targetCast.getAccessTOKEN() + "\nmessage error: " + response.message());
        }
        //TODO: может быть не только .yaml
        JsonNode jsonNode = MapperUtil.getMapperInstance(new File(".yaml")).readTree(response.body().string());
        MapperUtil.getMapperInstance(new File(".yaml")).writeValue(new File(ManagerImpl.getDownloadDirectory() + "/" + target.getHashFileName()) , jsonNode);
        return jsonNode;
    }
}
