package com.weebkun.github;

import com.weebkun.utils.HttpErrorException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 * represents an organisation.
 */
public class Organisation {

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

    /**
     * returns an array of repos belonging to an organisation.
     * @param org the name of the organisation
     * @param params the {@link Options} object with requested params
     * @return the array of org repos
     * @see Options for more info on params
     */
    public static Repository[] getRepos(String org, Options params) {
        if(params.perPage > 100) throw new IndexOutOfBoundsException("results per page exceeds 100.");
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
            repositories = Github.getMoshi().adapter(Repository[].class).fromJson(response.body().source());
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

    /**
     * creates a repository in a specified organisation.
     * the authenticated user must have access to this organisation and be able to create repositories in that organisation.
     *
     * use {@link RepositoryAdapter} instead if you dont want to specify the json yourself.
     * @param org the name of the organisation
     * @param json the json string
     * @throws HttpErrorException if the user cannot create repositories in the specified organisation or another error occurred
     * @see RepositoryAdapter
     */
    public static void createRepository(String org, String json) throws HttpErrorException {
        RequestBody body = RequestBody.create(json, MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/orgs/%s/repos", org))
                .post(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 201) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates a repository in this organisation.
     * use {@link RepositoryAdapter} if you do not want to specify the json yourself.
     * @param json the json string
     * @throws HttpErrorException if an error was received
     * @see RepositoryAdapter
     */
    public void createRepo(String json) throws HttpErrorException{
        RequestBody body = RequestBody.create(json, MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/orgs/%s/repos", this.name))
                .post(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 201) throw new HttpErrorException(response);
        } catch (IOException e) {
             e.printStackTrace();
        }
    }

    /**
     * meant for creating a repository in an organisation. the authenticated user must be a member of that organisation.
     * see <a href="https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#create-an-organization-repository">the github docs</a> for more info
     */
    public static final class RepositoryAdapter extends Repository.Adapter {
        // post /orgs/{org}/repos

        /**
         * the team id that will be granted access to this repo.
         */
        public int teamId;
    }
}
