package edu.kh.project.board.BoardMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Comment;

@Mapper
public interface CommentMapper {

	/** 댓글 조회
	 * @param boardNo
	 * @return
	 */
	List<Comment> select(int boardNo);

	/** 댓글 등록
	 * @param comment
	 * @return
	 */
	int insert(Comment comment);

	int update(Comment comment);

	int delete(int commentNo);
   
}
