package com.weebkun.utils;

/**
 * thrown when a scope originally requested and granted have since been rejected by the user.
 */
public class ScopeRevokedException extends RuntimeException{

    private final String[] scopes;

    /**
     * throw a new {@code ScopeRevokedException} with the scopes that were revoked by the user as variable arguments.
     * @param scopes the scopes
     */
    public ScopeRevokedException(String... scopes) {
        super("scopes rejected:\n" + String.join("\n", scopes));
        this.scopes = scopes;
    }

    public ScopeRevokedException(String message, String... scopes) {
        super(message);
        this.scopes = scopes;
    }
}
