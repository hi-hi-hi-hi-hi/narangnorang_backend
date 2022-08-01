package com.narangnorang.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narangnorang.config.auth.PrincipalDetails;
import com.narangnorang.service.CounselService;

@RestController
public class CounselController {

	@Autowired
	private CounselService counselService;

	@GetMapping("/api/counsel/list")
	public Map<String, Object> selectCounselorList() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		result.put("counselorList", counselService.selectCounselorList());

		return result;
	}

	@GetMapping("/api/counsel/userRegion")
	public Map<String, Object> getUserRegion(@AuthenticationPrincipal PrincipalDetails principalDetails)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		result.put("userRegion", principalDetails.getMemberDTO().getRegion());

		return result;
	}

}
