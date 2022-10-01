package com.zlatkosh.status;

import com.zlatkosh.dto.CheckServicesRequest;
import com.zlatkosh.dto.CheckServicesResponse;
import com.zlatkosh.entities.UserData;
import com.zlatkosh.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
class CheckServicesService {

    private final RetryableServiceCaller retryableServiceCaller;

    private final UserRepository userRepository;

    public CheckServicesResponse checkServices(CheckServicesRequest request) {
        log.debug("Executing checkServices for request '%s'.".formatted(request.toString()));
        return CheckServicesResponse.builder()
                .ads(retryableServiceCaller.callAdsService(request.getCc()))
                .userSupport(retryableServiceCaller.callCustomerSupportService(request.getTimezone()))
                .multiplayer(retryableServiceCaller.callMultiplayerService(getPlayCount(request.getUserId()), request.getCc()))
                .build();
    }

    public int getPlayCount(String username) {
        Optional<UserData> userDataOptional = userRepository.findById(username);
        if (userDataOptional.isPresent()) {
            UserData userData = userDataOptional.get();
            userData.setPlayCount(userData.getPlayCount() + 1);
            userRepository.save(userData);
            return userData.getPlayCount();
        } else {
            throw new IllegalArgumentException("No user found for username '%s'".formatted(username));
        }
    }
}
