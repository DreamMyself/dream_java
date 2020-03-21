package com.gui.it.code.bean;

import lombok.Data;

@Data
public class ColumnBean {
	private String tableSchema;
	private String tableName;
	private String columnName;
	private String dataType;
	private String columnComment;
	//驼峰转换后的字段
	private String upperCaseColumn;
	//占位符字段，如：#{id}
	private String paramColumn;
	//占位符字段，如：#{item.id}
	private String paramItemColumn;
}
