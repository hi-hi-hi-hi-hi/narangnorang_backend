package com.narangnorang.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.narangnorang.config.auth.PrincipalDetails;
import com.narangnorang.dto.ChallengeDTO;
import com.narangnorang.dto.DailyLogDTO;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.MoodStateDTO;
import com.narangnorang.service.MyNorangService;

@RestController
public class MyNorangController {

	@Autowired
	private MyNorangService myNorangService;

	// 일일 데이터 조회(한달)
	@GetMapping("/api/mynorang/dailylog")
	public Map<String, Object> selectDailyLogList(@AuthenticationPrincipal PrincipalDetails principalDetails,
			Integer year, Integer month) throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		Calendar calendar = Calendar.getInstance();
		if (year == null) {
			year = calendar.get(Calendar.YEAR);
		}
		if (month == null) {
			month = calendar.get(Calendar.MONTH) + 1;
		}
		calendar.set(year, month - 1, 1);
		int start = calendar.get(Calendar.DAY_OF_WEEK);
		int end = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		String datetime = simpleDateFormat.format(calendar.getTime());
		DailyLogDTO dailyLogDTO = new DailyLogDTO(0, memberId, datetime, 0, null);
		List<DailyLogDTO> dailyLogList = myNorangService.selectDailyLogList(dailyLogDTO);
		/* ----------코드 개선 필요함---------- */
		List<DailyLogDTO> dailyLogCalendar = new ArrayList<DailyLogDTO>();
		for (int i = 0; i < end; i++) {
			dailyLogCalendar.add(null);
		}
		Iterator<DailyLogDTO> iterator = dailyLogList.iterator();
		while (iterator.hasNext()) {
			DailyLogDTO element = iterator.next();
			int index = Integer.parseInt(element.getDatetime().substring(8, 10)) - 1;
			dailyLogCalendar.set(index, element);
		}
		/* ----------------------------------- */
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("year", year);
		response.put("month", month);
		response.put("start", start);
		response.put("end", end);
		response.put("dailyLogCalendar", dailyLogCalendar);
		return response;
	}

	// 일일 데이터 조회(하루)
	@GetMapping("/api/mynorang/dailylog/{datetime}")
	public Map<String, Object> selectDailyLog(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PathVariable String datetime) throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		DailyLogDTO dailyLogDTO = new DailyLogDTO(0, memberId, datetime, 0, null);
		dailyLogDTO = myNorangService.selectDailyLog(dailyLogDTO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("datetime", datetime);
		response.put("dailyLogDTO", dailyLogDTO);
		return response;
	}

	// 일일 데이터 저장
	@PostMapping("/api/mynorang/dailylog")
	public Map<String, Object> insertDailyLog(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody DailyLogDTO dailyLogDTO) throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		dailyLogDTO.setMemberId(memberId);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("flag", false);
		int cnt = myNorangService.insertDailyLog(dailyLogDTO);
		if (cnt == 1) {
			response.put("flag", true);
		}
		return response;
	}

	// 일일 데이터 수정
	@PutMapping("/api/mynorang/dailylog/{datetime}")
	public Map<String, Object> updateDailyLog(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PathVariable String datetime, @RequestBody DailyLogDTO dailyLogDTO) throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		dailyLogDTO.setMemberId(memberId);
		dailyLogDTO.setDatetime(datetime);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("flag", false);
		int cnt = myNorangService.updateDailyLog(dailyLogDTO);
		if (cnt == 1) {
			response.put("flag", true);
		}
		return response;
	}

	// 일일 데이터 삭제
	@DeleteMapping("/api/mynorang/dailylog/{datetime}")
	public Map<String, Object> deleteDailyLog(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PathVariable String datetime, @RequestBody DailyLogDTO dailyLogDTO) throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		dailyLogDTO.setMemberId(memberId);
		dailyLogDTO.setDatetime(datetime);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("flag", false);
		int cnt = myNorangService.deleteDailyLog(dailyLogDTO);
		if (cnt == 1) {
			response.put("flag", true);
		}
		return response;
	}

	// 기분 상태 조회(주간)
	@GetMapping("/api/mynorang/moodstate")
	public List<MoodStateDTO> selectMoodStateList(@AuthenticationPrincipal PrincipalDetails principalDetails,
			Integer year, Integer month, Integer date) throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		Calendar calendar = Calendar.getInstance();
		if (year == null) {
			year = calendar.get(Calendar.YEAR);
		}
		if (month == null) {
			month = calendar.get(Calendar.MONTH) + 1;
		}
		if (date == null) {
			date = calendar.get(Calendar.DATE);
		}
		calendar.set(year, month - 1, date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = simpleDateFormat.format(calendar.getTime());
		MoodStateDTO moodStateDTO = new MoodStateDTO(0, memberId, datetime, 0);
		List<MoodStateDTO> moodStateList = myNorangService.selectMoodStateList(moodStateDTO);
		return moodStateList;
	}

	// 챌린지 조회(전체)
	@GetMapping("/api/mynorang/challenge")
	public List<ChallengeDTO> selectChallengeList(@AuthenticationPrincipal PrincipalDetails principalDetails)
			throws Exception {
		MemberDTO login = principalDetails.getMemberDTO();
		int memberId = login.getId();
		List<ChallengeDTO> challengeList = myNorangService.selectChallengeList(memberId);
		return challengeList;
	}

}
