package com.forum.auth.repo;

import com.forum.core.AppRole;
import com.forum.core.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<AppRole, Integer> {
    Optional<AppRole> findByName(Role name);
}


