package com.suncompass.basic.codegen.service.impl;

import com.suncompass.basic.codegen.entity.dto.Template;
import com.suncompass.basic.codegen.mapper.TemplateMapper;
import com.suncompass.basic.codegen.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TemplateServiceImpl extends BaseServiceImpl<Template> implements TemplateService {

    @Autowired
    private TemplateMapper templateMapper;

    @Override
    public List<Template> getGroupIdAndTemplateName(String groupId, String templateName) {
        return templateMapper.getGroupIdAndTemplateName(groupId,templateName);
    }
}
