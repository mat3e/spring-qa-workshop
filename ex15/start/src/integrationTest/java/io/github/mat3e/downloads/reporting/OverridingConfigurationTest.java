package io.github.mat3e.downloads.reporting;

import io.github.mat3e.downloads.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext // look out - no caching
@IntegrationTest
class OverridingConfigurationTest {
    @Nested
    class NoOverride {
        @Autowired
        ReportingFacade facade;

        @Test
        void isCounting() {
            assertThat(facade).isInstanceOf(CountingFacade.class);
        }
    }

    @Nested
    class WithTestConfiguration {
        @TestConfiguration
        static class PrioritizedConfiguration {
            @Bean
            ReportingFacade testFacade() {
                return new CapturingReportingFacade();
            }
        }

        @Autowired
        ReportingFacade facade;

        @Test
        void isFromTests() {
            assertThat(facade).isInstanceOf(CapturingReportingFacade.class);
        }
    }
}
