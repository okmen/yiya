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
* <p>描述:导出Excel公共类 </p>
* <p>版本:ASOS二期 </p>
* @author PengHao
* @date 2016-7-29 下午2:29:56
 */
public class ExportExcel<T> {
	

	/**
	 * 导出CSV
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
			// 写入文件头部
			for (short i =0; i < headers.length; i++) {
				sb.append("\"").append(headers[i]).append("\",");
	        }
			csvWriter.write(sb.toString().substring(0, sb.toString().length() - 1));
			csvWriter.newLine();
			// 写入文件内容
			 // 遍历集合数据，产生数据行
	        Iterator<T> it = dataset.iterator();  
	        @SuppressWarnings("unused")
			int index = 0;
	        while (it.hasNext()) {
	        	index++;
	        	T t = (T) it.next();
    			sb.setLength(0);
	        	// 利用反射，根据javabean属性的先后顺序，动态调用getXxxx()方法得到属性值
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
							// 判断值的类型后进行强制类型转换
							Date date = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
						} else if (value instanceof OUserorders) {
							// 关联AosCell处理
							Class cCls = value.getClass();
							Method getCMethod = cCls.getMethod(GetterUtil.toGetter(mnames[1]), new Class[] {});
							Object value2 = getCMethod.invoke(value, new Object[]{});
							if (value2 != null) {
								textValue = value2.toString();
							}
						} else {
							// 其它数据类型都当作字符串简单处理
							if (value != null) {
								textValue = value.toString();
							}
						} 
						if (textValue != null) {
							// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
//							Pattern p = Pattern.compile("-?[0-9]+.*[0-9]*");
//							Pattern p = Pattern.compile("^//d+(//.//d+)?$");
//							Matcher matcher = p.matcher(textValue);
//							if (matcher.matches()) {
//								// 是数字当作double处理
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
	 * 导出Excel
	 * @param title sheet标题
	 * @param headers 表头
	 * @param fields 列Id's
	 * @param dataset 数据结果集
	 * @param out 
	 * @param pattern 日期格式化
	 */
	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	public void exportExcel(String title, String[] headers, String[] fields, Collection<T> dataset, OutputStream out, String pattern) {
		
		// 声明一个工作薄
		Workbook workbook = new SXSSFWorkbook();
		// 生成一个表格
		Sheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		CellStyle style = workbook.createCellStyle();
		// 设置样式
		style.setFillForegroundColor(HSSFColor.WHITE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
        // 生成一个字体
        Font font = workbook.createFont();  
        font.setColor(HSSFColor.BLACK.index);  
        font.setFontHeightInPoints((short) 12);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用到当前的样式  
        style.setFont(font);  
        // 生成并设置另一个样式  
        CellStyle style2 = workbook.createCellStyle();  
        style2.setFillForegroundColor(HSSFColor.WHITE.index);  
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
        Font font2 = workbook.createFont();   
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font2);  
        
        // 产生表格标题行
        Row row = sheet.createRow(0);
        for (short i =0; i < headers.length; i++) {
        	Cell cell = row.createCell(i);
        	cell.setCellStyle(style);
        	RichTextString text = new XSSFRichTextString(headers[i]);
        	cell.setCellValue(text);
        }
        
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();  
        int index = 0;
        while (it.hasNext()) {
        	index++;
        	row = sheet.createRow(index);
        	T t = (T) it.next();
        	// 利用反射，根据javabean属性的先后顺序，动态调用getXxxx()方法得到属性值
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
						// 判断值的类型后进行强制类型转换
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof OUserorders) {
						// 关联AosCell处理
						Class cCls = value.getClass();
						Method getCMethod = cCls.getMethod(GetterUtil.toGetter(mnames[1]), new Class[] {});
						Object value2 = getCMethod.invoke(value, new Object[]{});
						if (value2 != null) {
							textValue = value2.toString();
						}
					} else {
						// 其它数据类型都当作字符串简单处理
						if (value != null) {
							textValue = value.toString();
						}
					} 

					if (textValue != null) {
						// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
						Pattern p = Pattern.compile("^//d+(//.//d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
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
