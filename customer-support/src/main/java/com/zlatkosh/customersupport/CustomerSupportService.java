package com.zlatkosh.customersupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerSupportService {
    private static final String TIME_ZONE_EUROPE_LJUBLJANA = "Europe/Ljubljana";
    @Value("${check-status.availability.messagePattern}")
    private String availabilityMessagePattern;

    @Value("#{T(java.time.LocalTime).parse('${check-status.availability.startTime}')}")
    private LocalTime availabilityStartTime;

    @Value("#{T(java.time.LocalTime).parse('${check-status.availability.endTime}')}")
    private LocalTime availabilityEndTime;

    Set<DayOfWeek> WEEKEND_DAYS = Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    public CustomerSupportStatus checkStatus(ZonedDateTime zonedDateTime) {
        LocalDateTime localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of(TIME_ZONE_EUROPE_LJUBLJANA)).toLocalDateTime();
        return new CustomerSupportStatus(checkIfEnabled(localDateTime), constructAvailabilityMessage());
    }

     private String constructAvailabilityMessage() {

        return availabilityMessagePattern
                .formatted(availabilityStartTime, availabilityEndTime);
    }

    private boolean checkIfEnabled(LocalDateTime currentDateTime) {
        LocalTime currentTime = currentDateTime.toLocalTime();
        return !WEEKEND_DAYS.contains(currentDateTime.getDayOfWeek()) && currentTime.isAfter(availabilityStartTime) && currentTime.isBefore(availabilityEndTime);
    }
}
