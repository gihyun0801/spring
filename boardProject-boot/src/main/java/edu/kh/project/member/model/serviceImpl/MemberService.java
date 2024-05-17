package edu.kh.project.member.model.serviceImpl;

import java.util.List;

import edu.kh.project.member.model.dto.Member;

public interface MemberService {

	Member login(Member inputMember);

	int checkEmail(String memberEmail);

	int signup(Member inputMember, String[] member);

	Member fastLogin(String memberEmail);

	Member quickLogin(String memberEmail);

	List<Member> memberList();

	int resetPw(int memberNo);

	int resetDelFl(int memberNo);

}
