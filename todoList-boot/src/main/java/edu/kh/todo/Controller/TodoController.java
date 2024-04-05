package edu.kh.todo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todo.model.dto.Todo;
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
	 
	  
	  //상세 조회
	  @GetMapping("detail")
	  public String todoDetail(
			  @RequestParam("todoNo") int todoNo,
			  Model model,
			  RedirectAttributes ra
			  ) {
		  
		  Todo todo = service.todoDetail(todoNo);
		   
		  String path = null;
		  
		  if(todo != null) 
		  { // 조회된 결과가 1행이라도 있을때 
    					path = "todo/detail";
    					
    					model.addAttribute("todo", todo);
		  } else { //조회된 결과가 1행이라도 없을때
			     path = "redirect:/"; // 메인 페이지로 리다이렉트
			     
			     ra.addFlashAttribute("message", "조회된 결과가 없습니다");
			     
			     //redirect attributes : 
			     // 리다이렉트시 데이터를 requestScope -> (잠시) session scope 로
			     // 전달할 수 있는 객체 (응 답 후 request scope 로 복귀)
		  }
		  
		  
		  return path;
		  
		  
	  }
	  
	  
	  /** 완료여부 변경
	   *  todoNo, complete 두 필드가 세팅된 상태
	 * @param todoNo
	 * @param complete
	 * @return
	 */
	@RequestMapping("changeComplete")
	  public String todoComplete(
			  
			  @RequestParam("todoNo") int todoNo,
			  @RequestParam("complete") String complete,
			  RedirectAttributes ra
			  
			  ) {
		  // 변경 서비스 호출
		
		int result = service.changeComplete(todoNo, complete);
		  
		//변경 성공 시 : "변경 성공"
		//변경 실패 시 : "변경 실패"
		
		String message = null;
		
		if(result > 0) message = "변경 성공";
		else           message = "변경 실패";
		
		ra.addFlashAttribute("message", message);
		
		//현재 요청 주소 : /todo/changeComplete
		//응답 주소 : /todo/detail
		  
		  return "redirect:detail?todoNo=" + todoNo; //상대경로
		  
		  
		  
	  }
	
	@GetMapping("update")
	public String todoUpdate(
			@RequestParam("todoNo") int todoNo,
			Model model
			) {
		
		//상세 조회 서비스 호출 -> 수정화면에 출력할 이전 내용
		Todo todo = service.todoDetail(todoNo);
		
		model.addAttribute(todo);
		
		return "todo/update";
		
	}
	
	/** 할 일 수정
	 * @param todoTitle
	 * @param todoContent
	 * @param todoNo
	 * @param ra
	 * @return
	 */
	@PostMapping("update")
	public String todoUpdate2(
			@RequestParam("todoTitle") String todoTitle,
			@RequestParam("todoContent") String todoContent,
			@RequestParam("todoNo") int todoNo,
			RedirectAttributes ra
			
			) {
		
		int result = service.update(todoNo, todoTitle, todoContent);
		
		String message = null;
		String path = "redirect:";
		
		if(result > 0) {
			path += "/todo/detail?todoNo=" + todoNo;
			message = "수정 성공!!!";
			
			
			
			
		}
		else   		{
			
			path+= "/todo/update?todoNo=" + todoNo;
			message = "수정 실패";
			
			
		}
		
		
		
		
		
		ra.addAttribute("message", message);
		
		return path;
		
		
		
		
		 
		
	}
	
	
	
	
	/** 삭제 성공 시 메인 페이지로 돌아감
	 * @param todoNo
	 * @param ra
	 * @return
	 */
	@GetMapping("delete")
	public String delete(
			@RequestParam("todoNo") int todoNo,
			RedirectAttributes ra
			) {
		
		int result = service.delete(todoNo);
		
		String message = null;
		String path = "";
		
		if(result > 0) {
			message = "삭제 성공";
			path += "/";
		}else {
			message = "삭제 실패";
			path += "/todo/detail?todoNo=" + todoNo;
		}
		
		ra.addFlashAttribute("message", message);
		return "redirect:" + path;
		
	}
}
