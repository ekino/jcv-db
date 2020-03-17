package com.ekino.oss.jcv.db.example.jcvdbmongoexample.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.time.Instant;
import java.util.Optional;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware", dateTimeProviderRef = "utcDateTimeProvider")
public class AuditingConfiguration {

    @Bean
    public DateTimeProvider utcDateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }

    @Bean
    public AuditorAware<String> springSecurityAuditorAware() {
        return () -> Optional.of("system");
    }
}
