package com.narangnorang.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narangnorang.config.auth.PrincipalDetails;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.dto.TestResultDTO;
import com.narangnorang.service.TestService;

@RestController
public class TestController {

	@Autowired
	private TestService testService;

	// 테스트 결과 저장
	@PostMapping("/api/test/{type}/{result}")
	public Map<String, Object> insert(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PathVariable("type") int type, @PathVariable("result") int result) throws Exception {
		MemberDTO login = (MemberDTO) principalDetails.getMemberDTO();
		int memberId = login.getId();
		TestResultDTO testResultDTO = new TestResultDTO(0, memberId, null, type, result);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("flag", false);
		int cnt = testService.insert(testResultDTO);
		if (cnt == 1) {
			response.put("flag", true);
		}
		return response;
	}

	// 지난 테스트 결과
	@GetMapping("/api/mynorang/testresult")
	public List<TestResultDTO> selectList(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		MemberDTO login = (MemberDTO) principalDetails.getMemberDTO();
		int memberId = login.getId();
		List<TestResultDTO> testResultList = testService.selectList(memberId);
		return testResultList;
	}

}
