package com.generator;

import com.generator.base.Field;
import com.generator.common.DBModel;

import java.util.List;

/**
* @author houkaihong
* @Date 15:05 2019/10/22
* @description：TODO
*/
public class TableData {
    private static final long serialVersionUID = 1L;
    List<Field> rowSelectList;
    private String selecedTable;
    private String objectName;
    private String storePath;
    private DBModel model;
    private String pk;
    private Field pkColumn;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Field> getRowSelectList() {
        return rowSelectList;
    }

    public void setRowSelectList(List<Field> rowSelectList) {
        this.rowSelectList = rowSelectList;
    }

    public String getSelecedTable() {
        return selecedTable;
    }

    public void setSelecedTable(String selecedTable) {
        this.selecedTable = selecedTable;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public DBModel getModel() {
        return model;
    }

    public void setModel(DBModel model) {
        this.model = model;
    }

    public Field getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(Field pkColumn) {
        this.pkColumn = pkColumn;
    }
}
