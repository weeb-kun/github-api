package com.weebkun.github;

/**
 * the protection object returned in branch calls.
 */
public class Protection {
    public String url;
    public boolean enabled;
    public ProtectionStatusChecks required_status_checks;
    public EnforceAdmins enforce_admins;
    public RequiredPullRequestReviews required_pull_request_reviews;
    public Restrictions restrictions;
    public RequiredLinearHistory required_linear_history;
    public AllowForcePushes allow_force_pushes;
    public AllowDeletions allow_deletions;

    /**
     * setting for including admins in the enforcement of the protection policies.
     * if {@code enabled} is set to true, admins are also subject to the protection policies,
     * else admins are ignored(i.e. not restricted).
     */
    static class EnforceAdmins {
        public String url;
        public boolean enabled;
    }

    static class ProtectionStatusChecks {
        public String url;
        public String enforcement_level;
        public String[] contexts;
        public String contexts_url;
    }

    static class RequiredStatusChecks {
        public String url;
        public boolean strict;
        public String[] contexts;
        public String contexts_url;
    }

    static class RequiredPullRequestReviews {
        public String url;
        public DismissalRestrictions dismissal_restrictions;
        public boolean dismiss_stale_reviews;
        public boolean require_code_owner_reviews;
        public int required_approving_review_count;
    }

    static class DismissalRestrictions {
        public String url;
        public String users_url;
        public String teams_url;
        public User[] users;
        public Team[] teams;
    }

    static class Restrictions {
        String url;
        public String users_url;
        public String teams_url;
        public String apps_url;
        public User[] users;
        public Team[] teams;
        public App[] apps;
    }

    static class RequiredLinearHistory {
        public boolean enabled;
    }

    static class AllowForcePushes {
        public boolean enabled;
    }

    static class AllowDeletions {
        public boolean enabled;
    }

    /**
     * requires zzzax preview
     */
    static class RequireCommitSignatures {
        public String url;
        public boolean enabled;
    }
}