package edu.kh.project.board.exception;

public class ImageUpdateExceptption extends RuntimeException {
	public ImageUpdateExceptption() {
		super("이미지 수정/ 삽입중 예외 발생");
	}
	
   public ImageUpdateExceptption(String message) {
	   super(message);
   }
}
