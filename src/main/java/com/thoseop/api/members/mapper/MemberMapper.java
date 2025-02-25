package com.thoseop.api.members.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import com.thoseop.api.members.entity.MemberEntity;
import com.thoseop.api.members.http.request.MemberCreateRequest;
import com.thoseop.api.members.http.request.MemberUpdateRequest;
import com.thoseop.api.members.http.response.MemberCreatedResponse;
import com.thoseop.api.members.http.response.MemberDetailsResponse;
import com.thoseop.api.members.http.response.MemberResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {

    @Mappings({
            @Mapping(target = "memberId", source = "id"), 
            @Mapping(target = "memberName", source = "name"),
            @Mapping(target = "memberEmail", source = "email"),
            @Mapping(target = "memberMobileNumber", source = "mobileNumber"),
//            @Mapping(target = "memberPassword", source = "password"), 
//            @Mapping(target = "memberEnabled", source = "enabled"),
//            @Mapping(target = "memberStatus", ignore = true),
            @Mapping(target = "memberCreatedAt", source = "createdAt"),
//            @Mapping(target = "memberUpdatedAt", source = "updatedAt"),
//            @Mapping(target = "memberAuthorities", ignore = true), 
//            @Mapping(target = "memberAuthorities", source = "authorities"), 
            @Mapping(target = "memberAuthorities", expression = "java(memberEntity.getAuthorities().stream().map(memberAuthority -> memberAuthority.getName()).collect(java.util.stream.Collectors.toList()))"), 
//            @Mapping(target = "add", ignore = true), 
            @Mapping(target = "links", ignore = true), 
            //	@Mapping(target = "memberPwd", expression = "java(memberEntity.getAuthorities().toString())"), 
    })
    MemberResponse mapToResponse(MemberEntity memberEntity);

    @Mappings({
            @Mapping(target = "memberId", source = "id"), 
            @Mapping(target = "memberName", source = "name"),
            @Mapping(target = "memberEmail", source = "email"),
            @Mapping(target = "memberMobileNumber", source = "mobileNumber"),
//            @Mapping(target = "memberPassword", source = "password"), 
            @Mapping(target = "memberAccountNotExpired", source = "accountNotExpired"),
            @Mapping(target = "memberCredentialsNotExpired", source = "credentialsNotExpired"),
            @Mapping(target = "memberAccountNotLocked", source = "accountNotLocked"),
            @Mapping(target = "memberEnabled", source = "enabled"),
//            @Mapping(target = "memberStatus", ignore = true),
            @Mapping(target = "memberCreatedAt", source = "createdAt"),
            @Mapping(target = "memberUpdatedAt", source = "updatedAt"),
//            @Mapping(target = "memberAuthorities", ignore = true), 
//            @Mapping(target = "memberAuthorities", source = "authorities"), 
            @Mapping(target = "memberAuthorities", expression = "java(memberEntity.getAuthorities().stream().map(memberAuthority -> memberAuthority.getName()).collect(java.util.stream.Collectors.toList()))"), 
//            @Mapping(target = "add", ignore = true), 
            @Mapping(target = "links", ignore = true), 
            //	@Mapping(target = "memberPwd", expression = "java(memberEntity.getAuthorities().toString())"), 
    })
    MemberDetailsResponse mapToDetailsResponse(MemberEntity memberEntity);

    @Mappings({
            @Mapping(target = "memberId", source = "id"), 
            @Mapping(target = "memberName", source = "name"),
            @Mapping(target = "memberEmail", source = "email"),
            @Mapping(target = "memberMobileNumber", source = "mobileNumber"),
//            @Mapping(target = "memberPassword", source = "password"), 
//            @Mapping(target = "memberEnabled", source = "enabled"),
//            @Mapping(target = "memberStatus", ignore = true),
//            @Mapping(target = "memberCreatedAt", source = "createdAt"),
//            @Mapping(target = "memberUpdatedAt", source = "updatedAt"),
//            @Mapping(target = "memberAuthorities", ignore = true), 
//            @Mapping(target = "memberAuthorities", source = "authorities"), 
            @Mapping(target = "memberAuthorities", expression = "java(memberEntity.getAuthorities().stream().map(memberAuthority -> memberAuthority.getName()).collect(java.util.stream.Collectors.toList()))"), 
            @Mapping(target = "message", ignore = true),
//            @Mapping(target = "add", ignore = true), 
            @Mapping(target = "links", ignore = true), 
            //	@Mapping(target = "memberPwd", expression = "java(memberEntity.getAuthorities().toString())"), 
    })
    MemberCreatedResponse mapToCreatedResponse(MemberEntity memberEntity);

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
//            @Mapping(target = "memberPassword", source = "memberPassword"),
            @Mapping(target = "memberCreatedAt", ignore = true),
//            @Mapping(target = "memberUpdatedAt", ignore = true),
            @Mapping(target = "memberAuthorities", ignore = true), 
//            @Mapping(target = "add", ignore = true), 
            @Mapping(target = "links", ignore = true), 
    })
    MemberResponse mapRequestToResponse(MemberCreateRequest member);
}
