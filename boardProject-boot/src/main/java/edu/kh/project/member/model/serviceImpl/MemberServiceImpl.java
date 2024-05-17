package edu.kh.project.member.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class) //해당 클래스 메서드 종료시 까지
//예외 처리 발생하지 않으면 commit
//예외 발생하면 rollback
@Service // 비즈니스 로직 처리 역할 명시
@Slf4j
public class MemberServiceImpl implements MemberService{
 
	
	//등록된 bean 중에서 같은 타입 또는 상속관계인 bean을
	//자동으로 의존성 주입(DI)
	@Autowired
	private MemberMapper mapper;
	
	//BCrypt 암호화 객체 의존성 주입(SecurityConfig 참고)
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
 
	
	//로그인 
	@Override
	public Member login(Member inputMember) {
		
		//memberEmail : user01@kh.or.kr
		//memberPw : pass01!

		
		// 테스트
		
		// bcrypt.encode(문자열) : 문자열을 암호화하여 반환
		
//		String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		
//		log.debug("bcryptPassword" + bcryptPassword);
		
//		boolean result = bcrypt.matches(inputMember.getMemberPw(), bcryptPassword);
//		log.debug("result : " + result);
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원 조회
		
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		

		
		
		// 2. 만약에 일치하는 이메일이 없어서 조회 결과가 null 인 경우
		
		if(loginMember == null)  return null;
		
		// 3. 입력 받은 비밀번호(inputMember.getMemberPw() ) 
		// 암호화된 비밀번호 (loginMember.getMemberPw() ) 
		// 비교
		
		// 일치하지 않으면
		
//		if(  !bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())        ) {
//			return null;
//		}
		// 로그인 결과에서 비밀번호 제거
		
		loginMember.setMemberPw(null);
		
		return loginMember;
	}


	/**
	 * 이메일 중복검사 서비스
	 */
	@Override
	public int checkEmail(String memberEmail) {
		
		 int result = mapper.checkEmail(memberEmail);
		
		return result;
	}


	/**
	 * 회원가입 서비스
	 */
	@Override
	public int signup(Member inputMember, String[] member 
			) {
		
		
		// 주소가 입력되지 않으면 inputMember.getMemberAddress().equals(",,") 값으로 들어온다.
		
		// inputMembet.getMemgerARS
		//INPUTmEMBER.GETmEMBERaDDSS("
		
		// 주소가 입력된 경우
		
		if(!inputMember.getMemberAddress().equals(",,")) {
			
			// Sring.join("구분자", 배열_
			// 이렇게 작성하면 배열의 모든 요소 사이에 "구분자"를 추가하여
			// 하나의 문자열로 만드어 반환하는 메서드
			
			// 구분자로 ^^^ 쓴 이유
			// 주소, 상세주소에 없는 특수문자 작성
			// 나중에 다시 3분할 때 구분자로 이용할 예정
			
			String address  = String.join("^^^", member);
			
			System.out.println(address);
			//inputMember 로 합쳐진 주소를 세팅
			inputMember.setMemberAddress(address);
		}else { // 주소 입력
			inputMember.setMemberAddress(null);
		}
		
		// 이메일, 비밀번호 (pass02!), 닉네임 , 전화번호 주소
		
		//비밀번호를 암호화 하여 inputMember 에 세팅
		
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		
        inputMember.setMemberPw(encPw);
        
        int result = mapper.signup(inputMember);
        
     
		
		
		return result;
	}


	@Override
	public Member fastLogin(String memberEmail) {
		
		
		
		return mapper.fastLogin(memberEmail);
	}


	@Override
	public Member quickLogin(String memberEmail) {
		
		Member loginMember = mapper.quickLogin(memberEmail);
		
		
		if(loginMember == null) return null;
		
		loginMember.setMemberPw(null);
		
		return loginMember;
	}


	@Override
	public List<Member> memberList() {
		return mapper.memberList();
	}


	@Override
	public int resetPw(int memberNo) {
		
		
		String memberPw = bcrypt.encode("pass01!");
		
		Member member = new Member();
		member.setMemberNo(memberNo);
		member.setMemberPw(memberPw);
		
		int result = mapper.resetPw(member);
		
		return result;
	}


	@Override
	public int resetDelFl(int memberNo) {
		
		int result = mapper.resetDelFl(memberNo);
		
		return result;
	}
	
}
