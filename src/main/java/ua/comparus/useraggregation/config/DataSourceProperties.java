package ua.comparus.useraggregation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
@ConfigurationProperties(prefix = "data-sources")
@Data
public class DataSourceProperties {

    private List<DataSourceConfig> dataSources;

    @Data
    public static class DataSourceConfig {
        private String name;
        private String strategy;
        private String url;
        private String table;
        private String user;
        private String password;
        private MappingConfig mapping;

        @Data
        public static class MappingConfig {
            private String id;
            private String username;
            private String name;
            private String surname;
        }
    }
}