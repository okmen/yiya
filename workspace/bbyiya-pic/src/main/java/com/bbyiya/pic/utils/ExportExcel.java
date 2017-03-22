package com.bbyiya.pic.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import com.bbyiya.model.OUserorders;


/**
 * 
* <p>Title:ExportExcel </p>
* <p>����:����Excel������ </p>
* <p>�汾:ASOS���� </p>
* @author PengHao
* @date 2016-7-29 ����2:29:56
 */
public class ExportExcel<T> {
	

	/**
	 * ����CSV
	 * @param title
	 * @param headers
	 * @param fields
	 * @param dataset
	 * @param out
	 * @param pattern
	 */
	@SuppressWarnings({  "rawtypes", "unchecked" })
	public void exportCSV(String title, String[] headers, String[] fields, Collection<T> dataset, OutputStream out, String pattern) {
		BufferedWriter csvWriter = null;
		try {
			csvWriter = new BufferedWriter(new OutputStreamWriter(out, "GB2312"));
			StringBuffer sb = new StringBuffer();
			// д���ļ�ͷ��
			for (short i =0; i < headers.length; i++) {
				sb.append("\"").append(headers[i]).append("\",");
	        }
			csvWriter.write(sb.toString().substring(0, sb.toString().length() - 1));
			csvWriter.newLine();
			// д���ļ�����
			 // �����������ݣ�����������
	        Iterator<T> it = dataset.iterator();  
	        @SuppressWarnings("unused")
			int index = 0;
	        while (it.hasNext()) {
	        	index++;
	        	T t = (T) it.next();
    			sb.setLength(0);
	        	// ���÷��䣬����javabean���Ե��Ⱥ�˳�򣬶�̬����getXxxx()�����õ�����ֵ
	        	for (int i = 0; i < fields.length; i++) {
	        		String fieldName = fields[i];
	        		String getMethodName = GetterUtil.toGetter(fieldName);
	        		String mnames[] = getMethodName.split("\\.");
	        		if (mnames.length > 1) {
	        			getMethodName = mnames[0];// + "()." + mnames[1];
	        		}
	        		try {
						Class tCls = t.getClass();
						Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
						Object value = getMethod.invoke(t, new Object[]{});
						String textValue = null;
						if (value instanceof Date) {
							// �ж�ֵ�����ͺ����ǿ������ת��
							Date date = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
						} else if (value instanceof OUserorders) {
							// ����AosCell����
							Class cCls = value.getClass();
							Method getCMethod = cCls.getMethod(GetterUtil.toGetter(mnames[1]), new Class[] {});
							Object value2 = getCMethod.invoke(value, new Object[]{});
							if (value2 != null) {
								textValue = value2.toString();
							}
						} else {
							// �����������Ͷ������ַ����򵥴���
							if (value != null) {
								textValue = value.toString();
							}
						} 
						if (textValue != null) {
							// �������ͼƬ���ݣ�������������ʽ�ж�textValue�Ƿ�ȫ�����������
//							Pattern p = Pattern.compile("-?[0-9]+.*[0-9]*");
//							Pattern p = Pattern.compile("^//d+(//.//d+)?$");
//							Matcher matcher = p.matcher(textValue);
//							if (matcher.matches()) {
//								// �����ֵ���double����
//								sb.append(Double.parseDouble(textValue)).append(",");
//							} else {
//								sb.append("\"").append(textValue).append("\",");
//							} 
							if ("Integer".equals(getMethod.getReturnType().getSimpleName()) ||
									"Double".equals(getMethod.getReturnType().getSimpleName())) {
								sb.append(Double.parseDouble(textValue)).append(",");
							} else {
								sb.append("\"").append(textValue).append("\",");
							}
						} else {
							sb.append(",");
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					} finally {
					}
	        	}
				csvWriter.write(sb.toString().substring(0, sb.toString().length() - 1));
				csvWriter.newLine();
	        }
	        csvWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				csvWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����Excel
	 * @param title sheet����
	 * @param headers ��ͷ
	 * @param fields ��Id's
	 * @param dataset ���ݽ����
	 * @param out 
	 * @param pattern ���ڸ�ʽ��
	 */
	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	public void exportExcel(String title, String[] headers, String[] fields, Collection<T> dataset, OutputStream out, String pattern) {
		
		// ����һ��������
		Workbook workbook = new SXSSFWorkbook();
		// ����һ�����
		Sheet sheet = workbook.createSheet(title);
		// ���ñ��Ĭ���п��Ϊ15���ֽ�
		sheet.setDefaultColumnWidth(15);
		// ����һ����ʽ
		CellStyle style = workbook.createCellStyle();
		// ������ʽ
		style.setFillForegroundColor(HSSFColor.WHITE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
        // ����һ������
        Font font = workbook.createFont();  
        font.setColor(HSSFColor.BLACK.index);  
        font.setFontHeightInPoints((short) 12);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // ������Ӧ�õ���ǰ����ʽ  
        style.setFont(font);  
        // ���ɲ�������һ����ʽ  
        CellStyle style2 = workbook.createCellStyle();  
        style2.setFillForegroundColor(HSSFColor.WHITE.index);  
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // ������һ������  
        Font font2 = workbook.createFont();   
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // ������Ӧ�õ���ǰ����ʽ  
        style2.setFont(font2);  
        
        // ������������
        Row row = sheet.createRow(0);
        for (short i =0; i < headers.length; i++) {
        	Cell cell = row.createCell(i);
        	cell.setCellStyle(style);
        	RichTextString text = new XSSFRichTextString(headers[i]);
        	cell.setCellValue(text);
        }
        
        // �����������ݣ�����������
        Iterator<T> it = dataset.iterator();  
        int index = 0;
        while (it.hasNext()) {
        	index++;
        	row = sheet.createRow(index);
        	T t = (T) it.next();
        	// ���÷��䣬����javabean���Ե��Ⱥ�˳�򣬶�̬����getXxxx()�����õ�����ֵ
        	for (int i = 0; i < fields.length; i++) {
        		Cell cell = row.createCell(i);
        		cell.setCellStyle(style2);
        		String fieldName = fields[i];
        		String getMethodName = GetterUtil.toGetter(fieldName);
        		String mnames[] = getMethodName.split("\\.");
        		if (mnames.length > 1) {
        			getMethodName = mnames[0];// + "()." + mnames[1];
        		}
        		try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[]{});
					String textValue = null;
					if (value instanceof Date) {
						// �ж�ֵ�����ͺ����ǿ������ת��
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof OUserorders) {
						// ����AosCell����
						Class cCls = value.getClass();
						Method getCMethod = cCls.getMethod(GetterUtil.toGetter(mnames[1]), new Class[] {});
						Object value2 = getCMethod.invoke(value, new Object[]{});
						if (value2 != null) {
							textValue = value2.toString();
						}
					} else {
						// �����������Ͷ������ַ����򵥴���
						if (value != null) {
							textValue = value.toString();
						}
					} 

					if (textValue != null) {
						// �������ͼƬ���ݣ�������������ʽ�ж�textValue�Ƿ�ȫ�����������
						Pattern p = Pattern.compile("^//d+(//.//d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// �����ֵ���double����
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							/*RichTextString richString = new XSSFRichTextString(textValue);
							Font font3 = workbook.createFont();
							font3.setColor(HSSFColor.BLACK.index);
							richString.applyFont(font3);
							cell.setCellValue(richString);*/
							cell.setCellValue(textValue);
						} 
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
				}
        	}
        }
        try {
			workbook.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
