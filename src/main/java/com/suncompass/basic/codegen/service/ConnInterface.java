package com.suncompass.basic.codegen.service;

import com.suncompass.basic.codegen.entity.dto.DbConfig;

import java.sql.Connection;
import java.util.List;

public interface ConnInterface {

   Connection getConnection(DbConfig dbConfig);

    List getTableList(Connection conn, String tablePrefix, String[] tableNames, boolean close);

    List getColumnList(Connection conn, String tableName, boolean close);
}
