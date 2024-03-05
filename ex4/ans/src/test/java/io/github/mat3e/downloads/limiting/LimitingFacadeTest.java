package io.github.mat3e.downloads.limiting;

import io.github.mat3e.downloads.limiting.api.AccountId;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Optional;

import static io.github.mat3e.downloads.limiting.AccountSettingAssert.thenAccount;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LimitingFacadeTest {
    @Mock
    private Clock clock;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountSettingRepository accountSettingRepository;
    @Mock
    private MeterRegistry registry;

    @Captor
    private ArgumentCaptor<AccountSetting> accountSettingCaptor;

    @InjectMocks
    private LimitingFacade systemUnderTest;

    @Test
    void newAccount_overrideLimit_createsAccount() {
        // given
        var accountId = AccountId.valueOf("1");
        when(accountSettingRepository.findById(accountId)).thenReturn(Optional.empty());

        // when
        systemUnderTest.overrideAccountLimit(accountId, 1);

        // then
        verify(accountSettingRepository).save(accountSettingCaptor.capture());
        thenAccount(accountSettingCaptor.getValue())
                .hasId(accountId)
                .hasUpperDownloadsLimitOf(1);
    }

    @Test
    void existingAccount_overrideLimit_updatesAccount() {
        // given
        var accountId = AccountId.valueOf("1");
        var existingAccount = new AccountSetting("1", 2, 1);
        when(accountSettingRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));

        // when
        systemUnderTest.overrideAccountLimit(accountId, 1);

        // then
        thenAccount(existingAccount).hasUpperDownloadsLimitOf(1);
        verify(accountSettingRepository).save(existingAccount);
    }
}
