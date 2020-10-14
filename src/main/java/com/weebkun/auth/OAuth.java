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

package com.weebkun.auth;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.weebkun.api.Github;
import com.weebkun.api.MediaTypes;
import com.weebkun.utils.HttpErrorException;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * authenticates to github using OAuth tokens
 */
public class OAuth {

    private static boolean authorised = false;
    private static String token;
    private static OkHttpClient client = new OkHttpClient();

    static {
        // this gay shit finally works
        // all i have to do is build the client and reassign so the interceptor actually works
        client = client.newBuilder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder()
                .addHeader("accept", MediaTypes.JSON)
                .build());
            }
        }).build();
    }

    /**
     * tries to ask a user for authorisation.
     * @param clientId the oauth client id
     * @param scopes an array containing the scopes you would like to request. {@link Scopes}
     * @see Scopes for more info
     */
    public static void authenticate(String clientId, String[] scopes) {
        String code = sendRequest(clientId, scopes);
        AuthenticationStatus status = poll(clientId, code);
        while(!authorised) {
            status = poll(clientId, code);
        }
    }

    private static String sendRequest(String clientId, String[] scopes) {
        // set body params
        String json = String.format("{" +
                "\"client_id\": \"%s\"," +
                "\"scope\": \"%s\"" +
                "}", clientId, String.join(" ", scopes));
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://github.com/login/device/code")
                .post(body)
                .build();
        String code = "";
        // main bulk of code to send the authentication requests
        try (Response response = client.newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
            JsonAdapter<ResponseDeviceCode> adapter = Github.getMoshi().adapter(ResponseDeviceCode.class);
            ResponseDeviceCode res = adapter.fromJson(response.body().source());
            System.out.printf("go to %s for verification. the code is %s\n", res.verification_uri, res.user_code);
            code = res.device_code;
            // poll github if user has authorised already
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    private static AuthenticationStatus poll(String clientId, String code) {
        // sleep for 5 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String json = String.format("{" +
                "\"client_id\": \"%s\"," +
                "\"device_code\": \"%s\"," +
                "\"grant_type\": \"urn:ietf:params:oauth:grant-type:device_code\"" +
                "}", clientId, code);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            JsonAdapter<TokenResponse> adapter = Github.getMoshi().adapter(TokenResponse.class);
            TokenResponse res = adapter.fromJson(response.body().source());
            if(res.access_token != null) {
                // success
                token = res.access_token;
                authorised = true;
                return AuthenticationStatus.success;
            } else {
                // error occurred
                System.err.println(res.error);
                authorised = false;
                // poll again
                if(res.error.equals("authorization_pending")) return poll(clientId, code);
                // expired token, request another authorisation
                return AuthenticationStatus.expired_token;
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return AuthenticationStatus.success;
    }

    /**
     * checks if this application is already authorised.
     * @return true if already authorised
     */
    public static boolean isAuthorised() {
        return authorised;
    }

    /**
     * returns the access token received from the authentication process.
     * @return the access token
     */
    public static String getToken(){
        return OAuth.token;
    }
}
