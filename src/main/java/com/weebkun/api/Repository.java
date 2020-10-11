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

import com.google.gson.annotations.SerializedName;
import com.weebkun.utils.HttpErrorException;
import com.weebkun.utils.UnauthorisedException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 * represents a repository in github.
 *
 * to retrieve or create organisation repos, see {@link Organisation}.
 * @see Organisation
 */
public class Repository {

    private int id;
    private String node_id;

    /**
     * get a repository from github. {@code repo} scope required for private repositories.
     * @param owner the name of the owner
     * @param name the name of the repo
     * @return a desired repository
     * @throws UnauthorisedException if the repository is private and you do not have access to it
     */
    public static Repository get(String owner, String name) throws UnauthorisedException{
        // send request
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s", owner, name))
                .build();
        Repository repo = null;
        try (Response response = Github.getClient().newCall(request).execute()){
            if(response.code() != 200) throw new HttpErrorException(response);
            repo = Github.getGson().fromJson(response.body().string(), Repository.class);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return repo;
    }

    /**
     * lists all public repositories in the order they are created and from the {@code since} parameter.
     * @param since the ISO 8601 formatted timestamp
     * @param perPage results per page. max is 100
     * @param visibility visibility of the repos
     * @return the list of repositories
     */
    public static Repository[] getAllPublic(String since, int perPage, String visibility) {
        if(perPage > 100) throw new IndexOutOfBoundsException("results per page exceeds 100.");
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repositories?since=%s&per_page=%d&visibility=%s", since, perPage, visibility))
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
     * update a repository.
     * @param owner the owner of the repo
     * @param name the name of the repo
     * @param json the json string containing parameters to update
     * @throws HttpErrorException when a http error was received
     * @throws UnauthorisedException if you do not have access to the specified resource
     * see <a href="https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#update-a-repository">github</a> for more info on params
     */
    public static void update(String owner, String name, String json) throws UnauthorisedException{
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s", owner, name))
                .patch(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
            Repository repo = Github.getGson().fromJson(response.body().string(), Repository.class);
            System.out.printf("Repository %s successfully updated. Response:\nstatus: %s\n%s\n\n%s", repo.full_name, response.code() + " " + response.message(), response.headers(), response.body().string());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * deletes a repository.
     * if the user does not have admin access to the repo, a 403 forbidden response will be received, and an {@code UnauthorisedException} will be thrown.
     * if the app does not have the delete_repo scope, an {@code UnauthorisedException} will also be thrown.
     * @param owner the name of the owner
     * @param name the name of the repo
     * @throws UnauthorisedException if the delete_repo scope is not present or if the user cannot delete the repo. e.g. organisation repo.
     */
    public static void delete(String owner, String name) throws UnauthorisedException {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s", owner, name))
                .delete()
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 204) throw new UnauthorisedException(response);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates a repository for the authenticated user.
     * use {@link Builder} if you don't want to build the json string yourself.
     * @param json the json string
     * @throws HttpErrorException if the create operation failed
     */
    public static void create(String json) throws HttpErrorException{
        // post /user/repos
        RequestBody body = RequestBody.create(json, MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + "/user/repos")
                .post(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()){
            if(response.code() != 201) throw new HttpErrorException(response);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates a new repository from a template.<br>
     * Scopes needed: {@code public_repo} for public repos or {@code repo} for private.
     * you may also use the {@code repo} scope for public repos.
     * @param templateOwner the template owner
     * @param templateName the template repo name
     * @param owner the name of the owner of this new repo
     * @param name the name of this new repo
     * @param description the description
     * @param isPrivate whether this repo is private or public. true for private and false for public.
     * @throws HttpErrorException if the create operation failed.
     */
    public static void create(String templateOwner, String templateName, String owner, String name, String description, boolean isPrivate) throws HttpErrorException{
        RequestBody body = RequestBody.create(String.format("{" +
                "'owner': '%s'" +
                "'name': '%s'" +
                "'description': '%s'" +
                "'private': '%s'"+
                "}", owner, name, description, isPrivate), MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/generate", templateOwner, templateName))
                .post(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 201) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * update this repo.
     * @param json the json string
     * see <a href="https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#update-a-repository">github</a> for more info on params
     * @throws HttpErrorException if there was an error trying to update this repo
     */
    public void update(String json) throws HttpErrorException{
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s", this.owner.getName(), this.name))
                .patch(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
            Repository updatedRepo = Github.getGson().fromJson(response.body().string(), Repository.class);
            System.out.printf("Repository %s successfully updated. Response:\nstatus: %s\n%s\n\n%s\n", updatedRepo.full_name, response.code() + " " + response.message(), response.headers(), response.body().string());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * updates this repository when any of its fields are updated.
     *
     * parses this repository into a json string and sends a PATCH request with the json in the body.
     * @throws HttpErrorException if the the update operation failed
     */
    public void save() throws HttpErrorException{
        RequestBody body = RequestBody.create(Github.getGson().toJson(this), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s", this.owner.getName(), this.name))
                .patch(body)
                .build();
        try (Response response = Github.getClient().newCall(request).execute()){
            if(response.code() != 200) throw new HttpErrorException(response);
            Repository repo = Github.getGson().fromJson(response.body().string(), Repository.class);
            System.out.printf("Repository %s successfully updated. Response:\nstatus: %s\n%s\n\n%s\n", repo.full_name, response.code() + " " + response.message(), response.headers(), response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * deletes this repository.
     * @throws UnauthorisedException if the user does not have access to the repository
     */
    public void delete() throws UnauthorisedException{
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s", owner.getName(), name))
                .delete()
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 204) throw new UnauthorisedException(response);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns an {@code UpdateRepo} object for updating this repository. provides some chained methods to easily update this repo.
     * use this of you are lazy to build the json string yourself.
     * @return the UpdateRepo object
     */
    public UpdateRepo update(){
        // create new UpdateRepo and initialise all fields.
        // this eases the updating process so the user does not have to call so many methods.
        UpdateRepo repo = new UpdateRepo();
        repo.name = this.name;
        repo.owner = this.owner.getName();
        repo.description = this.description;
        repo.homepage = homepage;
        repo.isPrivate = this.is_private;
        repo.visibility = this.visibility;
        repo.hasIssues = this.has_issues;
        repo.hasProjects = this.has_projects;
        repo.hasWiki = this.has_wiki;
        repo.isTemplate = this.is_template;
        repo.defaultBranch = this.default_branch;
        repo.allowSquashMerge = this.allow_squash_merge;
        repo.allowMergeCommit = this.allow_merge_commit;
        repo.allowRebaseMerge = this.allow_rebase_merge;
        repo.deleteBranchOnMerge = this.delete_branch_on_merge;
        repo.archived = this.archived;
        return repo;
    }

    /**
     * gets an array of contributors for this repository.
     * supports pagination using {@code resultsPerPage} and {@code page} params.
     * @param includeAnonymous indicates whether to include anonymous contributors
     * @param resultsPerPage the number of results per page
     * @param page the current page number
     * @return the array of contributors
     */
    public User[] getContributors(boolean includeAnonymous, int resultsPerPage, int page){
        if(resultsPerPage > 100) throw new IndexOutOfBoundsException("results per page exceeds 100.");
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/contributors?anon=%s&per_page=%d&page=%d", owner.getName(), name, includeAnonymous, resultsPerPage, page))
                .build();
        User[] contributorList = {};
        try(Response response = Github.getClient().newCall(request).execute()) {
            contributorList = Github.getMoshi().adapter(User[].class).fromJson(response.body().source());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return contributorList;
    }

    /**
     * gets an array of contributors.
     * @param includeAnonymous indicates whether to include anonymous contributors
     * @return the array of contributors
     */
    public User[] getContributors(boolean includeAnonymous) {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/contributors?anon=%s", owner.getName(), name, includeAnonymous))
                .build();
        User[] contributorList = {};
        try(Response response = Github.getClient().newCall(request).execute()) {
            contributorList = Github.getMoshi().adapter(User[].class).fromJson(response.body().source());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return contributorList;
    }

    /**
     * lists the contributors for this repo.
     * uses default value of includeAnonymous = true.
     * @return the list of contributors including anonymous ones.
     */
    public User[] getContributors() {
        return getContributors(true);
    }

    /**
     * lists the languages and the number of bytes written for each language in this repo.
     * @return the language object
     */
    public Language listLanguages() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/languages", owner.getName(), name))
                .build();
        Language languages = null;
        try(Response response = Github.getClient().newCall(request).execute()) {
             languages = Github.getGson().fromJson(response.body().string(), Language.class);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return languages;
    }

    /**
     * lists this repo's tags.
     * @return the array of tags
     */
    public Tag[] listTags() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/tags", owner.getName(), name))
                .build();
        Tag[] tags = {};
        try(Response response = Github.getClient().newCall(request).execute()) {
            tags = Github.getMoshi().adapter(Tag[].class).fromJson(response.body().source());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return tags;
    }

    /**
     * lists this repo's tags and paginates the results.
     * @param resultsPerPage the number of results per page
     * @param page the current page
     * @return the array of tags paginated
     */
    public Tag[] listTags(int resultsPerPage, int page) {
        if(resultsPerPage > 100) throw new IndexOutOfBoundsException("results per page exceeds 100.");
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/tags?per_page=%d&page=%d", owner.getName(), name, resultsPerPage, page))
                .build();
        Tag[] tags = {};
        try(Response response = Github.getClient().newCall(request).execute()) {
            tags = Github.getMoshi().adapter(Tag[].class).fromJson(response.body().source());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return tags;
    }

    /**
     * lists the teams of this repo.
     * @return the list of teams
     */
    public Team[] listTeams() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/teams", owner.getName(), name))
                .build();
        Team[] teams = {};
        try(Response response = Github.getClient().newCall(request).execute()) {
            teams = Github.getMoshi().adapter(Team[].class).fromJson(response.body().source());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teams;
    }

    /**
     * list the teams of this repo and paginates the results.
     * @param resultsPerPage the number of results per page
     * @param page the current page
     * @return the list of teams
     */
    public Team[] listTeams(int resultsPerPage, int page) {
        if(resultsPerPage > 100) throw new IndexOutOfBoundsException("results per page exceeds 100.");
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/teams?per_page=%d&page=%d", owner.getName(), name, resultsPerPage, page))
                .build();
        Team[] teams = {};
        try(Response response = Github.getClient().newCall(request).execute()) {
            teams = Github.getMoshi().adapter(Team[].class).fromJson(response.body().source());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teams;
    }

    /**
     * replaces any existing topics on this repo with new ones.
     * set {@code topics} to an empty array to clear all topics.
     * @param topics the new array of topics
     * @throws HttpErrorException if something went wrong during the update.
     */
    public void replaceTopics(String[] topics) throws HttpErrorException{
        RequestBody body = RequestBody.create(String.format("{" +
                "'names': %s" +
                "}", Github.getMoshi().adapter(String[].class).toJson(topics)), MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/topics", owner.getName(), name))
                .put(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * transfer this repository to another user or organisation.
     * @param newOwnerName the name of the new owner
     * @throws HttpErrorException if the transfer operation failed
     */
    public void transfer(String newOwnerName, int[] teamIds) throws HttpErrorException{
        RequestBody body = RequestBody.create(String.format("{" +
                "'new_owner': '%s'" +
                "'team_ids': '%s'" +
                "}", newOwnerName, Github.getMoshi().adapter(int[].class).toJson(teamIds)), MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/transfer", owner.getName(), name))
                .post(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            // transfer not accepted
            if(response.code() != 202) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * transfers this repo to another user or organisation without teams.
     * if you wish to add teams to this repo after transfer, please use {@link #transfer(String, int[])}.
     * @param newOwnerName the name of the new owner
     * @throws HttpErrorException if the transfer operation failed
     * @see #transfer(String, int[])
     */
    public void transfer(String newOwnerName) throws HttpErrorException{
        RequestBody body = RequestBody.create(String.format("{" +
                "'new_owner': '%s'" +
                "}", newOwnerName), MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/transfer", owner.getName(), name))
                .post(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            // transfer not accepted
            if(response.code() != 202) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * converts this repository to its json representation.
     * @return the json
     */
    public String toJson(){
        return Github.getGson().toJson(this, Repository.class);
    }

    /**
     * get the id of this repository.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * get the global node id of this repo.
     * @return the node id
     */
    public String getNodeId() {
        return node_id;
    }

    public String name;
    public String full_name;
    public Owner owner;

    @SerializedName("private")
    public boolean is_private;

    // this is super gay
    public String html_url;
    public String description;
    public boolean fork;
    public License license;
    public Owner organization;
    public String url;
    public String forks_url;
    public String keys_url;
    public String collaborators_url;
    public String teams_url;
    public String hooks_url;
    public String issue_events_url;
    public String events_url;
    public String assignees_url;
    public String branches_url;
    public String tags_url;
    public String blobs_url;
    public String git_tags_url;
    public String git_refs_url;
    public String trees_url;
    public String statuses_url;
    public String languages_url;
    public String stargazers_url;
    public String contributors_url;
    public String subscribers_url;
    public String subscription_url;
    public String commits_url;
    public String git_commits_url;
    public String comments_url;
    public String issue_comment_url;
    public String contents_url;
    public String compare_url;
    public String merges_url;
    public String archive_url;
    public String downloads_url;
    public String issues_url;
    public String pulls_url;
    public String milestones_url;
    public String notifications_url;
    public String labels_url;
    public String releases_url;
    public String deployments_url;
    public String pushed_at;
    public String created_at;
    public String updated_at;
    public String git_url;
    public String ssh_url;
    public String clone_url;
    public String svn_url;
    public String homepage;
    public int size;
    public int stargazers_count;
    public int watchers_count;
    public String language;
    public boolean has_issues;
    public boolean has_projects;
    public boolean has_downloads;
    public boolean has_wiki;
    public boolean has_pages;
    public int forks_count;
    public String mirror_url;
    public int open_issues_count;
    public int forks;
    public int open_issues;
    public boolean is_template;
    public String[] topics;
    public int watchers;
    public String default_branch;
    public int network_count;
    public int subscribers_count;
    public boolean archived;
    public boolean disabled;
    public String visibility;
    public RepoPermissions permissions;
    public boolean allow_rebase_merge;
    public Repository template_repository;
    public String temp_clone_token;
    public boolean allow_squash_merge;
    public boolean delete_branch_on_merge;
    public boolean allow_merge_commit;
    /**
     * the direct parent of this forked repo.
     */
    public Repository parent;
    /**
     * the ultimate root repo of this fork.
     */
    public Repository source;

    /**
     * uitility class for updating a repository.
     * use this if you don't want to write the json string yourself.
     */
    public static final class UpdateRepo {

        private String owner;
        private String name;
        private String description;
        private String homepage;
        private boolean isPrivate = false;
        private String visibility = "public";
        private boolean hasIssues = true;
        private boolean hasProjects = true;
        private boolean hasWiki = true;
        private boolean isTemplate = false;
        private String defaultBranch = "main";
        private boolean allowSquashMerge = true;
        private boolean allowMergeCommit = true;
        private boolean allowRebaseMerge = true;
        private boolean deleteBranchOnMerge = false;
        private boolean archived = false;

        public UpdateRepo setOwner(String owner){
            this.owner = owner;
            return this;
        }

        public UpdateRepo setName(String name){
            this.name = name;
            return this;
        }

        public UpdateRepo setDescription(String description){
            this.description = description;
            return this;
        }

        public UpdateRepo setHomepage(String url){
            this.homepage = url;
            return this;
        }

        public UpdateRepo isPrivate(boolean isPrivate){
            this.isPrivate = isPrivate;
            return this;
        }

        public UpdateRepo setVisibility(String visibility){
            this.visibility = visibility;
            return this;
        }

        public UpdateRepo hasIssues(boolean hasIssues){
            this.hasIssues = hasIssues;
            return this;
        }

        public UpdateRepo hasProjects(boolean hasProjects){
            this.hasProjects = hasProjects;
            return this;
        }

        public UpdateRepo hasWiki(boolean hasWiki){
            this.hasWiki = hasWiki;
            return this;
        }

        public UpdateRepo isTemplate(boolean isTemplate){
            this.isTemplate = isTemplate;
            return this;
        }

        public UpdateRepo setDefaultBranch(String branch){
            this.defaultBranch = branch;
            return this;
        }

        public UpdateRepo allowSquashMerge(boolean allow){
            this.allowSquashMerge = allow;
            return this;
        }

        public UpdateRepo allowMergeCommit(boolean allow){
            this.allowMergeCommit = allow;
            return this;
        }

        public UpdateRepo allowRebaseMerge(boolean allow){
            this.allowRebaseMerge = allow;
            return this;
        }

        public UpdateRepo deleteBranchOnMerge(boolean delete){
            this.deleteBranchOnMerge = delete;
            return this;
        }

        public UpdateRepo archived(boolean archive){
            this.archived = archive;
            return this;
        }

        /**
         * build the json string for updating this repo.
         * @return the json string
         */
        public String json(){
            // build the json manually
            return String.format("{" +
                    "'name':'%s'" +
                    "'description': '%s'" +
                    "'homepage': '%s'" +
                    "'private': '%s'" +
                    "'visibility': '%s'" +
                    "'has_issues': '%s'" +
                    "'has_projects': '%s'" +
                    "'has_wiki': '%s'" +
                    "'is_template': '%s'" +
                    "'default branch': '%s'" +
                    "'allow_squash_merge': '%s'" +
                    "'allow_merge_commit': '%s'" +
                    "'allow_rebase_merge': '%s'" +
                    "'delete_branch_on_merge': '%s'" +
                    "'archived': '%s'" +
                    "}",
                    name,
                    description,
                    homepage,
                    isPrivate,
                    visibility,
                    hasIssues,
                    hasProjects,
                    hasWiki,
                    isTemplate,
                    defaultBranch,
                    allowSquashMerge,
                    allowMergeCommit,
                    allowRebaseMerge,
                    deleteBranchOnMerge,
                    archived);
        }

        /**
         * updates a repository in github with the specified fields.
         * @throws UnauthorisedException if the update operation failed
         */
        public void update() throws UnauthorisedException{
                Repository.update(owner, name, json());
        }
    }

    /**
     * builder class for creating repositories.
     * see <a href="https://docs.github.com/en/free-pro-team@latest/rest/reference/repos#create-an-organization-repository">the github docs</a> for the params. ignore {@code org} and {@code team_id} as those are for organisations.
     */
    public static class Builder {
        // post /user/repos

        public String name;
        public String description;
        public String homepage;
        public boolean isPrivate;
        public String visibility = "public";
        public boolean hasIssues = true;
        public boolean hasProjects = true;
        public boolean hasWiki = true;
        public boolean isTemplate = false;
        public boolean allowSquashMerge = true;
        public boolean allowMergeCommit = true;
        public boolean allowRebaseMerge = true;
        public boolean deleteBranchOnMerge = false;
        public boolean autoInit = false;
        public String gitignoreTemplate;
        public String licenseTemplate;

        /**
         * sends a post request to github requesting to create this repository.
         * @throws HttpErrorException if the create operation failed
         */
        public void build() throws HttpErrorException{
            String json = String.format("{" +
                            "'name':'%s'" +
                            "'description': '%s'" +
                            "'homepage': '%s'" +
                            "'private': '%s'" +
                            "'visibility': '%s'" +
                            "'has_issues': '%s'" +
                            "'has_projects': '%s'" +
                            "'has_wiki': '%s'" +
                            "'is_template': '%s'" +
                            "'auto_init': '%s'" +
                            "'gitignore_template': '%s" +
                            "'license_template': '%s'" +
                            "'allow_squash_merge': '%s'" +
                            "'allow_merge_commit': '%s'" +
                            "'allow_rebase_merge': '%s'" +
                            "'delete_branch_on_merge': '%s'" +
                            "}",
                    name,
                    description,
                    homepage,
                    isPrivate,
                    visibility,
                    hasIssues,
                    hasProjects,
                    hasWiki,
                    isTemplate,
                    autoInit,
                    gitignoreTemplate,
                    licenseTemplate,
                    allowSquashMerge,
                    allowMergeCommit,
                    allowRebaseMerge,
                    deleteBranchOnMerge);
            Repository.create(json);
        }
    }
}
