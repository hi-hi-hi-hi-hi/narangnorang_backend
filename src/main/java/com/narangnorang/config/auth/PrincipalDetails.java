package com.narangnorang.config.auth;

import com.narangnorang.dto.MemberDTO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class PrincipalDetails implements UserDetails {

    MemberDTO memberDTO;

    public PrincipalDetails(MemberDTO memberDTO) {
        this.memberDTO = memberDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberDTO.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return memberDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return memberDTO.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
