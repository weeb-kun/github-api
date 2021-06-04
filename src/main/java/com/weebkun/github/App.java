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
 * represents a github app.
 */
public class App {
    public int id;
    public String slug;
    public String node_id;
    public Owner owner;
    public String name;
    public String description;
    public String external_url;
    public String html_url;
    public String created_at;
    public String updated_at;
    public AppPermissions permissions;
    public String[] events;

    /**
     * specifies the app permissions.
     */
    static class AppPermissions {
        public String metadata;
        public String contents;
        public String issues;
        public String single_file;
    }
}