package edu.kh.project.member.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {

	int updateAuthKey(Map<String, String> map);

	int insertAuthKey(Map<String, String> map);

	int checkAuthKey(Map<String, String> map);

	int checkNickName(String memberNickname);

}
