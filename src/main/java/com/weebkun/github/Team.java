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
 * represents a team in an organisation.
 */
public class Team {
    private int id;
    private String node_id;

    public String url;
    public String html_url;
    public String name;
    public String slug;
    public String description;
    public String privacy;
    public String permission;
    public String members_url;
    public String repositories_url;
    public Team parent;
}
