package com.thoseop.api.members.http.response;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.thoseop.api.members.entity.AuthorityEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class MemberResponse extends RepresentationModel<MemberResponse> implements OtterApiResponse, Serializable {

    private static final long serialVersionUID = 7638859550206253144L;

    private Long memberId;
    private String memberName;
    private String memberEmail;
    private String memberMobileNumber;
//    private String memberPassword;
    private Date memberCreatedAt;
    private Date memberUpdatedAt;
    private Set<AuthorityEntity> memberAuthorities;
}
