package com.thoseop.api.members_logs.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thoseop.api.members_logs.entity.AuthenticationFailureLogEntity;
import com.thoseop.api.members_logs.entity.dto.MemberDetailsAuthenticationDTO;

import jakarta.transaction.Transactional;

@Transactional
public interface AuthenticationFailureLogRepository extends JpaRepository<AuthenticationFailureLogEntity, Long>{

    Optional<AuthenticationFailureLogEntity> findOneById(Long id);
    
    Page<AuthenticationFailureLogEntity> findAllByUsername(String username, Pageable pageable);
    
    @Query(value = """
		    SELECT new com.thoseop.api.members_logs.entity.dto.MemberDetailsAuthenticationDTO(
		    	me.id,
		    	me.email,
		    	me.accountNotExpired,
		    	me.credentialsNotExpired,
		    	me.accountNotLocked,
		    	me.enabled,
		    	afl.id,
		    	afl.eventResult,
		    	afl.remoteIpAddress,
		    	afl.message,
		    	afl.authTime) 
		    FROM MemberEntity me
		    LEFT JOIN AuthenticationFailureLogEntity afl ON afl.username = me.email
		    WHERE afl.username = ?1 
		    """) // FUNCIONA
    Page<MemberDetailsAuthenticationDTO> findMemberAuthLogs(String username, Pageable pageable);
}
