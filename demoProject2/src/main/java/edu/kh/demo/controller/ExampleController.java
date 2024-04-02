package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
