package org.comparus.test.task.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.annotation.PostConstruct;
import org.comparus.test.task.DataSourceDefinition;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class DataSourceInitializer implements BeanFactoryAware {

    private BeanFactory beanFactory;
    private final DataSourceDefinitionsHolder dataSourceDefinitionsHolder = new DataSourceDefinitionsHolder();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public DataSourceDefinitionsHolder dataSourceDefinitionsHolder() {
        return dataSourceDefinitionsHolder;
    }

    @PostConstruct
    public void onPostConstruct() {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        try {
            List<DataSourceDefinition> dataSourceDefinitions = objectMapper.readValue(getClass().getResourceAsStream("/application.yml"),
                    new TypeReference<Map<String, List<DataSourceDefinition>>>() {
                    }).get("data-sources");

            dataSourceDefinitions.forEach(dataSourceDefinition -> {
                DataSource dataSource = DataSourceBuilder.create()
                        .driverClassName(resolveDriverClassNameByStrategy(dataSourceDefinition.getStrategy()))
                        .url(dataSourceDefinition.getUrl())
                        .username(dataSourceDefinition.getUser())
                        .password(dataSourceDefinition.getPassword())
                        .build();
                String dataSourceDefinitionName = dataSourceDefinition.getName();
                configurableBeanFactory.registerSingleton(dataSourceDefinitionName, dataSource);
                dataSourceDefinitionsHolder.addDataSourceDefinition(dataSourceDefinitionName, dataSourceDefinition);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String resolveDriverClassNameByStrategy(String strategy) {
        return switch (strategy.toLowerCase()) {
            case "postgres" -> "org.postgresql.Driver";
            case "mysql" -> "com.mysql.cj.jdbc.Driver";
            case "mariadb" -> "org.mariadb.jdbc.Driver";
            default -> throw new RuntimeException("Unknown strategy: " + strategy);
        };
    }
}
