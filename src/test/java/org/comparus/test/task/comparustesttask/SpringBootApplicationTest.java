package org.comparus.test.task.comparustesttask;

import jakarta.annotation.PostConstruct;
import org.comparus.test.task.DataSourceDefinition;
import org.comparus.test.task.services.DataSourceDefinitionsHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@TestPropertySource(properties = {"spring.config.location=classpath:application-properties.yml"})
public class SpringBootApplicationTest implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Autowired
    public DataSourceDefinitionsHolder dataSourceDefinitionsHolder;

    @PostConstruct
    void prepareDatabaseContext() {
        JdbcDatabaseContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
                .withDatabaseName("integration-tests-db")
                .withUsername("test_user")
                .withPassword("test_password")
                .withInitScript("UserDataAggregatedControllerTest.sql");
        postgreSQLContainer.start();

        var dataSource = DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();

        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        String dataSourceDefinitionName = "test-ds";
        configurableBeanFactory.registerSingleton(dataSourceDefinitionName, dataSource);
        DataSourceDefinition dataSourceDefinition = new DataSourceDefinition();
        dataSourceDefinition.setTable("users___table");
        DataSourceDefinition.Mapping mapping = new DataSourceDefinition.Mapping();
        mapping.setId("user___id");
        mapping.setName("name");
        mapping.setUsername("user___name");
        mapping.setSurname("sur___name");
        dataSourceDefinition.setMapping(mapping);

        dataSourceDefinitionsHolder.addDataSourceDefinition(dataSourceDefinitionName, dataSourceDefinition);
    }

}
