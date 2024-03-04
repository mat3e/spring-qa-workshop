package io.github.mat3e.downloads.limiting;

import io.github.mat3e.downloads.exceptionhandling.BusinessException;
import io.github.mat3e.downloads.limiting.api.AccountId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("downloading_accounts")
class AccountSetting {
    static AccountSetting newFor(AccountId accountId, int limit) {
        assertAllowedLimit(limit);
        return new AccountSetting(accountId.id(), limit, null);
    }

    @Id
    private final String id;
    @Column("limitation")
    private Integer limit;
    @Version
    private final Integer version;

    AccountSetting(String id, Integer limit, Integer version) {
        this.id = id;
        this.limit = limit;
        this.version = version;
    }

    AccountId id() {
        return AccountId.valueOf(id);
    }

    int limit() {
        return limit;
    }

    void overrideLimit(int newLimit) {
        assertAllowedLimit(newLimit);
        this.limit = newLimit;
    }

    private static void assertAllowedLimit(int limit) {
        if (limit < 0) {
            throw new BusinessException("Limit must be non-negative");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AccountSetting that = (AccountSetting) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
