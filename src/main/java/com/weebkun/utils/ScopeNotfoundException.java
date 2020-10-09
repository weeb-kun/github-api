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

package com.weebkun.utils;

import okhttp3.Response;

/**
 * thrown when a required scope is not found.
 */
public class ScopeNotfoundException extends UnauthorisedException {

    private String scope;
    private Response response;

    public ScopeNotfoundException(String scope, Response response) {
        super(response);
        this.scope = scope;
        this.response = response;
    }

    public String getScope(){
        return scope;
    }

    public Response getResponse(){
        return response;
    }
}
