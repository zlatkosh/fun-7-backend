package com.zlatkosh.status;

import com.zlatkosh.dto.CheckServicesRequest;
import com.zlatkosh.dto.CheckServicesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class CheckServicesService {

    private final RetryableServiceCaller retryableServiceCaller;

    private final TransactionalServiceHelper transactionalServiceHelper;

    public CheckServicesResponse checkServices(CheckServicesRequest request) {
        log.debug("Executing checkServices for request '%s'.".formatted(request.toString()));
        return CheckServicesResponse.builder()
                .ads(retryableServiceCaller.callAdsService(request.getCc()))
                .userSupport(retryableServiceCaller.callCustomerSupportService(request.getTimezone()))
                .multiplayer(retryableServiceCaller.callMultiplayerService(transactionalServiceHelper.getPlayCount(request.getUserId()), request.getCc()))
                .build();
    }
}
