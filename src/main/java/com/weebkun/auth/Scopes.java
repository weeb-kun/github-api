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
 * indicates the scopes required for authentication.<br>
 * see <a href="https://docs.github.com/en/free-pro-team@latest/developers/apps/scopes-for-oauth-apps">github docs</a>
 * for more info.
 */
public final class Scopes {
    public static final String REPO = "repo";
    public static final String REPO_STATUS = "repo:status";
    public static final String REPO_DEPLOYMENT = "repo:deployment";
    public static final String PUBLIC_REPO = "public_repo";
    public static final String REPO_INVITE = "repo:invite";
    public static final String SECURITY_EVENTS = "scecurity_events";
    public static final String ADMIN_REPO_HOOK = "admin:repo_hook";
    public static final String WRITE_REPO_HOOK = "write:repo_hook";
    public static final String READ_REPO_HOOK = "read:repo_hook";
    public static final String ADMIN_ORG = "admin:org";
    public static final String WRITE_ORG = "write:org";
    public static final String READ_ORG = "read:org";
    public static final String ADMIN_PUBLIC_KEY = "admin:public_key";
    public static final String WRITE_PUBLIC_KEY = "write:public_key";
    public static final String READ_PUBLIC_KEY = "read:public_key";
    public static final String ADMIN_ORG_HOOK = "admin:org_hook";
    public static final String GIST = "gist";
    public static final String NOTIFICATIONS = "notifications";
    public static final String USER = "user";
    public static final String READ_USER = "read:user";
    public static final String USER_EMAIL = "user:email";
    public static final String USER_FOLLOW = "user:follow";
    public static final String DELETE_REPO = "delete_repo";
    public static final String WRITE_DISCUSSION = "write:discussion";
    public static final String READ_DISCUSSION = "read:discussion";
    public static final String WRITE_PACKAGES = "write:packages";
    public static final String READ_PACKAGES = "read:packages";
    public static final String DELETE_PACKAGES = "delete:packages";
    public static final String ADMIN_GPG_KEY = "admin:gpg_key";
    public static final String WRITE_GPG_KEY = "write:gpg_key";
    public static final String READ_GPG_KEY = "read:gpg_key";
    public static final String WORKFLOW = "workflow";
}
