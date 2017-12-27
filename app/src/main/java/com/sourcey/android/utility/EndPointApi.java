package com.sourcey.android.utility;

public enum EndPointApi {
    CATEGORY("004",String.format("%s/api/v1/categories?limit=100",Constants.HOST)),
    VERIFY("003",String.format("%s/api/v1/verify",Constants.HOST)),
    SINGUP("002",String.format("%s/api/v1/signup",Constants.HOST)),
    LOGIN("001",String.format("%s/api/v1/login",Constants.HOST));

    private String id;
    private String link;

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    EndPointApi() {
    }

    EndPointApi(String id, String link) {
        this.id = id;
        this.link = link;
    }
}
