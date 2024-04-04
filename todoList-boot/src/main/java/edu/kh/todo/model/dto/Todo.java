package edu.kh.todo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Todo {
   
	
	  private int todoNo; //번호
	  private String todoTitle; //제목
	  private String todoContent; //내용
	  private String complete; //완료여부
	  private String regDate; //등록날짜 String 변환 해서 가져옴
	  
	  
	
}
