package com.spencer.test.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.RegistrationListener;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;

/**
 * @description:
 * @author: huanghuojun
 * @date: 2018/8/12 14:33
 */
public class NSDServiceUtils {

    public static final String SERVICE_NAME = "Spencer";
    public static final String SERVICE_TYPE = "_http._tcp.";
    public static final int PORT = 12579;

    private NsdManager mNsdManager;
    private RegistrationListener mRegistrationListener;
    private RegistrationStateListener mRegistrationStateListener;

    public void startNSDServer(Context context, RegistrationStateListener listener){
        this.mRegistrationStateListener = listener;
        initRegistrationListener();
        registerService(context);
    }

    /**
     * 注册NSD服务端
     * @param context
     */
    @SuppressLint("NewApi")
    private void registerService(Context context) {
        //获取系统网络服务管理器
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);


        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        //客户端发现服务器需要对应这个Type字符串
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setPort(PORT);

        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }


    /**
     * 取消注册NSD服务器端
     */
    public void stopService(){
        if(mNsdManager != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mNsdManager.unregisterService(mRegistrationListener);
            }
    }

    @SuppressLint("NewApi")
    private void initRegistrationListener() {
        mRegistrationListener = new RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                if(mRegistrationStateListener != null)
                    mRegistrationStateListener.onRegistrationFailed(serviceInfo, errorCode);
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

                if(mRegistrationStateListener != null)
                    mRegistrationStateListener.onUnregistrationFailed(serviceInfo, errorCode);
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                if(mRegistrationStateListener != null)
                    mRegistrationStateListener.onServiceRegistered(serviceInfo);
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                if(mRegistrationStateListener != null)
                    mRegistrationStateListener.onServiceUnregistered(serviceInfo);
            }
        };
    }

    public interface RegistrationStateListener{
        void onServiceRegistered(NsdServiceInfo serviceInfo);     //注册NSD成功
        void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode);   //注册NSD失败
        void onServiceUnregistered(NsdServiceInfo serviceInfo);  //取消NSD注册成功
        void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode); //取消NSD注册失败
    }
}
