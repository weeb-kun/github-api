package com.weebkun.api;

import com.weebkun.auth.Scopes;

public class TestAuth {

    public static void main(String[] args) {
        Github.authenticate("f7b267eb6c04f3a1f5ac", new String[]{Scopes.REPO});
    }
}
