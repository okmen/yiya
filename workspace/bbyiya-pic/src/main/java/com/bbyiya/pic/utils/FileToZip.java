package com.bbyiya.pic.utils;
  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.util.zip.ZipEntry;  
import java.util.zip.ZipOutputStream;  
/** 
 * 将文件夹下面的文�? 
 * 打包成zip压缩文件 
 *  
 * @author admin 
 * 
 */  
public final class FileToZip {  
  
	public FileToZip(){}  
      
    public void zip(String souceFileName, String destFileName) {  
        File file = new File(souceFileName);  
        try {  
            zip(file, destFileName);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void zip(File souceFile, String destFileName) throws IOException {  
        FileOutputStream fileOut = null;  
        try {  
            fileOut = new FileOutputStream(destFileName);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }  
        ZipOutputStream out = new ZipOutputStream(fileOut);  
        zip(souceFile, out, "");  
        out.close();  
    }  
  
    public void zip(File souceFile, ZipOutputStream out, String base)  
            throws IOException {  
  
        if (souceFile.isDirectory()) {  
            File[] files = souceFile.listFiles();  
            out.putNextEntry(new ZipEntry(base + "/"));  
            base = base.length() == 0 ? "" : base + "/";  
            for (File file : files) {  
                zip(file, out, base + file.getName());  
            }  
        } else {  
            if (base.length() > 0) {  
                out.putNextEntry(new ZipEntry(base));  
            } else {  
                out.putNextEntry(new ZipEntry(souceFile.getName()));  
            }  
            System.out.println("filepath=" + souceFile.getPath());  
            FileInputStream in = new FileInputStream(souceFile);  
  
            int b;  
            byte[] by = new byte[1024];  
            while ((b = in.read(by)) != -1) {  
                out.write(by, 0, b);  
            }  
            in.close();  
        }  
    }  
    
    /**
     * 删除单个文件 
     * @param sPath 被删除文件的路径+文件�?
     * @return 单个文件删除成功返回true，否则返回false
     */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	
    public boolean deleteDirectory(String sPath) {
    	// 如果sPath不以文件分隔符结尾，自动添加文件分隔�?
    	if (!sPath.endsWith(File.separator)) {
    		sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则�??�?
		if (!dirFile.exists() || !dirFile.isDirectory()) {
		return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文�?(包括子目�?)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文�?
			if (files[i].isFile()) {
				flag=deleteFile(files[i].getAbsolutePath());
				if (!flag)break;
			} // 删除子目�?
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)break;
			}
		}
		if (!flag)return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
    /** 
     * Test 
     */  
    public static void main(String[] args) {  
    	FileToZip z = new FileToZip();  
        z.zip("C:\\Users\\kevin\\orderimg\\201703241120", "C:\\Users\\kevin\\aa.zip");  
    }  
      
}  