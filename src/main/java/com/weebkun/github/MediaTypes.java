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

package com.weebkun.github;

/**
 * media types given by the github api. version used is v3.
 * passed in to the 'accept' header in api calls.
 * see <a href="https://docs.github.com/en/free-pro-team@latest/rest/overview/media-types">github docs</a> for more info
 * check the X-GitHub-Media-Type header in the response for the media type.
 */
public class MediaTypes {

    // use this for oauth routes as they are not part of the rest api and will respond with 406 not acceptable when using DEFAULT.
    public static final String JSON = "application/json";

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

    /**
     * content type for building request bodies
     */
    public static final String REQUEST_BODY_TYPE = "application/json; charset=utf-8";

    // media types for previews
    public static final String MERCY_PREVIEW = "application/vnd.github.mercy-preview+json";
    public static final String WYANDOTTE_PREVIEW = "application/vnd.github.wyandotte-preview+json";
    public static final String ANT_MAN_PREVIEW = "application/vnd.github.ant-man-preview+json";
    public static final String SQUIRREL_GIRL_PREVIEW = "application/vnd.github.squirrel-girl-preview+json";
    public static final String MOCKINGBIRD_PREVIEW = "application/vnd.github.mockingbird-preview+json";
    public static final String INERTIA_PREVIEW = "application/vnd.github.inertia-preview+json";
    public static final String CLOAK_PREVIEW = "application/vnd.github.cloak-preview+json";
    public static final String BLACK_PANTHER_PREVIEW = "application/vnd.github.black-panther-preview+json";
    public static final String GIANT_SENTRY_FIST_PREVIEW = "application/vnd.github.giant-sentry-fist-preview+json";
    public static final String SCARLET_WITCH_PREVIEW = "application/vnd.github.scarlet-witch-preview+json";
    public static final String ZZZAX_PREVIEW = "application/vnd.github.zzzax-preview+json";
    public static final String LUKE_CAGE_PREVIEW = "application/vnd.github.luke-cage-preview+json";
    public static final String STARFOX_PREVIEW = "application/vnd.github.starfox-preview+json";
    public static final String FURY_PREVIEW = "application/vnd.github.fury-preview+json";
    public static final String FLASH_PREVIEW = "application/vnd.github.flash-preview+json";
    public static final String SURTUR_PREVIEW = "application/vnd.github.surtur-preview+json";
    public static final String CORSAIR_PREVIEW = "application/vnd.github.corsair-preview+json";
    public static final String SOMBRA_PREVIEW = "application/vnd.github.sombra-preview+json";
    public static final String SWITCHEROO_PREVIEW = "application/vnd.github.switcheroo-preview+json";
    public static final String GROOT_PREVIEW = "application/vnd.github.groot-preview+json";
    public static final String DORIAN_PREVIEW = "application/vnd.github.dorian-preview+json";
    public static final String LYDIAN_PREVIEW = "application/vnd.github.lydian-preview+json";
    public static final String LONDON_PREVIEW = "application/vnd.github.london-preview+json";
    public static final String BAPTISTE_PREVIEW = "application/vnd.github.baptiste-preview+json";
    public static final String NEBULA_PREVIEW = "application/vnd.github.nebula-preview+json";

}
