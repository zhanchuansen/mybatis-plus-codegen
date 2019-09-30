package com.suncompass.basic.codegen.service.impl;

import cn.hutool.core.io.IoUtil;
import com.suncompass.basic.codegen.entity.dto.DbConfig;
import com.suncompass.basic.codegen.entity.dto.Template;
import com.suncompass.basic.codegen.entity.vo.GenConfig;
import com.suncompass.basic.codegen.entity.vo.TableEntity;
import com.suncompass.basic.codegen.entity.vo.TemplateEntity;
import com.suncompass.basic.codegen.mapper.DbConfigMapper;
import com.suncompass.basic.codegen.mapper.TemplateMapper;
import com.suncompass.basic.codegen.service.ConnInterface;
import com.suncompass.basic.codegen.service.SysGeneratorService;
import com.suncompass.basic.codegen.util.ConnProducer;
import com.suncompass.basic.codegen.util.GenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 */
@Service
public class SysGeneratorServiceImpl implements SysGeneratorService {

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private DbConfigMapper dataSourceMapper;

    /**
     * 生成代码
     *
     * @param genConfig 生成配置
     * @return
     */
    @Override
    public byte[] generatorCode(GenConfig genConfig, String groupId) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        DbConfig dbConfig = genConfig.getDb();
        if (dbConfig.getId() != null && dbConfig.getId() > 0) {
            dbConfig = dataSourceMapper.get(dbConfig.getId());
        }

        ConnInterface service = ConnProducer.getConnImplement(dbConfig);
        Connection conn = service.getConnection(dbConfig);

        String tablePrefix = genConfig.getTablePrefix();
        List<TableEntity> tableNames = genConfig.getTableNames();
        String[] names = null;
        if (tableNames != null && tableNames.size() > 0) {
            names = tableNames.stream().map(o -> o.getTableName()).toArray(size -> new String[size]);
        }

        String projectPath = System.getProperty("user.dir");
        final String res = "template";
        try {
            Template condition = new Template();
            condition.setGroupId(groupId);
            List<Template> templateList = templateMapper.selectAll(0, 99, condition);
            if (templateList == null) {
                templateList = new ArrayList<>();
            }
            List<TemplateEntity> templates = GenUtils.getDefaultTemplates();
            for (TemplateEntity templateEntity : templates) {
                String templateName = templateEntity.getName();
                Optional<Template> optional = templateList.stream().filter(f -> f.getTemplateName().equals(templateName)).findFirst();
                if (!Optional.empty().equals(optional)) {
                    Template template = optional.get();
                    String templatePath = template.getTemplatePath();
                    File file = new File(projectPath + templatePath);
                    if (file.exists()) {
                        int index = templatePath.indexOf(res);
                        templatePath = templatePath.substring(index);
                        templateEntity.setPath(templatePath);
                    }
                }
            }

            List<Map<String, Object>> tables = service.getTableList(conn, tablePrefix, names, false);
            for (int i = 0; i < tables.size(); i++) {
                Map<String, Object> table = tables.get(i);
                List<Map<String, Object>> columns = service.getColumnList(conn, table.get("tableName").toString(), false);
                new GenUtils().generatorCode(genConfig, table, columns, zip, templates); //生成代码
            }

        } finally {
            try {
                conn.close();
            } catch (SQLException e) {

            }
        }

        IoUtil.close(zip);
        return outputStream.toByteArray();
    }


}
