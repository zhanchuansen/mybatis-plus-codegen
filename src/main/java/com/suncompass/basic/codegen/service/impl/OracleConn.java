package com.suncompass.basic.codegen.service.impl;

import com.suncompass.basic.codegen.entity.dto.DbConfig;
import com.suncompass.basic.codegen.service.ConnInterface;
import com.suncompass.basic.codegen.util.DbUtil;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

public class OracleConn implements ConnInterface {
    @Override
    public Connection getConnection(DbConfig dbConfig) {
        String ip = dbConfig.getIp();
        int port = dbConfig.getPort();
        String database = dbConfig.getDatabase();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        //String characterEncoding = dbConfig.getCharacterEncoding();
        Connection conn = null;

        String url = String.format("jdbc:oracle:thin:@%s:%d:%s", ip, port, database);//单实例:直接配置实例名连接

        //String url=String.format("jdbc:oracle:thin:@//%s:%d/%s",ip,port,database);//RAC最合理的方式是ServerName连接方式

      /*  if (StringUtils.hasText(characterEncoding)) {
            url += "&characterEncoding=" + characterEncoding;
        } else {
            url += "&characterEncoding=utf-8";
        }*/
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
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
                " select dt.table_name \"tableName\",dtc.comments \"tableComment\",uo.created \"createTime\" " +
                " from user_tables dt," +
                " user_tab_comments dtc," +
                " user_objects uo " +
                " where dt.table_name = dtc.table_name and dt.table_name = uo.object_name and uo.object_type='TABLE'";
        Object[] parameters = null;
        if (StringUtils.hasText(tablePrefix)) {
            sql += "and dt.table_name like concat('%', UPPER('" + tablePrefix + "'))";
        }
        sql += " order by dt.table_name asc ";
        List<Map<String, Object>> listMap = DbUtil.getList(conn, sql, parameters, close);
        return listMap;
    }

    @Override
    public List getColumnList(Connection conn, String tableName, boolean close) {
        String sql = "" +
                " select temp.column_name \"columnName\", temp.data_type \"dataType\", temp.comments \"columnComment\", " +
                " case temp.constraint_type when 'P' then 'PRI' when 'C' then 'UNI' else '' end " +
                " \"COLUMNKEY\",'' \"EXTRA\" " +
                " from ( " +
                " select col.column_id, col.column_name,col.data_type, colc.comments, uc.constraint_type," +
                //去重
                " row_number() over (partition by col.column_name order by uc.constraint_type desc) as row_flg " +
                " from user_tab_columns col " +
                " left join user_col_comments colc " +
                " on colc.table_name = col.table_name " +
                " and colc.column_name = col.column_name " +
                " left join user_cons_columns ucc " +
                " on ucc.table_name = col.table_name " +
                " and ucc.column_name = col.column_name " +
                " left join user_constraints uc " +
                " on uc.constraint_name = ucc.constraint_name " +
                " where col.table_name = upper('" + tableName + "') " +
                " ) temp " +
                " where temp.row_flg = 1 " +
                " order by temp.column_id";

        Object[] parameters = null;

        List<Map<String, Object>> listMap = DbUtil.getList(conn, sql, parameters, close);
        return listMap;
    }
}
