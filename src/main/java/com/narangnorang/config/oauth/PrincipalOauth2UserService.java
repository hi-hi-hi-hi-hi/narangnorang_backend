package com.narangnorang.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String name = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode(providerId);
        String email = oAuth2User.getAttribute("email");
        try {
            MemberDTO member = memberService.selectByKakaoId(providerId);
            if(member == null) {
                MemberDTO mDTO = new MemberDTO();
                // 회원가입 처리
                int randomNumber = 0;
                String tmpId = "";
                randomNumber = (int)Math.floor(Math.random() * (99999999 - 10000000 + 1)) + 99999999;
                tmpId = "google" + randomNumber + "@g.com";
                mDTO.setEmail(tmpId);
                mDTO.setPassword(password);
                mDTO.setName(name);
                mDTO.setPhone("미등록");
                mDTO.setRegion("미등록");
                mDTO.setKakaoId(providerId);
                memberService.generalSignUp(mDTO);

                MemberDTO memberDTO = memberService.selectByKakaoId(providerId);
                return new PrincipalDetails(memberDTO, oAuth2User.getAttributes());
            }
            else {
                return new PrincipalDetails(member, oAuth2User.getAttributes());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
