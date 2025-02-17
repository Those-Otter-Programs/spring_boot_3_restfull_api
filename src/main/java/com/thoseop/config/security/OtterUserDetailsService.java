package com.thoseop.config.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thoseop.api.members.entity.MemberEntity;
import com.thoseop.api.members.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OtterUserDetailsService implements UserDetailsService
{

    private final MemberRepository memberRepository;

    /**
     * 
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User details not found for the user: " + username));
        //	Filter
        List<GrantedAuthority> authorities = member.getAuthorities().stream().map(
                authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList()); 
        
        return new User(member.getEmail(), member.getPassword(), 
        	member.getEnabled(), member.getEnabled(), member.getEnabled(), member.getEnabled(),
		authorities);
    }
}
