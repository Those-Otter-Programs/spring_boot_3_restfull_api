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
import com.thoseop.api.members.http.request.MemberManagePasswordRequest;
import com.thoseop.api.members.http.request.MemberUpdatePasswordRequest;
import com.thoseop.api.members.http.request.MemberUpdateRequest;
import com.thoseop.api.members.http.response.MemberCreatedResponse;
import com.thoseop.api.members.http.response.MemberDetailsResponse;
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
    public MemberCreatedResponse saveMember(MemberCreateRequest request) {
	log.info("Saving member");

        MemberEntity entity = this.memberMapper.mapRequestToEntity(request);

        String hashPwd = this.passwordEncoder.encode(request.getMemberPassword());
        entity.setPassword(hashPwd);
        entity.setAccountNotExpired(true)
        	.setAccountNotLocked(true)
        	.setCredentialsNotExpired(true)
        	.setEnabled(false)
        	.setCreatedAt(new Date())
        	.setUpdatedAt(new Date());

	for (String rawAuthority : request.getMemberAuthorities()) 
	    entity.addAuthority(new AuthorityEntity().setName(rawAuthority));
	
        MemberEntity savedEntity = memberRepository.save(entity);

        return memberMapper.mapToCreatedResponse(savedEntity)
        	.setMessage("Member disabled by default. Account needs confirmation to get activated.");
    }
    
    /**
     * 
     * @param request
     * @return
     */
    public MemberResponse modifyMember(MemberUpdateRequest request) {
	log.info("Updating member {}", request.getMemberEmail());

        MemberEntity entity = this.memberMapper.mapRequestToEntity(request);

        for (String rawAuthority : request.getMemberAuthorities()) 
	    entity.addAuthority(new AuthorityEntity().setName(rawAuthority));

        MemberEntity updatedEntity = memberRepository.save(entity);

        return memberMapper.mapToResponse(updatedEntity)
        	.setMessage("Member updated");
    }

    /**
     * 
     * @param pageable
     * @return
     */
    public Page<MemberResponse> readMembers(Pageable pageable) {
	log.info("Reading all members - paginated");

	Page<MemberEntity> membersList = memberRepository.findAll(pageable);
	Page<MemberResponse> resultList = membersList.map(u -> 
	    memberMapper.mapToResponse(u).setMessage("Those Otter Programs by James Mallon")
	);

	return resultList;
    }
    
    /**
     * 
     * @param email
     * @return
     */
    public MemberDetailsResponse readMemberDetailsByEmail(String email) {
	log.info("Reading member by email");

	Optional<MemberEntity> result = this.memberRepository.findByEmail(email);

	return this.memberMapper.mapToDetailsResponse(result.get());
    }
    
    /**
     * 
     * @param email
     * @return
     */
    public MemberResponse readMemberByEmail(String email) {
	log.info("Reading member by email");

	Optional<MemberEntity> result = this.memberRepository.findByEmail(email);

	return this.memberMapper.mapToResponse(result.get());
    }
    
    /**
     * 
     * @param id
     * @param request
     * @return
     */
    public MemberResponse changeMemberPassword(String username, MemberUpdatePasswordRequest request) {
	log.info("Updating member {} password", username);

        String hashPwd = this.passwordEncoder.encode(request.getNewPassword());
	int res = this.memberRepository.setMemberPassword(username, hashPwd);

	MemberEntity entity = null;
	if (res == 1) {
	    entity = this.memberRepository.findByEmail(username).get();
	}
	
	return this.memberMapper.mapToResponse(entity)
		.setMessage("Password updated");
    }
    
    /**
     * 
     * @param request
     * @return
     */
    public MemberResponse manageMemberPassword(MemberManagePasswordRequest request) {
	log.info("Managing member {} password", request.getMemberUsername());

        String hashPwd = this.passwordEncoder.encode(request.getMemberNewPassword());
	int res = this.memberRepository.setMemberPassword(request.getMemberUsername(), hashPwd);

	MemberEntity entity = null;
	if (res == 1) {
	    entity = this.memberRepository.findByEmail(request.getMemberUsername()).get();
	}
	
	return this.memberMapper.mapToResponse(entity)
		.setMessage("Password updated");
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public MemberResponse changeMemberAccountExpiredStatus(Long id, MemberStatus status) {
        log.info("Changing member id[{}] status to {}", id, status.getStatus());

	int res = this.memberRepository.setMemberAccountExpiredStatus(id, status.getStatus());

	MemberEntity entity = null;
	if (res == 1) {
	    entity = this.memberRepository.findOneById(id).get();
	}

	return (status == MemberStatus.ENABLE) ?
		this.memberMapper.mapToResponse(entity).setMessage("User's account not expired") :
		this.memberMapper.mapToResponse(entity).setMessage("User's account expired");
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public MemberResponse changeMemberCredentialsExpiredStatus(Long id, MemberStatus status) {
        log.info("Changing member id[{}] status to {}", id, status.getStatus());

	int res = this.memberRepository.setMemberCredentialsExpiredStatus(id, status.getStatus());

	MemberEntity entity = null;
	if (res == 1) {
	    entity = this.memberRepository.findOneById(id).get();
	}

	return (status == MemberStatus.ENABLE) ?
		this.memberMapper.mapToResponse(entity).setMessage("User's credentials not expired") :
		this.memberMapper.mapToResponse(entity).setMessage("User's credentials expired");
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public MemberResponse changeMemberAccountLockedStatus(Long id, MemberStatus status) {
        log.info("Changing member id[{}] status to {}", id, status.getStatus());

	int res = this.memberRepository.setMemberAccountLockedStatus(id, status.getStatus());

	MemberEntity entity = null;
	if (res == 1) {
	    entity = this.memberRepository.findOneById(id).get();
	}

	return (status == MemberStatus.ENABLE) ?
		this.memberMapper.mapToResponse(entity).setMessage("User's account was unlocked") :
		this.memberMapper.mapToResponse(entity).setMessage("User's account was locked");
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public MemberResponse changeMemberEnabledStatus(Long id, MemberStatus status) {
        log.info("Changing member id[{}] status to {}", id, status.getStatus());

	int res = this.memberRepository.setMemberEnabledStatus(id, status.getStatus());

	MemberEntity entity = null;
	if (res == 1) {
	    entity = this.memberRepository.findOneById(id).get();
	}

	return (status == MemberStatus.ENABLE) ?
		this.memberMapper.mapToResponse(entity).setMessage("User's account was enabled") :
		this.memberMapper.mapToResponse(entity).setMessage("User's account was disabled");
    }
}
