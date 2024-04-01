package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//Bean : 스프링이 만들고 관리하는 객체



@Controller // 요청 / 응답 제어 역할인 Controller 임을 명시  + Bean 등록
public class ExampleController {
   
	 /*
	  *  요청 주소 매핑하는 방법
	  *  1) @RequestMapping("주소")
	  *  
	  *  2) @GetMapping("주소") : Get (조회) 방식 요청 매핑
	  * 
	  *     @PostMapping("주소") : Post (삽입) 방식 요청 매핑
	  *  
	  *     @PutMapping("주소") : Put (수정) 방식 요청 매핑
	  *   
	  *     @DeleteMappng("주소") : Delete (삭제) 방식 요청 매핑  
	  *     
	  *     //get, put, delete 는 역등하다 항상 결과값이 똑같다
	  *     //post 는 다르다 post를 이용해서 결제요청을 만들엇는데 post 메서드가 두 번 호출되서 중복된다
	  *     
	  *     
	  *     
	  *  
	  * */ 
	 
	 
	@GetMapping("example") // example Get 방식 요청 매핑
	public String exampleMethod(){
		return "example"; //forward 하려는 html 파일 경로 작성
	                      //단 ,ViewResolver 가 제공하는
		                  //Thymeleaf 접두사 , 접미사 제외하고 작성
		                  // 접두사 : classpath:/templates/
		                  //  접미사 : .html
		  
	}
	
}