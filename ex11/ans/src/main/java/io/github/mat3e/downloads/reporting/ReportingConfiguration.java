package io.github.mat3e.downloads.reporting;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class ReportingConfiguration {
    private final MeterRegistry meterRegistry;

    ReportingConfiguration(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Bean
    ReportingFacade defaultReportingFacade() {
        return new LoggingReportingFacade();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.reporting.counting", havingValue = "true")
    ReportingFacade countingReportingFacade(ReportingFacade original) {
        var result = new CountingFacade(meterRegistry);
        result.append(original);
        return result;
    }
}
