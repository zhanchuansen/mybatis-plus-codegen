package com.suncompass.basic.codegen.service;

import com.suncompass.basic.codegen.entity.dto.DbConfig;

import java.sql.Connection;
import java.util.List;

public interface DbConfigService extends BaseService<DbConfig>{

    Connection getConnection(DbConfig dbConfig);

    List<DbConfig> getPortAndDatabase(Integer port, String database);

}
