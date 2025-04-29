package com.youtube.central.repository;


import com.youtube.central.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, UUID> {

    public AppUser findByEmail(String email);
}
