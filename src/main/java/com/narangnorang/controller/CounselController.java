package com.narangnorang.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.narangnorang.dto.MemberDTO;
import com.narangnorang.service.CounselService;

@Controller
public class CounselController {
	
	@Autowired
	CounselService counselService;
	
	@ResponseBody
	@GetMapping("/api/counsel/list")
	public HashMap<String, Object> selectCounselorList() throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		result.put("counselorDTO", counselService.selectCounselorList());
		return result;
	}
	
	@GetMapping("/counsel/centerMap")
	public ModelAndView getUserRegion(HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();
		MemberDTO memberDTO = (MemberDTO) session.getAttribute("login");
		if(memberDTO != null) {
			mav.setViewName("counsel");
			mav.addObject("userRegion", memberDTO.getRegion());
		} else {
			mav.setViewName("common/sessionInvalidate");
		}
		return mav;
	}
}