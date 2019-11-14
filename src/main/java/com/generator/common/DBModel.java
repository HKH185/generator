package com.generator.common;
/**
* @author houkaihong
* @Date 15:02 2019/10/22
* @description：TODO
*/
public class DBModel {
    /**
     * 数据库连接URL
     */
    private String DBurl;

    /**
     * 数据库连接驱动
     */
    private String DBdriver;

    /**
     * 数据库类型
     */
    private String DBtype;

    /**
     * 数据库名称
     */
    private String DBname;


    /**
     * 数据库用户名
     */
    private String DBuser;

    /**
     * 数据库密码
     */
    private String DBpwd;

    public String getDBurl() {
        return DBurl;
    }

    public void setDBurl(String burl) {
        DBurl = burl;
    }

    public String getDBdriver() {
        return DBdriver;
    }

    public void setDBdriver(String bdriver) {
        DBdriver = bdriver;
    }

    public String getDBtype() {
        return DBtype;
    }

    public void setDBtype(String btype) {
        DBtype = btype;
    }

    public String getDBname() {
        return DBname;
    }

    public void setDBname(String bname) {
        DBname = bname;
    }

    public String getDBuser() {
        return DBuser;
    }

    public void setDBuser(String buser) {
        DBuser = buser;
    }

    public String getDBpwd() {
        return DBpwd;
    }

    public void setDBpwd(String bpwd) {
        DBpwd = bpwd;
    }


}
