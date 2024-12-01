package org.comparus.test.task.services;

import org.comparus.test.task.DataSourceDefinition;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceDefinitionsHolder {

    private final Map<String, DataSourceDefinition> dataSourceDefinitions = new HashMap<>();

    public void addDataSourceDefinition(String dataSourceDefinitionName, DataSourceDefinition dataSourceDefinition) {
        this.dataSourceDefinitions.put(dataSourceDefinitionName, dataSourceDefinition);
    }

    public DataSourceDefinition getDataSourceDefinition(String dataSourceDefinitionName) {
        return this.dataSourceDefinitions.get(dataSourceDefinitionName);
    }
}
