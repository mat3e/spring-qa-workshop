package io.github.mat3e.downloads.limiting;

import io.github.mat3e.downloads.limiting.api.AccountId;
import org.assertj.core.api.AbstractObjectAssert;

class AccountSettingAssert extends AbstractObjectAssert<AccountSettingAssert, AccountSetting> {
    static AccountSettingAssert thenAccount(AccountSetting accountSetting) {
        return new AccountSettingAssert(accountSetting);
    }

    private AccountSettingAssert(AccountSetting accountSetting) {
        super(accountSetting, AccountSettingAssert.class);
    }

    public AccountSettingAssert hasUpperDownloadsLimitOf(int value) {
        extracting(AccountSetting::limit).isEqualTo(value);
        return this;
    }

    public AccountSettingAssert hasId(AccountId value) {
        extracting(AccountSetting::id).isEqualTo(value);
        return this;
    }
}
