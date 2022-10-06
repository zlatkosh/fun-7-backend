package com.zlatkosh.repository;

import com.zlatkosh.entities.UserData;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserData, String> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    @NotNull
    Optional<UserData> findById(@NotNull String userId);
}