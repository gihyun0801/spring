package edu.kh.project.board.exception;

public class ImageDeleteException extends RuntimeException {
	public ImageDeleteException() {
		super("이미지 삭제 중 예외 발생");
	}
	
	public ImageDeleteException(String message) {
		super(message);
	}
}
