package io.github.mat3e.downloads.limiting;

import io.github.mat3e.downloads.limiting.api.AccountId;
import org.springframework.data.repository.Repository;

import java.util.Optional;

interface AccountRepository extends Repository<Account, String> {
    Account save(Account account);

    default Optional<Account> findById(AccountId id) {
        return findById(id.id());
    }

    Optional<Account> findById(String id);
}

interface AccountSettingRepository extends Repository<AccountSetting, String> {
    AccountSetting save(AccountSetting account);

    default Optional<AccountSetting> findById(AccountId id) {
        return findById(id.id());
    }

    Optional<AccountSetting> findById(String id);
}
