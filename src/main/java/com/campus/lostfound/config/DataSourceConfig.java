package com.campus.lostfound.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.net.URI;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    @Bean
    DataSource dataSource(
            @Value("${DATABASE_URL:jdbc:h2:file:./data/lostfound}") String databaseUrl,
            @Value("${DATABASE_USERNAME:sa}") String databaseUsername,
            @Value("${DATABASE_PASSWORD:}") String databasePassword,
            @Value("${DATABASE_DRIVER:}") String databaseDriver) {
        var config = new HikariConfig();

        if (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://")) {
            configureRenderPostgres(config, databaseUrl);
        } else {
            config.setJdbcUrl(databaseUrl);
            config.setUsername(databaseUsername);
            config.setPassword(databasePassword);
        }

        if (!databaseDriver.isBlank()) {
            config.setDriverClassName(databaseDriver);
        }

        return new HikariDataSource(config);
    }

    private void configureRenderPostgres(HikariConfig config, String databaseUrl) {
        var uri = URI.create(databaseUrl);
        var userInfo = uri.getUserInfo() == null ? ":" : uri.getUserInfo();
        var userParts = userInfo.split(":", 2);
        var database = uri.getPath() == null ? "" : uri.getPath();

        config.setJdbcUrl("jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + database);
        config.setUsername(userParts[0]);
        config.setPassword(userParts.length > 1 ? userParts[1] : "");
    }
}
