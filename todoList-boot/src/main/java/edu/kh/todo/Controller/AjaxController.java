package edu.kh.todo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("ajax")
@Slf4j
public class AjaxController {
	
	@Autowired
	// - 등록된 Bean중 같은 타입 또는 상속관계인 Bean 을
	//  해당 필드에 의존성 주입(DI)
	private TodoService service;

	@GetMapping("main")
	public String AjaxMain() {
		
		
		return "ajax/main";
		
	}
	
	/** 전체 Todo 개수 조회
	 * forward / redirect
	 * @return
	 */
	@ResponseBody
	@GetMapping("totalCount")
	public int getTotalCount() {
			
		// 전체 할 일 개수 조회 서비스 호출 및 응답
		
		int totalCount = service.getTotalCount();
		
		return totalCount;
	}
	
	@ResponseBody
	@GetMapping("getCompleteCount")
	public int getCompleteCount() {
		
		int completeCount = service.getCompleteCount22();
		
		return completeCount;
		
	}
	
	//[HttpMessageConverter]
	//Spring 에서 비동기 통신 시
	//전달되는 데이터의 자료형
	//응답하는 데이터의 자료형
	//위 두가지를 알맞은 형태로 가공 해주는 객체
	
	// 문자열, 숫자 <-> Text
	// DTO <-> JSON
	// Map <-> JSON
	
	
	@PostMapping("addTodo")
	@ResponseBody
	public int addTodo(
			@RequestBody Todo todo // 요청 body 에 담긴 값을 Todo 에 저장
			) {
		
		System.out.println(todo);
		int result = service.addTodoAjax(todo);
		
		
		return result;
		
	}
	
	@GetMapping("selectList")
	@ResponseBody
	public List<Todo> selectList() {
		List<Todo> todo = service.selectList();
		
		// list 를 js 에선 못읽어서 json 으로 형태로 변환하여 반환
		// httpMessage 가 알아서 json 으로 바꿔주고 그걸 String 으로 변환해서 보내줌
	    
		return todo;
	}
	
	@ResponseBody
	@RequestMapping("detail")
	public Todo selectTodo(@RequestParam("todoNo") int todoNo) {
		
		
		return service.todoDetail(todoNo);
	}
	
	@ResponseBody
	@DeleteMapping("delete")
	public int deleteTodo(
			@RequestBody int todoNo) {
		
		
		int result = service.deleteTodo(todoNo);
		
		return result;
		
	}
	
	@ResponseBody
	@RequestMapping("updateTodo")
	public int updateTodo(
			@RequestBody Todo todo
			) {
		
        		int result = service.updateTodo(todo);
		
		return result;
	}
	

	
}
