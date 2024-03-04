package io.github.mat3e.downloads.limiting;

import io.github.mat3e.downloads.reporting.ReportingFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class LimitingConfiguration {
    /* just IO dependencies and other modules */
    private final Clock clock;
    private final AccountRepository accountRepository;
    private final AccountSettingRepository accountSettingRepository;
    private final ReportingFacade reportingFacade;

    LimitingConfiguration(
            Clock clock,
            AccountRepository accountRepository,
            AccountSettingRepository accountSettingRepository,
            ReportingFacade reportingFacade) {
        this.clock = clock;
        this.accountRepository = accountRepository;
        this.accountSettingRepository = accountSettingRepository;
        this.reportingFacade = reportingFacade;
    }

    @Bean
    LimitingFacade limiting() {
        return new LimitingFacade(clock, accountRepository, accountSettingRepository, reportingFacade);
    }
}
