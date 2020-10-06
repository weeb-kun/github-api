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
import okhttp3.*;

import java.io.IOException;

/**
 * authenticates to github using OAuth tokens
 */
public class OAuth {

    private static boolean authorised = false;
    private static String token;

    /**
     * tries to authenticate with github.
     * @param clientId the oauth client id
     * @param scopes an array containing the scopes you would like to request
     */
    public static void authenticate(String clientId, String[] scopes) {
        // request for user and device code
        OkHttpClient client = new OkHttpClient();
        Moshi moshi = new Moshi.Builder().build();
        // set body params
        String json = String.format("{" +
                "'client_id': '%s'," +
                "'scope': '%s'" +
                "}", clientId, String.join(" ", scopes));
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://github.com/login/device/code")
                .addHeader("accept", "application/json")
                .post(body)
                .build();
        while(!authorised) {
            try ( Response response = client.newCall(request).execute()) {
                JsonAdapter<ResponseDeviceCode> adapter = moshi.adapter(ResponseDeviceCode.class);
                ResponseDeviceCode res = adapter.fromJson(response.body().source());
                System.out.printf("go to %s for verification. the code is %s\n", res.verification_uri, res.user_code);
                // poll github if user has authorised already
                AuthenticationStatus status = poll(clientId, res.device_code);
                if(status == AuthenticationStatus.success) return;
                if(status == AuthenticationStatus.expired_token) authenticate(clientId, scopes);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private static AuthenticationStatus poll(String clientId, String code) {
        // sleep for 5 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        String json = String.format("{" +
                "'client_id': '%s'," +
                "'device_code': '%s'" +
                "'grant_type': 'urn:ietf:params:oauth:grant-type:device_code'" +
                "}", clientId, code);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .addHeader("accept", "application/json")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<TokenResponse> adapter = moshi.adapter(TokenResponse.class);
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
                if(res.error.equals("authorization_pending")) poll(clientId, code);
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
