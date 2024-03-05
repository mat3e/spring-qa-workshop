package io.github.mat3e.downloads.limiting;

import io.github.mat3e.downloads.exceptionhandling.BusinessException;
import io.github.mat3e.downloads.limiting.api.AccountId;
import io.github.mat3e.downloads.limiting.api.Asset;
import io.github.mat3e.downloads.limiting.event.SuspiciousLimitingEvent;
import io.github.mat3e.downloads.reporting.ReportingFacade;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class LimitingFacade {
    private final Clock clock;
    private final AccountRepository accountRepository;
    private final AccountSettingRepository accountSettingRepository;
    private final ReportingFacade reportingFacade;

    LimitingFacade(
            Clock clock,
            AccountRepository accountRepository,
            AccountSettingRepository accountSettingRepository,
            ReportingFacade reportingFacade) {
        this.clock = clock;
        this.accountRepository = accountRepository;
        this.accountSettingRepository = accountSettingRepository;
        this.reportingFacade = reportingFacade;
    }

    public void overrideAccountLimit(AccountId accountId, int newLimit) {
        accountSettingRepository.findById(accountId).ifPresentOrElse(
                existingAccount -> {
                    existingAccount.overrideLimit(newLimit);
                    accountSettingRepository.save(existingAccount);
                },
                () -> accountSettingRepository.save(AccountSetting.newFor(accountId, newLimit)));
    }

    public void assignDownloadedAsset(AccountId accountId, Asset downloadedAsset) {
        saveFlushingEvents(
                getAccountBy(accountId),
                account -> account.assignAsset(downloadedAsset, clock));
    }

    public void removeDownloadedAsset(AccountId accountId, Asset downloadedAsset) {
        saveFlushingEvents(
                getAccountBy(accountId),
                account -> account.unassignAsset(downloadedAsset, clock));
    }

    private Account getAccountBy(AccountId accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> BusinessException.notFound("Account", accountId.id()));
    }

    private void saveFlushingEvents(Account account, Function<Account, List<SuspiciousLimitingEvent>> accountCommand) {
        accountCommand.apply(account).forEach(reportingFacade::recordEvent);
        accountRepository.save(account);
    }

    public Optional<List<Asset>> findForAccount(AccountId accountId) {
        return accountRepository.findById(accountId).map(Account::assets);
    }

    public static class AccountLimitExceeded extends BusinessException {
        private final int limit;

        AccountLimitExceeded(int limit) {
            super("Allowed limit of " + limit + " downloaded asset" + (limit > 1 ? "" : "s") + " exceeded");
            this.limit = limit;
        }

        public int limit() {
            return limit;
        }
    }
}
