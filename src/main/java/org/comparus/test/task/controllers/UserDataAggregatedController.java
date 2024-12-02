package org.comparus.test.task.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserDataAggregatedController {

    private final Logger logger = LogManager.getLogger(UserDataAggregatedController.class);

    private final ApplicationContext applicationContext;
    private final DataSourceDefinitionsHolder dataSourceDefinitionsHolder;

    @GetMapping("/users")
    public List<User> getUsers(UsersRequestDto usersRequestDto) {
        logger.debug("Got user request: {}", usersRequestDto);
        List<User> users = Collections.synchronizedList(new ArrayList<>());

        Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
        dataSources.entrySet().stream().parallel().forEach(entry -> {
            var datasourceName = entry.getKey();
            var dataSource = entry.getValue();
            var namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

            var dataSourceDefinition = dataSourceDefinitionsHolder.getDataSourceDefinition(datasourceName);
            var sql = buildQuery(dataSourceDefinition, usersRequestDto);
            try {
                logger.trace("Executing query: {}", sql);
                users.addAll(namedJdbcTemplate.query(sql,
                        new BeanPropertySqlParameterSource(usersRequestDto),
                        new BeanPropertyRowMapper<>(User.class)));
            } catch (Exception e) {
                logger.error("Exception occurred during {} datasource processing", datasourceName);
                throw e;
            }
        });

        return users;
    }

    private static String buildQuery(DataSourceDefinition dataSourceDefinition, UsersRequestDto request) {
        var mapping = dataSourceDefinition.getMapping();
        return "select * from (select " +
                mapping.getId() + " as id, " +
                mapping.getUsername() + " as username, " +
                mapping.getName() + " as name, " +
                mapping.getSurname() + " as surname from " +
                dataSourceDefinition.getTable() + ") a " +
                (request.isNotEmpty() ? " where " + request.transformToSqlNamedConditions() : "");
    }

}
