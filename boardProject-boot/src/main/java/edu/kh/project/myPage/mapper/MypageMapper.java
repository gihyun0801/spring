package edu.kh.project.myPage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

@Mapper
public interface MypageMapper {

	int updateInfo(Member inputMember);

	String selectPw(int memberNo);

	int changePw(Map<String, Object> paramMap);

	int deleteConfirm(int memberNo);

	int insertUploadFile(UploadFile uf);

	List<UploadFile> fileList();

	int profile(Member mem);

}
