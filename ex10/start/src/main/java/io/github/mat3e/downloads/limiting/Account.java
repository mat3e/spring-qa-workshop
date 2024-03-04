package io.github.mat3e.downloads.limiting;

import io.github.mat3e.downloads.limiting.api.Asset;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table("downloading_accounts")
class Account {

    @Id
    private final String id;
    @MappedCollection(idColumn = "account", keyColumn = "account_order")
    private final List<DownloadedAsset> assets = new ArrayList<>();
    @Column("limitation")
    private final Integer limit;
    @Version
    private final Integer version;

    Account(String id, List<DownloadedAsset> assets, Integer limit, Integer version) {
        this.id = id;
        this.assets.addAll(assets);
        this.limit = limit;
        this.version = version;
    }

    void assignAsset(Asset asset) {
        assets.add(DownloadedAsset.newFrom(asset));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Table("downloaded_assets")
    private static class DownloadedAsset {
        static DownloadedAsset newFrom(Asset asset) {
            return new DownloadedAsset(null, asset.id(), asset.countryCode(), null);
        }

        DownloadedAsset(Integer id, String assetId, String countryCode, Integer version) {
            this.id = id;
            this.assetId = assetId;
            this.countryCode = countryCode;
            this.version = version;
        }

        @Id
        private final Integer id;
        private final String assetId;
        private final String countryCode;
        @Version
        private final Integer version;
    }
}
