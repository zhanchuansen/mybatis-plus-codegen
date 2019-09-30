package com.suncompass.basic.codegen.controller;

import cn.hutool.core.util.CharsetUtil;
import com.suncompass.basic.codegen.entity.dto.Template;
import com.suncompass.basic.codegen.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Template")
public class TemplateController extends BaseController<Template> {

    @Autowired
    private TemplateService templateService;

    /**
     * 插入模板
     *
     * @param template
     * @return
     * @throws IOException
     */
    @RequestMapping("/insert")
    public ResponseEntity insert(@RequestBody Template template) throws IOException {
        List<Template> list = templateService.getGroupIdAndTemplateName(template.getGroupId(), template.getTemplateName());
        if (list != null && list.size() > 0) {
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED).body("模板名已存在");
        }
        String projectPath = System.getProperty("user.dir");//获取当前项目路径的地址
        System.out.println(projectPath);
        String[] splits = template.getTemplateName().split("\\.");
        splits[0] += "_" + UUID.randomUUID().toString().replace("-", "");
        String fileName = String.join(".", splits);
        String filePath = "/template/custom/" + fileName; //模板所在路径
        template.setTemplatePath(filePath);
        String content = template.getContent() != null ? template.getContent() : "";
        File file = new File(projectPath + filePath);
        System.out.println(file);
        if (!file.getParentFile().exists()) {//判断文件父目录是否存在
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(file), CharsetUtil.UTF_8);
        oStreamWriter.write(content);
        oStreamWriter.flush();
        return ResponseEntity.ok(templateService.insert(template));
    }

    /**
     * 查找当前模板读取模板内容
     *
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping("/get")
    public ResponseEntity get(@RequestParam("id") Serializable id) throws IOException {
        Template entity = templateService.get(id);
        String projectPath = System.getProperty("user.dir");
        String filePath = entity.getTemplatePath();
        File file = new File(projectPath + filePath);
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), CharsetUtil.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(read);
        StringBuilder content = new StringBuilder();
        String lineTxt = null;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            if (content.length() > 0) {
                content.append(System.lineSeparator());
            }
            content.append(lineTxt);
        }
        read.close();
        entity.setContent(content.toString());
        return ResponseEntity.ok(entity);
    }

    //修改模板
    @RequestMapping("update")
    public ResponseEntity update(@RequestBody Template template) throws IOException {
        Template entity = templateService.get(template.getId());
        String projectPath = System.getProperty("user.dir");
        String filePath = entity.getTemplatePath();
        File file = new File(projectPath + filePath);
        OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(file), CharsetUtil.UTF_8);
        String content = template.getContent();
        oStreamWriter.write(content);
        oStreamWriter.flush();
        return ResponseEntity.ok(templateService.update(template));
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody Template template) throws IOException {
        if (template.getId() == null || template.getId() == 0) {
            return this.insert(template);
        } else {
            return this.update(template);
        }
    }
}
