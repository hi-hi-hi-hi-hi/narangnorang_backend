package com.narangnorang.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.narangnorang.dto.MemberDTO;
import com.narangnorang.service.CounselService;

@RestController
public class CounselController {
	
	@Autowired
	CounselService counselService;
	
	@GetMapping("/api/counsel/list")
	public Map<String, Object> selectCounselorList() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("counselorList", counselService.selectCounselorList());
		
		return result;
	}
	
	@GetMapping("/api/counsel/userRegion")
	public Map<String, Object> getUserRegion(HttpSession session) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		
		result.put("userRegion", memberDTO.getRegion());
		
		return result;
	}
}