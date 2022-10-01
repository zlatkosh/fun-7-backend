package com.zlatkosh.ads;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdsService {
    private final RestTemplate restTemplate;

    @Value("${service.ads.availabilityMessage}")
    private String availabilityMessage;

    @Value("${service.ads.adPartnerUrl}")
    private String adPartnerUrl;

    @Value("${service.ads.adPartnerUsername}")
    private String adPartnerUsername;

    @Value("${service.ads.adPartnerPassword}")
    private String adPartnerPassword;

    public AdsStatus checkStatus(String countryCode) {
        AdPartnerResponse adPartnerResponse =
                restTemplate.exchange(
                        adPartnerUrl,
                        HttpMethod.GET,
                        new HttpEntity<>(createHeaders()),
                        AdPartnerResponse.class,
                        countryCode)
                .getBody();
        return new AdsStatus(adPartnerResponse.ads().equals("sure, why not!"), availabilityMessage);
    }

    private HttpHeaders createHeaders(){
        return new HttpHeaders() {{
            String auth = adPartnerUsername + ":" + adPartnerPassword;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
