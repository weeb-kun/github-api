package com.weebkun.api;

import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * represents an organisation.
 */
public class Organisation {

    /**
     * returns an array of repos belonging to an organisation.
     * @param org the name of the organisation
     * @param params the {@link Options} object with requested params
     * @return the array of org repos
     * @see Options for more info on params
     */
    public static Repository[] getRepos(String org, Options params) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/orgs/%s/repos"
                        .concat(params.type != null ? String.format("?type=%s&", params.type) : "")
                        .concat(params.sort != null ? String.format("sort=%s&", params.sort) : "")
                        .concat(params.direction != null ? String.format("direction=%s&", params.direction) : "")
                        .concat(params.perPage != 0 ? String.format("per_page=%d&", params.perPage) : "")
                        .concat(params.page != 0 ? String.format("page=%d", params.page) : ""), org))
                .build();
        Repository[] repositories = {};
        try (Response response = Github.getClient().newCall(request).execute()) {
            repositories = Github.getGson().fromJson(response.body().string(), Repository[].class);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return repositories;
    }

    /**
     * returns an array of repos that belong to this organisation.
     * @param params the {@link Options} object
     * @return the array of repos
     * @see Options
     */
    public Repository[] getRepos(Options params) {
        return Organisation.getRepos(this.name, params);
    }

    public String login;
    private int id;
    private String node_id;
    public String url;
    public String repos_url;
    public String events_url;
    public String hooks_url;
    public String issues_url;
    public String members_url;
    public String public_members_url;
    public String avatar_url;
    public String description;
    public String name;
    public String company;
    public String blog;
    public String location;
    public String email;
    public String twitter_username;
    public boolean is_verified;
    public boolean has_organisation_projects;
    public boolean has_repository_projects;
    public int public_repos;
    public int public_gists;
    public int followers;
    public int following;
    public String html_url;
    public String created_at;
    public String updated_at;
    public String type = "Organization";
    public int total_private_repos;
    public int owned_private_repos;
    public int private_gists;
    public int disk_usage;
    public int collaborators;
    public String billing_email;
    public Plan plan;
    public String default_repository_permission;
    public boolean members_can_create_repositories;
    public boolean two_factor_requirement_enabled;
    public String members_allowed_repository_creation_type;
    // use surtur preview media type
    public boolean members_can_create_public_repositories;
    public boolean members_can_create_private_repositories;
    public boolean members_can_create_internal_repositories;
    public boolean members_can_create_pages;
}
