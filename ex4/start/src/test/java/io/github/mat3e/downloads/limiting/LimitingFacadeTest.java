package io.github.mat3e.downloads.limiting;

import io.github.mat3e.downloads.limiting.api.AccountId;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LimitingFacadeTest {
    private final Clock clock = mock(Clock.class);
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final AccountSettingRepository accountSettingRepository = mock(AccountSettingRepository.class);
    private final MeterRegistry registry = mock(MeterRegistry.class);

    private final LimitingFacade systemUnderTest =
            new LimitingFacade(clock, accountRepository, accountSettingRepository, registry);

    @Test
    void newAccount_overrideLimit_createsAccount() {
        // given
        var accountId = AccountId.valueOf("1");
        when(accountSettingRepository.findById(accountId)).thenReturn(Optional.empty());

        // when
        systemUnderTest.overrideAccountLimit(accountId, 1);

        // then
        var accountSettingCaptor = ArgumentCaptor.forClass(AccountSetting.class);
        verify(accountSettingRepository).save(accountSettingCaptor.capture());
        var account = accountSettingCaptor.getValue();
        assertThat(account.id()).isEqualTo(accountId);
        assertThat(account.limit()).isEqualTo(1);
    }

    @Test
    void existingAccount_overrideLimit_updatesAccount() {
        // given
        var accountId = AccountId.valueOf("1");
        var existingAccount = mock(AccountSetting.class);
        when(accountSettingRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));

        // when
        systemUnderTest.overrideAccountLimit(accountId, 1);

        // then
        verify(existingAccount).overrideLimit(1);
        verify(accountSettingRepository).save(existingAccount);
    }
}
