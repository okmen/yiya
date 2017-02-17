package com.bbyiya.utils;

import java.io.File;

public class FileUtils {

	
	/**
	 * 文件夹知否存在（不存在创建）
	 * @param filePath
	 */
	public static void isDirExists(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				System.out.println("dir exists");
			} else {
				System.out.println("the same name file exists, can not create dir");
			}
		} else {
			System.out.println("dir not exists, create it ...");
			file.mkdir();
		}
	}
	
	/**
	 * 文件是否存在
	 * @param filePath
	 */
	public static boolean isFileExists(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}
}
