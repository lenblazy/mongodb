package com.lenibonje.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyObjectId {

    @JsonProperty("$oid")
    private String id;

    public MyObjectId() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
