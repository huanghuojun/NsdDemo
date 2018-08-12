package com.spencer.test.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spencer.test.R;
import com.spencer.test.base.BaseActivity;
import com.spencer.test.service.NSDService;
import com.spencer.test.utils.NSDClientUtils;

/**
 * @description:
 * @author: huanghuojun
 * @date: 2018/8/12 14:59
 */
public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";
    private TextView mContentTxt;
    private Button mFindBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, NSDService.class);
        startService(intent);

        mContentTxt = findView(R.id.context_txt);
        mFindBtn = findView(R.id.find_btn);

        mFindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNSD();
            }
        });
    }

    public void findNSD(){
        NSDClientUtils nsdClientUtils = new NSDClientUtils();
        nsdClientUtils.discoverService(this, new NSDClientUtils.StateListener() {
            @Override
            public void onFailed(String serviceType, int errorCode) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onSuccess(final NsdServiceInfo serviceInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mContentTxt.setText("service name : " + serviceInfo.getServiceName() + " \n service host: " + serviceInfo.getHost() +
                                "\n service port: " + serviceInfo.getPort());
                    }
                });

                Log.i(TAG, "service name : " + serviceInfo.getServiceName() + " \n service host: " + serviceInfo.getHost() +
                        "\n service port: " + serviceInfo.getPort());

                String hostAddress = serviceInfo.getHost().getHostAddress();
                Log.i(TAG, "host ip--> " + hostAddress );
            }
        });
    }
}
