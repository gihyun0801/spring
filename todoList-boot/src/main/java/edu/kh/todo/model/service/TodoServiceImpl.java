package edu.kh.todo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.mapper.TodoMapper;

@Transactional(rollbackFor = Exception.class)
@Service // 비즈니스 로직(데이터 가공, 트랜잭션 처리) 역할 당시 + Bean 등록
public class TodoServiceImpl implements TodoService{
     
	   @Autowired
	   private TodoMapper mapper;

	// 할일 목록 + 완료된 할 일 개수 조회
	@Override
	public Map<String, Object> selectAll() {
		
		// 1. 할 일 목록 조회
		
		List<Todo> todoList = mapper.selectAll();
		
		
		// 2. 완료된 할 일 개수 조회
		
		
		int completeCount = mapper.getCompleteCount();
		
		
		// Map 으로 묶어서 반환
		
		
		Map<String , Object> map = new HashMap<String, Object>();
		
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);
		
		return map;
	}

	@Override
	public int addTodo(String todoTitle, String todoContent) {
		
		//Connection 생성 / 반환 x
		//트랜잭션 제어 처리를 해줘야 한다 commit / rollback 으로 
		
		
		//마이바티스에서 SQL에 전달할 수 있는 파라미터의 개수는 오직 1개 
		// 가지고온 title,content 를 Todo DTO로 묶어서 전달
		
		Todo todo = new Todo();
		
		todo.setTodoTitle(todoTitle);
		todo.setTodoContent(todoContent);
		
		int result = mapper.addTodo(todo);
		
		return result;
	}
	    
	  
	 
}
