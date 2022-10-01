package com.zlatkosh.customersupport;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.ZonedDateTime;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(CustomerSupportServiceTest.class)
@ContextConfiguration(classes = {CustomerSupportService.class})
@TestPropertySource(locations = {"classpath:application-junit.properties"})
@TestConfiguration
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CustomerSupportServiceTest {
    @Autowired
    private CustomerSupportService customerSupportService;

    @Nested
    class checkStatus_tests {
        @Nested
        class IsWorkday {
            @Test
            public void is_valid_time_should_be_enabled() {
                boolean isEnabled = customerSupportService.checkStatus(ZonedDateTime.parse("2022-09-21T12:30:00+02:00[Europe/Ljubljana]")).userSupportEnabled();
                Assertions.assertTrue(isEnabled);
            }

            @Test
            public void is_valid_London_time_should_be_enabled() {
                boolean isEnabled = customerSupportService.checkStatus(ZonedDateTime.parse("2022-09-21T08:59:00+01:00")).userSupportEnabled();
                Assertions.assertTrue(isEnabled);
            }

            @Test
            public void is_before_valid_time_should_be_disabled() {
                boolean isEnabled = customerSupportService.checkStatus(ZonedDateTime.parse("2022-09-21T08:59:00+02:00[Europe/Ljubljana]")).userSupportEnabled();
                Assertions.assertFalse(isEnabled);
            }

            @Test
            public void is_after_valid_time_should_be_disabled() {
                boolean isEnabled = customerSupportService.checkStatus(ZonedDateTime.parse("2022-09-21T15:01:00+02:00[Europe/Ljubljana]")).userSupportEnabled();
                Assertions.assertFalse(isEnabled);
            }
        }

        @Nested
        class IsWeekend {
            @Test
            public void is_valid_time_on_Saturday_should_be_disabled() {
                boolean isEnabled = customerSupportService.checkStatus(ZonedDateTime.parse("2022-09-24T12:30:00+02:00[Europe/Ljubljana]")).userSupportEnabled();
                Assertions.assertFalse(isEnabled);
            }

            @Test
            public void is_valid_time_on_Sunday_should_be_disabled() {
                boolean isEnabled = customerSupportService.checkStatus(ZonedDateTime.parse("2022-09-25T12:30:00+02:00[Europe/Ljubljana]")).userSupportEnabled();
                Assertions.assertFalse(isEnabled);
            }

            @Test
            public void is_before_valid_time_on_Saturday_should_be_disabled() {
                boolean isEnabled = customerSupportService.checkStatus(ZonedDateTime.parse("2022-09-24T08:59:00+02:00[Europe/Ljubljana]")).userSupportEnabled();
                Assertions.assertFalse(isEnabled);
            }

            @Test
            public void is_after_valid_time_on_Sunday_should_be_disabled() {
                boolean isEnabled = customerSupportService.checkStatus(ZonedDateTime.parse("2022-09-25T15:01:00+02:00[Europe/Ljubljana]")).userSupportEnabled();
                Assertions.assertFalse(isEnabled);
            }
        }
    }
}
