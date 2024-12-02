package org.comparus.test.task.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comparus.test.task.DataSourceDefinition;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Service
public class DataSourceInitializer implements BeanFactoryAware {

    private final Logger logger = LogManager.getLogger(DataSourceInitializer.class);

    private BeanFactory beanFactory;
    private final DataSourceDefinitionsHolder dataSourceDefinitionsHolder = new DataSourceDefinitionsHolder();

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public DataSourceDefinitionsHolder dataSourceDefinitionsHolder() {
        return dataSourceDefinitionsHolder;
    }

    @PostConstruct
    public void onPostConstruct() throws IOException {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        var objectMapper = new ObjectMapper(new YAMLFactory());

        ofNullable(objectMapper.readValue(getClass().getResourceAsStream("/datasources.yml"),
                new TypeReference<Map<String, List<DataSourceDefinition>>>() {
                }).get("data-sources")).orElse(emptyList())
                .forEach(dataSourceDefinition -> {
                    try {
                        var dataSource = DataSourceBuilder.create()
                                .driverClassName(resolveDriverClassNameByStrategy(dataSourceDefinition.getStrategy()))
                                .url(dataSourceDefinition.getUrl())
                                .username(dataSourceDefinition.getUser())
                                .password(dataSourceDefinition.getPassword())
                                .build();
                        var dataSourceDefinitionName = dataSourceDefinition.getName();
                        configurableBeanFactory.registerSingleton(dataSourceDefinitionName, dataSource);
                        dataSourceDefinitionsHolder.addDataSourceDefinition(dataSourceDefinitionName, dataSourceDefinition);
                    } catch (Exception e) {
                        logger.error("Exception while building datasource {}", dataSourceDefinition);
                        throw new RuntimeException(e);
                    }
                });
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
