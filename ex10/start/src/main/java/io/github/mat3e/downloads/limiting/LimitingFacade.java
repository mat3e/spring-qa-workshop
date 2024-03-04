package io.github.mat3e.downloads.limiting;

import io.github.mat3e.downloads.limiting.api.AccountId;
import io.github.mat3e.downloads.limiting.api.Asset;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

public class LimitingFacade {
    private static final Logger log = getLogger(LimitingFacade.class);

    private final AccountRepository accountRepository;

    LimitingFacade(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void externalAssignDownloadedAssetTwice(String system, AccountId accountId, Asset downloadedAsset) {
        log.info(
                "External assigning asset call from system {} (account {}, asset {})",
                system,
                accountId,
                downloadedAsset);
        assignDownloadedAssetTwice(accountId, downloadedAsset);
    }

    @Transactional
    public void assignDownloadedAssetTwice(AccountId accountId, Asset downloadedAsset) {
        accountRepository.findById(accountId).ifPresent(account -> {
            account.assignAsset(downloadedAsset);
            accountRepository.save(account);
            account.assignAsset(downloadedAsset);
            accountRepository.save(account);
        });
    }
}
