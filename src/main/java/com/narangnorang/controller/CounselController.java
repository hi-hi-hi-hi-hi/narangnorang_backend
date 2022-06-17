package com.narangnorang.controller;

import java.util.HashMap;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.narangnorang.dto.MemberDTO;
import com.narangnorang.service.CounselService;

@RestController
public class CounselController {
	
	@Autowired
	CounselService counselService;
	
	@ResponseBody
	@GetMapping("/api/counsel/list")
	public HashMap<String, Object> selectCounselorList() throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		result.put("counselorList", counselService.selectCounselorList());
		return result;
	}
	
	@ResponseBody
	@GetMapping("/api/counsel/userRegion")
	public HashMap<String, Object> getUserRegion(HttpSession session) throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		result.put("userRegion", memberDTO.getRegion());
		
		return result;
	}
}