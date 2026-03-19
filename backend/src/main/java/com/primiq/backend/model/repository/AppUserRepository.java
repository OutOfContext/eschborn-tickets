package com.primiq.backend.model.repository;

import com.primiq.backend.model.dao.AppUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

  Optional<AppUser> findByReferenceId(String referenceId);

  Optional<AppUser> findByUsername(String username);
}

