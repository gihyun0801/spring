package edu.kh.project.email.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.member.model.serviceImpl.EmailService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("email")
@RequiredArgsConstructor //final 이 붙은 필드 혹은 @NotNull 이 붙은 필드에 자동으로 @AutoWired 가 적용됨 눈엔 보이지않음
public class EmailController {


	 private final EmailService service;
	
	/* @AutoWired 를 이용한 의존성 주입 방법은 3가지 존재
	 * 
	 * 1) 필드
	 * 2) setter
	 * 3) 생성자 (권장)
	 * 
	 * 
	 *  Lombok 라이브러리 에서 제공하는
	 *  
	 *  @RequiredArgsConstructor 를 이용하면
	 *  
	 *  필드 중
	 *  1) 초기화 되지 않은 final 이 붙은 필드
	 *  2) 초기화 되지 않은 @NotNull 이 붙은 필드
	 *  
	 *  1, 2 에 해당하는 필드에 대한
	 *  @AutoWired 생성자 구문을 자동 완성
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */
	// 첫번째 
	
	// 필드에 의존성 주입하는 방법 (권장 X)
	// @AutoWired // 의존성 주입(Di)
	// private EmailService service;
	
	// 두번째 
	
	// setter 이용
	
	//  @AutoWired
	// public void setService(){
	//   this.service = service;
	// }
	
	
	// 3) 생성자 (권장 방법)
	
	// private EmailService service;
	// private MemberService service2;
	
	// @AutoWired
	//  public EmailController(EmailService service, MemberService service2){
	//   this.service = service;
	 //  this.service2 = service2;
	// }
	 @ResponseBody
	 @PostMapping("signup")
	 public int signUp(
			 @RequestBody String email
			 ) {
		 
		 String authKey = service.sendEmail("signup", email);
		 
		 System.out.println(authKey);
		 
		 if(authKey != null) {
			 return 1; //인증번호가 반환되서 돌아옴
			           // == 이메일 보내기 성공
		 }
		 
		 return 0;
		 
		 
	 }
	 
	 // 이메일 인증번호 확인
	 
	 @ResponseBody
	 @PostMapping("checkAuthKey")
	 public int checkAuthKey(
			 
			 @RequestBody Map<String, String> map
			 
			 ) {
		 
		 // 입력 받은 이메일, 인증번호가 DB 에 있는지 조회
		 
		 int result =service.checkAuthKey(map);
		 
		 return result;
	 }
	 
	 @ResponseBody
	 @RequestMapping("checkNickName")
	 public int checkNickName(
			 
			 @RequestParam("memberNickname") String memberNickname
			 ) {
		 
		 int result = service.checkNickName(memberNickname);
		
		 
		 return result;
	 }
	
	
	
}


