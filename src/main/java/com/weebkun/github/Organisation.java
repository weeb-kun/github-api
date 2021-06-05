package com.weebkun.github;

import com.weebkun.utils.HttpErrorException;

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
    public static Repository[] getRepositories(String org, Options params) {
        if(params.perPage > 100) throw new IndexOutOfBoundsException("results per page exceeds 100.");
        return Github.getNetworkUtil().get(String.format("/orgs/%s/repos"
                .concat(params.type != null ? String.format("?type=%s&", params.type) : "")
                .concat(params.sort != null ? String.format("sort=%s&", params.sort) : "")
                .concat(params.direction != null ? String.format("direction=%s&", params.direction) : "")
                .concat(params.perPage != 0 ? String.format("per_page=%d&", params.perPage) : "")
                .concat(params.page != 0 ? String.format("page=%d", params.page) : ""), org),
                Repository[].class);
    }

    /**
     * returns an array of repos that belong to this organisation.
     * @param params the {@link Options} object
     * @return the array of repos
     * @see Options
     */
    public Repository[] getRepositories(Options params) {
        return Organisation.getRepositories(this.name, params);
    }

    /**
     * creates a repository in this organisation.
     * use {@link RepositoryAdapter} if you do not want to specify the json yourself.
     * @param adapter the new
     * @throws HttpErrorException if an error was received
     * @see RepositoryAdapter
     */
    public void createRepository(RepositoryAdapter adapter) throws HttpErrorException{
        Github.getNetworkUtil().post(String.format("/orgs/%s/repos", this.name),
                Github.getMoshi().adapter(RepositoryAdapter.class).nonNull().toJson((RepositoryAdapter) adapter.setArchived(null)));
    }

    /**
     * adapter to create a repository in an organisation. the authenticated user must be a member of that organisation.
     * see <a href="https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#create-an-organization-repository">the github docs</a> for more info
     */
    public static final class RepositoryAdapter extends Repository.Adapter {
        // post /orgs/{org}/repos

        private int teamId;
        private String org;
        transient String owner;

        public void setTeamId(int teamId) {
            this.teamId = teamId;
        }

        public RepositoryAdapter() {
            super("");
        }
    }
}
