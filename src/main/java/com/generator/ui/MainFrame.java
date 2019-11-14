package com.generator.ui;

import com.generator.common.DBConnection;
import com.generator.common.DBModel;
import com.generator.util.Config;
import com.generator.util.DBTool;
import com.generator.util.FrameTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
* @author houkaihong
* @Date 15:02 2019/10/22
* @description：TODO
*/
public class MainFrame extends JFrame implements ActionListener {
    JPanel panel = new JPanel();

    JLabel dbType_lab = new JLabel("数据库类型："),
            dbUrl_lab = new JLabel("数据库URL："),
            dbName_lab = new JLabel("数据库名字："),
            userName_lab = new JLabel("用户名："),
            password_lab = new JLabel("密码：");
    JTextField dbUrl_txt = new JTextField();
    JTextField dbName_txt = new JTextField();
    JTextField userName_txt = new JTextField();
    JPasswordField password_txt = new JPasswordField();
    JComboBox dbType = new JComboBox();

    JButton button1 = new JButton("确 定"), button2 = new JButton("退出");

    public MainFrame() {
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createTitledBorder("请输入下面相关信息："));
        dbType_lab.setBounds(20, 40, 120, 30);
        dbUrl_lab.setBounds(20, 90, 120, 30);
        dbName_lab.setBounds(20, 140, 120, 30);
        userName_lab.setBounds(20, 190, 120, 30);
        password_lab.setBounds(20, 240, 120, 30);
        dbType.setBounds(115, 40, 200, 30);
        dbType.addItem(DBConnection.MYSQL_FLAG);
        dbType.addItem(DBConnection.ORACLE_FLAG);
        dbUrl_txt.setBounds(115, 90, 200, 30);
        //dbUrl_txt.setText("jdbc:mysql://10.0.0.135:3306/");
        dbUrl_txt.setText("jdbc:mysql://120.78.76.207:3306/");
        dbName_txt.setBounds(115, 140, 200, 30);
        userName_txt.setBounds(115, 190, 200, 30);
        //userName_txt.setText("dcpopr");
        userName_txt.setText("dcp");
        password_txt.setBounds(115, 240, 200, 30);
        button1.setBounds(70, 280, 100, 30);
        button2.setBounds(180, 280, 100, 30);

        Properties config = Config.read();
        if (config != null) {
            dbName_txt.setText((String) config.get("dbName"));
            password_txt.setText((String) config.get("dbPwd"));
        }

        panel.add(dbUrl_lab);
        panel.add(dbName_lab);
        panel.add(userName_lab);
        panel.add(password_lab);
        panel.add(dbUrl_txt);
        panel.add(dbName_txt);
        panel.add(userName_txt);
        panel.add(password_txt);
        panel.add(dbType_lab);
        panel.add(dbType);
        panel.add(button1);
        panel.add(button2);

        button1.addActionListener(this);
        button2.addActionListener(this);

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            String db_url = dbUrl_txt.getText().trim();
            String db_name = dbName_txt.getText().trim();
            String db_Type = dbType.getSelectedItem().toString();
            String userName = userName_txt.getText().trim();
            String password = password_txt.getText().trim();
            if (!"".equals(userName) && !"".equals(password)) {
                DBModel model = new DBModel();
                StringBuffer sqlUrl = new StringBuffer();
                sqlUrl.append(db_url);
                if (db_url.substring(db_url.length() - 1, db_url.length()).equals("/")) {
                    sqlUrl.append(db_name);
                } else {
                    sqlUrl.append("/").append(db_name);
                }
                sqlUrl.append("?characterEncoding=UTF-8");
                model.setDBurl(sqlUrl.toString());
                model.setDBtype(db_Type);
                model.setDBuser(userName);
                model.setDBpwd(password);

                try {
                    Connection conn = DBTool.getDBCon(model);
                    Map paramMap = new HashMap<String, String>();
                    paramMap.put("dbName", db_name);
                    paramMap.put("dbPwd", password);
                    Config.write(paramMap);
                    showTableFrame(conn, model);
                    this.setVisible(false);
                } catch (SQLException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    JOptionPane.showMessageDialog(null, "数据库连接异常，错误信息：" + e1, "Generator提醒您", JOptionPane.ERROR_MESSAGE);
                } catch (ClassNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, "系统出现异常..." + e1, "Generator提醒您", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


            } else if ("".equals(userName)) {
                JOptionPane.showMessageDialog(null, "请输入用户名", "Generator提醒您", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "用户名密码不允许为空！！", "Generator提醒您", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == button2){
            System.exit(0);
        }
    }

    public void showTableFrame(Connection conn, DBModel model) {
        TableFrame tableF = new TableFrame(conn, model);
        tableF.setResizable(false);
        tableF.setSize(700, 400);
        FrameTool.setCenter(tableF);
        tableF.setDefaultCloseOperation(MainFrame.EXIT_ON_CLOSE);
        tableF.setTitle("Generator v0.1");
        tableF.setVisible(true);
    }
}
