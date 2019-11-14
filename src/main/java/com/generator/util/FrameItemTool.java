package com.generator.util;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author houkaihong
 * @Date 15:03 2019/10/22
 * @description：
 */
public class FrameItemTool {

    /**
     * 将数据库中所有表设置到JComboBox中
     *
     * @param conn
     * @return
     */
    public static JComboBox tablesComboBox(Connection conn) throws SQLException, ClassNotFoundException {
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("请选择");
        try {
            List tableList = DBTool.getAllTableName(conn);
            for (int i = 0; i < tableList.size(); i++) {
                comboBox.addItem(tableList.get(i));
            }
            comboBox.setMaximumRowCount(tableList.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comboBox;
    }


    public static TableModel getColumnData(Connection conn, String tableName) {
        TableModel dataModel = null;
        try {
            final String[] columnNames = {"select", "字段", "数据类型", "长度", "备注(可定义)", "验证类型"};
            final List<Object[]> columnList = DBTool.getAllColumnData(conn, tableName);
            dataModel = new AbstractTableModel() {
                public int getColumnCount() {
                    return columnNames.length;
                }

                public int getRowCount() {
                    return columnList.size();
                }

                public Object getValueAt(int row, int col) {
                    return columnList.get(row)[col];
                }

                public String getColumnName(int column) {
                    return columnNames[column];
                }

                public Class getColumnClass(int c) {
                    return getValueAt(0, c).getClass();
                }

                public boolean isCellEditable(int row, int col) {
                    return col != 1 && col != 2 && col != 3;
                }

                public void setValueAt(Object aValue, int row, int column) {
                    columnList.get(row)[column] = aValue;
                }
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataModel;
    }
}
