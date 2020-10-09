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

import java.io.IOException;

/**
 * indicates an http error was received.
 */
public class HttpErrorException extends RuntimeException{

    public int status;
    public String description;
    public String body;

    public HttpErrorException(Response response){
        super("received http error response: " + response.code() + response.message());
        try {
            this.status = response.code();
            this.description = response.message();
            this.body = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
