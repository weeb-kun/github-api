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
 * used for updating the protection status of a branch.
 * set the public fields and pass to {@link Branch#updateProtection(ProtectionOptions)} to update.
 */
public class ProtectionOptions {
    // todo do javadoc
    public RequiredStatusChecks required_status_checks;
    public boolean enforce_admins;
    public RequiredPullRequestReviews required_pull_request_reviews;
    /**
     * set to null for personal repos.
     */
    public Restrictions restrictions;
    public boolean required_linear_history = false;
    public boolean allow_force_pushes = false;
    public boolean allow_deletions = false;

    /**
     * parses this options object to a json string.
     * @return the json string
     */
    public String parse() {
        return "{\"required_status_checks\":" + (required_status_checks != null ? required_status_checks.parse() : "null,") +
                String.format("\"enforce_admins\":%s", enforce_admins) +
                "\"required_pull_request_reviews\":" +
                (required_pull_request_reviews != null ? required_pull_request_reviews.parse() : "null,") +
                (restrictions != null ? restrictions.parse() : "null,") +
                String.format("\"required_linear_history\":%s,", required_linear_history) +
                String.format("\"allow_force_pushes\":%s,", allow_force_pushes) +
                String.format("\"allow_deletions\":%s", allow_deletions);
    }

    static class RequiredStatusChecks {
        public boolean strict;
        public String[] contexts;

        public String parse() {
            String json = "{" +
                    String.format("\"strict\":%s", strict) +
                    "\"contexts\":[";
            for(String context : contexts) {
                json += String.format("\"%s\",", context);
            }
            return json.substring(0, json.length() - 1) + "]},";
        }
    }

    static class RequiredPullRequestReviews {
        /**
         * not needed for personal repos. set this field to null for personal repos.
         */
        public DismissalRestrictions dismissal_restrictions;
        public boolean dismiss_stale_reviews;
        public boolean require_code_owner_reviews;
        public int required_approving_review_count;

        public String parse() {
            return "{" + (dismissal_restrictions != null ? dismissal_restrictions.parse() : "") + "," +
                    String.format("\"dismiss_stale_reviews\":%s,", dismiss_stale_reviews) +
                    String.format("\"require_code_owner_reviews\":%s,", require_code_owner_reviews) +
                    String.format("\"required_approving_review_count\":%d}", required_approving_review_count);
        }
    }

    /**
     * pass in null for both arrays to disable dismissal restrictions.
     */
    static class DismissalRestrictions {
        public String[] users;
        public String[] teams;

        public String parse() {
            String json = "\"dismissal_restrictions\":{";
            if(users == null && teams == null) return json + "}";
            if(users != null) {
                json += "\"users\":[";
                for (String user : users) {
                    json += String.format("\"%s\",", user);
                }
                json += "]";
            }
            if(teams != null) {
                json += users != null ? ",\"teams\":[" : "\"teams\":[";
                for (String team : teams) {
                    json += String.format("\"%s\",", team);
                }
                json += "]";
            }
            return json + "}";
        }
    }

    static class Restrictions {
        public String[] users;
        public String[] teams;
        public String[] apps;

        public String parse() {
            String json = "\"restrictions\":{";
            if(users == null && teams == null && apps== null) return json + "},";
            if(users != null) {
                json += "\"users\":[";
                for (String user : users) {
                    json += String.format("\"%s\",", user);
                }
                json += "]";
            }
            if(teams != null) {
                json += users != null ? ",\"teams\":[" : "\"teams\":[";
                for (String team : teams) {
                    json += String.format("\"%s\",", team);
                }
                json += "]";
            }
            if(apps != null) {
                json += users != null || teams != null ? ",\"apps\":[" : "\"apps\":[";
                for (String app : apps) {
                    json += String.format("\"%s\",", app);
                }
                json += "]";
            }
            return json + "},";
        }
    }
}