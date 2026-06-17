package com.pos.wifi_connection.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "app.pagination")
@Data
public class AppPaginationProperties {
    private int defaultPage;
    private int defaultSize;
    private String defaultSort;
}
