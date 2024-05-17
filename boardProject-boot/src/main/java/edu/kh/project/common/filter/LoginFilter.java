package edu.kh.project.common.filter;
//  요청 응답시 걸러내거나 추가할 수 있는 객체

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 1.filter 라는 Interface 를 상속받아야한다.
// 2.doFilter() 메서드 오버라이딩
public class LoginFilter implements Filter{
	
	@Override
	public void doFilter(
			ServletRequest request, 
			ServletResponse response, 
			FilterChain chain)
			throws IOException, 
			ServletException {
		
		//Servlet : HttpServletRequest의 부모 타입
		//ServletResponse : HttpServletResponse 부모 타입
		
		//HTTP 통신이 가능한 형태로 다운 캐스팅
		
		HttpServletRequest req = (HttpServletRequest)request;
		
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// Session 얻어오기
		
		HttpSession session = req.getSession();
		
		// 세션에서 로그인한 회원 정보를 얻어옴
		// 얻어왔으나, 없을 때 -> 로그인이 되어있지 않은 상태
		
		if(session.getAttribute("loginMember") == null) {
			
			// loginError 재요청
			// resp를 이용해서 원하는 곳으로 리다이렉트
			
			resp.sendRedirect("/loginError");
			
		}else {
				// 로그인이 되어있는 경우
			
				// FIlterChain
				// - 다음 필터 또는 Dispatcher Servlet 과 연결된 객체
			
				// 다음 필터로 요청/응답 객체 전달
				//(만약 없으면 Dispatcher Servlet 전달)
			
				chain.doFilter(request, response);
				
				
		}
		
				
	}
	
}
