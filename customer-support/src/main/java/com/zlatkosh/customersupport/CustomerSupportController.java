package com.zlatkosh.customersupport;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer-support")
@Slf4j
public class CustomerSupportController {

    private final CustomerSupportService customerSupportService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully completed the check",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request! Request parameter could not be converted to type ZonedDateTime, " +
                    "please use something like '2007-12-03T10:15:30+01:00[Europe/Paris]",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @Operation(description = "This controller is used to check if Customer support is available " +
            "at the time defined in the input zonedDateTime parameter. " +
            "Expected zonedDateTime format is such as '2007-12-03T10:15:30+01:00[Europe/Paris]'")
    @GetMapping("/check-status")
    @ResponseBody
    public CustomerSupportStatus checkStatus(@RequestParam("zonedDateTime") String zonedDateTime) {
        try {
            log.info("Input zonedDateTime: %s".formatted(ZonedDateTime.parse(zonedDateTime)));
            return customerSupportService.checkStatus(ZonedDateTime.parse(zonedDateTime));
        } catch (DateTimeParseException e) {
            log.error("Bad request", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ("Bad request! '%s' could not be converted to " +
                    "ZonedDateTime, please use something like '2007-12-03T10:15:30+01:00[Europe/Paris]'")
                    .formatted(zonedDateTime));
        } catch (Exception e) {
            log.error("Internal Server Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error: %s".formatted(e.getMessage()));
        }
    }
}
