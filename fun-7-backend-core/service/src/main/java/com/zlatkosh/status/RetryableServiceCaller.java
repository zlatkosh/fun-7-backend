package com.zlatkosh.status;

import com.zlatkosh.dto.AdsStatus;
import com.zlatkosh.dto.CustomerSupportStatus;
import com.zlatkosh.dto.MultiplayerStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
class RetryableServiceCaller {
    private final RestTemplate restTemplate;

    @Value("${service.check.adServiceUrl}")
    private String adServiceUrl;

    @Value("${service.check.customerSupportServiceUrl}")
    private String customerSupportServiceUrl;

    @Value("${service.check.multiplayerServiceUrl}")
    private String multiplayerServiceUrl;

    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public String callAdsService(String countryCode) {
        log.debug("Executing callAdsService for countryCode '%s'.".formatted(countryCode));
        AdsStatus adPartnerResponse =
                restTemplate.getForObject(
                        adServiceUrl,
                        AdsStatus.class,
                        countryCode);
        return mapResponseStatus(adPartnerResponse.adsEnabled());
    }

    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public String callCustomerSupportService(String timezone) {
        log.debug("Executing callCustomerSupportService for timezone '%s'.".formatted(timezone));
        CustomerSupportStatus customerSupportPartnerResponse =
                restTemplate.getForObject(
                        customerSupportServiceUrl,
                        CustomerSupportStatus.class,
                        ZonedDateTime.now(ZoneId.of(timezone)).toString());
        return mapResponseStatus(customerSupportPartnerResponse.userSupportEnabled());
    }

    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public String callMultiplayerService(int playCount, String countryCode) {
        log.debug("Executing callMultiplayerService for playCount '%d' and countryCode '%s'.".formatted(playCount, countryCode));
        MultiplayerStatus multiplayerPartnerResponse =
                restTemplate.getForObject(
                        multiplayerServiceUrl,
                        MultiplayerStatus.class,
                        playCount,
                        countryCode);
        return mapResponseStatus(multiplayerPartnerResponse.multiplayerEnabled());
    }

    private String mapResponseStatus(boolean enabled) {
        if (enabled) {
            return "enabled";
        } else {
            return "disabled";
        }
    }
}
