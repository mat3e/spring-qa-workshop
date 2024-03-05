package io.github.mat3e.downloads.reporting;

import io.github.mat3e.downloads.eventhandling.DomainEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;

public interface ReportingFacade {
    void recordEvent(DomainEvent event);
}

class CountingFacade extends DelegatingReportingFacade {
    private final Counter suspiciousEventsCounter;

    CountingFacade(MeterRegistry registry) {
        super(new LoggingReportingFacade());
        suspiciousEventsCounter = registry.counter("downloads.limiting.suspicious_events");
    }

    @Override
    void doRecordEvent(DomainEvent event) {
        if (event.suspicious()) {
            suspiciousEventsCounter.increment();
        }
    }
}

class LoggingReportingFacade extends DelegatingReportingFacade {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoggingReportingFacade.class);

    @Override
    void doRecordEvent(DomainEvent event) {
        if (event.suspicious()) {
            log.warn("Suspicious event recorded: " + event);
            return;
        }
        log.info("Event recorded: " + event);
    }
}

abstract class DelegatingReportingFacade implements ReportingFacade {
    private final ReportingFacade delegate;

    DelegatingReportingFacade() {
        this(null);
    }

    DelegatingReportingFacade(ReportingFacade delegate) {
        this.delegate = delegate;
    }

    @Override
    public final void recordEvent(DomainEvent event) {
        doRecordEvent(event);
        if (delegate != null) {
            delegate.recordEvent(event);
        }
    }

    abstract void doRecordEvent(DomainEvent event);
}
