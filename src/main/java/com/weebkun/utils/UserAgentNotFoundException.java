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
 * thrown when a user agent is not defined.
 * this is for runtime assertion as github expects a valid user agent, otherwise the api will respond with 403 Forbidden.
 */
public class UserAgentNotFoundException extends RuntimeException {

    public UserAgentNotFoundException() {
        super("user agent not specified");
    }

    public UserAgentNotFoundException(String message) {
        super(message);
    }

    public UserAgentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAgentNotFoundException(Throwable cause) {
        super(cause);
    }
}
