package edu.kh.project.member.model.serviceImpl;

import java.util.Map;

public interface EmailService {

	String sendEmail(String string, String email);

	int checkAuthKey(Map<String, String> map);

	int checkNickName(String memberNickname);

}
