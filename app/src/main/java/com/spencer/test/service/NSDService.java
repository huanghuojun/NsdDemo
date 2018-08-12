package com.spencer.test.service;

import android.app.Service;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.spencer.test.utils.NSDServiceUtils;

/**
 * @description:
 * @author: huanghuojun
 * @date: 2018/8/12 15:03
 */
public class NSDService extends Service {

    public static final String TAG = "NSDService";

    private NSDServiceUtils mNSDUtils;
    private NSDServiceUtils.RegistrationStateListener mListener;

    @Override
    public void onCreate() {
        super.onCreate();

        initNsdRegisterState();

        mNSDUtils = new NSDServiceUtils();
        mNSDUtils.startNSDServer(this, mListener);

    }

    private void initNsdRegisterState() {
        mListener = new NSDServiceUtils.RegistrationStateListener() {
            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                Log.i(TAG, "in onServiceRegistered");
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.i(TAG, "in onRegistrationFailed  -->  errorCode = " + errorCode);
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                Log.i(TAG, "in onServiceUnregistered");
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.i(TAG, "in onUnregistrationFailed");
            }
        };
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
