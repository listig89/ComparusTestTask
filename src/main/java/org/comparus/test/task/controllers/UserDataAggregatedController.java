package org.comparus.test.task.controllers;

import org.comparus.test.task.DataSourceDefinition;
import org.comparus.test.task.dto.UsersRequestDto;
import org.comparus.test.task.entities.User;
import org.comparus.test.task.services.DataSourceDefinitionsHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserDataAggregatedController {

    private final ApplicationContext applicationContext;
    private final DataSourceDefinitionsHolder dataSourceDefinitionsHolder;

    public UserDataAggregatedController(ApplicationContext applicationContext,
                                        DataSourceDefinitionsHolder dataSourceDefinitionsHolder) {
        this.applicationContext = applicationContext;
        this.dataSourceDefinitionsHolder = dataSourceDefinitionsHolder;
    }

    @GetMapping("/users")
    public List<User> getUsers(UsersRequestDto usersRequestDto) {
        List<User> users = new ArrayList<>();

        Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
        dataSources.forEach((datasourceName, dataSource) -> {
            NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            DataSourceDefinition dataSourceDefinition = dataSourceDefinitionsHolder.getDataSourceDefinition(datasourceName);
            String sql = buildQuery(dataSourceDefinition, usersRequestDto);
            try {
                users.addAll(namedJdbcTemplate.query(sql,
                        new BeanPropertySqlParameterSource(usersRequestDto),
                        new BeanPropertyRowMapper<>(User.class)));
            }
            catch (Exception e) {
                System.out.println(datasourceName);
                throw e;
            }
        });

        return users;
    }

    private static String buildQuery(DataSourceDefinition dataSourceDefinition, UsersRequestDto request) {
        DataSourceDefinition.Mapping mapping = dataSourceDefinition.getMapping();
        return "select * from (select " +
                mapping.getId() + " as id, " +
                mapping.getUsername() + " as username, " +
                mapping.getName() + " as name, " +
                mapping.getSurname() + " as surname from " +
                dataSourceDefinition.getTable() + ") a " +
                (request.isNotEmpty() ? " where " + request.transformToSqlNamedConditions() : "");
    }

}
