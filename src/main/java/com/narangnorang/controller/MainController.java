package com.narangnorang.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.narangnorang.dto.MemberDTO;

@Controller
public class MainController {

	// 메인 (로그인 X)
	@GetMapping("/main")
	public String main() throws Exception {
		return "index";
	}

	// 세션 유지 테스트
	@PostMapping("/api/home")
	@ResponseBody
	public MemberDTO test(HttpSession session) throws Exception {
		return (MemberDTO) session.getAttribute("login");
	}

}
