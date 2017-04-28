package com.bbyiya.pic.utils;
  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.util.zip.ZipEntry;  
import java.util.zip.ZipOutputStream;  
/** 
 * ���ļ���������ļ� 
 * �����zipѹ���ļ� 
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
     *  ɾ�������ļ� 
     * @param sPath ��ɾ���ļ���·��+�ļ���
     * @return �����ļ�ɾ���ɹ�����true�����򷵻�false
     */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
	
    public boolean deleteDirectory(String sPath) {
    	// ���sPath�����ļ��ָ�����β���Զ�����ļ��ָ���
    	if (!sPath.endsWith(File.separator)) {
    		sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// ���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�
		if (!dirFile.exists() || !dirFile.isDirectory()) {
		return false;
		}
		boolean flag = true;
		// ɾ���ļ����µ������ļ�(������Ŀ¼)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// ɾ�����ļ�
			if (files[i].isFile()) {
				flag=deleteFile(files[i].getAbsolutePath());
				if (!flag)break;
			} // ɾ����Ŀ¼
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)break;
			}
		}
		if (!flag)return false;
		// ɾ����ǰĿ¼
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