package com.suncompass.basic.codegen.service.impl;

import com.suncompass.basic.codegen.entity.dto.DbConfig;
import com.suncompass.basic.codegen.enums.DbType;
import com.suncompass.basic.codegen.mapper.DbConfigMapper;
import com.suncompass.basic.codegen.service.DbConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@Service
public class DbConfigServiceImpl extends BaseServiceImpl<DbConfig> implements DbConfigService {

    @Autowired
    private DbConfigMapper dataSourceMapper;

    @Override
    public List<DbConfig> getPortAndDatabase(Integer port, String database) {
        return dataSourceMapper.getPortAndDatabase(port,database);
    }

    @Override
    public Connection getConnection(DbConfig dbConfig) {
        String ip = dbConfig.getIp();
        int port = dbConfig.getPort();
        String database = dbConfig.getDatabase();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        Connection conn = null;
        DbType dbType=dbConfig.getDbType();

        switch (dbType){
            case MySql:
                String characterEncoding = dbConfig.getCharacterEncoding();
                String url = String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&allowMultiQueries=true", ip, port, database);
                if (StringUtils.hasText(characterEncoding)) {
                    url += "&characterEncoding=" + characterEncoding;
                } else {
                    url += "&characterEncoding=utf-8";
                }
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection(url, username, password);
                } catch (Exception e) {
                    System.out.println("加载驱动异常");
                }
            case Oracle:
                String url2 = String.format("jdbc:oracle:thin:@%s:%d:%s", ip, port, database);//单实例:直接配置实例名连接
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                } catch (Exception e) {
                    System.out.print("加载驱动异常");
                }

                try {
                    conn = DriverManager.getConnection(url2, username, password);
                } catch (Exception e) {
                    System.out.print("创建连接异常");
                }
        }
        return conn;
    }

}
