package io.github.mat3e.downloads.limiting;

import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> db = new HashMap<>();

    @Override
    public Account save(Account account) {
        return db.put(account.id().id(), account);
    }

    @Override
    public Optional<Account> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }
}

class InMemoryAccountSettingRepository implements AccountSettingRepository {
    private final Clock clock;
    private final InMemoryAccountRepository accountRepository;
    private final Map<String, AccountSetting> db = new HashMap<>();

    InMemoryAccountSettingRepository(Clock clock, InMemoryAccountRepository accountRepository) {
        this.clock = clock;
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountSetting save(AccountSetting account) {
        var accountOverride = new Account(account.id().id(), new ArrayList<>(), account.limit(), null);
        accountRepository.findById(account.id()).ifPresent(existingAccount -> existingAccount.assets()
                .forEach(asset -> accountOverride.assignAsset(asset, clock)));
        accountRepository.save(accountOverride);
        return db.put(account.id().id(), account);
    }

    @Override
    public Optional<AccountSetting> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }
}
