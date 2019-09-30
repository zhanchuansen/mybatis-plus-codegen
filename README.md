# mybatis-plus-codegen

1.代码生成器结构如下
  src 为代码结构
  template 为模板目录供前端选择多种风格、可以自己定义
2.操作如下
   》模板管理
      》》添加模板组
      》》添加模板风格
      templateName：模板名称
      fileName：文件名称
      groupId：所属模板组
      content：内容
      Request URL: http://192.168.100.8:10010/code/Template/save
      Request Method: POST
      {"templateName":"List.vue.vm","fileName":"List","groupId":"2","content":"1111"}
   》配置列表
      》》添加数据库配置
      》》点击操作按钮查询该数据库所有表
      Request URL: http://192.168.100.8:10010/code/generator/tables
      Request Method: POST
      JSON参数格式
      {"id":1,"dbType":"MySql","ip":"192.168.100.8","port":5509,
      "database":"mysql","username":"root","password":"suncompass","characterEncoding":"utf-8"}
      
      》》选中所有表点击生成
      Request URL: http://192.168.100.8:10010/code/generator/code?groupId=1
      Request Method: POST
      JSON参数格式
      {"packageName":"1","groupId":"1","projectName":"1",
      "db":{"id":4,"dbType":"MySql","ip":"192.168.100.8","port":5509,"database":"exam","username":"root","password":"suncompass","characterEncoding":"utf-8"},
      "tableNames":[{"tableName":"answer"},{"tableName":"exam"},{"tableName":"paper"},{"tableName":"paper_category"},{"tableName":"quest_option"},{"tableName":"question"},{"tableName":"sys_dictionary"}]}
      
      
  
  
