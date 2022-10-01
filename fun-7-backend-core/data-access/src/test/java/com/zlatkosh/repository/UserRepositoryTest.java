package com.zlatkosh.repository;

import com.zlatkosh.config.PostgresContainerConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles(PostgresContainerConfig.PROFILE_JUNIT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserRepositoryTest extends PostgresTestcontainerConfigurer {
    @Autowired
    private UserRepository userRepository;

    @Nested
    class testing_existsById {
        @Test
        void GIVEN_Username_NonExistingUser_WHEN_existsById_THEN_return_false() {
            Assertions.assertFalse(userRepository.existsById("non_existing_user"));
        }

        @Test
        void GIVEN_Username_AdminUser_WHEN_existsById_THEN_return_true() {
            Assertions.assertTrue(userRepository.existsById("admin_user"));
        }
    }
}