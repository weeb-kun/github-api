package com.weebkun.utils;

import com.weebkun.tree.NTree;
import com.weebkun.tree.Node;
import kotlin.Pair;

import java.util.Map;

/**
 * helper class to parse json fields to a json string.
 */
public class JsonParser {

    private final Map<String, String> fields;

    /**
     * create a JsonParser object with the provided json fields.
     * @param fields the fields in the json
     */
    public JsonParser(Map<String, String> fields) {
        this.fields = fields;
    }

    /**
     * parses the provided fields to a json string
     * @return the json string
     */
    public String parse() {
        String json = "{";

        //iterate through map and construct json string from each entry
        for(Map.Entry<String, String> field : fields.entrySet()) {
            json = json.concat(String.format("\"%s\": \"%s\"", field.getKey(), field.getValue()));
        }
        return json + "}";
    }
}