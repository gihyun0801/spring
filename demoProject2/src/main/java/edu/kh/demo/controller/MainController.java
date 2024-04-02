package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller //IOC(제어의 역전) 요청/응답 제어 역할 명시 + Bean 등록
public class MainController {
       
	
	@RequestMapping("/")
	public String mainPage() {
		return "common/main";
	}
	
	
}
