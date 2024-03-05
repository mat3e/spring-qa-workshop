package io.github.mat3e.downloads.limiting.api;

import jakarta.validation.constraints.NotBlank;

public record AccountId(@NotBlank String id) {
    public static AccountId valueOf(String id) {
        return new AccountId(id);
    }
}
