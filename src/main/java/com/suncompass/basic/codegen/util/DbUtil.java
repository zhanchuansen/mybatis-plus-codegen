package com.suncompass.basic.codegen.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DbUtil {
    public static List<Map<String, Object>> getList(Connection conn, String sql, Object[] parameters, boolean close) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            stmt = conn.prepareStatement(sql);

            if (parameters != null && parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    stmt.setObject(i + 1, parameters[i]); //stmt索引从1开始
                }
            }

            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (int i = 1; i <= count; i++) {
                    String column = rsmd.getColumnLabel(i); //rsmd索引从1开始
                    Object value = rs.getObject(i); //rs索引从1开始
                    map.put(column, value);
                }
                listMap.add(map);
            }

        } catch (SQLException e) {

            System.out.print("查询数据异常");

        } finally {

            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {

            }

            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {

            }

            try {
                if (close && conn != null)
                    conn.close();
            } catch (SQLException e) {

            }
        }
        return listMap;
    }
}
