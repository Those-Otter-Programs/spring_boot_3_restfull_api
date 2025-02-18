package com.thoseop.api.members.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thoseop.api.members.entity.AuthorityEntity;
import com.thoseop.api.members.entity.MemberEntity;
import com.thoseop.api.members.entity.enums.MemberStatus;
import com.thoseop.api.members.http.request.MemberCreateRequest;
import com.thoseop.api.members.http.request.MemberUpdateRequest;
import com.thoseop.api.members.http.response.MemberResponse;
import com.thoseop.api.members.mapper.MemberMapper;
import com.thoseop.api.members.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 
     * @param request
     * @return
     */
    public MemberResponse saveMember(MemberCreateRequest request) {
	log.info("Saving member");

        MemberEntity entity = this.memberMapper.mapRequestToEntity(request);

        String hashPwd = passwordEncoder.encode(request.getMemberPassword());
        entity.setPassword(hashPwd);
        entity.setEnabled(false)
        	.setCreatedAt(new Date())
        	.setUpdatedAt(new Date());

	for (String rawAuthority : request.getMemberAuthorities()) { 
	    entity.addAuthority(new AuthorityEntity().setName(rawAuthority));
	}
	
        MemberEntity savedEntity = memberRepository.save(entity);

	return memberMapper.mapToResponse(savedEntity);
    }
    
    /**
     * 
     * @param request
     * @return
     */
    public MemberResponse modifyMember(MemberUpdateRequest request) {
	log.info("Updating member {}", request.getMemberEmail());

        MemberEntity entity = this.memberMapper.mapRequestToEntity(request);

        MemberEntity savedEntity = memberRepository.save(entity);

        return memberMapper.mapToResponse(savedEntity);
    }

    /**
     * 
     * @param pageable
     * @return
     */
    public Page<MemberResponse> readMembers(Pageable pageable) {
	log.info("Reading all members - paginated");

	Page<MemberEntity> membersList = memberRepository.findAll(pageable);
	Page<MemberResponse> resultList = membersList.map(u -> memberMapper.mapToResponse(u));

	return resultList;
    }
    
    /**
     * 
     * @param email
     * @return
     */
    public MemberResponse readMemberByEmail(String email) {
	log.info("Reading member by email");

	Optional<MemberEntity> result = this.memberRepository.findByEmail(email);
	return (result.isPresent())? 
		this.memberMapper.mapToResponse(result.get())
		: new MemberResponse();
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public MemberResponse changeMemberStatus(Long id, MemberStatus status) {
        log.info("Changing member id[{}] status to {}", id, status.getStatus());

	int res = this.memberRepository.setMemberStatus(id, status.getStatus());

	MemberEntity entity = null;
	if (res == 1) {
	    entity = this.memberRepository.findOneById(id).get();
	}

	return this.memberMapper.mapToResponse(entity);
    }
}
