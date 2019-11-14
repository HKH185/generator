package com.generator;

import com.generator.ui.MainFrame;
import com.generator.util.FrameTool;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
* @author houkaihong
* @Date 15:04 2019/10/22
* @description：TODO
*/
public class Main {
    public static void main(String[] args) {
        try {
            resetStyle();
            MainFrame login = new MainFrame();
            login.setResizable(false);
            login.setSize(340, 350);
            FrameTool.setCenter(login);
            login.setDefaultCloseOperation(MainFrame.EXIT_ON_CLOSE);
            login.setTitle("Generator v0.1");
            login.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void resetStyle() throws Exception {
        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
        org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        UIManager.put("RootPane.setupButtonVisible", false);
        initGlobalFontSetting(new Font("微软雅黑", Font.PLAIN, 14));
    }


    public static void initGlobalFontSetting(Font fnt) {
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource){
                UIManager.put(key, fontRes);
            }
        }
    }
}
