package com.bbyiya.common.vo;

public class FileUploadParam {

	//文件类型（ UploadTypeEnum ：1相片 ，2用户头像，3音乐 ，4 视频）
	private int fileType;
	private String fileTempPath;
	
	
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	public String getFileTempPath() {
		return fileTempPath;
	}
	public void setFileTempPath(String fileTempPath) {
		this.fileTempPath = fileTempPath;
	}
	
	
}
