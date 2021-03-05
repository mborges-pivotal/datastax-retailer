package com.borgescloud.datastax.retailer.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ChangeSet {

    private List<Changes> changeset;

    public ChangeSet() {
        changeset = new ArrayList<Changes>();
    }

    public void addChanges(String column, String value) {
        changeset.add(new Changes(column, value));
    }

    @Data
    @AllArgsConstructor
    public static class Changes {

        private String column;
        private String value;
    }
    
}
