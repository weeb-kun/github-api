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

import com.google.gson.annotations.SerializedName;
import com.weebkun.utils.HttpErrorException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 * represents a branch in a repo.
 */
public class Branch {
    public String owner;
    public String repo;
    public String name;
    public Commit commit;
    @SerializedName("protected")
    public boolean isProtected;
    public Protection protection;
    public String protection_url;

    /**
     * gets the protection status of a branch.
     * @return the branch's protection status
     */
    public Protection getProtection() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection", owner, repo, name))
                .build();
        Protection protection = null;
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
            protection = Github.getGson().fromJson(response.body().string(), Protection.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return protection;
    }

    /**
     * updates this repo's branch protection policies.
     * @param options the options obj specifying the protection policies to update. see {@link ProtectionOptions} for more info.
     * @see ProtectionOptions
     */
    public void updateProtection(ProtectionOptions options) {
        RequestBody body = RequestBody.create(options.parse(), MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection", owner, repo, name))
                .put(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * removes this branch's protection.
     * after calling this method, this branch will no longer be protected.
     */
    public void disableProtection() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection", owner, repo, name))
                .delete()
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 204) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the current setting for admin enforcement for this protected branch.
     * @return the enforce admins setting
     */
    public EnforceAdmins isAdminEnforced() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection/enforce_admins", owner, repo, name))
                .build();
        EnforceAdmins admin = null;
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
            admin = Github.getMoshi().adapter(EnforceAdmins.class).fromJson(response.body().source());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return admin;
    }

    /**
     * enforce the protection policies for admins
     */
    public void enforceAdmins() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection/enforce_admins", owner, repo, name))
                .post(RequestBody.create("", MediaType.get(MediaTypes.REQUEST_BODY_TYPE)))
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * do not enforce protection policies on admins
     */
    public void doNotEnforceAdmins() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection/enforce_admins", owner, repo, name))
                .delete()
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 204) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks if pull request reviews are required and the pull request review policies.
     * use the luke cage preview media type to get back the {@code required_approving_review_count}.
     * @return the pull request review object
     */
    public RequiredPullRequestReviews getRequiredPullRequestReviewPolicy() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection/required_pull_request_reviews", owner, repo, name))
                .build();
        RequiredPullRequestReviews protection = null;
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
            protection = Github.getMoshi().adapter(RequiredPullRequestReviews.class).fromJson(response.body().source());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return protection;
    }

    public void updatePullRequestReviewPolicy(ProtectionOptions.RequiredPullRequestReviews policy) {
        RequestBody body = RequestBody.create(policy.parse(), MediaType.get(MediaTypes.REQUEST_BODY_TYPE));
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection/required_pull_request_reviews", owner, repo, name))
                .patch(body)
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disablePullRequestReviewPolicy() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection/required_pull_request_reviews", owner, repo, name))
                .delete()
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 204) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks if commit signatures are required.
     * requires the zzzax preview media type.
     * @return the commit signature policy
     */
    public RequireCommitSignatures getCommitSignaturePolicy() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection/required_signatures", owner, repo, name))
                .build();
        RequireCommitSignatures required = null;
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
            required = Github.getMoshi().adapter(RequireCommitSignatures.class).fromJson(response.body().source());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return required;
    }

    /**
     * sets the commit signature protection policy for this branch.
     * requires admin access and the zzzax preview.
     */
    public void enableCommitSignaturePolicy() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection/required_signatures", owner, repo, name))
                .post(RequestBody.create("", MediaType.get(MediaTypes.REQUEST_BODY_TYPE)))
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * disables the commit signature protection policy for this branch.
     */
    public void disableCommitSignaturePolicy() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection/required_signatures", owner, repo, name))
                .delete()
                .build();
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 204) throw new HttpErrorException(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RequiredStatusChecks getStatusCheckPolicy() {
        Request request = new Request.Builder()
                .url(Github.getRoot() + String.format("/repos/%s/%s/branches/%s/protection/required_status_checks", owner, repo, name))
                .build();
        RequiredStatusChecks required = null;
        try(Response response = Github.getClient().newCall(request).execute()) {
            if(response.code() != 200) throw new HttpErrorException(response);
            required = Github.getMoshi().adapter(RequiredStatusChecks.class).fromJson(response.body().source());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return required;
    }
}
