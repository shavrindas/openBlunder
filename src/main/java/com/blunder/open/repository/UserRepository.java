package com.blunder.open.repository;

import com.blunder.open.entity.User;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
	User findByUsername(String username);
	
}
