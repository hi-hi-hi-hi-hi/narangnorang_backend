package com.narangnorang.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.narangnorang.dto.ChallengeDTO;
import com.narangnorang.dto.DailyLogDTO;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.MoodStateDTO;
import com.narangnorang.service.Norang2Service;

@RestController
public class Norang2Controller {

	@Autowired
	Norang2Service norang2Service;

	// 챌린지 조회(하루)
	@GetMapping("/norang2/challenge")
	public ChallengeDTO selectChallenge(HttpSession session) throws Exception {
		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		int memberId = memberDTO.getId();

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = simpleDateFormat.format(calendar.getTime());

		ChallengeDTO challengeDTO = new ChallengeDTO(0, memberId, datetime, null);
		challengeDTO = norang2Service.selectChallenge(challengeDTO);
		return challengeDTO;
	}

	// 일일 데이터 조회(하루)
	@GetMapping("/norang2/dailylog")
	public DailyLogDTO selectDailyLog(HttpSession session) throws Exception {
		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		int memberId = memberDTO.getId();

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = simpleDateFormat.format(calendar.getTime());

		DailyLogDTO dailyLogDTO = new DailyLogDTO(0, memberId, datetime, 0, null);
		dailyLogDTO = norang2Service.selectDailyLog(dailyLogDTO);
		return dailyLogDTO;
	}

	// 기분 상태 조회(접속일시 이후)
	@GetMapping("/norang2/moodstate")
	public MoodStateDTO selectMoodState(HttpSession session) throws Exception {
		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		int memberId = memberDTO.getId();
		String datetime = memberDTO.getDatetime();

		MoodStateDTO moodStateDTO = new MoodStateDTO(0, memberId, datetime, 0);
		moodStateDTO = norang2Service.selectMoodState(moodStateDTO);
		return moodStateDTO;
	}

	// 챌린지 저장
	@PostMapping("/norang2/challenge")
	public Map<String, Boolean> insertChallenge(HttpSession session, HttpServletRequest request,
			ChallengeDTO challengeDTO, @RequestParam("multipartFile") MultipartFile multipartFile) throws Exception {
		Map<String, Boolean> response = new HashMap<String, Boolean>();
		response.put("flag", false);

		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		int memberId = memberDTO.getId();

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = simpleDateFormat.format(calendar.getTime());

		String uploadPath = request.getSession().getServletContext().getRealPath("/")
				.concat("resources\\images\\challenge");

		multipartFile.transferTo(new File(uploadPath, memberId + "_" + datetime + ".png"));

		challengeDTO.setMemberId(memberId);
		challengeDTO.setDatetime(datetime);

		int flag = norang2Service.insertChallenge(challengeDTO);
		if (flag == 1) {
			response.put("flag", true);
		}
		return response;
	}

	// 일일 데이터 저장
	@PostMapping("/norang2/dailylog")
	public Map<String, Boolean> insertDailyLog(HttpSession session, DailyLogDTO dailyLogDTO) throws Exception {
		Map<String, Boolean> response = new HashMap<String, Boolean>();
		response.put("flag", false);

		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		int memberId = memberDTO.getId();

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = simpleDateFormat.format(calendar.getTime());

		dailyLogDTO.setMemberId(memberId);
		dailyLogDTO.setDatetime(datetime);

		int flag = norang2Service.insertDailyLog(dailyLogDTO);
		if (flag == 1) {
			response.put("flag", true);
		}
		return response;
	}

	// 기분 상태 저장
	@PostMapping("/norang2/moodstate")
	public Map<String, Boolean> insertMoodState(HttpSession session, MoodStateDTO moodStateDTO) throws Exception {
		Map<String, Boolean> response = new HashMap<String, Boolean>();
		response.put("flag", false);

		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		int memberId = memberDTO.getId();

		moodStateDTO.setMemberId(memberId);

		int flag = norang2Service.insertMoodState(moodStateDTO);
		if (flag == 1) {
			response.put("flag", true);
		}
		return response;
	}

	// 에러 처리
	@ExceptionHandler({ Exception.class })
	public String error() throws Exception {
		return "common/error";
	}

}