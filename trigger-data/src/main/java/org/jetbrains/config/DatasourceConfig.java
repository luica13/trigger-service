package org.jetbrains.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.jetbrains")
@EntityScan(basePackages="org.jetbrains.entity")
public class DatasourceConfig {

}