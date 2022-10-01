package com.zlatkosh.user;

import com.zlatkosh.repository.RoleRepository;
import com.zlatkosh.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public List<String> getUserRoles(String username) {
        return roleRepository.findRolesByUsername_Id(username)
                .stream()
                .map(role -> role.getId().getRoleName())
                .collect(Collectors.toList());
    }
}
