package com.narangnorang.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.narangnorang.dto.MemberDTO;
import com.narangnorang.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private final MemberService memberService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			MemberDTO member = memberService.selectByEmail(username);
			if (member != null) {
				return new PrincipalDetails(member);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
