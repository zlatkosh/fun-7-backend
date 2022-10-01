package com.zlatkosh.multiplayer;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Setter
public class MultiplayerService extends MultiplayerServiceConfig {
    private List<String> eligibleCountryCodes;

    private int minimumPlayCount;

    private String availabilityMessagePattern;

    public MultiplayerStatus checkStatus(int playCount, String countryCode) {
        boolean eligible = playCount > minimumPlayCount && eligibleCountryCodes.contains(countryCode);
        return new MultiplayerStatus(eligible, constructAvailabilityMessage());
    }

    private String constructAvailabilityMessage() {
        return availabilityMessagePattern
                .formatted(minimumPlayCount, eligibleCountryCodes);
    }
}
