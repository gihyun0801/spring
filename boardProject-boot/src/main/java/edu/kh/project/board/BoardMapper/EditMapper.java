package edu.kh.project.board.BoardMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;

@Mapper
public interface EditMapper {

	/** 개시글 작성
	 * @param inputBoard
	 * @return
	 */
	int boardInsert(Board inputBoard);

	/** 게시글 이미지 모두 삽입
	 * @param uploadLList
	 * @return
	 */
	int insertUploadList(List<BoardImg> uploadLList);

	/** 게시글 부분 수정(제목/내용)
	 * @param inputBoard
	 * @return
	 */
	int boardUpdate(Board inputBoard);

	/** 게시글 이미지 삭제
	 * @param map
	 * @return
	 */
	int deleteImage(Map<String, Object> map);

	/** 게시글 이미지 수정
	 * @param img
	 * @return
	 */
	int updateImg(BoardImg img);

	/** 게시글 이미지 삽입
	 * @param img
	 * @return
	 */
	int insertImg(BoardImg img);

}
