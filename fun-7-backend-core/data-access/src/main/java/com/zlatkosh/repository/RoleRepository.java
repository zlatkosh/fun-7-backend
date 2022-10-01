package com.zlatkosh.repository;

import com.zlatkosh.entities.Role;
import com.zlatkosh.entities.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleId> {
    List<Role> findRolesByUsername_Id(String username);
}