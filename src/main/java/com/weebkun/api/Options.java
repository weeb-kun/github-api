package com.weebkun.api;

import com.weebkun.utils.ParamConflictException;

/**
 * options object for request params.
 * contains properties frequently passed in to requests, especially for pagination, sorting and filtering.
 *
 * <p>
 *     note: do not use {@code type} with {@code visibility} or {@code affiliation}.
 *     do not use {@code since} timestamp with {@code before} and vice versa.
 * </p>
 */
public class Options {
    public String visibility;
    public String affiliation;

    /**
     * cannot be used with visibility or affiliation.
     * will throw a {@link ParamConflictException} when used with visibility or affiliation.
     */
    public String type;

    public String sort;
    public String direction;
    public int perPage;
    public int page;
    /**
     * cannot be used with before.
     */
    public String since;
    /**
     * cannot be used with since.
     */
    public String before;
}
