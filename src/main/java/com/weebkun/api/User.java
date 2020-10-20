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

import com.weebkun.api.repo.Repository;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * a user of github.
 */
public class User {
    private String login;
    private String id;
    private String node_id;

    public String getLogin() {
        return login;
    }

    public String getId() {
        return id;
    }

    public String getNodeId() {
        return node_id;
    }

    /**
     * returns an array of public repos for this user.
     * default values of type=all, sort=full_name, direction=asc;
     * @return the array of public repos
     */
    public Repository[] getRepos() {
        return getRepos("all", "full_name", "asc");
    }

    /**
     * returns an array of repos with sorting.
     * @param type the type of repos. can be all, owner, or member.
     * @param sort which field to sort by. can be created, updated, pushed, or full_name.
     * @param direction the direction to sort. can be asc or desc.
     * @return the array of repos
     */
    public Repository[] getRepos(String type, String sort, String direction) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/users/%s/repos?type=%s&sort=%s&direction=%s", name, type, sort, direction))
                .build();
        Repository[] repositories = {};
        try(Response response = Github.getClient().newCall(request).execute()) {
            repositories = Github.getGson().fromJson(response.body().string(), Repository[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return repositories;
    }

    /**
     * returns an array of repos with pagination.
     * @param perPage results per page. max 100
     * @param page the current page
     * @return the array of repos
     */
    public Repository[] getRepos(int perPage, int page){
        return getRepos("all", "full_name", "asc", perPage, page);
    }

    /**
     * returns an array of repos with sorting and pagination.
     * @param type the type of repos. can be all, owner, or member.
     * @param sort the field to sort by. can be created, updated, pushed, or full_name.
     * @param direction the direction of sorting. can be asc or desc.
     * @param perPage results per page. max 100.
     * @param page the current page
     * @return the array of repos
     */
    public Repository[] getRepos(String type, String sort, String direction, int perPage, int page) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/users/%s/repos?type=%s&sort=%s&direction=%s&per_page=%d&page=%d", name, type, sort, direction, perPage, page))
                .build();
        Repository[] repositories = {};
        try(Response response = Github.getClient().newCall(request).execute()) {
            repositories = Github.getGson().fromJson(response.body().string(), Repository[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return repositories;
    }

    // omg kms
    public String avatar_url;
    public String gravatar_id;
    public String url;
    public String html_url;
    public String followers_url;
    public String following_url;
    public String gists_url;
    public String starred_url;
    public String subscriptions_url;
    public String organizations_url;
    public String repos_url;
    public String events_url;
    public String received_events_url;
    public String type = "User";
    public boolean site_admin;
    public String name;
    public String company;
    public String blog;
    public String location;
    public String email;
    public boolean hireable;
    public String bio;
    public String twitter_username;
    public int public_repos;
    public int public_gists;
    public int followers;
    public int following;
    public String created_at;
    public String updated_at;
    // private info. requires user scope
    public int private_gists;
    public int total_private_repos;
    public int owned_private_repos;
    public int disk_usage;
    public int collaborators;
    public boolean two_factor_authentication;
    public Plan plan;
}
