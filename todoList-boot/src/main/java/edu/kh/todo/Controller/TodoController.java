package edu.kh.todo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todo.model.service.TodoService;

@RequestMapping("todo")
@Controller
public class TodoController {
   
	@Autowired
	private TodoService service;
	
	  @RequestMapping("add")
	  public String addTodo(
			  
			  @RequestParam(value = "todoTitle", required=false, defaultValue="1") String todoTitle,
			  @RequestParam(value = "todoContent", required=false, defaultValue="1") String todoContent
		 , RedirectAttributes ra
			  ) {
		  
		 int result = service.addTodo(todoTitle, todoContent);
		  
		 String message = null;
		 
		 if(result > 0) {
			message = "할 일 추가 성공";
		 }else {
			 message = "할 일 추가 실패";
		 }
		 ra.addFlashAttribute("message", message);
		 
		 //삽입 결과에 따라 message 값 지정
		 
		 //RedirectAttributes.addFlashAttribute() 를 이용하면 
		 //잠깐 리다이렉트하는순간만 sessionScope로 변한다
		 
		 //[원리]
		 // 응답 전 : request Scope
		 // redirect 한다고 했을때 갑자기 session Scope 로 변한다
		 // 응답 후 : request Scope 로 다시 복귀한다
		 
		 // 리다이렉트 후 1회성으로 사용할 데이터를 속성으로 추가
		  
		  return "redirect:/";
		  
	  }
	 
}
