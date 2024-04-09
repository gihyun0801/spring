package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.serviceImpl.MemberService;
import lombok.extern.slf4j.Slf4j;

@SessionAttributes({"loginMember"})
@Controller
@Slf4j
@RequestMapping("member")
public class MemberController {
 
	@Autowired //의존성 주입
	private MemberService service;
	
	 
	/*
	 *  [로그인]
	 *  특정 사이트에 아이디/비밀번호 등을 입력해서
	 *  해당 정보가 있으면 조회/서비스 이용
	 *  
	 *  - 로그인 한 정보를 session 에 기록하여
	 *    로그아웃 또는 브라우저 종료 시 까지
	 *    해당 정보를 계속 이용할 수 있게 함 
	 * */
	
	/**
	 * 
	 *  로그인
	 * 
	 * @param inputMember : 커맨드객체 
	 * @param ra
	 * @param model
	 * @return
	 */
	@PostMapping("login")
	public String memberLogin(
			Member inputMember,
			RedirectAttributes ra,
			Model model) {
		
		
		Member loginMember = service.login(inputMember);
		
		String message = "";
		
		if(loginMember == null) {
			message = "아이디 또는 비밀번호가 일치하지 않습니다";
			ra.addFlashAttribute("message" , message);
		}
		
		if(loginMember != null) {
			//Session. scope 에 loginMember 추가
			model.addAttribute("loginMember", loginMember);
			// 1단계 : request scope 에 세팅됨
			
			
			// 2단계 클래스위에 @Session attribute 추가 
		}
		
		
		
		return "redirect:/";
		
	}
	 
	
}
