package com.suncompass.basic.codegen.service;
import com.suncompass.basic.codegen.entity.dto.Template;

import java.util.List;

public interface TemplateService extends BaseService<Template>{


    List<Template> getGroupIdAndTemplateName(String groupId,String templateName);

}
