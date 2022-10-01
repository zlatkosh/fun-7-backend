package com.zlatkosh.multiplayer;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(MultiplayerServiceTest.class)
@ContextConfiguration(classes = {MultiplayerService.class})
@TestPropertySource(locations = {"classpath:application-junit.properties"})
@TestConfiguration
@EnableConfigurationProperties
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MultiplayerServiceTest extends MultiplayerServiceConfig {
    @Autowired
    private MultiplayerService multiplayerService;

    private List<String> eligibleCountryCodes;
    private int minimumPlayCount;

    @Nested
    class testing_checkStatus {

        @Test
        public void GIVEN_low_playCount_and_supported_country_WHEN_invoked_THEN_return_multiplayer_disabled() {
            MultiplayerStatus multiplayerStatus = multiplayerService.checkStatus(minimumPlayCount, eligibleCountryCodes.get(0));
            Assertions.assertFalse(multiplayerStatus.multiplayerEnabled());
        }

        @Test
        public void GIVEN_good_playCount_and_unsupported_country_WHEN_invoked_THEN_return_multiplayer_disabled() {
            MultiplayerStatus multiplayerStatus = multiplayerService.checkStatus(minimumPlayCount + 1, "YYY");
            Assertions.assertFalse(multiplayerStatus.multiplayerEnabled());
        }

        @Test
        public void GIVEN_good_playCount_and_supported_country_WHEN_invoked_THEN_return_multiplayer_enabled() {
            MultiplayerStatus multiplayerStatus = multiplayerService.checkStatus(minimumPlayCount + 1, eligibleCountryCodes.get(0));
            Assertions.assertTrue(multiplayerStatus.multiplayerEnabled());
        }
    }

    public void setEligibleCountryCodes(List<String> eligibleCountryCodes) {
        this.eligibleCountryCodes = eligibleCountryCodes;
    }

    public void setMinimumPlayCount(int minimumPlayCount) {
        this.minimumPlayCount = minimumPlayCount;
    }
}