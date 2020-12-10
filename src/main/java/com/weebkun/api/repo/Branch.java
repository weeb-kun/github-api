package com.weebkun.api.repo;

import com.google.gson.annotations.SerializedName;
import com.weebkun.api.Github;
import com.weebkun.api.MediaTypes;
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
    public void removeProtection() {
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
    public EnforceAdmins getAdminProtection() {
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
}
