package io.github.mat3e.downloads.reporting;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReportingConfiguration {
    private final MeterRegistry meterRegistry;

    ReportingConfiguration(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Bean
    ReportingFacade reportingFacade() {
        return new CountingFacade(meterRegistry);
    }
}
