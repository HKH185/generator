package com.generator.util;

import javax.swing.*;
import java.awt.*;

/**
* @author houkaihong
* @Date 15:03 2019/10/22
* @description：TODO
*/
public class FrameTool {

    /**
     * 屏幕居中方法
     *
     * @param jframe
     */
    public static void setCenter(JFrame jframe) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        int frameH = jframe.getHeight();
        int frameW = jframe.getWidth();
        jframe.setLocation((screenWidth - frameW) / 2, (screenHeight - frameH) / 2);
    }
}
