package com.narangnorang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	// 메인 (로그인 X)
	@GetMapping("/")
	public String main() throws Exception {
		return "index.html";
	}

}
