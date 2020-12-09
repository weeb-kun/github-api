package com.weebkun.api.repo;

import com.google.gson.annotations.SerializedName;

/**
 * represents a branch in a repo.
 */
public class Branch {
    public String name;
    public Commit commit;
    @SerializedName("protected")
    public boolean isProtected;
    public Protection protection;
    public String protection_url;
}
