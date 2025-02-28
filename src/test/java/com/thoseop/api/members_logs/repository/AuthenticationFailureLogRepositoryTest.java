package com.thoseop.api.members_logs.repository;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.thoseop.api.members_logs.entity.AuthenticationFailureLogEntity;
import com.thoseop.api.members_logs.entity.dto.MemberDetailsAuthenticationDTO;

@DisplayName("Testing AuthenticationFailureLogRepository")
@ActiveProfiles(value = {"test"})
@TestPropertySource(locations="classpath:application_test.properties")
@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationFailureLogRepositoryTest {
    
    @Autowired
    public AuthenticationFailureLogRepository repository;
    
    @BeforeEach
    void setUp() throws Exception {}

    @DisplayName("test Find One By Id_when Existent Id_then Return Auth Log Data")
    @ParameterizedTest
    @ValueSource(longs = {1l, 2l, 3l, 4l})
    @Order(1)
    void testFindOneById_whenExistentId_thenReturnAuthLogData(Long id) {
	AuthenticationFailureLogEntity entity = repository.findOneById(id).get();

        Assertions.assertNotNull(entity.getId(),
                () -> "Auth log id should not be null");
        Assertions.assertNotNull(entity.getUsername(),
                () -> "Auth log username should not be null");
        Assertions.assertNotNull(entity.getEventResult(),
                () -> "Auth log eventResult should not be null");
        Assertions.assertNotNull(entity.getRemoteIpAddress(),
                () -> "Auth log remoteIpAddress should not be null");
        Assertions.assertNotNull(entity.getMessage(),
                () -> "Auth log message should not be null");
        Assertions.assertNotNull(entity.getAuthTime(),
                () -> "Auth log authTime should not be null");
        Assertions.assertNotNull(entity.getCreatedAt(),
                () -> "Auth log createdAt should not be null");
    }

    static Stream<Arguments> pageableFindAllByUsernameSeed() {
	return Stream.of(
		Arguments.of(PageRequest.of(0, 8, Sort.by(Direction.ASC, "id"))),
		Arguments.of(PageRequest.of(1, 2, Sort.by(Direction.ASC, "username"))),
		Arguments.of(PageRequest.of(0, 4, Sort.by(Direction.DESC, "eventResult"))),
		Arguments.of(PageRequest.of(1, 2, Sort.by(Direction.ASC, "remoteIpAddress"))),
		Arguments.of(PageRequest.of(0, 4, Sort.by(Direction.DESC, "message"))),
		Arguments.of(PageRequest.of(0, 8, Sort.by(Direction.ASC, "authTime"))),
		Arguments.of(PageRequest.of(0, 6, Sort.by(Direction.DESC, "createdAt")))
            );
    }

    @DisplayName("test Find All By Username_when Existent Username_then Return Auth Log Data")
    @ParameterizedTest
    @MethodSource("pageableFindAllByUsernameSeed")
    @Order(2)
    void testFindAllByUsername_whenExistentUsername_thenReturnAuthLogData(Pageable pageable) {	
	AuthenticationFailureLogEntity entity = repository.findAllByUsername("ayrton.senna@bravo.com", pageable).getContent().get(0);

        Assertions.assertNotNull(entity.getId(),
                () -> "Auth log id should not be null");
        Assertions.assertNotNull(entity.getUsername(),
                () -> "Auth log username should not be null");
        Assertions.assertNotNull(entity.getEventResult(),
                () -> "Auth log eventResult should not be null");
        Assertions.assertNotNull(entity.getRemoteIpAddress(),
                () -> "Auth log remoteIpAddress should not be null");
        Assertions.assertNotNull(entity.getMessage(),
                () -> "Auth log message should not be null");
        Assertions.assertNotNull(entity.getAuthTime(),
                () -> "Auth log authTime should not be null");
        Assertions.assertNotNull(entity.getCreatedAt(),
                () -> "Auth log createdAt should not be null");
    }

    static Stream<Arguments> pageableFindMemberAuthLogsSeed() {
	return Stream.of(
		Arguments.of(PageRequest.of(0, 8, Sort.by(Direction.ASC, "me.id"))),
		Arguments.of(PageRequest.of(1, 2, Sort.by(Direction.ASC, "me.email"))),
		Arguments.of(PageRequest.of(0, 4, Sort.by(Direction.DESC, "me.accountNotExpired"))),
		Arguments.of(PageRequest.of(1, 2, Sort.by(Direction.ASC, "me.credentialsNotExpired"))),
		Arguments.of(PageRequest.of(0, 4, Sort.by(Direction.DESC, "me.accountNotLocked"))),
		Arguments.of(PageRequest.of(0, 8, Sort.by(Direction.ASC, "me.enabled"))),
		Arguments.of(PageRequest.of(1, 2, Sort.by(Direction.DESC, "afl.id"))),
		Arguments.of(PageRequest.of(0, 4, Sort.by(Direction.DESC, "afl.eventResult"))),
		Arguments.of(PageRequest.of(0, 4, Sort.by(Direction.DESC, "afl.remoteIpAddress"))),
		Arguments.of(PageRequest.of(1, 2, Sort.by(Direction.ASC, "afl.message"))),
		Arguments.of(PageRequest.of(0, 6, Sort.by(Direction.DESC, "afl.authTime")))
            );
    }

    @DisplayName("")
    @ParameterizedTest
    @MethodSource("pageableFindMemberAuthLogsSeed")
    @Order(3)
    void testFindMemberAuthLogs_whenParameterizedValidPaginationParams_thenReturnMembersData(Pageable pageable) {
        MemberDetailsAuthenticationDTO memberFailAuthLogs = repository.findMemberAuthLogs("ayrton.senna@bravo.com", pageable).getContent().get(0);

        Assertions.assertNotNull(memberFailAuthLogs.getMemberId(), 
        	() -> "Auth log's memberId should not be null");
        Assertions.assertNotNull(memberFailAuthLogs.getMemberUsername(), 
        	() -> "Auth log's memberUsername should not be null");
        Assertions.assertNotNull(memberFailAuthLogs.getMemberAccountNotExpired(), 
        	() -> "Auth log's memberAccountNotExpired should not be null");
        Assertions.assertNotNull(memberFailAuthLogs.getMemberCredentialsNotExpired(), 
        	() -> "Auth log's memberCredentialsNotExpired should not be null");
        Assertions.assertNotNull(memberFailAuthLogs.getMemberAccountNotLocked(), 
        	() -> "Auth log's memberAccountNotLockedshould not be null");
        Assertions.assertNotNull(memberFailAuthLogs.getMemberEnabled(), 
        	() -> "Auth log's MemberEnabledshould not be null");

        Assertions.assertNotNull(memberFailAuthLogs.getLogId(), 
        	() -> "Auth log's logId should not be null");
        Assertions.assertNotNull(memberFailAuthLogs.getLogEventResult(), 
        	() -> "Auth log's logEventResult should not be null");
        Assertions.assertNotNull(memberFailAuthLogs.getLogRemoteIpAddress(), 
        	() -> "Auth log's logRemoteIpAddress should not be null");
        Assertions.assertNotNull(memberFailAuthLogs.getLogMessage(), 
        	() -> "Auth log's logMessage should not be null");
        Assertions.assertNotNull(memberFailAuthLogs.getLogTime(), 
        	() -> "Auth log's logTime should not be null");
    }
}
