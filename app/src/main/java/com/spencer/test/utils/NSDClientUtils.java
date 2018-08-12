package com.spencer.test.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.util.Log;

/**
 * @description:
 * @author: huanghuojun
 * @date: 2018/8/12 15:15
 */
public class NSDClientUtils {

    public static final String TAG = "NSDClientUtils";
    public static final String SERVICE_TYPE = "_http._tcp.";

    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private NsdManager.ResolveListener mResolveListener;
    private StateListener mListener;

    @SuppressLint("NewApi")
    public void discoverService(Context context, StateListener listener){
        this.mListener = listener;
        initDiscoveryListener();

        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    @SuppressLint("NewApi")
    private void startResolver(NsdServiceInfo serviceInfo){
        initResolveListener();

        mNsdManager.resolveService(serviceInfo, mResolveListener);
    }

    @SuppressLint("NewApi")
    private void initResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                if(mListener != null)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mListener.onFailed(serviceInfo.getServiceType(), 0);
                    }
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                if(mListener != null)
                    mListener.onSuccess(serviceInfo);
            }
        };
    }

    @SuppressLint("NewApi")
    private void initDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.i(TAG, "in onStartDiscoveryFailed");
                if(mListener != null)
                    mListener.onFailed(serviceType, errorCode);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.i(TAG, "in onStartDiscoveryFailed");
                if(mListener != null)
                    mListener.onFailed(serviceType, errorCode);
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.i(TAG, "in onDiscoveryStarted");
                if(mListener != null)
                    mListener.onFailed(serviceType, -1);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "in onDiscoveryStopped");
                if(mListener != null)
                    mListener.onFailed(serviceType, 0);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Log.i(TAG, "in onServiceFound --> " + serviceInfo.getServiceName());
                }
                startResolver(serviceInfo);
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.i(TAG, "in onServiceLost");
            }
        };
    }

    public interface StateListener{
        public void onFailed(String serviceType, int errorCode);
        public void onSuccess(NsdServiceInfo serviceInfo);
    }
}
