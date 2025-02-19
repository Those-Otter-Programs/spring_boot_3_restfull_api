package com.thoseop.api.members.entity.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Long memberId;
    private String memberName;
    private String memberEmail;
    private String memberMobileNumber;
    private String memberPassword;
    private Boolean memberEnabled;
    private Date memberCreatedAt;
    private Date memberUpdatedAt;
    
    public MemberDTO(String memberEmail, String memberPassword) {
	this.memberEmail = memberEmail;
	this.memberPassword = memberPassword;
    }
}
