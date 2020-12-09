package com.weebkun.utils;

import java.util.Map;

/**
 * helper class to parse query fields to query strings.
 */
public class QueryStringParser {

    private final Map<String, String> fields;

    /**
     * creates a query string parser object with the provided mapping of query fields
     * @param fields the query fields
     */
    public QueryStringParser(Map<String, String> fields) {
        this.fields = fields;
    }

    /**
     * parses a mapping of fields to a query string.
     * @return the query string
     */
    public String parse() {
        String query = "?";

        //iterate over the mapping and construct the query string
        for(Map.Entry<String, String> field : fields.entrySet()) {
            query = query.concat(String.format("%s=%s&", field.getKey(), field.getValue()));
        }

        // remove last "&" from query string
        return query.substring(0, query.length() - 1);
    }
}
