package edu.kh.todo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {
  
	  @Autowired
	  private TodoService service;
	  //Service 메서드 호출 후 결과 반환 받기
	  
	  
	   
	
	 @RequestMapping("/")
	public String mainPage(Model model) {
		 
		 //주입 확인
		 
		 log.debug("service : " + service);
		 
		 Map<String, Object> map = service.selectAll();
		 
  
		 List<Todo> list = (List<Todo>)map.get("todoList") ;
		 
		 int count = (int)map.get("completeCount");
		 
		 model.addAttribute("todoList",list);
		 model.addAttribute("completeCount",count);
		  
		 
		
		 return "common/main";
		 
	}
	
}
