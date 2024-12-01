package org.comparus.test.task.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.springframework.util.StringUtils.hasLength;

@Getter
@Setter
@ToString
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

}
