package com.thoseop.api.members_logs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import com.thoseop.api.members_logs.entity.AuthenticationFailureLogEntity;
import com.thoseop.api.members_logs.http.response.AuthenticationFailureLogResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthLogMapper {

   @Mappings({
        @Mapping(target = "logId", source = "id"), 
        @Mapping(target = "logMemberUsername", source = "username"), 
        @Mapping(target = "logEventResult", source = "eventResult"), 
        @Mapping(target = "logRemoteIpAddress", source = "remoteIpAddress"), 
        @Mapping(target = "logMessage", source = "message"), 
        @Mapping(target = "logAuthTime", source = "authTime"), 
        @Mapping(target = "logCreatedAt", source = "createdAt"), 
        @Mapping(target = "links", ignore = true), 
   })
   AuthenticationFailureLogResponse mapToAuthenticationFailureResponse(AuthenticationFailureLogEntity entity);
}
