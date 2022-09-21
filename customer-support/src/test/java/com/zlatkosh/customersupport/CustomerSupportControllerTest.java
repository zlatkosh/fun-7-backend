package com.zlatkosh.customersupport;

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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerSupportController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CustomerSupportControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerSupportService service;

    @Test
    public void when_checkStatus_then_return_success_reply() throws Exception{
        when(service.checkStatus())
                .thenReturn(new CustomerSupportStatus(true, "Mock availability message"));

        mvc.perform(get("/customer-support/check-status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userSupportEnabled", is(true)))
                .andExpect(jsonPath("$.availabilityMessage", is("Mock availability message")));
    }

    @Test
    public void when_checkStatus_throws_exception_then_return_internal_server_error_reply() throws Exception{
        when(service.checkStatus())
                .thenThrow(new RuntimeException("Failed to check status"));

        mvc.perform(get("/customer-support/check-status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
