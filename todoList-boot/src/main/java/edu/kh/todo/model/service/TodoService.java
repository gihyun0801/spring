package edu.kh.todo.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.todo.model.dto.Todo;

public interface TodoService {

	
	/** 할 일 목록 + 완료된 할 일 개수 조회
	 * @return map
	 */
	Map<String, Object> selectAll();

	/* * 할 일 추가
	 * @param todoTitle
	 * @param todoContent
	 * @return
	 */
	int addTodo(String todoTitle, String todoContent);

	Todo todoDetail(int todoNo);

	int changeComplete(int todoNo, String complete);

	int update(int todoNo, String todoTitle, String todoContent);

	int delete(int todoNo);

	int getTotalCount();

	int getCompleteCount22();

	int addTodoAjax(Todo todo);

	List<Todo> selectList();

	int deleteTodo(int todoNo);

	int updateTodo(Todo todo);


	
	
	
	
	
}
