package com.suncompass.basic.codegen.service;

import com.suncompass.basic.codegen.entity.vo.GenConfig;

/**
 * @author lengleng
 * @date 2019/2/1
 */
public interface SysGeneratorService {
	/**
	 * 生成代码
	 *
	 * @param tableNames 表名称
	 * @return
	 */
	byte[] generatorCode(GenConfig tableNames,String groupId);
}
