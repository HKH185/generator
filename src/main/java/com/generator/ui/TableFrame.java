package com.generator.ui;

import com.generator.Main;
import com.generator.TableData;
import com.generator.base.Field;
import com.generator.base.RegexField;
import com.generator.common.DBModel;
import com.generator.generate.FileGenerate;
import com.generator.util.Config;
import com.generator.util.DBTool;
import com.generator.util.FrameItemTool;
import org.apache.commons.lang.StringUtils;
import org.jb2011.lnf.beautyeye.utils.JVM;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
* @author houkaihong
* @Date 15:02 2019/10/22
* @description：TODO
*/
public class TableFrame extends JFrame implements ActionListener {
    Connection conn = null;
    JLabel tablesLab = new JLabel("请选择表："),
            objectLab = new JLabel("对象命名："),
            storePathLab = new JLabel("存储路径：");

    JTextField storePathTxt = new JTextField();
    JTextField objectTxt = new JTextField();
    JFileChooser outputPath = new JFileChooser();

    JComboBox tablesCombox = new JComboBox();
    JList jlistTables;
    DefaultListModel jlistModelTables;
    JList jlistLeft;

    JButton commit = new JButton("确 定");
    JButton back = new JButton("返回");
    JButton openFc = new JButton("选择");


    private final DBModel dbModel;
    private JTable tableView;

    public TableFrame(Connection conn, DBModel dbModel) {

        super();
        this.conn = conn;
        this.dbModel = dbModel;
        try {
            initGUI(conn);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void initGUI(Connection conn) throws Exception {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        outputPath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        {
            tablesLab.setBounds(20, 20, 70, 30);
            getContentPane().add(tablesLab);
        }
        {
            objectLab.setBounds(370, 20, 70, 30);
            getContentPane().add(objectLab);
        }
        {
            objectTxt.setBounds(440, 20, 230, 30);
            getContentPane().add(objectTxt);
        }
        {
            tablesCombox = FrameItemTool.tablesComboBox(conn);
            tablesCombox.setMaximumRowCount(10);
            tablesCombox.setBounds(100, 20, 230, 30);
            getContentPane().add(tablesCombox);
            tablesCombox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        tablesComboxActionPerformed(actionEvent);
                    } catch (SQLException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            });
        }
        {//没有选中事件
            jlistModelTables = new DefaultListModel();
            jlistTables = new JList(jlistModelTables);
            jlistTables.setVisibleRowCount(1);
            JScrollPane jspTables = new JScrollPane(jlistLeft);
            jspTables.setBounds(130, 20, 120, 30);

        }
        {
            tableView = new JTable();
            tableView.setModel(new DefaultTableModel() {
                @Override
                public Class getColumnClass(int c) {
                    Object value = getValueAt(0, c);
                    if (value != null) {
                        return value.getClass();
                    }else{
                        return super.getClass();
                    }
                }
            });
            tableView.setRowHeight(25);
            JScrollPane scrollpane = new JScrollPane(tableView);
            scrollpane.setBounds(20, 70, 655, 200);
            getContentPane().add(scrollpane);

        }

        {
            storePathLab.setBounds(20, 295, 240, 20);
            getContentPane().add(storePathLab);
        }
        {
            storePathTxt.setBounds(90, 290, 470, 30);
            Properties config = Config.read();
            if (config != null) {
                storePathTxt.setText((String) config.get("outpath"));
            }
            getContentPane().add(storePathTxt);
        }
        {
            openFc.setBounds(575, 290, 100, 30);
            openFc.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int returnVal = outputPath.showOpenDialog(TableFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        storePathTxt.setText(outputPath.getSelectedFile().getPath());
                    }
                }
            });
            getContentPane().add(openFc);
        }
        {
            commit.setBounds(220, 330, 100, 30);
            getContentPane().add(commit);
            commit.addActionListener(this);
        }
        {
            back.setBounds(380, 330, 100, 30);
            getContentPane().add(back);
            back.addActionListener(this);
        }

        pack();
        setSize(370, 450);
        setLocationRelativeTo(null);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == commit) {
            String selecedTable = tablesCombox.getSelectedItem().toString().trim();
            if (!"请选择".equals(selecedTable)) {

                int rowSize = tableView.getModel().getRowCount();
                java.util.List<Field> rowSelectList = new ArrayList();
                for (int i = 0; i < rowSize; i++) {

                    Boolean isSelect = (Boolean) tableView.getModel().getValueAt(i, 0);
                    String columnName = (String) tableView.getModel().getValueAt(i, 1);

                    if (isSelect) {
                        try {
                            String remark = (String) tableView.getModel().getValueAt(i, 4);
                            Object regexField = tableView.getModel().getValueAt(i, 5);
                            Field field = DBTool.getColumnField(conn, dbModel.getDBtype(), selecedTable, columnName);
                            if (!field.getColumnComment().equals(remark)) {
                                field.setColumnComment(remark);
                            }
                            if (regexField instanceof RegexField) {
                                field.setRegex(((RegexField) regexField).getKey());
                            }
                            rowSelectList.add(field);
                        } catch (SQLException e1) {
                            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }

                String storePath = storePathTxt.getText().trim();
                String objectName = objectTxt.getText().trim();

                if (rowSelectList.size() <= 0) {
                    JOptionPane.showMessageDialog(null, "至少要添加一个字段");
                    return;
                }
                if (StringUtils.isBlank(objectName)) {
                    JOptionPane.showMessageDialog(null, "对象名不能为空");
                    return;
                }

                if (StringUtils.isBlank(storePath)) {
                    JOptionPane.showMessageDialog(null, "存储路径不可为空");
                    return;
                } else {
                    Map paramMap = new HashMap<String, String>();
                    paramMap.put("outpath", storePath);
                    Config.write(paramMap);
                }

                try {
                    String pkName = DBTool.getPrimaryKey(conn, selecedTable);
                    TableData tableData = new TableData();
                    tableData.setPk(pkName);
                    tableData.setSelecedTable(selecedTable);
                    tableData.setObjectName(objectTxt.getText());
                    tableData.setStorePath(storePath);
                    tableData.setRowSelectList(rowSelectList);
                    for(Field field:rowSelectList){
                        if(pkName!=null&&pkName.equals(field.getColumnName())){
                            tableData.setPkColumn(field);
                        }
                    }
                    tableData.setModel(dbModel);
                    FileGenerate.engineEntry(tableData, conn);
                    JOptionPane.showMessageDialog(null, "已经成功生成");
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "系统出现异常..." + e1, "提醒您", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "请选择表！", "提醒您", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == back) {
            this.setVisible(false);
            Main.main(null);
        }
    }


    /**
     * tablesCombox 选中表后列出所有字段列
     */
    private void tablesComboxActionPerformed(ActionEvent evt) throws SQLException, ClassNotFoundException {
        String selectedTable = tablesCombox.getSelectedItem().toString();
        if (conn != null) {
            if ("请选择".equals(selectedTable)) {
                JOptionPane.showMessageDialog(null, "请选择表", "STAR提醒您", JOptionPane.ERROR_MESSAGE);
            } else {
                String objectName = DBTool.clearString(selectedTable);
                objectTxt.setText(objectName);

                TableModel tableModel = FrameItemTool.getColumnData(conn, selectedTable);
                if (JVM.current().isOrLater(JVM.JDK1_6)) {

                    try {
                        //以下代码完成：TableRowSorter sorter = new TableRowSorter(dataModel);
                        Class c = Class.forName("javax.swing.table.TableRowSorter");
                        Constructor constructor = c.getConstructor(TableModel.class); //构造函数参数列表的class类型
                        Object trs = constructor.newInstance(tableModel); //传参

                        //以下代码完成：tableView.setRowSorter(sorter);
                        Method m2 = JTable.class.getMethod("setRowSorter", Class.forName("javax.swing.RowSorter"));//注意反身时，参数类只能本类本身，子类是不行的（比如不能直接用c）
                        m2.invoke(tableView, trs);
                    } catch (Exception e) {
                        System.err.println("错误：为1.6及更高版本设置表格排序支持失败," + e.getMessage());
                    }
                }


                tableView.setModel(tableModel);
                final MyCheckBoxRenderer check = new MyCheckBoxRenderer();
                check.setSelected(true);
                tableView.getColumn("select").setHeaderRenderer(check);
                tableView.getColumn("select").setMaxWidth(30);
                tableView.getTableHeader().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (tableView.getColumnModel().getColumnIndexAtX(e.getX()) == 0) {//如果点击的是第0列，即checkbox这一列
                            JCheckBox Checkbox = (JCheckBox) check;
                            boolean b = !check.isSelected();
                            check.setSelected(b);
                            tableView.getTableHeader().repaint();
                            for (int i = 0; i < tableView.getRowCount(); i++) {
                                tableView.getModel().setValueAt(b, i, 0);//把这一列都设成和表头一样
                            }
                        }
                    }
                });


                JComboBox validationBox = new JComboBox();

                setValidationBoxValue(validationBox);

                TableColumn valColumn = tableView.getColumn("验证类型");
                valColumn.setCellEditor(new DefaultCellEditor(validationBox));
                valColumn.setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    protected void setValue(Object value) {
                        if (value instanceof RegexField) {
                            RegexField regexField = (RegexField) value;
                            setText(regexField.getName());
                        } else {
                            super.setValue(value);
                        }

                    }
                });
            }
        }
    }

    private void setValidationBoxValue(JComboBox validationBox) {
        validationBox.addItem("");
        validationBox.addItem(new RegexField("en", "纯英文"));
        validationBox.addItem(new RegexField("number", "纯数字"));
        validationBox.addItem(new RegexField("account", "字母数字下划线"));
        validationBox.addItem(new RegexField("email", "电子邮箱"));
        validationBox.addItem(new RegexField("url", "网址"));
        validationBox.addItem(new RegexField("id", "身份证"));
        validationBox.addItem(new RegexField("phone", "电话号码"));
        validationBox.addItem(new RegexField("zip", "中国邮政编码"));
        validationBox.addItem(new RegexField("custom", "自定义"));
    }

    class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

        public MyCheckBoxRenderer() {
            this.setBorderPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            // TODO Auto-generated method stub
            return this;
        }
    }
}
