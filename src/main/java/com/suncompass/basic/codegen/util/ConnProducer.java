package com.suncompass.basic.codegen.util;

import com.suncompass.basic.codegen.entity.dto.DbConfig;
import com.suncompass.basic.codegen.service.ConnInterface;
import com.suncompass.basic.codegen.service.impl.MySqlConn;
import com.suncompass.basic.codegen.service.impl.OracleConn;

public class ConnProducer {
    public static ConnInterface getConnImplement(DbConfig dbConfig) {
        ConnInterface conn = null;
        switch (dbConfig.getDbType()) {
            case MySql:
                conn = new MySqlConn();
                break;
            case Oracle:
                conn=new OracleConn();
        }

        return conn;
    }
}
