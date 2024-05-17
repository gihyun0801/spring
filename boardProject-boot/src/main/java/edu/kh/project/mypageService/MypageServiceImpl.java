package edu.kh.project.mypageService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.Utility;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.mapper.MypageMapper;
import edu.kh.project.myPage.model.dto.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:/config.properties")
@SessionAttributes("${loginMember}")
public class MypageServiceImpl implements MypageService{

		private final MypageMapper mapper;
		
		private final BCryptPasswordEncoder encoder;
		
		@Value("${my.profile.web-path}")
		private String  profileWebPath;
		
		@Value("${my.profile.folder-path}")
		private String  profileFolderPath;

		/**
		 * 회원 정보 수정
		 */
		@Override
		public int updateInfo(Member inputMember, String[] memberAddress) {
			
			
			//입력된 주소가 있을경우
			//
			
			if(!inputMember.getMemberAddress().equals(",,")) {
				
				
				String str = String.join("^^^", memberAddress);
				
				inputMember.setMemberAddress(str);
				
			}else {
				inputMember.setMemberAddress(null);
			}
			
			log.debug("inputMember:" + inputMember);
			int result = mapper.updateInfo(inputMember);
			
			return result;
		}

		/**
		 * 비밀번호 수정
		 */
		@Override
		public int changePw(Map<String, Object> paramMap, int memberNo) {
			
			
			// 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
			String originPw = mapper.selectPw(memberNo);
			
			System.out.println(originPw);
			
			String memberPw = (String)paramMap.get("currentPw");
			
			if(!encoder.matches(memberPw, originPw)) {
				return 0;
			}
			
			// 같을 경우
			
			// 새 비밀번호를 암호화 진행
			
			String encPw = encoder.encode(memberPw);
			
			paramMap.put("encPw", encPw);
			paramMap.put("memberNo", memberNo);
			
			return mapper.changePw(paramMap);
		}

		@Override
		public int deleteConfirm(String memberPw, int memberNo) {
			
			
			System.out.println(memberNo);
			
			String originPw = mapper.selectPw(memberNo);
			
					
		
			
			if(!encoder.matches(memberPw, originPw)) {
				return 0;
			}
			
			System.out.println("아아아아");
			
		
			
			return mapper.deleteConfirm(memberNo);
		}

		@Override
		public String fileUpload1(MultipartFile uploadfile) {
			
			//MultipartFile 이 제공하는 메서드
			//- getSize() : 파일의 크기 
			//- isEmpty() : 업로드한 파일이 없을 경우 true
			//- getOriginalFileName() : 원본 파일 명
			//- transferTo(경로) : 메모리 또는 임시 저장 경로에 업로드된 파일을 원하는 경로에 전송하는 일 (서버 어떤 폴더에 저장할지 지정)

			//transferTo 는 DB 에 내용이 잘 올라갓을시 이 메서드를 사용한다.
			
			if(uploadfile.isEmpty()) { // 업로드한 파일이 없을 경우
				return null;
			}
			
			// 업로드한 파일이 있을경우
			
			// C://uploadFiles/test/파일명 으로 서버에 저장
			try {
				uploadfile.transferTo(
						new File("C:\\uploadFiles\\test\\" + uploadfile.getOriginalFilename())
						);
				
				// 웹에서 해당 파일에 접근할 수 있는 경로를 반환
				
				// 서버 : C:/uploadFiles/test/a.jpg 
				// 클라이언트가 웹에서 접근할때는 : /myPage/file/a.jpg
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		
			
			//클라이언트가 올린파일은 메모리에 올라가고, 임계값이 넘으면 임시저장경로
			//transferto를 사용하면 실제 저장할 경로를 설정해준다
			
			return "/myPage/file/" + uploadfile.getOriginalFilename();
			//하나의 경로를 반환해준것이다 
			
			//웹 접근 주소 반환을 한것이다 접근할수있는
			// /myPage/file/ + uploadFile.getORiginFilename()
		}

		/**
		 * 파일 업로드 test 2
		 */
		@Override
		public int fileUpload2(int memberNo, MultipartFile uploadfile) {
			
			// 업로드된 파일이 없다면
			// 선택된 파일이 없는 경우
			
			if(uploadfile.isEmpty()) {
				return 0;
			}
			
			// 1. 서버에 저장할 파일 경로 만들기
			
			// 파일이 저장될 서버 폴더 경로
			String folderPath = "C:\\uploadFiles\\test\\";
			
			// 클라이언트가 파일이 저장된 폴더에 접근할 수 있는 주소
			String webPath = "/myPage/file/";
			
			// 2. DB 에 전달할 데이터를 DTO 로 묶어서 INSERT 호출하기
			// webPath, memberNo, 원본 파일명 , 변경된 파일명
			
			String fileRename = Utility.fileRename(uploadfile.getOriginalFilename());
			
			// Builder 패턴을 이용해서 UploadFile 객체 생성
			// 장점 1) 반복되는 참조변수명, set 구문 생략
			// 장점 2) method chaining을 이용해서 한 줄로 작성 가능
			
			UploadFile uf = UploadFile.builder()
							.memberNo(memberNo)
							.filePath(webPath)
							.fileOriginalName(uploadfile.getOriginalFilename())
							.fileRename(fileRename)
							.build();
			
			int result = mapper.insertUploadFile(uf);
			
			// 3.삽입 성공 시 파일을 지정된 서버 폴더에 저장
			
			// 삽입 실패 시
			
			if(result == 0) {
				return 0;
			}
			
			// 삽입 성공 시
			
			// C:\\uploadFiles\\text\\변경된파일명 으로
			// 파일을 서버 컴퓨터에 저장
			
			try {
				
				uploadfile.transferTo(new File(folderPath + fileRename));
							//C:\\uploadFiles\\test\\2024041711603_000001.png
			// -> CheckedException 발생 -> 예외 처리 필수
				
		   // @Transactional은 RuntimeException(UncheckedException 대표) 만 처리
		   // -> rollbackFor 속성 이용해서
		   // 롤백할 예외 범위를 수정
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
		//	UploadFile uf = new UploadFile();
		//	uf.setMemberNo(memberNo);
		//	uf.setFilePath(webPath);
		//	uf.setFileOriginalName(uploadfile.getOriginalFilename());
		//	uf.setFileRename(fileRename);
			
			
			
			
			/* DB에 파일 저장이 가능은 하지만
			 * DB 에 부하를 줄이기 위해서
			 * 
			 * 1) 파일 자체를 DB 에 넣지 않고
			 * 2) 서버에는 파일이 저장되어잇는 경로를 저장한다
			 * 3) 삽입/수정이 성공 했으면 서버에 파일을 저장
			 * 4) 만약 파일 저장 실패 시
			 *  -> 예외 발생
			 *  -> @Transactional을 이용해서 rollback 수행 
			 *
			 * 
			 * 
			 * */
			return result;
		}

		@Override
		public List<UploadFile> fileList() {
			return mapper.fileList();
		}

		/**
		 * 여러 파일 업로드
		 */
		@Override
		public int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo) {
			
			// 1. aaaList 처리
			
			int result1 = 0;
			
			// 업로드된 파일이 없을 경우를 제외하고 업로드
			
			for(MultipartFile file : aaaList) {
				
				if(file.isEmpty()) {
					continue;
				}
				
				//fileUpload2() 메서드 호출(재활용)
				// -> 파일 하나 업로드 + DB INSERT
				try {
					result1 += fileUpload2(memberNo, file);
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				
				
			}
			
			//bbblist 처리
			
			int result2 = 0;
			
			for(MultipartFile file : bbbList) {
				
				if(file.isEmpty()) {
					continue;
				}
				
				//fileUpload2() 메서드 호출(재활용)
				// -> 파일 하나 업로드 + DB INSERT
				try {
					result2 += fileUpload2(memberNo, file);
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				
				
			}
			
			return result1 + result2;
		}

		@Override
		public int profile(MultipartFile profileImg, Member loginMember) {
			
			  int memberNo = loginMember.getMemberNo();
			
			// 수정할 경로
			
			// 변경명 저장
			
			// 업로드한 이미지가 있을 경우
			// - 있을 경우 : 수정할 경로 조합 (클라이언트 접근 + 경로리네임파일명 )
			
			String updatePath = null;
			
			String rename = null;		
			
			if(!profileImg.isEmpty()) {
				
				//updatePath 조합
				
				// 1. 파일명 변경
				rename = Utility.fileRename(profileImg.getOriginalFilename());
				
				// 2. /myPage/profile/변경된파일명
				
				updatePath = profileWebPath + rename;
				
				
				
			}
			
			// 수정된 프로필 이미지 경로 + 회원 번호를 저장할 DTO 객체
			
			Member mem = Member.builder().
					    memberNo(memberNo).
					    profileImg(updatePath).
					    build();
			
			//UPDATE 수행
			int result = mapper.profile(mem);
			
			if(result >0) {
				// 프로필 이미지를 없앤 경우 == NULL 로 수정한 경우
				
				if(!profileImg.isEmpty()) {
					
					try {
						profileImg.transferTo(new File(profileFolderPath + rename));
						
					}catch(Exception e) {e
						.printStackTrace();
					}
					
				}
				
				
				// 세션 회원 정보에서 프로필 이미지 경로를
				// 업데이트한 경로로 변경
				
				
				
			}
			
			loginMember.setProfileImg(updatePath);
			
			
			return result;
		}
	
}
