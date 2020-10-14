package com.weebkun.auth;

import com.weebkun.api.Github;

public class TestAuth {

    public static void main(String[] args) {
        Github.authenticate("f7b267eb6c04f3a1f5ac", new String[]{Scopes.REPO});
        System.out.println(OAuth.getToken());
    }
}
