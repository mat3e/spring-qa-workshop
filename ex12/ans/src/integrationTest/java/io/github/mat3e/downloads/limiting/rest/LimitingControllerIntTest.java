package io.github.mat3e.downloads.limiting.rest;

import io.github.mat3e.downloads.exceptionhandling.BusinessException;
import io.github.mat3e.downloads.limiting.LimitingFacade;
import io.github.mat3e.downloads.limiting.api.AccountId;
import io.github.mat3e.downloads.limiting.api.Asset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class LimitingControllerIntTest {
    private static final String ACCOUNT_ID = "1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LimitingFacade facadeNeededByController;

    @Test
    void illegalParams_returnsBadRequest() throws Exception {
        // expect 400
        httpPostAsset("{ \"countryCode\": \"US\" }").andExpect(status().isBadRequest());
        httpPostAsset("{ \"id\": \"123\" }").andExpect(status().isBadRequest());
    }

    @Test
    void nonExisting_returnsNotFound() throws Exception {
        given(facadeNeededByController.findForAccount(AccountId.valueOf(ACCOUNT_ID)))
                .willThrow(BusinessException.notFound("Account", ACCOUNT_ID));
        doThrow(BusinessException.notFound("Account", ACCOUNT_ID))
                .when(facadeNeededByController)
                .assignDownloadedAsset(eq(AccountId.valueOf(ACCOUNT_ID)), any(Asset.class));
        doThrow(BusinessException.notFound("Account", ACCOUNT_ID))
                .when(facadeNeededByController)
                .removeDownloadedAsset(eq(AccountId.valueOf(ACCOUNT_ID)), any(Asset.class));

        // expect 404
        httpGetAssets("lookMaNotExistingId").andExpect(status().isNotFound());
        httpPostAsset("{ \"id\": \"123\", \"countryCode\": \"US\" }").andExpect(status().isNotFound());
        httpDeleteAsset("123", "US").andExpect(status().isNotFound());
    }

    @Test
    void overLimit_returnsUnprocessableEntity() throws Exception {
        // given
        doThrow(LimitingFacade.AccountLimitExceeded.class)
                .when(facadeNeededByController)
                .assignDownloadedAsset(eq(AccountId.valueOf(ACCOUNT_ID)), any(Asset.class));

        // expect 422
        httpPostAsset("{ \"id\": \"123\", \"countryCode\": \"US\" }").andExpect(status().isUnprocessableEntity());
    }

    private ResultActions httpDeleteAsset(String assetId, String countryCode) throws Exception {
        return mockMvc.perform(delete("/api/accounts/{id}/assets/{assetId}", ACCOUNT_ID, assetId)
                .queryParam("countryCode", countryCode));
    }

    private ResultActions httpGetAssets(String accountId) throws Exception {
        return mockMvc.perform(get("/api/accounts/{id}/assets", accountId).contentType(APPLICATION_JSON));
    }

    private ResultActions httpPostAsset(String jsonLines) throws Exception {
        return httpPostAssetForAccount(ACCOUNT_ID, jsonLines);
    }

    private ResultActions httpPostAssetForAccount(String accountId, String jsonLines) throws Exception {
        return mockMvc.perform(post("/api/accounts/{id}/assets", accountId)
                .contentType(APPLICATION_JSON)
                .content(String.join("\n", jsonLines)));
    }
}
