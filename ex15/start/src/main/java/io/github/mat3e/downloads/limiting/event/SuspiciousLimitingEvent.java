package io.github.mat3e.downloads.limiting.event;

import io.github.mat3e.downloads.eventhandling.DomainEvent;
import io.github.mat3e.downloads.limiting.api.AccountId;
import io.github.mat3e.downloads.limiting.api.Asset;

import java.time.Clock;
import java.time.Instant;

public sealed abstract class SuspiciousLimitingEvent implements DomainEvent {
    public static SuspiciousLimitingEvent assetDuplicated(
            Clock occurrenceClock,
            AccountId accountId,
            Asset asset) {
        return new AssetDuplicated(occurrenceClock, accountId, asset);
    }

    public static SuspiciousLimitingEvent assetAlreadyInDifferentCountry(
            Clock occurrenceClock,
            AccountId accountId,
            Asset newAsset,
            String existingAssetCountry) {
        return new AssetAssignedInDifferentCountry(occurrenceClock, accountId, newAsset, existingAssetCountry);
    }

    public static SuspiciousLimitingEvent unnecessaryRemoval(
            Clock occurrenceClock,
            AccountId accountId,
            Asset asset) {
        return new UnassignedRemoved(occurrenceClock, accountId, asset);
    }

    private final Instant occurredOn;
    private final AccountId accountId;

    SuspiciousLimitingEvent(Clock clock, AccountId accountId) {
        this.occurredOn = clock.instant();
        this.accountId = accountId;
    }

    @Override
    public final Instant occurredOn() {
        return occurredOn;
    }

    public final AccountId accountId() {
        return accountId;
    }

    @Override
    public final String toString() {
        return "Account (" + accountId + ") at " + occurredOn() + " " + description().trim();
    }

    abstract String description();

    @Override
    public boolean suspicious() {
        return true;
    }
}

final class AssetAssignedInDifferentCountry extends SuspiciousLimitingEvent {
    private final Asset asset;
    private final String existingAssetCountry;

    AssetAssignedInDifferentCountry(
            Clock occurrenceClock,
            AccountId accountId,
            Asset newAsset,
            String existingAssetCountry) {
        super(occurrenceClock, accountId);
        this.asset = newAsset;
        this.existingAssetCountry = existingAssetCountry;
    }

    @Override
    String description() {
        return "assigned " + asset + " while it was already assigned in " + existingAssetCountry;
    }
}

final class AssetDuplicated extends SuspiciousLimitingEvent {
    private final Asset asset;

    AssetDuplicated(Clock occurrenceClock, AccountId accountId, Asset asset) {
        super(occurrenceClock, accountId);
        this.asset = asset;
    }

    public Asset asset() {
        return asset;
    }

    @Override
    String description() {
        return "assigned already assigned asset: " + asset;
    }
}

final class UnassignedRemoved extends SuspiciousLimitingEvent {
    private final Asset asset;

    UnassignedRemoved(Clock occurrenceClock, AccountId accountId, Asset asset) {
        super(occurrenceClock, accountId);
        this.asset = asset;
    }

    @Override
    String description() {
        return "removed unassigned asset: " + asset;
    }
}
