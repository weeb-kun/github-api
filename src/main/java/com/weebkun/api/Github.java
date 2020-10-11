/*
Copyright 2020 weebkun

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.weebkun.api;

import com.google.gson.Gson;
import com.squareup.moshi.Moshi;
import com.weebkun.auth.OAuth;
import com.weebkun.utils.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * main class of this library.
 * use one of the provided authentication methods to authenticate to the api.
 */
public class Github {
    private static String token;
    private static TokenType type;
    private static final String ROOT = "https://api.github.com";
    private static OkHttpClient client = new OkHttpClient();
    private static String USER_AGENT = "Java-github-api";
    private static final Gson GSON = new Gson();
    private static final Moshi moshi = new Moshi.Builder().build();

    public static final String gsonMediaType = "application/json; charset=utf-8";

    static {
        // default interceptor to add accept, user-agent and authorization headers to all requests
        client.newBuilder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder()
                        .addHeader("accept", MediaTypes.DEFAULT)
                        .addHeader("accept", MediaTypes.MERCY_PREVIEW)
                        .addHeader("accept", MediaTypes.NEBULA_PREVIEW)
                        .addHeader("user-agent", USER_AGENT)
                        .addHeader("authorization", String.format("%s %s", type == TokenType.OAUTH ? "token" : "basic", token))
                        .build());
            }
        });
    }

    /**
     * authenticates using oauth. requests authorisation from the user.
     * @param clientId the clientId of this application
     * @param scopes the scopes being requested
     * @throws AlreadyAuthenticatedException if this app has already authenticated
     * @see OAuth#authenticate(String, String[]) 
     */
    public static void authenticate(String clientId, String[] scopes) throws AlreadyAuthenticatedException {
        if(token != null) throw new AlreadyAuthenticatedException();
        OAuth.authenticate(clientId, scopes);
        // get token
        Github.token = OAuth.getToken();
    }

    /**
     * authenticates with a personal access token.
     * @param token the pat
     * @throws AlreadyAuthenticatedException if already authenticated
     */
    public static void authenticate(String token) throws AlreadyAuthenticatedException {
        if(Github.token != null) throw new AlreadyAuthenticatedException();
        Github.token = token;
    }

    /**
     * initialise the {@link OkHttpClient}. chain calls to add interceptors, etc.
     * @return client builder for chaining
     */
    public static Builder init(){
        return new Builder();
    }

    /**
     * set an existing client instance as the global client instance.
     * use this if u have a custom configuration.
     * @param client the client
     */
    public static void setClient(OkHttpClient client) {
        Github.client = client;
    }

    /**
     * get the global instance of {@code OkHttpClient}.
     * @return the global http client
     */
    public static OkHttpClient getClient(){
        return client;
    }

    /**
     * get the root url.
     * @return the root
     */
    public static String getRoot() {
        return ROOT;
    }

    /**
     * returns the global Gson instance.
     * @return the Gson instance
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     * get the global Moshi instance.
     * @return the moshi instance
     */
    public static Moshi getMoshi(){
        return moshi;
    }

    /**
     * sets the user agent for all api requests.
     * @param agent the string denoting the user agent.
     */
    public static void setAgent(String agent) {
        Github.USER_AGENT = agent;
    }

    /**
     * gets the authenticated user.
     * if authenticated using oauth using the {@code user} scope or through basic authentication using a PAT,
     * public and private info is returned. if the scope is not used, only public info is returned.
     * @return the authenticated user
     * @throws NotAuthenticatedException if there is no user currently authenticated in this app.
     * @throws HttpErrorException if any other errors received.
     */
    public static User getAuthenticatedUser() throws NotAuthenticatedException, HttpErrorException {
        Request request = new Request.Builder()
                .url(ROOT + "/user")
                .build();
        User user = null;
        try(Response response = client.newCall(request).execute()) {
            if(response.code() == 401) throw new NotAuthenticatedException("error: user not authenticated.\nauthenticate using Github.authenticate() first.");
            if(response.code() != 200) throw new HttpErrorException(response);
            user = moshi.adapter(User.class).fromJson(response.body().source());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * returns an array of repos that the authenticated user has explicit permission to access.
     * @param params the {@link Options} object with the requested params
     * @return the array of repos
     * @throws ParamConflictException see {@link Options} for more info
     * @throws UnauthorisedException if no user is currently authenticated
     * @throws HttpErrorException if any other http error was received
     * @see Options for the properties you can configure
     */
    public static Repository[] listUserRepos(Options params) throws ParamConflictException, HttpErrorException{
        if(params.perPage > 100) throw new IndexOutOfBoundsException("results per page exceeds 100.");
        if(params.before != null && params.since != null) throw new ParamConflictException("since and before used together.");
        Request request = new Request.Builder()
                .url(ROOT + "/user/repos".concat(params.visibility != null ? String.format("?visibility=%s&", params.visibility) : "")
                .concat(params.affiliation != null ? String.format("affiliation=%s&", params.affiliation) : "")
                .concat(params.type != null ? String.format("type=%s&", params.type) : "")
                .concat(params.sort != null ? String.format("sort=%s&", params.sort) : "")
                .concat(params.direction != null ? String.format("direction=%s&", params.direction) : "")
                .concat(params.perPage != 0 ? String.format("per_page=%d&", params.perPage) : "")
                .concat(params.page != 0 ? String.format("page=%d&", params.page) : "")
                .concat(params.since != null ? String.format("since=%s", params.since) : "")
                .concat(params.since == null && params.before != null ? String.format("before=%s", params.before) : ""))
                .build();
        Repository[] repositories = {};
        try(Response response = client.newCall(request).execute()) {
            if(response.code() == 422) throw new ParamConflictException(String.format("error: type conflict: %s\n%s\n\n%s",
                    response.code() + " " + response.message(), response.headers(), response.body()));
            if(response.code() == 401) throw new UnauthorisedException(response);
            if(response.code() != 200) throw new HttpErrorException(response);
            repositories = GSON.fromJson(response.body().string(), Repository[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return repositories;
    }

    /**
     * a default client is given but if you wish to configure your own client, use this builder class or {@link Github#setClient(OkHttpClient)}.
     */
    public static final class Builder {

        private final OkHttpClient.Builder builder;

        public Builder(){
            this.builder = new OkHttpClient.Builder();
        }

        public Builder addInterceptor(Interceptor interceptor) {
            builder.addInterceptor(interceptor);
            return this;
        }

        public Builder addNetworkInterceptor(Interceptor interceptor) {
            builder.addNetworkInterceptor(interceptor);
            return this;
        }

        public Builder addEventListener(EventListener listener) {
            builder.eventListener(listener);
            return this;
        }

        public Builder addEventListenerFactory(EventListener.Factory factory) {
            builder.eventListenerFactory(factory);
            return this;
        }

        public void build(){
            Github.client = builder.build();
        }
    }
}
