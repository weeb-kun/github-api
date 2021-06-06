package com.weebkun.github;

import com.squareup.moshi.Moshi;
import com.weebkun.utils.HttpErrorException;
import com.weebkun.utils.UnauthorisedException;
import okhttp3.*;

import java.io.IOException;

class Network {

    private final OkHttpClient client;
    private final Moshi moshi = Github.getMoshi();

    protected Network(OkHttpClient client) {
        this.client = client;
    }

    protected <T> T get(String endPoint, Class<T> type) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + endPoint)
                .build();
        T result;
        try(Response response = client.newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
            result = moshi.adapter(type).fromJson(response.body().source());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            return null;
        }
        return result;
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

    protected void put(String endPoint, String json) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + endPoint)
                .put(RequestBody.create(json, MediaType.get(MediaTypes.REQUEST_BODY_TYPE)))
                .build();
        try(Response response = client.newCall(request).execute()) {
            if(response.code() != 204) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void patch(String endPoint, String json) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + endPoint)
                .patch(RequestBody.create(json, MediaType.get(MediaTypes.REQUEST_BODY_TYPE)))
                .build();
        try(Response response = client.newCall(request).execute()) {
            if(response.code() == 403) throw new UnauthorisedException(response);
            if(response.code() != 200) throw new HttpErrorException(response);
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

    protected void delete(String endPoint, String json) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + endPoint)
                .delete(RequestBody.create(json, MediaType.get(MediaTypes.REQUEST_BODY_TYPE)))
                .build();
        try(Response response = client.newCall(request).execute()) {
            if(response.code() != 204) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
