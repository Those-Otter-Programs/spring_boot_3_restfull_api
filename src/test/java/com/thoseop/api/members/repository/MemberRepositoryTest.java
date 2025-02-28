package com.thoseop.api.members.repository;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.thoseop.api.members.entity.MemberEntity;

@DisplayName("Testing MemberRepository")
@ActiveProfiles(value = { "test" })
@TestPropertySource(locations = "classpath:application_test.properties")
@DataJpaTest
@Import(BCryptPasswordEncoder.class)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberRepositoryTest {
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository repository;

    @BeforeEach
    void setUp() throws Exception {}

    @DisplayName("test Find One By Id_when Existent Id_then Return Member Data")
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    @Order(1)
    void testFindOneById_whenExistentId_thenReturnMemberData(Long memberId) {
	// g
	// w
	MemberEntity entity = repository.findOneById(memberId).get();
	// t
	Assertions.assertNotNull(entity.getId(), 
		() -> "Member's id should not be null");
	Assertions.assertEquals(memberId, entity.getId(), 
		() -> "Member's id was not the expected");
	
	Assertions.assertNotNull(entity.getName(), 
            () -> "Member's name should not be null");
	Assertions.assertNotNull(entity.getEmail(),  
            () -> "Member's email should not be null");
	Assertions.assertNotNull(entity.getMobileNumber(), 
            () -> "Member's mobile number should not be null");
	Assertions.assertNotNull(entity.getPassword(), 
            () -> "Member's password should not be null");
	Assertions.assertTrue(entity.getAccountNotExpired(), 
            () -> "Member's accountNotExpired should be true");
	Assertions.assertTrue(entity.getCredentialsNotExpired(), 
            () -> "Member's credentialsNotExpired should be true");
	Assertions.assertTrue(entity.getAccountNotLocked(), 
            () -> "Member's accountNotLocked should be true");
	Assertions.assertTrue(entity.getEnabled(), 
            () -> "Member's enabled should be true");
	Assertions.assertNotNull(entity.getCreatedAt(), 
            () -> "Member's createdAt should not be null");
	Assertions.assertNotNull(entity.getUpdatedAt(), 
            () -> "Member's updatedAt should not be null");
    }

    @DisplayName("test Find One By Id_when Non Existent Id_then Throw No Such Element Exception")
    @Test
    @Order(2)
    void testFindOneById_whenNonExistentId_thenThrowNoSuchElementException() {
	// g
	Long memberId = 1000L;
	// w
	// t
	Assertions.assertThrows(NoSuchElementException.class, () -> {
            repository.findOneById(memberId).get();
	}, () -> "Should throw NoSuchElementException");
    }

    @DisplayName("test Find By Email_when Existent Email_then Return Member Data")
    @ParameterizedTest
    @ValueSource(strings = {"ayrton.senna@bravo.com", "carlos.gracie@bravo.com", 
	    "mfredson2@amazon.com", "bgavagan3@slideshare.net", "akhrishtafovich4@cmu.edu"})
    @Order(3)
    void testFindByEmail_whenExistentEmail_thenReturnMemberData() {
	// g
	String memberEmail = "ayrton.senna@bravo.com";
	// w
	MemberEntity entity = repository.findByEmail(memberEmail).get();
	// t
	Assertions.assertNotNull(entity.getEmail(), 
		() -> "Member's email should not be null");
	Assertions.assertEquals(memberEmail, entity.getEmail(), 
		() -> "Member's email was not the expected");
    }

    @DisplayName("test Find By Email_when Non Existent Email_then Throw No Such Element Exception") 
    @Test
    @Order(4)
    void testFindByEmail_whenNonExistentEmail_thenThrowNoSuchElementException() {
	// g
	String memberEmail = "non.existent@test.com";
	// w
	// t
	Assertions.assertThrows(NoSuchElementException.class, () -> {
             repository.findByEmail(memberEmail).get();
	}, () -> "Should throw NoSuchElementException");
    }

    @DisplayName("test Find All Pageable_when Valid Pagination Params_then Return Members Data")
    @Test
    @Order(5)
    void testFindAllPageable_whenValidPaginationParams_thenReturnMembersData() {
	// g
        Pageable pageable = PageRequest.of(0, 8, Sort.by(Direction.ASC, "id"));
	// w
        MemberEntity entity = repository.findAll(pageable).getContent().get(0);
        // t
	Assertions.assertNotNull(entity.getId(), 
            () -> "Member's id should not be null");
	Assertions.assertEquals(1L, entity.getId(), 
            () -> "Member's id was not the expected");
	Assertions.assertNotNull(entity.getName(), 
            () -> "Member's name should not be null");
	Assertions.assertEquals("Ayrton Senna", entity.getName(), 
            () -> "Member's name was not the expected");
	Assertions.assertNotNull(entity.getEmail(), 
            () -> "Member's email should not be null");
	Assertions.assertEquals("ayrton.senna@bravo.com", entity.getEmail(), 
            () -> "Member's email was not the expected");
	Assertions.assertNotNull(entity.getMobileNumber(), 
            () -> "Member's mobile number should not be null");
	Assertions.assertEquals("(11) 98765-4321", entity.getMobileNumber(), 
            () -> "Member's mobile number was not the expected");
	Assertions.assertNotNull(entity.getPassword(), 
            () -> "Member's password should not be null");
	Assertions.assertTrue(entity.getAccountNotExpired(), 
            () -> "Member's accountNotExpired should be true");
	Assertions.assertTrue(entity.getCredentialsNotExpired(), 
            () -> "Member's credentialsNotExpired should be true");
	Assertions.assertTrue(entity.getAccountNotLocked(), 
            () -> "Member's accountNotLocked should be true");
	Assertions.assertTrue(entity.getEnabled(), 
            () -> "Member's enabled should be true");
	Assertions.assertNotNull(entity.getCreatedAt(), 
            () -> "Member's createdAt should not be null");
	Assertions.assertNotNull(entity.getUpdatedAt(), 
            () -> "Member's updatedAt should not be null");
    }

    static Stream<Arguments> pageableSeed() {
	return Stream.of(
		Arguments.of(PageRequest.of(2, 4, Sort.by(Direction.DESC, "id"))),
		Arguments.of(PageRequest.of(0, 8, Sort.by(Direction.ASC, "email"))),
		Arguments.of(PageRequest.of(1, 2, Sort.by(Direction.DESC, "name")))
            );
    }

    @DisplayName("test Find All Pageable_when Parameterized Valid Pagination Params_then Return Members Data")
    @ParameterizedTest
    @MethodSource("pageableSeed")
    @Order(6)
    void testFindAllPageable_whenParameterizedValidPaginationParams_thenReturnMembersData(Pageable pageable) {
	// g   
	// w
        MemberEntity entity = repository.findAll(pageable).getContent().get(0);
        // t
        Assertions.assertNotNull(entity.getId(),
                () -> "Member's id should not be null");
        Assertions.assertNotNull(entity.getName(),
                () -> "Member's name should not be null");
        Assertions.assertNotNull(entity.getEmail(),
                () -> "Member's email should not be null");
        Assertions.assertNotNull(entity.getMobileNumber(),
                () -> "Member's mobile number should not be null");
        Assertions.assertNotNull(entity.getPassword(),
                () -> "Member's password should not be null");
        Assertions.assertTrue(entity.getAccountNotExpired(),
                () -> "Member's accountNotExpired should be true");
        Assertions.assertTrue(entity.getCredentialsNotExpired(),
                () -> "Member's credentialsNotExpired should be true");
        Assertions.assertTrue(entity.getAccountNotLocked(),
                () -> "Member's accountNotLocked should be true");
        Assertions.assertTrue(entity.getEnabled(),
                () -> "Member's enabled should be true");
        Assertions.assertNotNull(entity.getCreatedAt(),
                () -> "Member's createdAt should not be null");
        Assertions.assertNotNull(entity.getUpdatedAt(),
                () -> "Member's updatedAt should not be null");
    }

    @DisplayName("test Find All Pageable_when Invalid Pagination Params_then Throws Property Reference Exception")
    @Test
    @Order(7)
    void testFindAllPageable_whenInvalidPaginationParams_thenThrowsPropertyReferenceException() {
	// g
        Pageable pageable = PageRequest.of(0, 8, Sort.by(Direction.ASC, "notsortable"));
	// w
        // t
        Assertions.assertThrows(PropertyReferenceException.class, () -> {
            repository.findAll(pageable).getContent().get(0);
        });
    }

    @DisplayName("test Set Member Account Expired Status_when Existent User Id_then Return New Account Expired Status")
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Order(8)
    void testSetMemberAccountExpiredStatus_whenExistentUserId_thenReturnNewAccountExpiredStatus(boolean status) {
	// g
	Long memberId = 3l;
	// w
	repository.setMemberAccountExpiredStatus(memberId, status);
	MemberEntity entity = repository.findById(memberId).get();
	// t
	Assertions.assertEquals(status, entity.getAccountNotExpired());
    }

    @DisplayName("test Set Member Credentials Expired Status_when Existent User Id_then Return New Credentials Expired Status")
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Order(9)
    void testSetMemberCredentialsExpiredStatus_whenExistentUserId_thenReturnNewCredentialsExpiredStatus(boolean status) {
	// g
	Long memberId = 3l;
	// w
	repository.setMemberCredentialsExpiredStatus(memberId, status);
	MemberEntity entity = repository.findById(memberId).get();
	// t
	Assertions.assertEquals(status, entity.getCredentialsNotExpired());
    }

    @DisplayName("test Set Member Account Locked Status_when Existent User Id_then Return New Account Locked Status")
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Order(10)
    void testSetMemberAccountLockedStatus_whenExistentUserId_thenReturnNewAccountLockedStatus(boolean status) {
	// g
	Long memberId = 3l;
	// w
	repository.setMemberAccountLockedStatus(memberId, status);
	MemberEntity entity = repository.findById(memberId).get();
	// t
	Assertions.assertEquals(status, entity.getAccountNotLocked());
    }

    @DisplayName("test Set Member Enabled Status_when Existent User Id_thenReturn Enabled Status")
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Order(11)
    void testSetMemberEnabledStatus_whenExistentUserId_thenReturnEnabledStatus(boolean status) {
	// g
	Long memberId = 3l;
	// w
	repository.setMemberEnabledStatus(memberId, status);
	MemberEntity entity = repository.findById(memberId).get();
	// t
	Assertions.assertEquals(status, entity.getEnabled());
    }

    @DisplayName("test Set Member Password_when Existent User Email And Bcrypt Pass_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = {"new_mick_pass", "mick_pass"})
    @Order(12)
    void testSetMemberPassword_whenExistentUserEmailAndBcryptPass_thenReturnHTTP200(String memberPass) {
	// g
	String memberEmail = "mfredson2@amazon.com";
	// w
	String bcryptPass = passwordEncoder.encode(memberPass);

	repository.setMemberPassword(memberEmail, bcryptPass);
	MemberEntity entity = repository.findByEmail(memberEmail).get();
	// t
	Assertions.assertTrue(passwordEncoder.matches(memberPass, entity.getPassword()));
    }

    @DisplayName("test Set Member Password_when Existent User Email And Noop Pass_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = {"{noop}new_mick_pass", "{noop}mick_pass"})
    @Order(13)
    void testSetMemberPassword_whenExistentUserEmailAndNoopPass_thenReturnHTTP200(String memberPass) {
	// g
	String memberEmail = "mfredson2@amazon.com";
	// w
	repository.setMemberPassword(memberEmail, memberPass);
	MemberEntity entity = repository.findByEmail(memberEmail).get();
	// t
	Assertions.assertEquals(memberPass, entity.getPassword());
    }
}
