package ua.comparus.useraggregation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiDataSourceConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource defaultDataSource() {
        // Настройте дефолтный DataSource
        DataSourceProperties.DataSourceConfig config = dataSourceProperties.getDataSources().get(0);
        return DataSourceBuilder.create()
                .url(config.getUrl())
                .username(config.getUser())
                .password(config.getPassword())
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(defaultDataSource());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan("ua.comparus.useraggregation.entities");
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); // Укажите нужный диалект

        factory.setJpaPropertyMap(properties);
        return factory;
    }
    @Bean
    public Map<String, DataSource> dataSources() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        for (DataSourceProperties.DataSourceConfig config : dataSourceProperties.getDataSources()) {
            DataSource dataSource = DataSourceBuilder.create()
                    .url(config.getUrl())
                    .username(config.getUser())
                    .password(config.getPassword())
                    .build();
            dataSourceMap.put(config.getName(), dataSource);
        }
        return dataSourceMap;
    }

    @Bean
    public Map<String, LocalContainerEntityManagerFactoryBean> entityManagerFactories() {
        Map<String, LocalContainerEntityManagerFactoryBean> factoryMap = new HashMap<>();
        for (Map.Entry<String, DataSource> entry : dataSources().entrySet()) {
            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
            factory.setDataSource(entry.getValue());
            factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            factory.setPackagesToScan("ua.comparus.useraggregation.entities");
            Map<String, Object> properties = new HashMap<>();
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            factory.setJpaPropertyMap(properties);
            factory.afterPropertiesSet();
            factoryMap.put(entry.getKey(), factory);
        }
        return factoryMap;
    }
}