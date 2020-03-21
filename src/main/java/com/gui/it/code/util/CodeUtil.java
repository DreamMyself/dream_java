package com.gui.it.code.util;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

public class CodeUtil {
	/**
	 * 下划线字段转成驼峰
	 * @param column
	 * @return
	 */
	public static String toCamelCase(String column) {
		if (StringUtils.isEmpty(column)) {
			return column;
		}
		String temp = new String(column);
		while (temp.indexOf("_") != -1) {
			String left = temp.substring(0, temp.indexOf("_"));
			String right = temp.substring(temp.indexOf("_") + 1);
			temp = left + upperFirstCase(right);
		}
		return temp;
	}
	
	/**
	 * 首字母转成大写
	 * @param column
	 * @return
	 */
	public static String upperFirstCase(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		String first = str.substring(0, 1);
		return first.toUpperCase() + str.substring(1);
	}
	
	public static final Map<String, String> TYPE = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("varchar", "String");
			put("enum", "String");
			put("text", "String");
			put("int", "int");
			put("tinyint", "boolean");
			put("double", "double");
			put("float", "double");
			put("timestamp", "Date");
		}
	};
}
