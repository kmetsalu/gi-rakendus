package com.kaarelkaasla.backend.repositories;


import com.kaarelkaasla.backend.entities.RoleTypes;
import com.kaarelkaasla.backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleTypes name);
}
