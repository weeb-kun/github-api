package com.weebkun.github;

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
    /**
     * can be 'all', 'public' or 'private'.
     */
    public String visibility;
    /**
     * can be 'owner', 'collaborator' or 'organization_member'.
     */
    public String affiliation;

    /**
     * can be 'all', 'owner', 'public', 'private', or 'member'.
     *
     * cannot be used with visibility or affiliation.
     * will throw a {@link ParamConflictException} when used with visibility or affiliation.
     */
    public String type;

    /**
     * sorting options.
     * can be 'created', 'updated', 'pushed' or 'full_name'.
     */
    public String sort;

    /**
     * the direction of sorting.
     * can be 'asc' or 'desc'.
     */
    public String direction;

    /**
     * the number of results per page.
     * max is 100.
     */
    public int perPage;

    /**
     * current page to get.
     */
    public int page;

    /**
     * ISO 8601 timestamp indicating only to show updates after this time.
     * cannot be used with before.
     */
    public String since;

    /**
     * ISO 8601 timestamp indicating only to show updates before this time.
     * cannot be used with since.
     */
    public String before;
}
