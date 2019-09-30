package com.suncompass.basic.codegen.controller;

import com.suncompass.basic.codegen.entity.dto.DbConfig;
import com.suncompass.basic.codegen.service.DbConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.List;

@RestController
@RequestMapping("/DbConfig")
public class DbConfigController extends BaseController<DbConfig> {

    @Autowired
    private DbConfigService dbConfigService;

    @RequestMapping("/insert")
    public ResponseEntity insert(@RequestBody DbConfig dbConfig) {
        Connection conn = dbConfigService.getConnection(dbConfig);
        if (conn == null) {
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("加载驱动异常，无法连接");
        }
        List<DbConfig> list = dbConfigService.getPortAndDatabase(dbConfig.getPort(), dbConfig.getDatabase());
        if (list != null && list.size() > 0) {
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("连接已存在");
        }
        return ResponseEntity.ok(dbConfigService.insert(dbConfig));
    }

    @RequestMapping("/update")
    public ResponseEntity update(@RequestBody DbConfig dbConfig) {
        Connection conn = dbConfigService.getConnection(dbConfig);
        if (conn == null) {
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("加载驱动异常，无法连接");
        }
        List<DbConfig> list = dbConfigService.getPortAndDatabase(dbConfig.getPort(), dbConfig.getDatabase());
        if (list != null && list.size() > 0) {
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("连接已存在");
        }
        return ResponseEntity.ok(dbConfigService.update(dbConfig));
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody DbConfig dbConfig) {
        if (dbConfig.getId() == null || dbConfig.getId() == 0) {
            return insert(dbConfig);
        } else {
            return update(dbConfig);
        }
    }
}
