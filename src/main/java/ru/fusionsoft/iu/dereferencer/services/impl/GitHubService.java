package ru.fusionsoft.iu.dereferencer.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.fusionsoft.iu.dereferencer.managers.impl.ManagerImpl;
import ru.fusionsoft.iu.dereferencer.reference.external.GitHubReference;
import ru.fusionsoft.iu.dereferencer.reference.external.GitReference;
import ru.fusionsoft.iu.dereferencer.services.GitService;
import ru.fusionsoft.iu.dereferencer.utils.MapperUtil;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;


public class GitHubService implements GitService {

    public GitHubService(){}
    @Override
    public JsonNode get(GitReference target) throws IOException {
        if (!(target instanceof GitHubReference)) throw new IllegalArgumentException();

        GitHubReference targetCast = (GitHubReference) target;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(targetCast.getUri().toURL())
                .build();
        if(target.getAccessTOKEN() != null && !target.getAccessTOKEN().equals("") ){
            new Request.Builder(request).addHeader("Authorization", "token " + targetCast.getAccessTOKEN());
        }

        Response response = client.newCall(request).execute();

        if (response.code() == 404) {
            throw new ConnectException("The connection cannot be established. Invalid token or link. Check:\nbranch - " +
                    targetCast.getBranch() + "\nusername - " + targetCast.getUser() + "\nrepository - " +
                    targetCast.getRepo() + "\npath to file - " + targetCast.getPathToFile() + "\nTOKEN - " +
                    targetCast.getAccessTOKEN());
        }
        //TODO: может быть не только .yaml
        JsonNode jsonNode = MapperUtil.getMapperInstance(new File(".yaml")).readTree(response.body().string());
        MapperUtil.getMapperInstance(new File(".yaml")).writeValue(new File(ManagerImpl.getDownloadDirectory() + "/" + target.getHashFileName()) , jsonNode);
        return jsonNode;
    }



}
