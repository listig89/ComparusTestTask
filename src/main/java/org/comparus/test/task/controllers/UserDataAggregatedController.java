package org.comparus.test.task.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.Map;

@RestController
public class UserDataAggregatedController {

    private final ApplicationContext applicationContext;

    public UserDataAggregatedController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @GetMapping("/users")
    public String getUsers() {
        Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
        dataSources.values().forEach(ds -> {
            JdbcTemplate jdt = new JdbcTemplate(ds);
            int result = jdt.queryForObject(
                    "select max(user_id) from users", Integer.class);
            System.out.println(result);
        });

        return "Test";
    }


//
//    @GetMapping("/users1")
//    public List<DataSourceDef> getUsers1() throws IOException {
//        JacksonJsonParser jacksonJsonParser = new JacksonJsonParser(new ObjectMapper(new YAMLFactory()));
//
//        String text = new BufferedReader(
//                new InputStreamReader(getClass().getResourceAsStream("/application.yml"), StandardCharsets.UTF_8))
//                .lines()
//                .collect(Collectors.joining("\n"));
//
//        Map<String, Object> stringObjectMap = jacksonJsonParser.parseMap(text);
//
//        return (List<DataSourceDef>) stringObjectMap.get("data-sources");
//
//    }

}
