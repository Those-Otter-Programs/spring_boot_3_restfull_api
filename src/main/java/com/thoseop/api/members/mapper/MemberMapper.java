package com.thoseop.api.members.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import com.thoseop.api.members.entity.MemberEntity;
import com.thoseop.api.members.http.request.MemberCreateRequest;
import com.thoseop.api.members.http.request.MemberUpdateRequest;
import com.thoseop.api.members.http.response.MemberAccountExpiredResponse;
import com.thoseop.api.members.http.response.MemberAccountLockedResponse;
import com.thoseop.api.members.http.response.MemberCreatedResponse;
import com.thoseop.api.members.http.response.MemberCredentialsExpiredResponse;
import com.thoseop.api.members.http.response.MemberDetailsResponse;
import com.thoseop.api.members.http.response.MemberEnabledResponse;
import com.thoseop.api.members.http.response.MemberResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {

    @Mappings({
            @Mapping(target = "memberId", source = "id"), 
            @Mapping(target = "memberName", source = "name"),
            @Mapping(target = "memberEmail", source = "email"),
            @Mapping(target = "memberMobileNumber", source = "mobileNumber"),
            @Mapping(target = "memberCreatedAt", source = "createdAt"),
            @Mapping(target = "memberAuthorities", expression = "java(memberEntity.getAuthorities().stream().map(memberAuthority -> memberAuthority.getName()).collect(java.util.stream.Collectors.toList()))"), 
            @Mapping(target = "links", ignore = true), 
    })
    MemberResponse mapToResponse(MemberEntity memberEntity);

    @Mappings({
            @Mapping(target = "memberId", source = "id"), 
            @Mapping(target = "memberName", source = "name"),
            @Mapping(target = "memberEmail", source = "email"),
            @Mapping(target = "memberMobileNumber", source = "mobileNumber"),
            @Mapping(target = "memberAccountNotExpired", source = "accountNotExpired"),
            @Mapping(target = "memberCredentialsNotExpired", source = "credentialsNotExpired"),
            @Mapping(target = "memberAccountNotLocked", source = "accountNotLocked"),
            @Mapping(target = "memberEnabled", source = "enabled"),
            @Mapping(target = "memberCreatedAt", source = "createdAt"),
            @Mapping(target = "memberUpdatedAt", source = "updatedAt"),
            @Mapping(target = "memberAuthorities", expression = "java(memberEntity.getAuthorities().stream().map(memberAuthority -> memberAuthority.getName()).collect(java.util.stream.Collectors.toList()))"), 
            @Mapping(target = "links", ignore = true), 
    })
    MemberDetailsResponse mapToDetailsResponse(MemberEntity memberEntity);

    @Mappings({
            @Mapping(target = "memberId", source = "id"), 
            @Mapping(target = "memberName", source = "name"),
            @Mapping(target = "memberEmail", source = "email"),
            @Mapping(target = "memberMobileNumber", source = "mobileNumber"),
            @Mapping(target = "memberAuthorities", expression = "java(memberEntity.getAuthorities().stream().map(memberAuthority -> memberAuthority.getName()).collect(java.util.stream.Collectors.toList()))"), 
            @Mapping(target = "message", ignore = true),
            @Mapping(target = "links", ignore = true), 
    })
    MemberCreatedResponse mapToCreatedResponse(MemberEntity memberEntity);

    @Mappings({
            @Mapping(target = "memberUsername", source = "email"),
            @Mapping(target = "memberAccountNotExpired", source = "accountNotExpired"), 
            @Mapping(target = "links", ignore = true), 
    })
    MemberAccountExpiredResponse mapToAccountExpiredResponse(MemberEntity memberEntity);
    
    @Mappings({
            @Mapping(target = "memberUsername", source = "email"),
            @Mapping(target = "memberCredentialsNotExpired", source = "credentialsNotExpired"), 
            @Mapping(target = "links", ignore = true), 
    })
    MemberCredentialsExpiredResponse mapToCredentialsExpiredResponse(MemberEntity memberEntity);

    @Mappings({
            @Mapping(target = "memberUsername", source = "email"),
            @Mapping(target = "memberAccountNotLocked", source = "accountNotLocked"), 
            @Mapping(target = "links", ignore = true), 
    })
    MemberAccountLockedResponse mapToAccountLockedResponse(MemberEntity memberEntity);
    
    @Mappings({
            @Mapping(target = "memberUsername", source = "email"),
            @Mapping(target = "memberEnabled", source = "enabled"), 
            @Mapping(target = "links", ignore = true), 
    })
    MemberEnabledResponse mapToEnabledResponse(MemberEntity memberEntity);

    @Mappings({
            @Mapping(target = "id", ignore = true), 
            @Mapping(target = "name", source = "memberName"),
            @Mapping(target = "email", source = "memberEmail"),
            @Mapping(target = "mobileNumber", source = "memberMobileNumber"),
            @Mapping(target = "password", source = "memberPassword"), 
            @Mapping(target = "enabled", ignore = true), 
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "authorities", ignore = true), 
    })
    MemberEntity mapRequestToEntity(MemberCreateRequest member);

    @Mappings({
            @Mapping(target = "id", ignore = true), 
            @Mapping(target = "name", source = "memberName"),
            @Mapping(target = "email", source = "memberEmail"),
            @Mapping(target = "mobileNumber", source = "memberMobileNumber"),
            @Mapping(target = "password", ignore = true), 
            @Mapping(target = "enabled", ignore = true), 
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "authorities", ignore = true), 
    })
    MemberEntity mapRequestToEntity(MemberUpdateRequest member);

    @Mappings({
            @Mapping(target = "memberId", ignore = true),
            @Mapping(target = "memberName", source = "memberName"),
            @Mapping(target = "memberEmail", source = "memberEmail"),
            @Mapping(target = "memberMobileNumber", source = "memberMobileNumber"),
            @Mapping(target = "memberCreatedAt", ignore = true),
            @Mapping(target = "memberAuthorities", ignore = true), 
            @Mapping(target = "links", ignore = true), 
    })
    MemberResponse mapRequestToResponse(MemberCreateRequest member);
}
