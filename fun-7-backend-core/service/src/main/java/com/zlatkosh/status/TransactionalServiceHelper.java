package com.zlatkosh.status;

import com.zlatkosh.entities.UserData;
import com.zlatkosh.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionalServiceHelper {
    private final UserRepository userRepository;

    @Transactional
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