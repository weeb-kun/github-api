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

/**
 * media types given by the github api. version used is v3.
 * passed in to the 'accept' header in api calls.
 * see <a href="https://docs.github.com/en/free-pro-team@latest/rest/overview/media-types">github docs</a> for more info
 * check the X-GitHub-Media-Type header in the response for the media type.
 */
public class MediaTypes {
    public static final String DEFAULT = "application/vnd.github.v3+json";
    public static final String RAW = "application/vnd.github.v3.raw+json";
    public static final String TEXT = "application/vnd.github.v3.text+json";
    public static final String HTML = "application/vnd.github.v3.html+json";
    public static final String FULL = "application/vnd.github.v3.full+json";

    public static final String BLOB_DEFAULT = "application/vnd.github.v3+json";
    public static final String BLOB_RAW = "application/vnd.github.v3.raw";

    public static final String DIFF = "application/vnd.github.v3.diff+json";
    public static final String PATCH = "application/vnd.github.v3.patch+json";
    public static final String SHA = "application/vnd.github.v3.sha+json";

    public static final String REPO_RAW = "application/vnd.github.v3.raw";
    public static final String REPO_HTML = "application/vnd.github.v3.html";

    public static final String GIST_RAW = "application/vnd.github.v3.raw";
    public static final String GIST_BASE64 = "application/vnd.github.v3.base64";
}
