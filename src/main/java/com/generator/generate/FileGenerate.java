package com.generator.generate;

import com.generator.TableData;
import com.generator.base.Field;
import com.generator.util.DBTool;
import com.generator.util.DateUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
* @author houkaihong
* @Date 15:02 2019/10/22
* @description：TODO
*/
public class FileGenerate {

    static String appPath = System.getProperty("user.dir") + File.separator;
    static String templatePath = appPath + "template" + File.separator;


    public static void engineEntry(TableData tableData, Connection conn) {

        List<File> allTemplates = new ArrayList<File>();
        getAllTemplate(templatePath, allTemplates);

        try {
            for (File tplFile : allTemplates) {
                String tplPath = getVelocityTplPath(tplFile);
                String outPath = getOutPath(tableData, tplFile);
                Template tpl = Velocity.getTemplate(tplPath, "UTF-8");
                File outFile = new File(outPath);
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                PrintWriter pw = new PrintWriter(outPath,"UTF-8");

                tpl.merge(getContext(tableData, conn), pw);
                pw.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static VelocityContext getContext(TableData tableData, Connection conn) throws SQLException {
        VelocityContext context = new VelocityContext();
        //context.put("objectName", tableData.getObjectName());

        context.put("dbName", tableData.getModel().getDBname());
        context.put("pk", tableData.getPk());


        Map<String,String> resultMap=getTableInfo(tableData.getSelecedTable(),conn);
        context.put("functionName", resultMap.get("tableComment"));

        context.put("author", DBTool.getConfigInfo("author"));
        List<Field> fieldList = getFieldList(tableData, conn);
        context.put("columns", fieldList);

        context.put("basePackage", DBTool.getConfigInfo("basePackage"));
        context.put("moduleName", DBTool.getConfigInfo("moduleName"));
        context.put("className", getClassName(tableData.getObjectName()));
        context.put("tableName", tableData.getSelecedTable());
        String[] arrStr = getMappingPrefix(tableData.getSelecedTable());
        context.put("tableNamePrefix1", arrStr[0]);
        context.put("tableNamePrefix2", arrStr[1]);
        context.put("datetime", DateUtils.getDate());
        context.put("pkColumn", tableData.getPkColumn());
        context.put("entityName", DBTool.getConfigInfo("entityName"));
        context.put("ClassName", tableData.getObjectName());
        context.put("packageName", DBTool.getConfigInfo("basePackage")+"."+DBTool.getConfigInfo("moduleName"));



        return context;
    }
    private static String[] getMappingPrefix(String str){
        String [] elements = str.split("_");
        StringBuffer sb = new StringBuffer();
        for (int i=1; i<elements.length;i++) {
            if(i==1){
                sb.append(elements[i]);
            }else {
                sb.append(elements[i].substring(0, 1).toUpperCase()).append(elements[i].substring(1, elements[i].length()));
            }
        }
        String[] result = new String[2];
        result[0]=elements[0];
        result[1]=sb.toString();
        return result;
    }
    private static String getClassName(String str){
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isUpperCase(c)&&i==0) {
                    sb.append(Character.toLowerCase(c));
                } else  {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    private static Map<String,String> getTableInfo(String tableName,Connection conn){
        PreparedStatement ps = null;
        ResultSet result = null;
        Map<String,String> resultMap = new HashMap<>();
        try {
            if(conn!=null){
                String sqlStr = "select table_name, table_comment, create_time, update_time from information_schema.tables\n" +
                        "\t\twhere table_name NOT LIKE 'qrtz_%' and table_name NOT LIKE 'gen_%' and table_schema = (select database())\n" +
                        "\t\tand table_name = '"+tableName+"'";
                //创建prepareStatement对象，用于执行SQL
                ps = conn.prepareStatement(sqlStr);
                //获取查询结果集
                result = ps.executeQuery();

                while(result.next()){
                    resultMap.put("tableName",result.getString(1));
                    resultMap.put("tableComment",result.getString(2));
                    resultMap.put("createTime",result.getString(3));
                    resultMap.put("updateTime",result.getString(4));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(result != null){
                    result.close();
                }
                if(ps != null){
                    ps.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return resultMap;
    }
    private static List<Field> getFieldList(TableData tableData, Connection conn) throws SQLException {
        List<Field> rowSelectList = tableData.getRowSelectList();

        return rowSelectList;
    }

    public static String getVelocityTplPath(File tplFile) {
        String path = tplFile.getPath();
        path = path.replace(appPath, "");
        return path;
    }

    public static String getOutPath(TableData data, File tplFile) throws IOException {
        StringBuffer outPath = new StringBuffer();
        outPath.append(data.getStorePath()).append(File.separator);
        LineNumberReader lnr = new LineNumberReader(new FileReader(tplFile));
        lnr.setLineNumber(0);

        String vmInfValue = lnr.readLine();

        vmInfValue = vmInfValue.replaceFirst("^#\\*", "").replaceFirst("\\*#$", "");
        vmInfValue = vmInfValue.replaceAll("\\{objectName\\}", data.getObjectName());

        vmInfValue = vmInfValue.replaceAll("\\{tableName\\}", data.getSelecedTable());
        vmInfValue = vmInfValue.replaceAll("\\{dbName\\}", data.getModel().getDBname());

        vmInfValue = vmInfValue.replaceAll("\\{ClassName\\}", data.getObjectName());
        vmInfValue = vmInfValue.replaceAll("\\{entityName\\}", DBTool.getConfigInfo("entityName"));
        vmInfValue = vmInfValue.replaceAll("\\{serviceName\\}", DBTool.getConfigInfo("serviceName"));
        vmInfValue = vmInfValue.replaceAll("\\{daoName\\}", DBTool.getConfigInfo("daoName"));
        vmInfValue = vmInfValue.replaceAll("\\{xmlName\\}", DBTool.getConfigInfo("xmlName"));
        vmInfValue = vmInfValue.replaceAll("\\{controllerName\\}", DBTool.getConfigInfo("controllerName"));


        vmInfValue = vmInfValue.replaceAll("\\{moduleName\\}", DBTool.getConfigInfo("moduleName"));
        vmInfValue = vmInfValue.replaceAll("\\{basePackageName\\}", DBTool.getConfigInfo("basePackageName"));

        outPath.append(vmInfValue);
        return outPath.toString();
    }


    public static void getAllTemplate(String path, List<File> allTemplates) {
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (prefix.equals("vm")) {
                    allTemplates.add(file);
                }
            } else {
                getAllTemplate(file.getPath(), allTemplates);
            }
        }
    }


}
