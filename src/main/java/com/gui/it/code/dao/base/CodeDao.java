package com.gui.it.code.dao.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gui.it.code.bean.ColumnBean;

public interface CodeDao {

	List<ColumnBean> getColumnByTableName(@Param("schema") String schema, @Param("tableName") String tableName);

}
