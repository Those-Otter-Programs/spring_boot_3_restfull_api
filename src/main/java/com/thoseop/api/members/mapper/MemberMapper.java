package com.thoseop.api.members.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import com.thoseop.api.members.entity.MemberEntity;
import com.thoseop.api.members.http.request.MemberRequest;
import com.thoseop.api.members.http.response.MemberResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {

    @Mappings({
            @Mapping(target = "memberId", source = "id"), 
            @Mapping(target = "memberName", source = "name"),
            @Mapping(target = "memberEmail", source = "email"),
            @Mapping(target = "memberMobileNumber", source = "mobileNumber"),
//            @Mapping(target = "memberPassword", source = "password"), 
            @Mapping(target = "memberCreatedAt", source = "createdAt"),
            @Mapping(target = "memberUpdatedAt", source = "updatedAt"),
            @Mapping(target = "memberAuthorities", ignore = true), 
//            @Mapping(target = "add", ignore = true), 
            @Mapping(target = "links", ignore = true), 
            //	@Mapping(target = "memberPwd", expression = "java(user.getAuthorities().toString())"), 
    })
    MemberResponse mapToResponse(MemberEntity memberEntity);

    @Mappings({
            @Mapping(target = "id", ignore = true), 
            @Mapping(target = "name", source = "memberName"),
            @Mapping(target = "email", source = "memberEmail"),
            @Mapping(target = "mobileNumber", source = "memberMobileNumber"),
            @Mapping(target = "password", source = "memberPassword"), 
            @Mapping(target = "createdAt", source = "memberCreatedAt"),
            @Mapping(target = "updatedAt", source = "memberUpdatedAt"),
            @Mapping(target = "authorities", ignore = true), 
    })
    MemberEntity mapRequestToEntity(MemberRequest member);

    @Mappings({
            @Mapping(target = "memberId", ignore = true),
            @Mapping(target = "memberName", source = "memberName"),
            @Mapping(target = "memberEmail", source = "memberEmail"),
            @Mapping(target = "memberMobileNumber", source = "memberMobileNumber"),
//            @Mapping(target = "memberPassword", source = "memberPassword"),
            @Mapping(target = "memberCreatedAt", source = "memberCreatedAt"),
            @Mapping(target = "memberUpdatedAt", source = "memberUpdatedAt"),
            @Mapping(target = "memberAuthorities", ignore = true), 
//            @Mapping(target = "add", ignore = true), 
            @Mapping(target = "links", ignore = true), 
    })
    MemberResponse mapRequestToResponse(MemberRequest member);
}
