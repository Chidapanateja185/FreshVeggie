package com.Auth.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Auth.Model.EndUser;

public interface EndUserRepository extends JpaRepository<EndUser, Integer> {

	Optional<EndUser> findByEmail(String email);
}
