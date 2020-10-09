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
import com.weebkun.utils.AlreadyAuthenticatedException;
import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
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
    private static String USER_AGENT;
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

    public static Moshi getMoshi(){
        return moshi;
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
