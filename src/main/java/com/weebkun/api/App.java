package com.weebkun.api;

public class App {
    public int id;
    public String slug;
    public String node_id;
    public Owner owner;
    public String name;
    public String description;
    public String external_url;
    public String html_url;
    public String created_at;
    public String updated_at;
    public AppPermissions permissions;
    public String[] events;
}

class AppPermissions {
    public String metadata;
    public String contents;
    public String issues;
    public String single_file;
}