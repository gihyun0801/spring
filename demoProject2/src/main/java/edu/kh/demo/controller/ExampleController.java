package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.model.dto.Student.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("example")
@Controller
@Slf4j //lombok 라이브러리 객체 debug
public class ExampleController {

	
	@GetMapping("ex1")
	public String ex1(HttpServletRequest req, Model model) {
		
		req.setAttribute("test1", "HttpsServletRequest로 전달한 값");
		
		
		/*
		 * Model
		 * Spring 에서 데이터 전달 역할을 하는 객체
		 * 
		 * - org.springframework.ui 패키지
		 * 
		 * - 기본 scope : request
		 * 
		 * -@SessionAtrributes 와 함께 사용시 session Scope 로 범위가 변환
		 * 
		 *  [기본 사용법]
		 *  
		 *  Model.addAttribute("key","value");
		 * 
		 * */
		
		model.addAttribute("test2","Model로 이용한 전달한 값");
		
		//단일 값(숫자, 문자열) Model을 이용해서 html로 전달
		
		
		model.addAttribute("productName", "종이컵");
		model.addAttribute("price", 2000);
		
		//복수 값(배열 , List) Model을 이용해서 html로 전달
		List<String> fruitList = new ArrayList<>();
		fruitList.add("사과");
		fruitList.add("딸기");
		fruitList.add("바나나");
		
		model.addAttribute("fruitList",fruitList);		
		
		Student std = new Student();
		
		std.setStudentNo("12345");
		std.setName("홍길동");
		std.setAge(22);
		
		model.addAttribute("std", std);
		
		
		
		
		//List<Student> 객체 model 을 이용해서 html로 전달
		
		List<Student> stdList = new ArrayList<>();
		stdList.add(new Student("11111", "김일번",20));
		stdList.add(new Student("22222", "최이번",25));
		stdList.add(new Student("33333", "홍삼번",30));
		
		model.addAttribute("stdList",stdList);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return "example/ex1";
	}
	
	
	
	@PostMapping("ex2")
	public String ex2(Model model) {
		
		
		model.addAttribute("str", "<h1>테스트 중 &times;</h1>");
		
		
		return "example/ex2";
	}
	
	@GetMapping("ex3")
	public String ex3(Model model) {
		
		//Model = 데이터 전달용 객체 scope 범위는 request
		
		model.addAttribute("boardNo", 10);
		
		model.addAttribute("key", "제목");
		
		model.addAttribute("query","검색어");
		
		return "example/ex3";
		
	}
	
	
	/* @PathVariable 어노테이션 
	 * - 주소 중 일부분을 변수 값 처럼 사용
	 * + 해당 어노테이션으로 얻어온 값은 request Scope 에 세팅됨
	 *  
	 * 
	 * */
	
	@GetMapping("ex3/{number}")
	public String pathVariableTest(
			@PathVariable("number") int number 
			) {
		//주소 중 {number} 에 해당하는 부분의 값을 가져와 매개변수에 저장
		// + request scope에 세팅
		
		log.debug("number : " + number);
		
		return "example/testResult";
	}
	
	@GetMapping("ex4")
	public String ex4(Model model) {
		
		Student std = new Student("67890","잠만보",22);
		
		model.addAttribute("std" , std);
		
		model.addAttribute("num", 100);
		
		return "example/ex4";
		
	}
	
	@GetMapping("ex5")
	public String ex5(Model model) {
		
		// Model Spring 에서 값 전달 역할을 하는 객체 
		// 기본적으로 request scope + session 으로 확장 가능
	    Student std = new Student();
		
		std.setStudentNo("22222");
		
		model.addAttribute("message", "타임리프 + JavaScript 사용 연습");
		model.addAttribute("num1", 12345);
		model.addAttribute("std", std);
		
		
		
		
		return "example/ex5";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
