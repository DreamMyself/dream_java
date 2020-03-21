package com.gui.it.code.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import com.gui.it.code.bean.ColumnBean;
import com.gui.it.code.dao.base.CodeDao;
import com.gui.it.code.service.base.CodeService;
import com.gui.it.code.util.CodeUtil;

@Service
public class CodeServiceImpl implements CodeService {
	
	@Autowired
	private CodeDao codeDao;

	@Override
	public void generetor(String schema, String tableName) {
		List<ColumnBean> list = codeDao.getColumnByTableName(schema, tableName);
		List<String> origColumnList = new ArrayList<>();
		List<String> paramColumnList = new ArrayList<>();
		List<String> itemColumnList = new ArrayList<>();
		List<String> updateColumnList = new ArrayList<>();
		List<String> conditionColumnList = new ArrayList<>();
		List<String> columnContentList = new ArrayList<>();
		for (ColumnBean columnBean : list) {
			columnBean.setUpperCaseColumn(CodeUtil.toCamelCase(columnBean.getColumnName()));
			columnBean.setParamColumn("#{" + columnBean.getUpperCaseColumn() + "}");
			columnBean.setParamItemColumn("#{item." + columnBean.getUpperCaseColumn() + "}");
			origColumnList.add(columnBean.getColumnName());
			paramColumnList.add(columnBean.getParamColumn());
			itemColumnList.add(columnBean.getParamItemColumn());
			updateColumnList.add(columnBean.getColumnName() + " = " + columnBean.getParamColumn());
			conditionColumnList.add(getConditionSql(columnBean));
			columnContentList.add(getPrivateProperty(columnBean));
		}
		String tableNameC = CodeUtil.toCamelCase(tableName);
		String upperTableName = CodeUtil.upperFirstCase(tableNameC);
		
		String origAllColumn = StringUtils.collectionToDelimitedString(origColumnList, ",\n\t\t\t");
		String origAllColumnParam = StringUtils.collectionToDelimitedString(paramColumnList, ",\n\t\t\t");
		String origAllItemColumnParam = StringUtils.collectionToDelimitedString(itemColumnList, ",\n\t\t\t\t");
		String allColumnContiion = StringUtils.collectionToDelimitedString(conditionColumnList, "\n");
		String columnContent = StringUtils.collectionToDelimitedString(columnContentList, "\n");
		String allUpdateColumn = StringUtils.collectionToDelimitedString(updateColumnList, ",\n\t\t\t");
		
		//导出Bean
		String beanFileName = upperTableName + "Bean.java";
		String beanTemplete = getTempleteContent("Bean");
		beanTemplete = beanTemplete.replace("{schema}", schema);
		beanTemplete = beanTemplete.replace("{upperTableName}", upperTableName);
		beanTemplete = beanTemplete.replace("{columnContent}", columnContent);
		outTempleteContent(beanFileName,beanTemplete);
		//导出Dao
		String daoFileName = upperTableName + "Dao.java";
		String daoTemplete = getTempleteContent("Dao");
		daoTemplete = daoTemplete.replace("{schema}", schema);
		daoTemplete = daoTemplete.replace("{upperTableName}", upperTableName);
		daoTemplete = daoTemplete.replace("{tableName}", tableNameC);
		outTempleteContent(daoFileName,daoTemplete);
		//导出Controller
		String controllerFileName = upperTableName + "Controller.java";
		String controllerTemplete = getTempleteContent("Controller");
		controllerTemplete = controllerTemplete.replace("{schema}", schema);
		controllerTemplete = controllerTemplete.replace("{upperTableName}", upperTableName);
		controllerTemplete = controllerTemplete.replace("{tableName}", tableNameC);
		outTempleteContent(controllerFileName,controllerTemplete);
		//导出Service
		String serviceFileName = upperTableName + "Service.java";
		String serviceTemplete = getTempleteContent("Service");
		serviceTemplete = serviceTemplete.replace("{schema}", schema);
		serviceTemplete = serviceTemplete.replace("{upperTableName}", upperTableName);
		serviceTemplete = serviceTemplete.replace("{tableName}", tableNameC);
		outTempleteContent(serviceFileName,serviceTemplete);
		//导出ServiceImpl
		String serviceImplFileName = upperTableName + "ServiceImpl.java";
		String serviceImplTemplete = getTempleteContent("ServiceImpl");
		serviceImplTemplete = serviceImplTemplete.replace("{schema}", schema);
		serviceImplTemplete = serviceImplTemplete.replace("{upperTableName}", upperTableName);
		serviceImplTemplete = serviceImplTemplete.replace("{tableName}", tableNameC);
		outTempleteContent(serviceImplFileName,serviceImplTemplete);
		//导出Mapper
		String mapperFileName = upperTableName + "Mapper.xml";
		String mapperTemplete = getTempleteContent("Mapper");
		mapperTemplete = mapperTemplete.replace("{schema}", schema);
		mapperTemplete = mapperTemplete.replace("{upperTableName}", upperTableName);
		mapperTemplete = mapperTemplete.replace("{origTableName}", tableName);
		mapperTemplete = mapperTemplete.replace("{origAllColumn}", origAllColumn);
		mapperTemplete = mapperTemplete.replace("{origAllColumnParam}", origAllColumnParam);
		mapperTemplete = mapperTemplete.replace("{origAllItemColumnParam}", origAllItemColumnParam);
		mapperTemplete = mapperTemplete.replace("{allColumnContiion}", allColumnContiion);
		mapperTemplete = mapperTemplete.replace("{allUpdateColumn}", allUpdateColumn);
		outTempleteContent(mapperFileName,mapperTemplete);
	}

	private String getPrivateProperty(ColumnBean columnBean) {
		String temp = "\t/** %s */\n\tprivate %s %s;";
		return String.format(temp, columnBean.getColumnComment(),
				CodeUtil.TYPE.get(columnBean.getDataType()),
				columnBean.getUpperCaseColumn());
	}

	private String getTempleteContent(String str) {
		try {
			File file = ResourceUtils.getFile("classpath:templete/" + str + "Templete.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String temp = null;
			StringBuffer buffer = new StringBuffer();
			while ((temp = br.readLine()) != null) {
				buffer.append(temp).append("\n");
			}
			br.close();
			return buffer.toString();
		} catch (Exception e) {
			System.out.println("文件读取失败：" + str + "Templete.txt");
		}
		return null;
	}
	
	private void outTempleteContent(String fileName, String content) {
		try {
			File path = new File("D:/codeFile");
			File file = new File("D:/codeFile/" + fileName);
			if (!path.exists()) {
				path.mkdirs();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			System.out.println("文件导出失败：" + fileName);
		}
	}

	private String getConditionSql(ColumnBean columnBean) {
		String condition = "\t\t<if test=\"%s != null\">\n\t\t\tand %s %s %s\n\t\t</if>";
		String upperCaseColumn = columnBean.getUpperCaseColumn();
		String columnName = columnBean.getColumnName();
		String link = "=";
		String param = columnBean.getParamColumn();
		String dataType = columnBean.getDataType();
		if (dataType.equalsIgnoreCase("varchar") || dataType.equalsIgnoreCase("text")) {
			link = "like";
			param = "concat('%'," + param + ",'%')";
		}
		return String.format(condition, upperCaseColumn, columnName, link, param);
	}
}
