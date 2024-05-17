package edu.kh.project.board.model.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.BoardMapper.EditMapper;
import edu.kh.project.board.exception.BoardInsertException;
import edu.kh.project.board.exception.ImageDeleteException;
import edu.kh.project.board.exception.ImageUpdateExceptption;
import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.common.util.Utility;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
@PropertySource("classpath:/config.properties")
public class EditBoardServiceImpl implements EditBoardService{
	
	private final EditMapper mapper;
	
	@Value("${my.board.web-path}")
	private String webPath;
	
	@Value("${my.board.folder-path}")
	private String folderPath;

	/**
	 * 	게시글 작성
	 */
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) {
		
		// 1. 게시글 부분을 먼저
		// Board 테이블 insert 하기
		// -> Insert 결과를 작성된 게시글 번호(생성된 시퀀스 번호) 반환 받기
		
		int result = mapper.boardInsert(inputBoard);
		
		// 삽입 샐피 시 
		
		if(result == 0) return 0;
		
		// 삽입된 게시글의 번호를 변수로 저장 
		// -> mapper.xml 에서 selectKet 태그를 이용해서 생성됨
		// boardNo 가 inputBoard에 저장된 상태 
		
		int boardNo = inputBoard.getBoardNo();
		
		// 2. 업로드된 이미지가 실제로 존재하는 경우
		// 업로드된 이미지만 별도로 저장하여
		// BOARD_IMG 테이블에 삽입하는 코드 작성
		
		// 실제 업로드된 이미지와 정보를 모아둘 lIST 생성
		
		List<BoardImg> uploadLList = new ArrayList<>(); 
		
		// images 리스트에서 하나씩 꺼내어 선택된 파일이 있는지 검사
		
		for(int i = 0; i < images.size(); i++) {
			
			if(!images.get(i).isEmpty()) {
				// 원본명
				
				String originalName = images.get(i).getOriginalFilename();
				
				// 변경명
				
				String rename = Utility.fileRename(originalName);
				
				
				// 모든 값을 저장할 DTO 생성 (BoardImg)
				
				
				BoardImg img = BoardImg.builder().
						       imgPath(webPath).
						       imgOriginalName(originalName).
						       imgRename(rename).
						       boardNo(boardNo).
						       imgOrder(i).
						       uploadFile(images.get(i)).build();
				
				uploadLList.add(img);
				 
				
			}
			
		}
		
		// 선택한 파일이 없을 경우
		
		if(uploadLList.isEmpty()) {
			return boardNo;
		}
		
		// 선택한 파일이 존재할 경우
		// -> Board_img 테이블에 insert + 서버에 파일 저장
		
		
		// reuslt == 삽입된 행의 개수 == uploadList.size()
		result = mapper.insertUploadList(uploadLList);
		
		// 다중 insert 성공 확인 (uploadList에 저장된 값이 모두 정상 삽입 되었나)
		
		if(result == uploadLList.size()) {
			
			// 서버에 파일 저장
			for(BoardImg img : uploadLList) {
				try {
					img.getUploadFile().transferTo(new File(folderPath+img.getImgRename()));
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				 
			}
			
		}else {
			// 부분적으로 삽입 실패 -> 전체 서비스 실패로 판단
			// -> 이전에 삽입된 내용 모두 rollback
			
			// -> rollback 하는 방법
			// == 

			throw new BoardInsertException("이미지가 정상 삽입 되지않음");
		
		
		
			
		}
		
		return boardNo;
	}

	@Override
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrder) {
		
		
		// 1. 게시글 (제목/내용) 부분 수정 
		
		int result = mapper.boardUpdate(inputBoard);
		
		// 수정 실패 시 바로 리턴
		if(result == 0) return 0;
		
		// -----------------------------------------------------------------------------------------
		
		// 2. 기존 O -> 삭제된 이미지(deleteOrder) 가 있는 경우
		
		
		if(deleteOrder != null && !deleteOrder.equals("")) {
				
			Map<String, Object> map = new HashMap<>();
			map.put("deleteOrder", deleteOrder);
			map.put("boardNo", inputBoard.getBoardNo());
			
			// 성공하면 삭제된 행의 개수 리턴
			result = mapper.deleteImage(map);
				
			// 삭제 실패한 경우(부분 실패 포함) -> 롤백
			if(result == 0) {
				throw new ImageDeleteException();
			}
			
		}
		
		
		// 3. 선택한 파일이 존재할 경우
		//    해당 파일 정보만 모아두는 List 객체 생성
		List<BoardImg> uploadLList = new ArrayList<>(); 
		
		// images 리스트에서 하나씩 꺼내어 선택된 파일이 있는지 검사
		
		for(int i = 0; i < images.size(); i++) {
			
			if(!images.get(i).isEmpty()) {
				// 원본명
				
				String originalName = images.get(i).getOriginalFilename();
				
				// 변경명
				
				String rename = Utility.fileRename(originalName);
				
				
				// 모든 값을 저장할 DTO 생성 (BoardImg)
				
				
				BoardImg img = BoardImg.builder().
						       imgPath(webPath).
						       imgOriginalName(originalName).
						       imgRename(rename).
						       boardNo(inputBoard.getBoardNo()).
						       imgOrder(i).
						       uploadFile(images.get(i)).build();
				
				uploadLList.add(img);
				 
				
				// 4. 업로드 하려는 이미지 정보(img)를 이용해서
				// 수정 또는 삽입 수행
				
				// 1) 기존에 이미지가 있었던 것이 - > 새 이미지로 변경 하려고 하면 - > 수정
				
				result = mapper.updateImg(img);
				
				if(result == 0) {
					// 수정실패 == 돌아왔는데 result 가 0 이다 그럼 IMG_ORDER 에 이미지가 없었음
					// -> 삽입 수행
					
					// 2) 기존에 없었던 얘는 -> 새 이미지 추가
					result = mapper.insertImg(img);
				}
				
				//수정 또는 삭제가 실패한 경우
				
			
			}
			if(result == 0) {
				throw new ImageUpdateExceptption(); // 예외 발생 -> 롤백
			}
		}
		
		
		// 선택된 파일이 없을 경우
		if(uploadLList.isEmpty()) {
				return result;
		}
		
		//수정, 새 이미지 파일을 서버에 저장
		
		for(BoardImg img : uploadLList) {
			try {
				img.getUploadFile().transferTo(new File(folderPath + img.getImgRename()));
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		return result;
	}

}
