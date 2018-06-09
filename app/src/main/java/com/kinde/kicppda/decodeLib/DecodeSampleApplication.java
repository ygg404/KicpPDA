package com.kinde.kicppda.decodeLib;

import android.app.Application;
import android.view.WindowManager;

/**
 * Created by YGG on 2018/6/6.
 */

public class DecodeSampleApplication  extends Application {
    private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getWindowParams() {
        return windowParams;
    }
}
