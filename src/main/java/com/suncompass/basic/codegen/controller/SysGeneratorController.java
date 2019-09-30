package com.suncompass.basic.codegen.controller;

import cn.hutool.core.io.IoUtil;
import com.suncompass.basic.codegen.entity.dto.DbConfig;
import com.suncompass.basic.codegen.entity.vo.GenConfig;
import com.suncompass.basic.codegen.service.ConnInterface;
import com.suncompass.basic.codegen.service.DbConfigService;
import com.suncompass.basic.codegen.service.SysGeneratorService;
import com.suncompass.basic.codegen.util.ConnProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器
 */
@RestController
@RequestMapping("/generator")
public class SysGeneratorController {

    @Autowired
    private DbConfigService dbConfigService;

    @Autowired
    private SysGeneratorService sysGeneratorService;


    @PostMapping("/tables")
    @ResponseBody
    public List getTables(@RequestBody DbConfig dbConfig) {
        if (dbConfig.getId() != null && dbConfig.getId() > 0) {
            dbConfig = dbConfigService.get(dbConfig.getId());
        }
        ConnInterface service = ConnProducer.getConnImplement(dbConfig);
        Connection conn = service.getConnection(dbConfig);
        List list = service.getTableList(conn, null, null, true);
        return list;
    }

    @PostMapping("/column")
    public List getColumn(@RequestBody  GenConfig genConfig){
        DbConfig dbConfig=genConfig.getDb();
        String tableName=genConfig.getTableName();
        ConnInterface service = ConnProducer.getConnImplement(dbConfig);
        Connection conn = service.getConnection(dbConfig);
        List list = service.getColumnList(conn, tableName, true);
        return list;
    }

    @PostMapping("/code")
    public void code(@RequestBody GenConfig genConfig,String groupId, HttpServletResponse response) throws IOException {
        byte[] data = sysGeneratorService.generatorCode(genConfig, groupId);
        String filename = genConfig.getPackageName();
        if (!StringUtils.hasText(filename)) {
            filename = "response";
        }
        response.reset();
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s.zip", filename));
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
    }
}
