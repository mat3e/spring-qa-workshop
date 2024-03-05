package io.github.mat3e.downloads.reporting;

import io.github.mat3e.downloads.eventhandling.DomainEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.Instant;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(OutputCaptureExtension.class)
class LoggingReportingFacadeTest {
    private final ReportingFacade toTest = new LoggingReportingFacade();

    @Test
    void recordEvent_logs(CapturedOutput output) {
        // when
        toTest.recordEvent(new StringEvent("info event"));

        then(output.getOut()).contains("INFO").contains("info event").doesNotContain("WARN");

        // when
        toTest.recordEvent(new SuspiciousStringEvent("warn event"));

        then(output.getOut()).contains("WARN").contains("warn event");
    }

    private static class SuspiciousStringEvent extends StringEvent {
        SuspiciousStringEvent(String value) {
            super(value);
        }

        @Override
        public boolean suspicious() {
            return true;
        }
    }

    private static class StringEvent implements DomainEvent {
        private final String value;

        StringEvent(String value) {
            this.value = value;
        }

        @Override
        public Instant occurredOn() {
            return Instant.EPOCH;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
