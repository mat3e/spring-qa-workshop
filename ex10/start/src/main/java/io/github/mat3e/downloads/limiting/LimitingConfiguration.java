package io.github.mat3e.downloads.limiting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LimitingConfiguration {
    /* just IO dependencies and other modules */
    private final AccountRepository accountRepository;

    LimitingConfiguration(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Bean
    LimitingFacade limiting() {
        return new LimitingFacade(accountRepository);
    }
}
