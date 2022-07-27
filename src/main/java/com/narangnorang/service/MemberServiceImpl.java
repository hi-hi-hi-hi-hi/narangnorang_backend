package com.narangnorang.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.narangnorang.dao.MemberDAO;
import com.narangnorang.dao.MiniroomDAO;
import com.narangnorang.dto.MemberDTO;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDAO memberDAO;
	@Autowired
	MiniroomDAO miniroomDAO;

	// 로그인
	@Transactional
	@Override
	public MemberDTO selectMember(Map<String, String> map) throws Exception {
		memberDAO.updateDatetime(map);
		return memberDAO.selectMember(map);
	}

	// 아이디 중복 체크
	@Override
	public int checkId(String email) throws Exception {
		return memberDAO.checkId(email);
	}

	// 닉네임 중복 체크
	@Override
	public int checkNickname(String nickname) throws Exception {
		return memberDAO.checkNickname(nickname);
	}

	// 일반회원가입
	@Transactional
	@Override
	public int generalSignUp(MemberDTO dto) throws Exception {
		String name = dto.getName();
		return memberDAO.generalSignUp(dto) & miniroomDAO.insertDefaultItems(name) & miniroomDAO.insertDefaultMyItems1() & miniroomDAO.insertDefaultMyItems2();
	}

	// 상담사 회원가입
	@Override
	public int counselorSignUp(MemberDTO dto) throws Exception {
		return memberDAO.counselorSignUp(dto);
	}

	// email로 회원 찾기
	@Override
	public MemberDTO selectByEmail(String email) throws Exception {
		return memberDAO.selectByEmail(email);
	}

	// 새 비밀번호 설정
	@Override
	public int newPw(MemberDTO dto) throws Exception {
		return memberDAO.newPw(dto);
	}

	// 일반회원 정보수정
	@Override
	public int generalEdit(MemberDTO dto) throws Exception {
		return memberDAO.generalEdit(dto);
	}

	// 상담사회원 정보수정
	@Override
	public int counselorEdit(MemberDTO dto) throws Exception {
		return memberDAO.counselorEdit(dto);
	}

	// 모든 회원
	@Override
	public List<MemberDTO> selectAll() throws Exception {
		return memberDAO.selectAll();
	}

	// 선택 계정 삭제
	@Override
	public int delSelected(List<String> list) throws Exception {
		return memberDAO.delSelected(list);
	}

	// 미승인 상담사 회원
	@Override
	public List<MemberDTO> selectByPrivileage2() throws Exception {
		return memberDAO.selectByPrivileage2();
	}

	// 선택 상담사 승급
	@Override
	public int privileageUp(List<String> list) throws Exception {
		return memberDAO.privileageUp(list);
	}

	// 카카오 로그인
	@Override
	public String getKakaoAccessToken(String authorize_code) {
		String access_Token = "";
		String refresh_Token = "";
		String reqURL = "https://kauth.kakao.com/oauth/token";

		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=94007325c197e3be03e0c5690a45abdb");

			sb.append("&redirect_uri=http://localhost:8091/kakaoLogin");
			sb.append("&code=" + authorize_code);
			bw.write(sb.toString());
			bw.flush();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line="";
			String result="";
			
			while ((line = br.readLine()) != null) {
				result += line;
			}
			
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);
			
			access_Token = element.getAsJsonObject().get("access_token").getAsString();
			refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
			
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return access_Token;
	}

	@Override
	public HashMap<String, String> getKakaoUserInfo(String access_Token) {
		HashMap<String, String> userInfo = new HashMap<>();
		String reqURL = "https://kapi.kakao.com/v2/user/me";

		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			
			conn.setRequestProperty("Authorization", "Bearer " + access_Token); 
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = "";
			String result = "";
			
			while ((line = br.readLine()) != null) {
				result += line;
			}
			
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);
			
			JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
			
			String id = element.getAsJsonObject().get("id").getAsString();
			String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
			
			userInfo.put("id", id);
			userInfo.put("name", nickname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userInfo;
	}
	@Override
	public String getNaverUserInfo(String access_Token) {
		String token = access_Token;// 네아로 접근 토큰 값";
		StringBuffer response = null;
		System.out.println(token);
		String header = "Bearer " + token; // Bearer 다음에 공백 추가
		try {
			String apiURL = "https://openapi.naver.com/v1/nid/me";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", header);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if(responseCode==200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {  // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			System.out.println("확인:" + response.toString());
		} catch (Exception e) {
			System.out.println(e);
		}
		return response.toString();
	}

	@Override
	public MemberDTO selectByKakaoId(String kakaoId) throws Exception {
		return memberDAO.selectByKakaoId(kakaoId);
	}

}
