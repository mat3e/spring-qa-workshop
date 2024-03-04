package io.github.mat3e.downloads.limiting;

import io.github.mat3e.downloads.limiting.api.AccountId;
import io.github.mat3e.downloads.limiting.api.Asset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
class LimitingFacadeIntTest {
    @Autowired
    private LimitingFacade facade;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "downloaded_assets");
    }

    @Nested
    class UniqueAssetViolation {
        @Test
        void skipsSaveInOrdinaryFlow() {
            // when
            catchException(() -> facade.assignDownloadedAssetTwice(
                    AccountId.valueOf("sample-account"),
                    Asset.withId("123").inCountry("US")));

            then(JdbcTestUtils.countRowsInTable(jdbcTemplate, "downloaded_assets")).isZero();
        }

        @Test
        void skipsSaveInExternalSystemFlow() {
            // when
            catchException(() -> facade.externalAssignDownloadedAssetTwice(
                    "system",
                    AccountId.valueOf("sample-account"),
                    Asset.withId("123").inCountry("US")));

            then(JdbcTestUtils.countRowsInTable(jdbcTemplate, "downloaded_assets")).isZero();
        }
    }
}
