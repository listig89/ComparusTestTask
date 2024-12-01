package org.comparus.test.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSourceDefinition {
    private String name;
    private String strategy;
    private String url;
    private String table;
    private String user;
    private String password;
    private Mapping mapping;

    @Getter
    @Setter
    public static class Mapping {
        private String id;
        private String username;
        private String name;
        private String surname;
    }
}