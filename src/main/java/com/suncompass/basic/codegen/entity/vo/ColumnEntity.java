package com.suncompass.basic.codegen.entity.vo;

public class ColumnEntity {
    /**
     * 列表
     */
    private String columnName;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 备注
     */
    private String comments;
    /**
     * 驼峰属性
     */
    private String caseAttrName;
    /**
     * 普通属性
     */
    private String lowerAttrName;
    /**
     * 属性类型
     */
    private String attrType;
    /**
     * 其他信息
     */
    private String extra;

    /**
     * 属性名首字母大写
     *
     * @return
     */
    public String getCapitalName() {
        if (this.lowerAttrName.length() <= 1) {
            return this.lowerAttrName.toUpperCase();
        } else {
            String setGetName = this.lowerAttrName;
            String firstChar = setGetName.substring(0, 1);
            return Character.isLowerCase(firstChar.toCharArray()[0]) && Character.isUpperCase(setGetName.substring(1, 2).toCharArray()[0]) ? firstChar.toLowerCase() + setGetName.substring(1) : firstChar.toUpperCase() + setGetName.substring(1);
        }

    }


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCaseAttrName() {
        return caseAttrName;
    }

    public void setCaseAttrName(String caseAttrName) {
        this.caseAttrName = caseAttrName;
    }

    public String getLowerAttrName() {
        return lowerAttrName;
    }

    public void setLowerAttrName(String lowerAttrName) {
        this.lowerAttrName = lowerAttrName;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
