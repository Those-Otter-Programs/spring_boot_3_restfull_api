package com.thoseop.api.members.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.stereotype.Service;

import com.thoseop.api.members.entity.MemberEntity;
import com.thoseop.api.members.http.request.MemberRequest;
import com.thoseop.api.members.http.response.MemberResponse;
import com.thoseop.api.members.mapper.MemberMapper;
import com.thoseop.api.members.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService
{

    private final MemberRepository memberRepository;
    private final MemberMapper customerMapper;
    //    private final PasswordEncoder passwordEncoder;

    public MemberResponse saveCustomer(MemberRequest request)
    {
        MemberEntity entity = this.customerMapper.mapRequestToEntity(request);

        String hashPwd = new BCryptPasswordEncoder(BCryptVersion.$2B, 12).encode(entity.getPassword());
        entity.setPassword(hashPwd);
//        entity.setRole("ROLE_%s".formatted(request.getCustomerRole()));

        MemberEntity savedEntity = memberRepository.save(entity);

        return customerMapper.mapToResponse(savedEntity);
    }
}
