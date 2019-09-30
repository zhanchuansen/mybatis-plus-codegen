package com.suncompass.basic.codegen.mapper;
import com.suncompass.basic.codegen.entity.dto.DbConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbConfigMapper extends BaseMapper<DbConfig>{

    List<DbConfig> getPortAndDatabase(@Param("port") Integer port, @Param("database") String database);
}
