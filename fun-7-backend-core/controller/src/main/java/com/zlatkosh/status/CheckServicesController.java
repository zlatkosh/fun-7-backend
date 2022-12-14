package com.zlatkosh.status;

import com.zlatkosh.dto.CheckServicesRequest;
import com.zlatkosh.dto.CheckServicesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/check-services")
@Slf4j
public class CheckServicesController {
    private final CheckServicesService checkServicesService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned the status of the other three microservices.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request!",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized request!",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @Operation(description = "This controller is used to return the status of the other three microservices (ads, multiplayer and customer-support). ")
    @GetMapping("/get-status")
    @ResponseBody
    public CheckServicesResponse checkServices(CheckServicesRequest checkServicesRequest) {
        try {
            return checkServicesService.checkServices(checkServicesRequest);
        } catch (IllegalArgumentException e) {
            log.error("Bad request", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request: %s".formatted(e.getMessage()));
        } catch (Exception e) {
            log.error("Internal Server Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error: %s".formatted(e.getMessage()));
        }
    }
}
