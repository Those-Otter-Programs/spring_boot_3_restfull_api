package com.thoseop.api.members_logs.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thoseop.api.members_logs.entity.AuthenticationFailureLogEntity;

import jakarta.transaction.Transactional;

@Transactional
public interface AuthenticationFailureLogRepository extends JpaRepository<AuthenticationFailureLogEntity, Long>{

    Optional<AuthenticationFailureLogEntity> findOneById(Long id);
    
    Page<AuthenticationFailureLogEntity> findAllByUsername(String username, Pageable pageable);
}
