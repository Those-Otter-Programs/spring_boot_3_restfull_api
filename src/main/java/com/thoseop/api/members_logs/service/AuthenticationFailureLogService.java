package com.thoseop.api.members_logs.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thoseop.api.members_logs.entity.AuthenticationFailureLogEntity;
import com.thoseop.api.members_logs.exception.AuthLogNotFoundException;
import com.thoseop.api.members_logs.http.response.AuthenticationFailureLogResponse;
import com.thoseop.api.members_logs.mapper.AuthLogMapper;
import com.thoseop.api.members_logs.repository.AuthenticationFailureLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthenticationFailureLogService {

    private final AuthenticationFailureLogRepository authFailLogRepository;
    private final AuthLogMapper authLogMapper;
    
    /**
     * 
     * @param request
     * @return
     */
    public AuthenticationFailureLogResponse saveAuthLog(AuthenticationFailureLogEntity entity) {
	log.info("AuthenticationLogService - saving authentication failure log");
	
        AuthenticationFailureLogEntity savedEntity = authFailLogRepository.save(entity);

	return authLogMapper.mapToAuthenticationFailureResponse(savedEntity);
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public AuthenticationFailureLogResponse readlogById(Long id) {
	log.info("AuthenticationLogService - read authentication failure log by id");
        
	// try to find log by id
	Optional<AuthenticationFailureLogEntity> entity = authFailLogRepository.findOneById(id);
	
	// check if log exists
	if (entity.isEmpty()) 
	    throw new AuthLogNotFoundException("Log not found"); 
        
	return authLogMapper.mapToAuthenticationFailureResponse(entity.get());
    }
    
    /**
     * 
     * @param pageable
     * @return
     */
    public Page<AuthenticationFailureLogResponse> readMemberAuthenticationFailLogs(String username, Pageable pageable) {
	log.info("AuthenticationLogService - read member authentication failures logs");

	Page<AuthenticationFailureLogEntity> logList = authFailLogRepository.findAllByUsername(username, pageable);
	Page<AuthenticationFailureLogResponse> logListResponse = 
		logList.map(log -> authLogMapper.mapToAuthenticationFailureResponse(log));

	return logListResponse;
    }
}
