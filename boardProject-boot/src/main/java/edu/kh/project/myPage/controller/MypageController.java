package edu.kh.project.myPage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.core.net.SyslogOutputStream;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.mypageService.MypageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
@SessionAttributes({"loginMember"})
@Controller
@Slf4j
@RequestMapping("myPage")
@RequiredArgsConstructor
public class MypageController {
		private final MypageService service;
		
		/** 내 정보 조회 및 수정
		 * @param loginMember
		 * @return
		 */
		@GetMapping("info")
		public String myPageInfo(
				@SessionAttribute("loginMember") Member loginMember,
				Model model
			
				) {
		//주소	
		String memberAddress = loginMember.getMemberAddress();
		
		//주소가 있을 경우에만 동작
		
		if(memberAddress != null) {
			// 구분자 "^^^" 을 기준으로
			//memberAddress 값을 쪼개어 String[]로 반환
			// --> ["04540", "중구 남대문로120", "3층 E 강의장"]
			
			String [] arr = memberAddress.split("\\^\\^\\^");
			
			model.addAttribute("postcode", arr[0]);
			model.addAttribute("address", arr[1]);
			model.addAttribute("detailAddress", arr[2]);
		}
		
		
		
		log.debug("memberAddress :" + memberAddress);
			
			return "mypage/myPage-info";
		}
		
		
		/** 프로필 이미지 변경 화면 이동
		 * @return
		 */
		@GetMapping("profile")
		public String profile() {
			return "myPage/myPage-profile";
		}
		
		/** 비밀번호 변경 화면 이동
		 * @return
		 */
		@GetMapping("changePw")
		public String changePw() {
			return "myPage/myPage-changePw";
		}
		
		/** 회원탈퇴이동
		 * @return
		 */
		@GetMapping("secession")
		public String secession() {
			return "myPage/myPage-secession";
		}
		
		/** 제출된 회원 닉네임, 전화번호 주소(,,)
		 * @param inputMember 로그인한 회원정보(회원 번호 사용할 예정)
		 * @param loginMember
		 * @param memberAddress
		 * @param ra
		 * @return
		 */
		@PostMapping("info")
		public String updateInfo(
				Member inputMember, 
				@SessionAttribute("loginMember") Member loginMember,
				@RequestParam("memberAddress") String[] memberAddress,
				RedirectAttributes ra
				) {
			
			log.debug("loginMember: "+ loginMember);
			
			int memberNo = loginMember.getMemberNo();
			
			inputMember.setMemberNo(memberNo);
			
			log.debug("inputMember: "+ inputMember);
			
			int result = service.updateInfo(inputMember, memberAddress);
			
			String message = null;
			
			if(result > 0) {
				message = "수정 완료";
				ra.addFlashAttribute("message",message);
				
				//loginMember 는
				// 세션에 저장된 로그인한 회원 정보가 저장된 객체를 참조하고 있다
				
				// -> loginMember를 수정하면
				// 세션에 저장된 로그인한 회원 정보가 수정된다!
				
				// == 세션 데이터와 DB 데이터를 맞춤
				
				loginMember.setMemberNickname(inputMember.getMemberNickname());
				loginMember.setMemberTel(inputMember.getMemberTel());
				loginMember.setMemberAddress(inputMember.getMemberAddress());
			}else {
				message = "수정 실패";
				ra.addFlashAttribute("message",message);
			}
			
			
			
			
			
			return "redirect:info";
		}
		
		/** 비밀번호 변경
		 * @return
		 */
		@PostMapping("changePw")
		public String changePw(
				@RequestParam Map<String, Object> paramMap,
				@SessionAttribute("loginMember") Member loginMember,
				RedirectAttributes ra
				) {
			
			int memberNo = loginMember.getMemberNo();
			
			//현재 + 새 + 회원번호를 서비스로 전달
			
			int result = service.changePw(paramMap, memberNo);
			
			String message = null;
			String path = null;
			
			System.out.println(result);
			if(result > 0) {
				message = "비밀번호 변경 완료";
				ra.addFlashAttribute("message", message);
				
				path = "/myPage/info";
				
				loginMember.setMemberPw(message);
				
			}else {
				path = "/myPage/changePw";
				
				message = "비밀번호 변경 실패";
				
				ra.addFlashAttribute("message", message);
			}
			
			
			return "redirect:" + path;
		}
		
		
		/** 회원탈퇴
		 * @param memberPw
		 * @param loginMember
		 * @param status
		 * @param ra
		 * @return
		 */
		@PostMapping("secession")
		public String deleteConfirm(
				@RequestParam("memberPw") String memberPw,
				@SessionAttribute("loginMember") Member loginMember,
				SessionStatus status,
				RedirectAttributes ra
				) {
			
			int memberNo = loginMember.getMemberNo();
			int result = service.deleteConfirm(memberPw,memberNo);
			
			String message = null;
			String path = null;
			
			if(result > 0) {
				status.setComplete();
				message = "탈퇴 완료";
				path = "/";
				ra.addFlashAttribute("message", message);
			}else {
				message ="탈퇴 실패";
				ra.addFlashAttribute("message", message);
				path = "/myPage/secession";
			}
			
			System.out.println(result);
			
			return "redirect:" + path;
		}
		
		
		@GetMapping("fileTest")
		public String filePage() {
			
			return "myPage/myPage-fileTest";
			
		}
		
		/*
		 * Spring 에서 파일 업로드를 처리하는 방법
		 * -enctype = "multipart/form-data" 로 클라이언트 요청을 받으면
		 * (문자, 숫자, 파일 등이 섞여있는 요청)
		 * 
		 * 이를 MultipartResolver를 이용해서
		 * 섞여있는 파라미터를 분리
		 * 
		 * 문자열, 숫자 -> String
		 * 파일         -> MultipartFile
		 * 
		 * */
		
		@PostMapping("file/test1")
		public String fileUpload1(
				@RequestParam("uploadFile") MultipartFile uploadfile,
				RedirectAttributes ra
			
				) throws Exception{
			
			String path = service.fileUpload1(uploadfile);
			
			// 파일이 저장되어 웹에서 접근할 수 있는 경로가 반환되었을때
			
			if(path != null) {
				ra.addFlashAttribute("path", path);
			}
			
			return "redirect:/myPage/fileTest";
		}
		
		@PostMapping("file/test2")
		public String fileUpload2(
				@RequestParam("uploadFile") MultipartFile uploadfile,
				RedirectAttributes ra,
				@SessionAttribute("loginMember") Member loginMember
				) {
			
			
			//로그인한 회원의 번호 (누가 업로드 했는가)
			int memberNo = loginMember.getMemberNo();
			// 업로드된 파일 정보를 DB 에 INSERT 후 결과 행의 개수 반환 받을 예정
			
			int result = service.fileUpload2(memberNo, uploadfile);
			
			String message = null;
			
			if(result > 0) {
				message = "파일 업로드 성공";
			
			}else {
				message = "파일 업로드 실패...";
			}
			
			ra.addFlashAttribute("message", message);
			
			
			
			return "redirect:/myPage/fileTest";
			
		}
		
		@GetMapping("fileList")
		public String fileList(
				Model model
				) {
			
			// 파일 목록 조회 서비스 호출
			List<UploadFile> list = service.fileList();
			
			
			
			// model list 담아서 
			model.addAttribute("list", list);
			
			// myPage/myPage-fileList.html
			
		
			
			return "myPage/myPage-fileList";
		}
		
		@PostMapping("file/test3")
		public String uploadfile3(
				@RequestParam("aaa") List<MultipartFile> aaaList,
				@RequestParam("bbb") List<MultipartFile> bbbList,
				RedirectAttributes ra,
				@SessionAttribute("loginMember") Member loginMember
				) {
			// aaa 파일 미제출 시
			// -> 0번, 1번 인덱스 파일이 모두 비어있음
			
			
			// bbb(mutiple) 파일 미제출 시
			// 0번 인덱스 파일이 비어있음
			
			int memberNo = loginMember.getMemberNo();
			
			int result = service.fileUpload3(aaaList, bbbList, memberNo);
			
			String message = null;
			
			if(result == 0) {
				message = "업로드 된 파일이 없습니다";
			}else {
				message =  result + "개의 파일이 업로드 되었습니다";
			}
			
			ra.addFlashAttribute("message", message);
			
			return "redirect:/myPage/fileTest";
			
		}
		
		@PostMapping("profile")
		public String profile(
				@RequestParam("profileImg") MultipartFile profileImg,
				@SessionAttribute("loginMember") Member loginMember,
				RedirectAttributes ra
				) {
			
		  
		    
		    // 서비스 호출
		    // /myPage/profile/변경된파일명 형태의 문자열
		    // 현재 로그인한 회원의 PROFILE_IMG 컬럼값으로 수정(UPDATE)
		    int result = service.profile(profileImg, loginMember);
		    
		    String message = null;
		    
		    if(result >0) message = "변경 성공";
		    else message= "변경 실패";
		    
		    ra.addFlashAttribute("message", message);
			
			return "redirect:profile"; //리다이렉트 - /myPage/profile (상대경로)
		}
		
		
}
