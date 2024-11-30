package org.comparus.test.task.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.annotation.PostConstruct;
import org.comparus.test.task.DataSourceDefinition;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class DataSourceInitializer implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void onPostConstruct() {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        try {
            objectMapper.readValue(getClass().getResourceAsStream("/application.yml"),
                            new TypeReference<Map<String, List<DataSourceDefinition>>>() {
                            }).get("data-sources")
                    .forEach(dataSourceDefinition -> {
                        DataSource dataSource = DataSourceBuilder.create()
//                        .driverClassName("com.mysql.cj.jdbc.Driver")
                                .url(dataSourceDefinition.getUrl())
                                .username(dataSourceDefinition.getUser())
                                .password(dataSourceDefinition.getPassword())
                                .build();
                        configurableBeanFactory.registerSingleton(dataSourceDefinition.getName(), dataSource);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Initialized.");
    }
}
