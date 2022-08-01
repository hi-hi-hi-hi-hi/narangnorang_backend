package com.narangnorang.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.narangnorang.config.auth.PrincipalDetails;
import com.narangnorang.dto.ChallengeDTO;
import com.narangnorang.dto.DailyLogDTO;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.MoodStateDTO;
import com.narangnorang.service.ChatBotService;

@RestController
public class ChatBotController {

	@Autowired
	private ChatBotService chatBotService;

	@Value("${spring.servlet.multipart.location}")
	private String uploadPath;

	// 챌린지 조회(하루)
	@GetMapping("/api/chatbot/challenge")
	public Map<String, Object> selectChallenge(@AuthenticationPrincipal PrincipalDetails principalDetails)
			throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = simpleDateFormat.format(calendar.getTime());
		ChallengeDTO challengeDTO = new ChallengeDTO(0, memberId, datetime, null);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("flag", false);
		challengeDTO = chatBotService.selectChallenge(challengeDTO);
		if (challengeDTO != null) {
			response.put("flag", true);
		} else {
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			String challenge = null;
			switch (dayOfWeek) {
			case 1:
				challenge = "셀카";
				break;
			case 2:
				challenge = "동물";
				break;
			case 3:
				challenge = "새";
				break;
			case 4:
				challenge = "나무";
				break;
			case 5:
				challenge = "벤치";
				break;
			case 6:
				challenge = "케이크";
				break;
			case 7:
				challenge = "음료";
				break;
			}
			response.put("challenge", challenge);
		}
		return response;
	}

	// 일일 데이터 조회(하루)
	@GetMapping("/api/chatbot/dailylog")
	public Map<String, Object> selectDailyLog(@AuthenticationPrincipal PrincipalDetails principalDetails)
			throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = simpleDateFormat.format(calendar.getTime());
		DailyLogDTO dailyLogDTO = new DailyLogDTO(0, memberId, datetime, 0, null);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("flag", false);
		dailyLogDTO = chatBotService.selectDailyLog(dailyLogDTO);
		if (dailyLogDTO != null) {
			response.put("flag", true);
		}
		return response;
	}

	// 기분 상태 조회(접속일시 이후)
	@GetMapping("/api/chatbot/moodstate")
	public Map<String, Object> selectMoodState(@AuthenticationPrincipal PrincipalDetails principalDetails)
			throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		String datetime = login.getDatetime();
		MoodStateDTO moodStateDTO = new MoodStateDTO(0, memberId, datetime, 0);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("flag", false);
		moodStateDTO = chatBotService.selectMoodState(moodStateDTO);
		if (moodStateDTO != null) {
			response.put("flag", true);
		}
		return response;
	}

	// 챌린지 저장
	@PostMapping("/api/chatbot/challenge")
	public Map<String, Object> insertChallenge(@AuthenticationPrincipal PrincipalDetails principalDetails,
			HttpServletRequest request, ChallengeDTO challengeDTO,
			@RequestParam("multipartFile") MultipartFile multipartFile) throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = simpleDateFormat.format(calendar.getTime());
		multipartFile.transferTo(new File(uploadPath + "challenge/", memberId + "_" + datetime + ".png"));
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("flag", false);
		/* ----------이미지 분석---------- */
		boolean result = getResult();
		/* ----------------------------------- */
		if (result) {
			challengeDTO.setMemberId(memberId);
			challengeDTO.setDatetime(datetime);
			int cnt = chatBotService.insertChallenge(challengeDTO);
			if (cnt == 1) {
				login.setPoint(login.getPoint() + 5);
				response.put("flag", true);
			}
		}
		return response;
	}

	// 일일 데이터 저장
	@PostMapping("/api/chatbot/dailylog")
	public Map<String, Object> insertDailyLog(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody DailyLogDTO dailyLogDTO) throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = simpleDateFormat.format(calendar.getTime());
		dailyLogDTO.setMemberId(memberId);
		dailyLogDTO.setDatetime(datetime);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("flag", false);
		int cnt = chatBotService.insertDailyLog(dailyLogDTO);
		if (cnt == 1) {
			response.put("flag", true);
		}
		return response;
	}

	// 기분 상태 저장
	@PostMapping("/api/chatbot/moodstate")
	public Map<String, Object> insertMoodState(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody MoodStateDTO moodStateDTO) throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		moodStateDTO.setMemberId(memberId);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("flag", false);
		int cnt = chatBotService.insertMoodState(moodStateDTO);
		if (cnt == 1) {
			response.put("flag", true);
		}
		return response;
	}

	private boolean getResult() {
		return true;
	}

}
