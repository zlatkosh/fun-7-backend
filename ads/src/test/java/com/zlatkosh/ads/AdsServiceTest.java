package com.zlatkosh.ads;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@SpringJUnitConfig(AdsServiceTest.class)
@ContextConfiguration(classes = {AdsService.class, RestTemplate.class})
@TestPropertySource(locations = {"classpath:application-junit.properties"})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AdsServiceTest {
    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private AdsService adsService;

    @Nested
    class testing_checkStatus {

        @Test
        public void GIVEN_any_string_WHEN_partner_returns_why_not_THEN_return_ads_enabled() {
            when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(AdPartnerResponse.class), anyString()))
                    .thenReturn(ResponseEntity.ok().body(new AdPartnerResponse("sure, why not!")));

            AdsStatus adsStatus = adsService.checkStatus("US");
            Assertions.assertTrue(adsStatus.adsEnabled());
        }

        @Test
        public void GIVEN_any_string_WHEN_partner_returns_you_shall_not_pass_THEN_return_ads_disabled() {
            when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(AdPartnerResponse.class), anyString()))
                    .thenReturn(ResponseEntity.ok().body(new AdPartnerResponse("you shall not pass!")));

            AdsStatus adsStatus = adsService.checkStatus("random_string");
            Assertions.assertFalse(adsStatus.adsEnabled());
        }

        @Test
        public void GIVEN_any_string_WHEN_partner_throws_internal_server_error_THEN_rethrow() {
            HttpServerErrorException error = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Request handler committed seppuku!");
            when(restTemplate.exchange(anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(AdPartnerResponse.class),
                    anyString())
            ).thenThrow(error);

            Assertions.assertThrows(HttpServerErrorException.class, () -> adsService.checkStatus("random_string"));
        }
    }
}