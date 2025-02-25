package com.thoseop.api.members.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.thoseop.api.members.entity.MemberEntity;

import jakarta.transaction.Transactional;

@Transactional
public interface MemberRepository extends CrudRepository<MemberEntity, Long> {

    Optional<MemberEntity> findOneById(Long id);
    Optional<MemberEntity> findByEmail(String email);

    Page<MemberEntity> findAll(Pageable pageable);

    /* for internal system's calls - possible future implementation of mechanism to check
     * if user has not authenticated for a certain period of time. Not intended to 
     * be used along with http controllers */
    @Modifying
    @Query("""
    	UPDATE MemberEntity m
    	SET m.accountNotExpired = ?2
    	WHERE m.id = ?1
    		""")
    int setMemberAccountExpiredStatus(Long id, boolean accountExpired);

    /* for internal system's calls - possible future implementation, verifying each
     * time a user authenticate, if it hasn't changed its password for a certain 
     * amount of time - not intended to be used along with controllers.
     */
    @Modifying
    @Query("""
    	UPDATE MemberEntity m
    	SET m.credentialsNotExpired = ?2
    	WHERE m.id = ?1
    		""")
    int setMemberCredentialsExpiredStatus(Long id, boolean credentialsExpired);

    @Modifying
    @Query("""
    	UPDATE MemberEntity m
    	SET m.accountNotLocked = ?2
    	WHERE m.id = ?1
    		""")
    int setMemberAccountLockedStatus(Long id, boolean accountLocked);

    @Modifying
    @Query("""
    	UPDATE MemberEntity m
    	SET m.enabled = ?2
    	WHERE m.id = ?1
    		""")
    int setMemberEnabledStatus(Long id, boolean enabled);

    @Modifying
    @Query("""
    	UPDATE MemberEntity m
    	SET m.password = ?2
    	WHERE m.email = ?1
    		""")
    int setMemberPassword(String email, String password);
}
