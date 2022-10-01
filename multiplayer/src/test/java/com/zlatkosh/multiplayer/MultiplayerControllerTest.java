package com.zlatkosh.multiplayer;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MultiplayerController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MultiplayerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MultiplayerService service;

    @Test
    public void GIVEN_any_input_values_WHEN_checkStatus_THEN_return_success_reply() throws Exception {
        when(service.checkStatus(anyInt(), anyString()))
                .thenReturn(new MultiplayerStatus(true, "Mock availability message"));

        mvc.perform(get("/multiplayer/check-status")
                        .param("playCount", "1")
                        .param("countryCode", "Any value")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.multiplayerEnabled", is(true)))
                .andExpect(jsonPath("$.availabilityMessage", is("Mock availability message")));
    }

    @Test
    public void GIVEN_any_input_values_WHEN_checkStatus_throws_exception_THEN_return_internal_server_error_reply() throws Exception {
        when(service.checkStatus(anyInt(), anyString()))
                .thenThrow(new RuntimeException("Failed to check status"));

        mvc.perform(get("/multiplayer/check-status")
                        .param("playCount", "1")
                        .param("countryCode", "Any value")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
