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

import com.weebkun.auth.OAuth;
import com.weebkun.utils.AlreadyAuthenticatedException;

/**
 * main class of this library.
 * use one of the provided authentication methods to authenticate to the api.
 */
public class Github {
    private static String token;
    private static final String ROOT = "https://api.github.com";

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

    public static void request(){

    }
}
