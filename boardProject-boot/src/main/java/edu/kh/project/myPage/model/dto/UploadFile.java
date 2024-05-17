package edu.kh.project.myPage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder 
public class UploadFile {
		private int fileNo;
		private String filePath;
		private String fileOriginalName;
		private String fileRename;
		private String fileUploadDate;
		private int memberNo;
		private String memberNickname;
}
