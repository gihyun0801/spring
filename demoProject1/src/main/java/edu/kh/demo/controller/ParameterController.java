package edu.kh.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.demo.model.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("param")
@Controller
@Slf4j
public class ParameterController {

	
	@RequestMapping("main") //param/main Get 방식 요청 매핑
	public String paramMain() {
		
		
		 //접두사 : classpath :/templates/
		// 접미사 : /html
		// -> src/main/resources/templates/param/param-main.html
		
		
		return "param/param-main";
	}
	
	/*
	 * 1.HttpServletRequest.getParameter("key")이용
	 * - 요청 클라이언트 정보, 제출된 파라미터 등을 저장한 객체
	 * - 클라이언트 요청 시 생성
	 * 
	 * 
	 * ArgumentResolver(전달 인자 해결사)
	 * 
	 * 
	 * */
	
	@PostMapping("test1")
	public String parameter1(HttpServletRequest req) {
		
		//1 . HttpServletRequest.getParameter("key") 이용
		
		String inputName = req.getParameter("inputName");
		int inputAge = Integer.parseInt(req.getParameter("inputAge"));
		String inputAddress = req.getParameter("inputAddress");
		
		// debug : 코드 오류 해결 
		// -> 코드 오류 없는데 정상 수행 안될 때 
		// -> 값이 잘못된 경우 -> 값 추적
		
		
		/*
		 * Spring 에서 Redirect(재요청)
		 * 
		 * -Controller 메서드 반환 값에
		 * "redirect : 요청 주소"; 작성 
		 * */
		
		log.debug("inputName : " + inputName);
		log.debug("inputAge : " + inputAge);
		log.debug("inputAddress : " + inputAddress);
		
		
		return "redirect:/param/main"; 
	}
	
	/*
	 *  2. @RequestParam : 어노테이션을 이용 - 낱개 파라미터 얻어오기
	 * 
	 *  - request 객체를 이용한 파라미터 전달 어노테이션
	 *  - 매개변수 앞에 해당 어노테이션을 작성하면, 매개변수에 값이 주입됨.
	 *  - 주입되는 데이터는 매개변수의 타입에 맞게 형변환/파싱이 자동으로 수행됨
	 *  
	 *  [기본 작성법]
	 *  
	 *  @RequestParam("key") 자료형 매개변수명
	 *  
	 *  [속성 추가 작성법]
	 *  @RequestParam(value="name", required="false", defaultValue="1")
	 *  
	 *  
	 *  value : 전달받은 input 태그의 name 속성값 
	 *  
	 *  required : 입력된 name 속성값 파라미터 필수 여부 지정(기본값 : true)
	 * 
	 * */
	
	@PostMapping("test2")
	public String parameter2(@RequestParam("title") String title, 
			                  @RequestParam("writer") String writer,
			                   @RequestParam("price") int price,
			                   @RequestParam(value="publisher", required=false, defaultValue="1") String publisher) {
		log.debug("title : " + title);
		log.debug("writer : " + writer);
		log.debug("price : " + price);
		log.debug("publisher : " + publisher);
		
		
		return "redirect:/param/main";
	}
	
	/*
	 * 3. @RequestParam 여러 개 파라미터
	 * 
	 * 
	 * String[]
	 * List<자료형>
	 * Map<String, Object>
	 * 
	 * required 속성은 사용 가능하나,
	 * defaultValue 속성은 사용 불가
	 * */
	
	
	@PostMapping("test3")
	public String paramTest3(@RequestParam(value="color", required=false) String[] colorArr,
			@RequestParam(value="fruit", required=false) List<String> fruitList,
			@RequestParam Map<String,Object> paramMap
			) {
		
		
		log.debug("colorArr : " + Arrays.toString(colorArr));
		log.debug("fruitList : " + fruitList);
		log.debug("paramMap: " + paramMap);
		
		
		return "redirect:/param/main";
	}
	/*
	* 4. @ModelAttribute을 이용한 파라미터 얻어오기
	 * 
	 * -DTO(또는 VO)와 같이 사용하는 어노테이션
	 * 
	 * 
	 * 전달 받은 파라미터의** name 속성 값이 **
	 * 
	 * 값이 사용되는** DTO의 필드명과 같으면**
	 * 
	 * 자동으로 setter를 호출해서 필드에 값을 세팅
	 * 
    * @ModelAttribute를 이용해 값이 필들에 세팅된 객체를
    * *커맨드 객체* 라고 부른다 ***
 
    @ModelAttribute 사용 시 주의사항 ***
    // DTO 에 기본생성자, setter 가 필수로 존재해야 한다!
	 * */
 
   /* @ModelAttribute 어노테이션은 생략가능 */
	
	
	@PostMapping("test4")
	public String paramTest4(@ModelAttribute MemberDTO inputMember) {
		
          
		 log.debug("inputMember : " + inputMember.toString());
		
		return "redirect:/param/main";
	}
	
	
	
	
	
	
	
}
