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

    @Modifying
    @Query("""
    	UPDATE MemberEntity m
    	SET m.enabled = ?2
    	WHERE m.id = ?1
    		""")
    int setMemberStatus(Long id, boolean enabled);

    @Modifying
    @Query("""
    	UPDATE MemberEntity m
    	SET m.password = ?2
    	WHERE m.email = ?1
    		""")
    int setMemberPassword(String email, String password);
}