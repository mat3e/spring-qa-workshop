package io.github.mat3e.downloads.eventhandling;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();

    default boolean suspicious() {
        return false;
    }
}
