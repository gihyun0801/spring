package edu.kh.project.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.serviceImpl.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
	@RequestMapping("login")
	public String memberLogin(
			Member inputMember,
			RedirectAttributes ra,
			Model model,
			@RequestParam(value="saveId", required=false) String saveId,
			
			HttpServletResponse resp
			) {
		// 체크박스
		// - 체크가 된 경우 : "on" 
		// - 체크가 안된 경우 null 
		
		System.out.println("inputMember : " + inputMember);
		
		
		Member loginMember = service.login(inputMember);
		
		String message = "";
		
		System.out.println("로그인맴버 : " + loginMember);
		
		if(loginMember == null) {
			message = "아이디 또는 비밀번호가 일치하지 않습니다";
			ra.addFlashAttribute("message" , message);
		}
		
		// 로그인 성공 시 
		if(loginMember != null) {
			//Session. scope 에 loginMember 추가
			model.addAttribute("loginMember", loginMember);
			// 1단계 : request scope 에 세팅됨
			
			
			// 2단계 클래스위에 @Session attribute 추가 
			
			// **********************************************
			
			// 아이디 저장 (Cookie 사용)
			
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			//saveId=user01@kh.or.kr
			
			// 클라이언트가 어떤 요청을 할 때 쿠키가 첨부될지 지정
			
			// ex) "/" setPath 에다가 최상위 주소를 작성하면 Ip 또는 도메인 또는 localhost 라는 뜻이다
			//  		뒤에 "/" --> 메인 페이지 + 그 하위 주소 모두
			//  / 이렇게 작성햇으니 최상위 주소 하고 그 하위 모든 주소에서 쿠키가 적용됨
			cookie.setPath("/");
			
			
			if(saveId != null) { //아이디 저장 체크 시
				cookie.setMaxAge(60 * 60 * 24 * 30); // 30일 (초 단위로 지정)
				
			}else {
				cookie.setMaxAge(0);
			}
			
			
			resp.addCookie(cookie);
			
		}
		
		
		
		
		return "redirect:/"; //메인페이지 재요청
		
	}
	
	
	
	/** 로그아웃  
	 * session에 저장된 로그인된 회원 정보를 없앰(만료,무효화)
	 * @return
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		status.setComplete();
		
		return "redirect:/"; //메인 페이지 리다이렉트
	}
	
	
	/** 회원가입페이지
	 * @return
	 */
	@GetMapping("signup")
	public String signUp() {
		
		return "member/signup";
		
	}
	
	@GetMapping("checkEmail")
	@ResponseBody
	public int checkEmail(
			@RequestParam("memberEmail") String memberEmail
			) {
		
		return service.checkEmail(memberEmail);
		
	}
	
	/** 회원 가입 입력된 회원정보 (memberEmail, memberPw, memberNickname, memberTel, memberAddress(따로받아서 처리) )
	 * @param memberAddress : 입력한 주소 input 3개의 값을 배열로 전달 [우편번호, 도로명/지번주소, 상세주소]
	 * @param member
	 * @param ra : 리다이렉트 시 request scope 로 데이터 전달하는 객체
	 * @return
	 */
	@PostMapping("signup")
	public String signup(
			Member inputMember,
			@RequestParam(value="memberAddress", required=false) String[] member
			, RedirectAttributes ra
			) {
		
		 // 회원 가입 서비스 호출
		
		int result = service.signup(inputMember, member);
		   String path = null;
	        String message = null;
	        
	        if(result > 0) {
	        	
	        	message = inputMember.getMemberNickname() + "님의 가입을 환영합니다";
	        	path = "/";
	        	ra.addFlashAttribute("message", message);
	        	
	        }else {
	        	message = "회원 가입 실패..";
	        	path = "/signup";
	        	ra.addFlashAttribute("message", message);
	        }
		return "redirect:" + path;
		
		
	}
	
	@GetMapping("quickLogin")
	public String quick(
			Model model,
			@RequestParam("memberEmail") String memberEmail
			,RedirectAttributes ra
			) {
		
	    Member loginMember = service.quickLogin(memberEmail);
	    
	    String message = null;
	   
	    
	    if(loginMember == null) {
	    	message = "로그인 실패";
	    	ra.addFlashAttribute("message", message);
	    }else {
	    	model.addAttribute("loginMember", loginMember);
	    }
		
		return "redirect:/";
		
	}
	
	@GetMapping("memberList")
	@ResponseBody
	public List<Member> memberlist() {
		
		
		return service.memberList();
		
	}
	
	@GetMapping("resetPw")
	public String resetPw(
			@RequestParam("memberNo") int memberNo
			) {
		
		int result = service.resetPw(memberNo);
		
		
		
		
		
		return "redirect:/";
		
	}
	
	@GetMapping("resetDelFl")
	public String resetDelFl(
			
			@RequestParam("memberNo") int memberNo
			
			) {
		
		
		int result = service.resetDelFl(memberNo);
		
		return "redirect:/";
	}
	
	
	 
	
}
