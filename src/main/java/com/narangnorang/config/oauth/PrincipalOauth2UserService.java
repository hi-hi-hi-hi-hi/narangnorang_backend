package com.narangnorang.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.narangnorang.config.auth.PrincipalDetails;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.service.MemberService;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    	OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oAuth2User.getAttribute("sub");
        String name = provider + "_" + providerId;
//        String password = bCryptPasswordEncoder.encode(providerId);
        String role = "ROLE_USER";
        String email = oAuth2User.getAttribute("email");

        try {
            MemberDTO member = memberService.selectByKakaoId(providerId);
            if(member == null) {
            }else {
            	return new PrincipalDetails(member, oAuth2User.getAttributes());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return super.loadUser(userRequest);
    }
}
