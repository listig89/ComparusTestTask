package org.comparus.test.task.dto;

import org.springframework.util.StringUtils;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.springframework.util.StringUtils.hasLength;

public class UsersRequestDto {

    private String id;
    private String username;
    private String name;
    private String surname;

    public boolean isNotEmpty() {
        return hasLength(id) || hasLength(username) || hasLength(name) || hasLength(surname);
    }

    public String transformToSqlNamedConditions() {
        return Stream.of(
                        hasLength(id) ? " id = :id " : "",
                        hasLength(username) ? " username = :username " : "",
                        hasLength(name) ? " name = :name " : "",
                        hasLength(surname) ? " surname = :surname " : "")
                .filter(StringUtils::hasLength)
                .collect(joining(" and "));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
