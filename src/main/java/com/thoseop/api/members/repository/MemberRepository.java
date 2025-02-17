package com.thoseop.api.members.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.thoseop.api.members.entity.MemberEntity;

public interface MemberRepository extends CrudRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByEmail(String email);
    
    Page<MemberEntity> findAll(Pageable pageable);
}