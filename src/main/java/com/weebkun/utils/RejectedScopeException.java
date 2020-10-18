package com.weebkun.utils;

/**
 * thrown when requested scopes are rejected by the user.
 */
public class RejectedScopeException extends RuntimeException {

    private final String[] scopes;

    /**
     * throw a new {@code RejectedScopeException} with the scopes rejected as variable arguments.
     * @param scopes the scopes that are rejected.
     */
    public RejectedScopeException(String... scopes) {
        super("rejected scopes:\n" + String.join("\n", scopes));
        this.scopes = scopes;
    }

    public RejectedScopeException(String message, String... scopes) {
        super(message);
        this.scopes = scopes;
    }
}
