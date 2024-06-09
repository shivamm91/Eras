package com.fabhotel.Eras.repository;

import java.util.Optional;

import com.fabhotel.Eras.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

	

	

}
