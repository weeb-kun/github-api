package com.weebkun.api;

import com.weebkun.auth.Scopes;
import org.junit.Assert;

public class Test {

    /**
     * asserts this repo's name is equal to 'github-api'
     */
    @org.junit.Test
    public void getThisRepoName() {
        // authenticate using my oauth client id
        Github.authenticate("f7b267eb6c04f3a1f5ac", new String[]{Scopes.REPO});
        Assert.assertEquals("github-api", Repository.get("weeb-kun", "github-api").name);
    }
}
