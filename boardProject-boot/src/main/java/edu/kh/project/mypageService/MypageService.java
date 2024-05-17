package edu.kh.project.mypageService;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

public interface MypageService {

	int updateInfo(Member inputMember, String[] memberAddress);

	int changePw(Map<String, Object> paramMap, int memberNo);

	int deleteConfirm(String memberPw,int memberNo);

	String fileUpload1(MultipartFile uploadfile);

	int fileUpload2(int memberNo, MultipartFile uploadfile);

	List<UploadFile> fileList();

	int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo);

	int profile(MultipartFile profileImg, Member loginMember);
	
}
