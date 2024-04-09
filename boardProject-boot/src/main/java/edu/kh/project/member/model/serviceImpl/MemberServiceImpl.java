package edu.kh.project.member.model.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		
		if(  !bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())        ) {
			return null;
		}
		// 로그인 결과에서 비밀번호 제거
		
		loginMember.setMemberPw(null);
		
		return loginMember;
	}
	
}
