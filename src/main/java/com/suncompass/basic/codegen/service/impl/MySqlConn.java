package com.suncompass.basic.codegen.service.impl;

import com.suncompass.basic.codegen.entity.dto.DbConfig;
import com.suncompass.basic.codegen.service.ConnInterface;
import com.suncompass.basic.codegen.util.DbUtil;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

public class MySqlConn implements ConnInterface {

    @Override
    public Connection getConnection(DbConfig dbConfig) {

        String ip = dbConfig.getIp();
        int port = dbConfig.getPort();
        String database = dbConfig.getDatabase();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        String characterEncoding = dbConfig.getCharacterEncoding();
        Connection conn = null;

        String url = String.format("jdbc:p6spy:mysql://%s:%d/%s?useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&allowMultiQueries=true", ip, port, database);
        if (StringUtils.hasText(characterEncoding)) {
            url += "&characterEncoding=" + characterEncoding;
        } else {
            url += "&characterEncoding=utf-8";
        }
        try {
            Class.forName("com.p6spy.engine.spy.P6SpyDriver");
        } catch (Exception e) {
            System.out.print("加载驱动异常");
        }

        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.print("创建连接异常");
        }
        return conn;
    }

    @Override
    public List getTableList(Connection conn, String tablePrefix, String[] tableNames, boolean close) {

        String sql = "" +
                " select table_name tableName, engine, table_comment tableComment, create_time createTime " +
                " from information_schema.tables " +
                " where table_schema = (select database()) ";

        Object[] parameters = null;
        if (StringUtils.hasText(tablePrefix)) {
            sql += " and table_name like concat('" + tablePrefix + "', '%') ";
        } else if (tableNames != null && tableNames.length > 0) {
            sql += " and find_in_set(table_name, '" + String.join(",", tableNames) + "') ";
        }

        sql += " order by table_name asc ";

        List<Map<String, Object>> listMap = DbUtil.getList(conn, sql, parameters, close);
        return listMap;
    }


    @Override
    public List getColumnList(Connection conn, String tableName, boolean close) {

        String sql = "" +
                " select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra as extra " +
                " from information_schema.columns " +
                " where table_schema = (select database()) and table_name = '" + tableName + "' " +
                " order by ordinal_position ";

        Object[] parameters = null;

        List<Map<String, Object>> listMap = DbUtil.getList(conn, sql, parameters, close);
        return listMap;
    }
}
