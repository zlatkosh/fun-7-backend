package com.zlatkosh.admin;

import com.zlatkosh.dto.RoleDetails;
import com.zlatkosh.dto.UserDetails;
import com.zlatkosh.entities.Role;
import com.zlatkosh.entities.UserData;
import com.zlatkosh.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class AdminService {
    private final UserRepository userRepository;

    private final Function<Role, RoleDetails> roleToRoleDetailsMapper = role ->
            RoleDetails.builder()
                    .roleName(role.getId().getRoleName())
                    .roleDescription(role.getDescription())
                    .build();

    private final Function<UserData, UserDetails> userDataToUserDetailsMapper = userData ->
            UserDetails.builder()
                    .username(userData.getId())
                    .countryCode(userData.getCountry().getTwoLetterCode())
                    .roles(userData.getRoles().stream()
                            .map(roleToRoleDetailsMapper)
                            .collect(Collectors.toList()))
                    .timeZone(userData.getTimeZone())
                    .build();

    List<UserDetails> listAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .stream()
                .map(userDataToUserDetailsMapper)
                .collect(Collectors.toList());
    }

    UserDetails getUserDetails(String username) {
        Optional<UserData> optionalUserData = userRepository.findById(username);
        if (optionalUserData.isPresent()) {
            return optionalUserData.map(userDataToUserDetailsMapper).get();
        } else {
            throw new IllegalArgumentException("No user found for username '%s'".formatted(username));
        }
    }

    void deleteUser(String username) {
        userRepository.deleteById(username);
    }
}
