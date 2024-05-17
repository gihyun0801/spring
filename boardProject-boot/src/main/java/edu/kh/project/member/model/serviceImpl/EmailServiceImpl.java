package edu.kh.project.member.model.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.kh.project.common.config.EmailConfig;
import edu.kh.project.member.model.mapper.EmailMapper;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

	private final EmailMapper mapper;
	
	
   //타임리프 (템플릿 엔진)을 이용해서 html 코드를 -> java 로 변환
	private final SpringTemplateEngine templateEngine;
	
	
	private final JavaMailSender mailSender;
	
 
	
	// 이메일 보내기 
	@Override
	public String sendEmail(String htmlName, String email
			) {
		
		 	// 6자리 난수(인증 코드) 생성
		   String authKey = createAuthKey();
		   
		   try {
			   
			   // 제목 
			   String subject = null;
			   
			   switch(htmlName) {
			   case "signup" : 
				    subject = "[boardProject] 회원 가입 인증번호 입니다.";
				    break;
			   }
			   
			   // 인증 메일 보내기
			   
			   //MimeMessage : Java 에서 메일을 보내는 객체
			   MimeMessage mimeMessage = mailSender.createMimeMessage();
			   
			   //MimeMessageHelper : 
			   //Spring 에서 제공하는 메일 발송 도우미(간단 + 타임리프)
			   MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			   // 1번 매개변수 : MimeMessage
			   // 2번 매개변수 : 파일 전송 사용 ? true / false
			   // 3번 매개변수 : 문자 인코딩 지정
			   
			   helper.setTo(email); //받는 사람 이메일 지정
			   helper.setSubject(subject); //이메일 제목 지정
			   
			   helper.setText(  loadHtml(authKey, htmlName), true  ); // 변경 -> html보낼거임(변경예정)
			   // true 가 뭐냐 HTML 코드 해석 여부를 물어보는거다 (innerHtml) 해석 여부
			   
			   // CID (Content-ID) 를 이용해 메일에 이미지 첨부
			   // logo 추가예정
			   // 파일 첨부와는 다르다, 이메일 내용 자체에 사용할 이미지 첨부
			   helper.addInline("logo", new ClassPathResource("static/images/logo.jpg"));
			   
			   mailSender.send(mimeMessage);
			   
		   }catch(Exception e) {
			   e.printStackTrace();
			   return null;
		   }
		  		
		   //이메일 + 인증 번호를 "TB_AUTH_KEY" 테이블 저장
		   Map<String, String> map = new HashMap<String, String>();
		   
		   map.put("authKey", authKey);
		   map.put("email", email);
		   
		   int result = mapper.updateAuthKey(map);
		   
		   // 2) 1번 update 실패 시 insert 시도
		   if(result == 0) {
			   result = mapper.insertAuthKey(map);
		   }
		 
		   //수정, 삽입 후에도 result 가 0 == 실패
		   if(result == 0) {
			   return null;
		   }
		
		   // 성공
		   return authKey;//오류없이 완료되면 authKey = 난수 반환
	}
	
	
	
	
	
	private String loadHtml(String authKey, String htmlName) {
		
		// org.thymeleaf.Context 선택
		
		Context context = new Context();
		
		// 타임리프가 적용된 HTML 사용할값 추가
		context.setVariable("authKey", authKey);
		
		//template/email 폴더에서 htmlName과 같은
		//~.html 파일 내용을 읽어와 string 으로 변환
		return templateEngine.process("email/" +  htmlName, context);
	}





	/** 인증번호 생성 (영어 대문자 + 소문자 + 숫자 6자리)
	    * @return authKey
	    */
	   public String createAuthKey() {
	   	String key = "";
	       for(int i=0 ; i< 6 ; i++) {
	          
	           int sel1 = (int)(Math.random() * 3); // 0:숫자 / 1,2:영어
	          
	           if(sel1 == 0) {
	              
	               int num = (int)(Math.random() * 10); // 0~9
	               key += num;
	              
	           }else {
	              
	               char ch = (char)(Math.random() * 26 + 65); // A~Z
	              
	               int sel2 = (int)(Math.random() * 2); // 0:소문자 / 1:대문자
	              
	               if(sel2 == 0) {
	                   ch = (char)(ch + ('a' - 'A')); // 대문자로 변경
	               }
	              
	               key += ch;
	           }
	          
	       }
	       return key;
	   }





	@Override
	public int checkAuthKey(Map<String, String> map) {
		
		int result = mapper.checkAuthKey(map);
		
	
		
		return  result;
	}





	@Override
	public int checkNickName(String memberNickname) {
		
		 int result = mapper.checkNickName(memberNickname);
		
		return result;
	}

	
	
	
	
	
	
}

/*
 * 
 *  Google SMTP 를 이용한 이메일 전송하기
 *  
 *  - SMTP(Simple Mail Transfer Protocal, 간단한 메일 전송 규약)
 *  --> 이메일 메시지를 보내고 받을 때 사용하는 약속(규약, 방법)
 *  
 *  
 *  - Google SMTP 
 *  
 *  Java Mail Sender -> Google SMTP -> 대상에게 이메일 전송
 *  
 *  - Java Mail Sender 에 Google SMTP 이용 설정 추가
 *  
 *  1) config.properties 내용 추가(계정, 앱비밀번호) 이게 있어야 java smtp 를 이용할 수 있음
 *  2) EmailConfig.java 
 * 
  * 
 * */
