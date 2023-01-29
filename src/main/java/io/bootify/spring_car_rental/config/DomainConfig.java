package io.bootify.spring_car_rental.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.bootify.spring_car_rental.domain")
@EnableJpaRepositories("io.bootify.spring_car_rental.repos")
@EnableTransactionManagement
public class DomainConfig {
}
