package edu.kh.project.board.model.service;

import java.util.List;

import edu.kh.project.board.model.dto.Comment;

public interface CommentService {

	/** 댓글목록조회
	 * @param boardNo
	 * @return
	 */
	List<Comment> select(int boardNo);

	int insert(Comment comment);

	int update(Comment comment);

	int delete(int commentNo);

}
