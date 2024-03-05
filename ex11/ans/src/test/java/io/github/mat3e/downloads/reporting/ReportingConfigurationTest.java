package io.github.mat3e.downloads.reporting;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.mock;

class ReportingConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withBean(MeterRegistry.class, () -> mock(MeterRegistry.class))
            .withUserConfiguration(ReportingConfiguration.class);

    @Test
    void providesDefaultReportingBean() {
        contextRunner.run(context -> then(context)
                .hasSingleBean(ReportingFacade.class)
                .getBeanNames(ReportingFacade.class).hasSize(1));
    }

    @Test
    void providesCountingReportingBean() {
        contextRunner.withPropertyValues("app.reporting.counting=true")
                .run(context -> then(context).hasNotFailed()
                        .hasSingleBean(CountingFacade.class)
                        .getBeanNames(ReportingFacade.class).hasSize(2));
    }
}
