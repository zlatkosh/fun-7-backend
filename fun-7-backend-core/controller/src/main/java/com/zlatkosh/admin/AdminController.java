package com.zlatkosh.admin;

import com.zlatkosh.dto.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a list of all users.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized request!",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @Operation(description = "This controller is used to return a Pageable list of details for all users. ")
    @GetMapping("/list-all-users")
    @ResponseBody
    public List<UserDetails> listAllUsers (Pageable pageable) {
        try {
            return adminService.listAllUsers(pageable);
        } catch (Exception e) {
            log.error("Internal Server Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error: %s".formatted(e.getMessage()));
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user's details.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, user not found!",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized request!",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @Operation(description = "This controller is used to return user details for a given username. ")
    @GetMapping("/get-user-details")
    @ResponseBody
    public UserDetails getUserDetails (String username) {
        try {
            return adminService.getUserDetails(username);
        } catch (IllegalArgumentException e) {
            log.error("Bad request", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request: %s".formatted(e.getMessage()));
        } catch (Exception e) {
            log.error("Internal Server Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error: %s".formatted(e.getMessage()));
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the user.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, user not found!",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized request!",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @Operation(description = "This controller is used delete a user for a given username. ")
    @DeleteMapping("/delete-user")
    public void deleteUser (String username) {
        try {
            adminService.deleteUser(username);
        } catch (EmptyResultDataAccessException e) {
            log.error("Bad request", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request: A user with username '%s' does not exist!".formatted(username));
        } catch (Exception e) {
            log.error("Internal Server Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error: %s".formatted(e.getMessage()));
        }
    }
}
