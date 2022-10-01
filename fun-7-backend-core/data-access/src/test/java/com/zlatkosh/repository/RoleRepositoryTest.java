package com.zlatkosh.repository;

import com.zlatkosh.config.PostgresContainerConfig;
import com.zlatkosh.entities.Role;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles(PostgresContainerConfig.PROFILE_JUNIT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RoleRepositoryTest extends PostgresTestcontainerConfigurer {

    @Autowired
    RoleRepository roleRepository;

    @Nested
    class testing_findRolesByUsernameId {
        @Test
        void GIVEN_UsernameId_NonExistingUser_WHEN_findRolesByUsernameId_THEN_return_empty_ArrayList() {
            Assertions.assertEquals(new ArrayList<Role>(), roleRepository.findRolesByUsername_Id("non_existing_user"));
        }

        @Test
        void GIVEN_UsernameId_AdminUser_WHEN_findRolesByUsernameId_THEN_return_ADMIN_role() {
            List<Role> roles = roleRepository.findRolesByUsername_Id("admin_user");
            Assertions.assertEquals(1, roles.size());
            Assertions.assertEquals("ADMIN", roles.get(0).getId().getRoleName());
        }
    }
}