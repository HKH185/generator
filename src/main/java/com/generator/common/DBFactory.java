package com.generator.common;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 * 数据库工厂类
 *
 * @author houkaihong
 */
public class DBFactory {
    static List<String> sqlList = new ArrayList<String>();
    /**
     * 数据库连接对象
     */
    private DBConnection dbConn = null;

    /**
     * 获取数据库连接对象实例
     * dbType ：数据库类型 ：如 mysql,oracle,db2 目前只支持这三种数据库
     *
     * @return 返回DBConnection对象
     */
    public DBConnection getDBConnectionInstance(DBModel db) {
        dbConn = new DBConnection(db);
        return dbConn;
//		/** 如果为null就创建一个新的实例化对象且返回 */
// 		if (dbConn == null || dbConn.) {
//			dbConn = new DBConnection(db);
//			return dbConn;
//		}
//		/** 如果不为null就直接返回当前的实例化对象 */
//		else {
//			return dbConn;
//		}
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    public void closeConnection(Connection conn, DBModel dbType) {
        /** 如果为null就创建一个新的实例化对象 */
        if (dbConn == null) {
            dbConn = new DBConnection(dbType);
        }
        dbConn.closeConnection(conn);
        /** 调用关闭连接的方法 */
    }

    /**
     * 获取某张表的所以列名，并存入Vector中
     *
     * @param tableName 表名
     * @return
     * @throws SQLException
     */
    public static Vector getColumns(Connection conn, DBModel dbConn, String tableName) throws SQLException {
        Vector v = new Vector();
        //预编译的 SQL 语句
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName);
        //获取结果集
        ResultSet rs = pstmt.executeQuery();
        //获取所有字段名
        ResultSetMetaData rsmd = rs.getMetaData();
        if (rsmd != null) {
            int count = rsmd.getColumnCount();
            for (int i = 1; i <= count; i++) {
                System.out.println(tableName + "->ColumnName======" + rsmd.getColumnName(i));
                //判断当前字段是否为主键，如果不是主键则加入到Vector集合中
                if (!DBFactory.isPrimaryKey(conn, tableName, rsmd.getColumnName(i)))
                    v.add(rsmd.getColumnName(i));
            }
        }
        return v;
    }

    /**
     * 判断某表中的字段是否是主键
     *
     * @param tableName  表面
     * @param columnName 列名
     * @return 返回 true 为是主键 ，返回false则不是
     * @throws SQLException
     */
    public static boolean isPrimaryKey(Connection conn, String tableName, String columnName) throws SQLException {
        boolean flag = false;
        DatabaseMetaData dbMeta = conn.getMetaData();
        ResultSet primaryKey = dbMeta.getPrimaryKeys(null, null, tableName);
        while (primaryKey.next()) {
            System.out.print("表名:" + primaryKey.getString(3));
            System.out.print("  列名:" + primaryKey.getString(4));
            System.out.println("  主键名:" + primaryKey.getString(6));
            if (columnName.equals(primaryKey.getString(4))) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 向tableName表中插入Vector集合中的一条数据
     *
     * @param tableName 表名
     * @param datas     Vector封装的一条数据
     * @throws SQLException
     * @throws SQLException
     * @throws SQLException 抛出 SQL异常
     */
    public static void insertData(DBModel dbConn, String tableName, Vector datas, boolean isInsert) throws Exception {
        DBFactory db = new DBFactory();
        // 获取数据库连接
        Connection conn = db.getDBConnectionInstance(dbConn).getConnection();
        // 拼接插入数据的SQL
        StringBuffer buffer = new StringBuffer(200);
        buffer.append("INSERT INTO " + tableName + "(");
        // getColumns方法用于获取所有列的集合
        Vector columns = null;
        try {
            columns = getColumns(conn, dbConn, tableName);
            for (int i = 0; i < columns.size(); i++) {
                buffer.append(columns.get(i) + ",");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        buffer.delete(buffer.length() - 1, buffer.length());
        buffer.append(") ");
        StringBuffer debugBuffer = new StringBuffer(buffer.length() * 4);
        debugBuffer.append(buffer.toString());
        debugBuffer.append("VALUES(");
        // 获取所有Excel中数据的集合 从参数中传过来
        Vector _datas = datas;
        // 如果表中的字段个数与Excel的列数相同的话就进行下面操作
        if (columns.size() == _datas.size()) {
            for (int j = 0; j < _datas.size(); j++) {
                debugBuffer.append("'").append(_datas.get(j)).append("',");
            }
            debugBuffer.delete(debugBuffer.length() - 1, debugBuffer.length());
            debugBuffer.append(")");
            System.out.println(debugBuffer.toString());
            sqlList.add(debugBuffer.toString());
            if (isInsert) {
                PreparedStatement pstmt;
                try {
                    pstmt = conn.prepareStatement(debugBuffer.toString());
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        } else {
            System.out.println("对不起，您所提供的Excel与数据库中表字段个数不一致，请仔细检查后再操作...");
            throw new Exception();
        }
        // 关闭数据库链接
        db.closeConnection(conn, dbConn);
    }

}
