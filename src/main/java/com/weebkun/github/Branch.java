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

import com.squareup.moshi.Json;
import com.squareup.moshi.ToJson;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * represents a branch in a repo.
 */
public class Branch {
    /**
     * the owner of the repository
     */
    public String owner;
    /**
     * the name of the repository
     */
    public String repo;
    /**
     * the name of the branch
     */
    public String name;
    public Commit commit;
    @Json(name = "protected")
    public boolean isProtected;
    public Protection protection;
    public String protection_url;

    /**
     * gets the protection status of a branch.
     * @return the branch's protection status
     */
    public Protection getProtection() {
        Protection protection = Github.getNetworkUtil().get(String.format("/repos/%s/%s/branches/%s/protection", owner, repo, name),
        Protection.class);
        protection.owner = owner;
        protection.repo = repo;
        protection.name = name;
        return protection;
    }

    /**
     * the protection object returned in branch calls.
     */
    public static class Protection {
        private transient String owner;
        private transient String repo;
        private transient String name;
        public String url;
        public RequiredStatusChecks required_status_checks;
        public EnforceAdmins enforce_admins;
        public RequiredPullRequestReviews required_pull_request_reviews;
        public Restrictions restrictions;
        public RequiredLinearHistory required_linear_history;
        public AllowForcePushes allow_force_pushes;
        public AllowDeletions allow_deletions;

        /**
         * updates this branch's protection policies
         */
        public void update() {
            Github.getNetworkUtil().put(String.format("/repos/%s/%s/branches/%s/protection", owner, repo, name),
                    Github.getMoshi().adapter(Protection.class).toJson(this));
        }

        /**
         * removes this branch's protection
         * after calling this method, this branch will no longer be protected.
         */
        public void disable() {
            Github.getNetworkUtil().delete(String.format("/repos/%s/%s/branches/%s/protection", owner, repo, name));
        }

        /**
         * gets the current setting for admin enforcement for this protected branch
         * @return the enforce admins setting
         */
        public Protection.EnforceAdmins isAdminPolicyEnforced() {
            return Github.getNetworkUtil().get(String.format("/repos/%s/%s/branches/%s/protection/enforce_admins", owner, repo, name),
                    Protection.EnforceAdmins.class);
        }

        /**
         * set the enforce_admins policy
         * @param enforce set true to enforce the admin branch protection
         */
        public void enforceAdmins(boolean enforce) {
            if(enforce) Github.getNetworkUtil().post(String.format("/repos/%s/%s/branches/%s/protection/enforce_admins", owner, repo, name), "");
            else Github.getNetworkUtil().delete(String.format("/repos/%s/%s/branches/%s/protection/enforce_admins", owner, repo, name));
        }

        /**
         * checks if pull request reviews are required and the pull request review policies
         * use the luke cage preview media type to get back the {@code required_approving_review_count}
         * @return the pull request review object
         */
        public Protection.RequiredPullRequestReviews getRequiredPullRequestReviewPolicy() {
            return Github.getNetworkUtil().get(String.format("/repos/%s/%s/branches/%s/protection/required_pull_request_reviews", owner, repo, name),
                    Protection.RequiredPullRequestReviews.class);
        }

        /**
         * updates the pull request protection policy
         * @param policy the policy object
         * @param enabled indicate whether to disable this policy or leave it enabled
         */
        public void updatePullRequestReviewPolicy(Protection.RequiredPullRequestReviews policy, boolean enabled) {
            if(enabled) Github.getNetworkUtil().patch(String.format("/repos/%s/%s/branches/%s/protection/required_pull_request_reviews", owner, repo, name),
                    Github.getMoshi().adapter(Protection.RequiredPullRequestReviews.class).toJson(policy));
            else Github.getNetworkUtil().delete(String.format("/repos/%s/%s/branches/%s/protection/required_pull_request_reviews", owner, repo, name));
        }

        /**
         * checks if commit signatures are required.
         * requires the zzzax preview media type.
         * @return the commit signature policy
         */
        public Protection.RequireCommitSignatures getCommitSignaturePolicy() {
            return Github.getNetworkUtil().get(String.format("/repos/%s/%s/branches/%s/protection/required_signatures", owner, repo, name),
                    Protection.RequireCommitSignatures.class);
        }

        /**
         * updates the commit signature policy
         * @param enabled indicates whether the policy is enabled or disabled
         */
        public void updateCommitSignaturePolicy(boolean enabled) {
            if(enabled) Github.getNetworkUtil().post(String.format("/repos/%s/%s/branches/%s/protection/required_signatures", owner, repo, name),
                    "");
            else Github.getNetworkUtil().delete(String.format("/repos/%s/%s/branches/%s/protection/required_signatures", owner, repo, name));
        }

        public Protection.RequiredStatusChecks getStatusCheckPolicy() {
            return Github.getNetworkUtil().get(String.format("/repos/%s/%s/branches/%s/protection/required_status_checks", owner, repo, name),
                    Protection.RequiredStatusChecks.class);
        }

        /**
         * setting for including admins in the enforcement of the protection policies.
         * if {@code enabled} is set to true, admins are also subject to the protection policies,
         * else admins are ignored(i.e. not restricted).
         */
        static class EnforceAdmins {
            public String url;
            public boolean enabled;
        }

        static class RequiredStatusChecks {
            public String url;
            public String enforcement_level;
            /**
             * only used when updating the branch protection
             */
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

        /**
         * moshi adapter to parse a protection object to update
         */
        public static class UpdateAdapter {

            public ProtectionJson.RequiredPullRequestReviews toJson(Protection.RequiredPullRequestReviews requiredPullRequestReviews) {
                return new ProtectionJson.RequiredPullRequestReviews(
                        new ProtectionJson.DismissalRestrictions(requiredPullRequestReviews.dismissal_restrictions),
                        requiredPullRequestReviews.dismiss_stale_reviews,
                        requiredPullRequestReviews.require_code_owner_reviews,
                        requiredPullRequestReviews.required_approving_review_count);
            }

            @ToJson
            public ProtectionJson toJson(Branch.Protection protection) {
                // create json
                ProtectionJson json = new ProtectionJson();
                json.required_status_checks = protection.required_status_checks != null ? new ProtectionJson.RequiredStatusChecks(
                        protection.required_status_checks.strict,
                        protection.required_status_checks.contexts) : null;
                json.enforce_admins = protection.enforce_admins.enabled;
                json.required_pull_request_reviews = new ProtectionJson.RequiredPullRequestReviews(
                        new ProtectionJson.DismissalRestrictions(protection.required_pull_request_reviews.dismissal_restrictions),
                        protection.required_pull_request_reviews.dismiss_stale_reviews,
                        protection.required_pull_request_reviews.require_code_owner_reviews,
                        protection.required_pull_request_reviews.required_approving_review_count);
                json.restrictions = new UpdateAdapter.ProtectionJson.Restrictions(protection.restrictions);
                json.required_linear_history = protection.required_linear_history.enabled;
                json.allow_force_pushes = protection.allow_force_pushes.enabled;
                json.allow_deletions = protection.allow_deletions.enabled;
                return json;
            }

            private static class ProtectionJson {
                // todo do javadoc
                UpdateAdapter.ProtectionJson.RequiredStatusChecks required_status_checks;
                boolean enforce_admins;
                UpdateAdapter.ProtectionJson.RequiredPullRequestReviews required_pull_request_reviews;
                UpdateAdapter.ProtectionJson.Restrictions restrictions;
                boolean required_linear_history = false;
                boolean allow_force_pushes = false;
                boolean allow_deletions = false;

                static class RequiredStatusChecks {
                    RequiredStatusChecks(boolean strict, String[] contexts) {
                        this.strict = strict;
                        this.contexts = contexts;
                    }

                    boolean strict;
                    String[] contexts;
                }

                static class RequiredPullRequestReviews {
                    RequiredPullRequestReviews(DismissalRestrictions restrictions,
                                               boolean dismiss_stale_reviews,
                                               boolean require_code_owner_reviews,
                                               int required_approving_review_count) {
                        this.dismissal_restrictions = restrictions;
                        this.dismiss_stale_reviews = dismiss_stale_reviews;
                        this.require_code_owner_reviews = require_code_owner_reviews;
                        this.required_approving_review_count = required_approving_review_count;
                    }

                    DismissalRestrictions dismissal_restrictions;
                    boolean dismiss_stale_reviews;
                    boolean require_code_owner_reviews;
                    int required_approving_review_count;
                }

                static class DismissalRestrictions {
                    DismissalRestrictions(Protection.DismissalRestrictions restrictions) {
                        this.users = Arrays.stream(restrictions.users).map(User::getLogin).collect(Collectors.toList()).toArray(new String[]{});
                        this.teams = Arrays.stream(restrictions.teams).map(team -> team.slug).collect(Collectors.toList()).toArray(new String[]{});
                    }

                    String[] users;
                    String[] teams;
                }

                static class Restrictions {
                    Restrictions(Branch.Protection.Restrictions restrictions) {
                        this.users = Arrays.stream(restrictions.users).map(User::getLogin).collect(Collectors.toList()).toArray(new String[]{});
                        this.teams = Arrays.stream(restrictions.teams).map(team -> team.slug).collect(Collectors.toList()).toArray(new String[]{});
                        this.apps = Arrays.stream(restrictions.apps).map(app -> app.slug).collect(Collectors.toList()).toArray(new String[]{});
                    }

                    String[] users;
                    String[] teams;
                    String[] apps;
                }
            }
        }
    }
}
