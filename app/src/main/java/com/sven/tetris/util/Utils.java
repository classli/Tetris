package com.sven.tetris.util;

import android.app.Activity;
import android.os.Build;
import android.view.Window;

/**
 * Created by weixiao on 18/3/11.
 */

public class Utils {

    /**
     * 修改状态栏颜色，支持5.0以上版本
     * @param activity
     * @param colorId
     */
    public static void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }
    }

    public  static int[][] change(int [][]matrix){
        int [][]temp=new int[matrix[0].length][matrix.length];
        int dst=matrix.length-1;
        for(int i=0;i<matrix.length;i++,dst--){
            for(int j=0;j<matrix[0].length;j++){
                temp[j][dst]=matrix[i][j];
            }
        }
        return temp;
    }
}
