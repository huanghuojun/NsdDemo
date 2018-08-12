package com.spencer.test.base;

import android.app.Activity;
import android.view.View;

/**
 * @description:
 * @author: huanghuojun
 * @date: 2018/8/12 15:12
 */
public class BaseActivity extends Activity {

    public <T extends View> T findView(int id){
        return (T)findViewById(id);
    }
}
