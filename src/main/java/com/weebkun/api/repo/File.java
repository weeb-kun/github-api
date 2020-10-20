package com.weebkun.api.repo;

import com.weebkun.api.Github;
import com.weebkun.api.MediaTypes;
import com.weebkun.utils.HttpErrorException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * represents a file in a repo.
 */
public final class File extends Content {
    public String content;
    public String owner;
    public String repo;

    /**
     * updates this file.
     * @param message the commit message
     * @param branch the branch
     * @param newData the new InputStream of data.
     * @throws HttpErrorException if the update operation failed. e.g. due to conflict or wrong path or branch
     * @throws IOException if the data is unable to be encoded
     */
    public void update(String message, String branch, InputStream newData) throws HttpErrorException, IOException{
        String json = "{" +
                String.format("\"message\": \"%s\"", message) +
                String.format("\"content\": \"%s\"", Base64.getEncoder().encodeToString(newData.readAllBytes())) +
                String.format("\"sha\": \"%s\"", sha) +
                String.format("\"branch\": \"%s\"", branch) +
                "}";
        RequestBody body = RequestBody.create(json, MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/contents/%s", owner, repo, path))
                .put(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if (response.code() != 200) throw new HttpErrorException(response);
        }
    }

    /**
     * deletes this file from this repo.
     * @param message the commit message
     * @param branch the branch
     * @throws HttpErrorException if the delete operation failed e.g. due to conflict etc.
     */
    public void delete(String message, String branch) throws HttpErrorException {
        String json = "{" +
                String.format("\"message\": \"%s\"", message) +
                String.format("\"sha\": \"%s\"", sha) +
                String.format("\"branch\": \"%s\"", branch) +
                "}";
        RequestBody body = RequestBody.create(json, MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/contents/%s", owner, repo, path))
                .delete(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
