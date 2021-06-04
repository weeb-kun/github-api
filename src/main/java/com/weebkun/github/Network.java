package com.weebkun.github;

import com.google.gson.Gson;
import com.squareup.moshi.Moshi;
import com.weebkun.utils.HttpErrorException;
import okhttp3.*;

import java.io.IOException;

/**
 * util class for building http requests and receiving the responses.
 */
public class Network {

    private final OkHttpClient client;
    private final Gson gson = Github.getGson();
    private final Moshi moshi = Github.getMoshi();

    protected Network(OkHttpClient client) {
        this.client = client;
    }

    protected <T> T get(String endPoint, Class<T> type, boolean isGson) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + endPoint)
                .build();
        T result = null;
        try(Response response = client.newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
            result = !isGson ? moshi.adapter(type).fromJson(response.body().source()) :
            gson.fromJson(response.body().string(), type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    protected void patch(String endPoint, String json) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + endPoint)
                .patch(RequestBody.create(json, MediaType.get(MediaTypes.REQUEST_BODY_TYPE)))
                .build();
        try(Response response = client.newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void post(String endPoint, String json) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + endPoint)
                .post(RequestBody.create(json, MediaType.get(MediaTypes.REQUEST_BODY_TYPE)))
                .build();
        try(Response response = client.newCall(request).execute()) {
            if(response.code() != 201) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void delete(String endPoint) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + endPoint)
                .delete()
                .build();
        try(Response response = client.newCall(request).execute()) {
            if(response.code() != 204) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
