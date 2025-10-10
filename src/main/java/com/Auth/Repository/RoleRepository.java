package com.Auth.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Auth.Model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
