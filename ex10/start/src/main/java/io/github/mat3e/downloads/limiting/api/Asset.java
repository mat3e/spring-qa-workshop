package io.github.mat3e.downloads.limiting.api;

import jakarta.validation.constraints.NotBlank;

public record Asset(@NotBlank String id, @NotBlank String countryCode) {
    public static AssetBuilder withId(String id) {
        return new AssetBuilder(id);
    }

    public static class AssetBuilder {
        private final String id;

        private AssetBuilder(String id) {
            this.id = id;
        }

        public Asset inCountry(String countryCode) {
            return new Asset(id, countryCode);
        }
    }
}
