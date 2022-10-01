package com.zlatkosh.ads;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ads")
@Slf4j
public class AdsController {

    private final AdsService adsService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully completed the check",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Required request parameter 'countryCode' for method parameter type String is not present.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @Operation(description = "This controller is used to check if Multiplayer is available.")
    @GetMapping("/check-status")
    @ResponseBody
    public AdsStatus checkStatus(@RequestParam("countryCode") String countryCode) {
        try {
            return adsService.checkStatus(countryCode);
        } catch (Exception e) {
            log.error("Internal Server Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error: %s".formatted(e.getMessage()));
        }
    }
}
