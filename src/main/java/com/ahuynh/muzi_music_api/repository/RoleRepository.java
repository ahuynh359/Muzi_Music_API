package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.role.Role;
import com.ahuynh.muzi_music_api.model.entity.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
