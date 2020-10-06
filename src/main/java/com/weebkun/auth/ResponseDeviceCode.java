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

/**
 * represents the response from an authentication request.
 * see <a href=https://docs.github.com/en/free-pro-team@latest/developers/apps/authorizing-oauth-apps#response-parameters">the github docs</a> for more information.
 */
public class ResponseDeviceCode {
    /**
     * the device code
     */
    public String device_code;

    /**
     * the user code to show the user
     */
    public String user_code;

    /**
     * the url to bring the user to for authorisation
     */
    public String verification_uri;

    /**
     * the number of seconds before the device_code and user_code expire
     */
    public int expires_in;

    /**
     * the minimum interval in seconds that must pass before another token request
     */
    public int interval;
}
