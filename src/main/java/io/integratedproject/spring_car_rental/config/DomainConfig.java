package io.integratedproject.spring_car_rental.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.integratedproject.spring_car_rental.entity")
@EnableJpaRepositories("io.integratedproject.spring_car_rental.repository")
@EnableTransactionManagement
public class DomainConfig {
}
