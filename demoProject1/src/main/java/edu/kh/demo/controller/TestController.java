package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//Java 객체 : new 연산자에 의해 Heap 영역에
//          클래스에 작성된 내용대로 생성된 것

// instance : 개발자가 만들고 관리하는 객체

// Bean : Spring Container(Spring)이 만들고 관리하는 객체
@Controller // 요청/응답을 제어할 컨트롤러 역할 명시 + Bean으로 등록(== 객체로 생성해서 스프링이 관리)
public class TestController{
	
	// @RequestMapping("요청주소")
	// - 요청 주소를 처리할 메서드를 매핑하는 어노테이션
	
	// 1) 메서드에 작성 : 
	// - 요청주소와 메서드를 매핑
	// - GET/POST 가리지 않고 매핑 (속성을 통해서 지정 가능)
	
	// 2) 메서드 단 말고 클래스단에서 쓸라면
	
	// - 공통 주소를 매핑
	// ex) /todo/insert, /todo/select , /todo/update
	
	
	
/*  
 *  @RequestMapping("공통주소 = todo")
 *  @Controller
 *  public class TodoController{
 *  
 *  
 *  }
 *  
 *  @RequestMappng("나머지 주소 ex) insert")
 *  public String insert(){}
 *  
 *  @RequestMappng("나머지 주소 ex) update")
 *  public String update(){}
 *  
 *  @RequestMapping("나머지 주소 ex) select")
 *  public String select(){}
 * 
 * */
	
	@RequestMapping("test") //test 요청 시 처리할 메서드 매핑(Get/post) 가리지 않고
	public String testMethod() {
		
		System.out.println("/test 요청 받음");
		
		//Controller 단에서 반환형이 String 인 이유는
		// - 메서드에서 반환되는 문자열이
		// forward할 html 파일 경로가 되기 때문
		
		//src/main/resources/templates/ .html  여기 html 앞에다가 적는값은 
		// 저기로 응답을 보내준단 뜻이다
		// test를 적엇으니 저기에 test 가 들어가게된다
		
		//Thymeleaf : JSP 대신 사용하는 템플릿 엔진
		
		//classpath == src/main/resources
		// 접두사 : classpath:/templates/
		// 접미사 : .html  <= 확장자
		
		//src/main/resources/templates/test.html <- 이 경로로 응답을 보내주겠다.
		//forward 하고 똑같다 보면 된다
		return "test";
		
	}
	
	
	
	
}
