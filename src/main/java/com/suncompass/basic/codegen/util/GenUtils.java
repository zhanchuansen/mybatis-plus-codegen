package com.suncompass.basic.codegen.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.suncompass.basic.codegen.entity.vo.ColumnEntity;
import com.suncompass.basic.codegen.entity.vo.GenConfig;
import com.suncompass.basic.codegen.entity.vo.TableEntity;
import com.suncompass.basic.codegen.entity.vo.TemplateEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器工具类
 */
public class GenUtils {

    private static final String ENTITY_JAVA_VM = "Entity.java.vm";
    private static final String MAPPER_JAVA_VM = "Mapper.java.vm";
    private static final String MAPPER_XML_VM = "Mapper.xml.vm";
    private static final String SERVICE_JAVA_VM = "Service.java.vm";
    private static final String SERVICE_IMPL_JAVA_VM = "ServiceImpl.java.vm";
    private static final String CONTROLLER_JAVA_VM = "Controller.java.vm";
    private static final String BASE_CONTROLLER_TXT_VM = "BaseController.txt.vm";
    private static final String APPLICATION_JAVA_VM = "WebApplication.java.vm";
    private static final String POM_TXT = "pom.txt.vm";
    private static final String MYBATIS_PLUS_CONFIG = "myBatisPlusConfig.txt.vm";


    private static final String LIST_VUE_VM = "List.vue.vm";
    private static final String DETAIL_VUE_VM = "Detail.vue.vm";

    public static List<TemplateEntity> getDefaultTemplates() {
        List<TemplateEntity> templates = new ArrayList<>();
        templates.add(new TemplateEntity(ENTITY_JAVA_VM, "template/Entity.java.vm"));
        templates.add(new TemplateEntity(MAPPER_JAVA_VM, "template/Mapper.java.vm"));
        templates.add(new TemplateEntity(MAPPER_XML_VM, "template/Mapper.xml.vm"));
        templates.add(new TemplateEntity(SERVICE_JAVA_VM, "template/Service.java.vm"));
        templates.add(new TemplateEntity(SERVICE_IMPL_JAVA_VM, "template/ServiceImpl.java.vm"));
        templates.add(new TemplateEntity(CONTROLLER_JAVA_VM, "template/Controller.java.vm"));
        templates.add(new TemplateEntity(BASE_CONTROLLER_TXT_VM, "template/BaseController.txt.vm"));
        templates.add(new TemplateEntity(APPLICATION_JAVA_VM, "template/WebApplication.java.vm"));
        templates.add(new TemplateEntity(POM_TXT, "template/pom.txt.vm"));
        templates.add(new TemplateEntity(MYBATIS_PLUS_CONFIG, "template/myBatisPlusConfig.txt.vm"));
        templates.add(new TemplateEntity(LIST_VUE_VM, "template/List.vue.vm"));
        templates.add(new TemplateEntity(DETAIL_VUE_VM, "template/Detail.vue.vm"));

        return templates;
    }

    /**
     * 生成代码
     */
    public void generatorCode(GenConfig genConfig, Map<String, Object> table, List<Map<String, Object>> columns, ZipOutputStream zip, List<TemplateEntity> templates) {

        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName").toString());
        if (StrUtil.isNotBlank(genConfig.getComments())) {
            tableEntity.setComments(genConfig.getComments());
        } else {
            tableEntity.setComments(table.get("tableComment").toString());
        }
        String tablePrefix;
        if (StrUtil.isNotBlank(genConfig.getTablePrefix())) {
            tablePrefix = genConfig.getTablePrefix();
        } else {
            tablePrefix = config.getString("tablePrefix");
        }

        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), tablePrefix);
        tableEntity.setCaseClassName(className);
        tableEntity.setLowerClassName(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columnList = new ArrayList<>();
        for (Map<String, Object> column : columns) {

            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName").toString());
            columnEntity.setDataType(column.get("dataType").toString());
            columnEntity.setComments(column.get("columnComment").toString());
            columnEntity.setExtra(column.get("extra").toString());


            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setCaseAttrName(attrName);
            columnEntity.setLowerAttrName(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && "BigDecimal".equals(attrType)) {
                hasBigDecimal = true;
            }

            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey").toString()) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columnList.add(columnEntity);
        }
        tableEntity.setColumns(columnList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }
        //设置velocity资源加载器
        Properties prop = new Properties();
        //prop.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH,"D:/代码生成器/");//打包成jar资源路径释放
        prop.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, "E:/pig-codegen/");//本地运行指定盘

        Velocity.init(prop);
        //封装模板数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("tableName", tableEntity.getTableName());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getCaseClassName());
        map.put("classname", tableEntity.getLowerClassName());
        map.put("pathName", tableEntity.getLowerClassName().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("datetime", new Date());

        if (StrUtil.isNotBlank(genConfig.getComments())) {
            map.put("comments", genConfig.getComments());
        } else {
            map.put("comments", tableEntity.getComments());
        }

        if (StrUtil.isNotBlank(genConfig.getAuthor())) {
            map.put("author", genConfig.getAuthor());
        } else {
            map.put("author", config.getString("author"));
        }

        if (StrUtil.isNotBlank(genConfig.getModuleName())) {
            map.put("moduleName", genConfig.getModuleName());
        } else {
            map.put("moduleName", config.getString("moduleName"));
        }

        if (StrUtil.isNotBlank(genConfig.getPackageName())) {
            map.put("package", genConfig.getPackageName());
            map.put("mainPath", genConfig.getPackageName());
        } else {
            map.put("package", config.getString("package"));
            map.put("mainPath", config.getString("mainPath"));
        }
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        //List<String> templates = getTemplates();
        for (TemplateEntity templateEntity : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(templateEntity.getPath(), CharsetUtil.UTF_8);
            tpl.merge(context, sw);

            try {
                //添加到zip
                String fileName = getFileName(templateEntity.getName(), tableEntity.getCaseClassName(), map.get("package").toString(), genConfig.getProjectName());
                zip.putNextEntry(new ZipEntry(Objects.requireNonNull(fileName)));
                IoUtil.write(zip, CharsetUtil.UTF_8, false, sw.toString());
                IoUtil.close(sw);
                zip.closeEntry();
            } catch (IOException e) {
                //throw new CheckedException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }


    /**
     * 列名转换成Java属性名
     */
    private String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    private String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    private Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            return null;
        }
    }

    /**
     * 获取文件名
     */
    private String getFileName(String template, String className, String packageName, String projectName) {
        if (!org.springframework.util.StringUtils.hasText(projectName)) {
            projectName = "product";
        }

        String packagePath = projectName + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;

        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains(ENTITY_JAVA_VM)) {
            return packagePath + "entity" + File.separator + className + ".java";
        }

        if (template.contains(MAPPER_JAVA_VM)) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }

        if (template.contains(SERVICE_JAVA_VM)) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains(SERVICE_IMPL_JAVA_VM)) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains(CONTROLLER_JAVA_VM)) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains(APPLICATION_JAVA_VM)) {
            return packagePath + "WebApplication.java";
        }

        String pomPath = projectName + File.separator;
        if (template.contains(POM_TXT)) {
            return pomPath + "pom.txt";
        }
        if (template.contains(MYBATIS_PLUS_CONFIG)) {
            return pomPath + "myBatisPlusConfig.txt";
        }

        if (template.contains(BASE_CONTROLLER_TXT_VM)) {
            return pomPath + "BaseController.txt";
        }

        if (template.contains(MAPPER_XML_VM)) {
            return projectName + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + className + "Mapper.xml";
        }

        String frontName = projectName + "-UI";
        if (template.contains(LIST_VUE_VM)) {
            return frontName + File.separator + "src" + File.separator + "views" + File.separator + className.toLowerCase() + File.separator + className + "List.vue";
        }

        if (template.contains(DETAIL_VUE_VM)) {
            return frontName + File.separator + "src" + File.separator + "views" + File.separator + className.toLowerCase() + File.separator + className + "Detail.vue";
        }
        return null;
    }
}