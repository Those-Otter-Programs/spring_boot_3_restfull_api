package com.thoseop.api.members.http.request;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
@EqualsAndHashCode
@ToString
public class MemberRequest implements Serializable {

    private static final long serialVersionUID = 1645777648004835606L;

    private String memberName;
    private String memberEmail;
    private String memberMobileNumber;
    private String memberPassword;
    private Date memberCreatedAt;
    private Date memberUpdatedAt;
    private Set<AuthorityEntity> memberAuthorities;
}
