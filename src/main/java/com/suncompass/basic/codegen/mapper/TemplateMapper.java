package com.suncompass.basic.codegen.mapper;
import com.suncompass.basic.codegen.entity.dto.DbConfig;
import com.suncompass.basic.codegen.entity.dto.Template;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateMapper extends BaseMapper<Template>{

    List<Template> getGroupIdAndTemplateName(@Param("groupId") String groupId, @Param("templateName") String templateName);
}
