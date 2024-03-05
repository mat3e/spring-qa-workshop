package io.github.mat3e.downloads.reporting;

import io.github.mat3e.downloads.eventhandling.DomainEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public interface ReportingFacade {
    void recordEvent(DomainEvent event);
}

class CountingFacade extends DelegatingReportingFacade {
    private final Counter suspiciousEventsCounter;

    CountingFacade(MeterRegistry registry) {
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
    private static final Logger log = getLogger(LoggingReportingFacade.class);

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
    private static final Logger log = getLogger(DelegatingReportingFacade.class);
    private ReportingFacade delegate;

    void append(ReportingFacade next) {
        if (delegate == null) {
            delegate = next;
            return;
        }
        if (delegate instanceof DelegatingReportingFacade delegatingFacade) {
            delegatingFacade.append(next);
            return;
        }
        log.error(
                "Wrong configuration - attempting to further append reporting facade while non-delegating one already appended");
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
