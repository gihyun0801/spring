package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.model.dto.Student;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("practice")
@Controller
@Slf4j
public class SubController {
 
	 @PostMapping("ex1")
	public String ex1(@RequestParam(value="inputName", required=false, defaultValue="1") String name, 
			@RequestParam(value="inputAge", required=false, defaultValue="1") int age,
			@RequestParam(value="color", required=false, defaultValue="1") List<String> color)
	                 
	 
	 
	 {
		 
		 log.debug(name);
		 log.debug(String.valueOf(age));
		 log.debug(color.toString());
		 
		 
		 return "redirect:/";
	}
	 
	 @PostMapping("ex2")
	 public String ex2(Model model) {
		 
		 
		 
		 return "redirect:/";
	 }
	 
	 @RequestMapping("ex3")
	 public String ex3(Model model) {
		 
		 
		 model.addAttribute("str","안녕하세요");
		 List<Student> stdList = new ArrayList<Student>();
		 
		 Student student = new Student("12345","아아아아아");
		 
		 
		 stdList.add(new Student("11111", "성기현"));
		 stdList.add(new Student("22222", "이성범"));
		 stdList.add(new Student("33333", "이정재"));
		 
		 model.addAttribute("stdList", stdList);
		 model.addAttribute("student",student);
		 
		 
		 model.addAttribute("list", "<h1>이건</h1>");
		 
		 
		 return "new/main";
		 
		 
	 }
	 
	 
	
}
