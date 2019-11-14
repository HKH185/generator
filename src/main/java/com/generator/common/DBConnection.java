package com.generator.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * 数据库链接类
 * 适用于oracle，db2，mysql
 * 通过构造函数传入值区别是那种数据库
 *
 * @author houkaihong
 */
public class DBConnection {
    public static final String ORACLE_FLAG = "oracle";     //TODO
    public static final String DB2_FLAG = "db2";          //TODO
    public static final String MYSQL_FLAG = "mysql";
    /**
     * 数据库连接URL
     */
    private String DBurl;

    /**
     * 数据库连接驱动
     */
    private String DBdriver;

    /**
     * 数据库用户名
     */
    private String DBuser;

    /**
     * 数据库密码
     */
    private String DBpwd;


    public DBConnection(DBModel model) {
        this.initDBConfig(model);
    }

    /**
     * 初始化数据库值
     */
    private void initDBConfig(DBModel model) {

        if (MYSQL_FLAG.equals(model.getDBtype())) {
            DBdriver = "com.mysql.jdbc.Driver";
        }
        if(ORACLE_FLAG.equals(model.getDBtype())){
            DBdriver = "oracle.jdbc.driver.OracleDriver";
        }
        DBurl = model.getDBurl();
        DBuser = model.getDBuser();
        DBpwd = model.getDBpwd();

    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    private boolean checkIsNull(String str) {
        boolean flag = false;
        if ("".equals(str) || str == null) {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        /** 声明Connection连接对象 */
        Connection conn = null;

        /** 使用Class.forName()方法自动创建这个驱动程序的实例且自动调用DriverManager来注册它 */
        Class.forName(DBdriver);
        /** 通过DriverManager的getConnection()方法获取数据库连接 */
        conn = (Connection) DriverManager.getConnection(DBurl, DBuser, DBpwd);

        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    public void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                /** 判断当前连接连接对象如果没有被关闭就调用关闭方法 */
                if (!conn.isClosed()) {
                    conn.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("关闭数据库连接异常：" + ex);
        }
    }

    public static void main(String[] args) {
//		new DBConnection(MYSQL_FLAG).getConnection();
    }
}
