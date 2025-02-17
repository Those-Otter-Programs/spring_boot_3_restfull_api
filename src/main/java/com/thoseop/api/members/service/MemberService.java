package com.thoseop.api.members.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.stereotype.Service;

import com.thoseop.api.members.entity.MemberEntity;
import com.thoseop.api.members.http.request.MemberRequest;
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

    /**
     * 
     * @param request
     * @return
     */
    public MemberResponse saveMember(MemberRequest request) {
	log.info("Saving member");

        MemberEntity entity = this.memberMapper.mapRequestToEntity(request);

        String hashPwd = new BCryptPasswordEncoder(BCryptVersion.$2B, 12).encode(entity.getPassword());
        entity.setPassword(hashPwd);

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
}
