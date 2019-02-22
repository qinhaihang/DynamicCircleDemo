package com.sensetime.qinhaihang_vendor.qhhview.utils;

import android.util.Log;
import android.view.View;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/2/21 15:04
 * @des
 * @packgename com.sensetime.qinhaihang_vendor.qhhview.utils
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class Dimension {

    public static int measureDimension(int measureSpec){
        int defaultSize = 800;
        int model = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        Log.d("qhh","size = "+size);
        switch (model) {
            case View.MeasureSpec.EXACTLY:
                return size;
            case View.MeasureSpec.AT_MOST: //对应wrap_content
                return Math.min(size, defaultSize);
            case View.MeasureSpec.UNSPECIFIED:
                return defaultSize;
            default:
                return defaultSize;
        }
    }

    public static int measureDimension(View view,int measureSpec){
        int defaultSize = 800;
        int model = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);

        return defaultSize;
    }
}
