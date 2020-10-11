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

/**
 * thrown when your app tries to call an endpoint that requires authentication or when this app is not authenticated yet.
 */
public class NotAuthenticatedException extends RuntimeException {

    public NotAuthenticatedException(){
        super("called a restricted endpoint without proper authentication");
    }

    public NotAuthenticatedException(String message) {
        super(message);
    }
}
