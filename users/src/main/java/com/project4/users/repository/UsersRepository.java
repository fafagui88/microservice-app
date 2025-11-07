package com.project4.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project4.users.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long> { }