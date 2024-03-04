package io.github.mat3e.downloads.reporting;

import io.github.mat3e.downloads.eventhandling.DomainEvent;
import org.assertj.core.api.ListAssert;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CapturingReportingFacade implements ReportingFacade {
    private final List<DomainEvent> capturedEvents = new ArrayList<>();

    @Override
    public void recordEvent(DomainEvent event) {
        capturedEvents.add(event);
    }

    public ListAssert<DomainEvent> recordedEvents() {
        return assertThat(capturedEvents);
    }
}
