package com.weebkun.api.repo;

import javax.annotation.Nullable;

/**
 * represents a content object for a repo.
 * this can be a file, a directory, a symlink or a submodule.
 */
public class Content {
    public String type;
    /**
     * the url for the repo of this submodule.
     */
    @Nullable
    public String submodule_git_url;
    /**
     * the path of this symlink's target.
     */
    @Nullable
    public String target;
    public String encoding;
    public int size;
    public String name;
    public String path;
    public String sha;
    public String url;
    public String git_url;
    public String html_url;
    public String download_url;
    public Links _links;
}
