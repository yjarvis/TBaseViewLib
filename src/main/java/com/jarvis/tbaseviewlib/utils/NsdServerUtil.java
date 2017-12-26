package com.jarvis.tbaseviewlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * nsd网络发现工具类，用于发现局域网内同应用的设备
 * Created by tansheng on 2017/7/31.
 */

public class NsdServerUtil {
    private Context context;
    private NsdManager nsdManager;
    private NsdManager.RegistrationListener registrationListener;
    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager.ResolveListener connectListener;
    private NsdServerUtil.ResolvedCallBackListener listener;
    private final String SERVER_TYPE = "_http._tcp.";
    private String serverName = "TBaseView";
    private HashMap<String, NsdServiceInfo> hashMap;
    private ArrayList<NsdServiceInfo> infoList;
    private String ipAddress;
    private String TAG = "_28";

    private static NsdServerUtil instance;


    public static NsdServerUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (NsdServerUtil.class) {
                if (instance == null) {
                    instance = new NsdServerUtil(context);
                }
            }
        }
        return instance;
    }

    public NsdServerUtil(Context context) {
        this.context = context;
        hashMap = new HashMap<>();
        infoList = new ArrayList<>();
        nsdManager = (NsdManager) context.getApplicationContext().getSystemService(Context.NSD_SERVICE);
        ipAddress = getLocAddress();
    }

    /**
     * 注册服务
     */
    public NsdServerUtil registerService() {
        // 注意：注册网络服务时不要对端口进行硬编码，通过如下这种方式为你的网络服务获取
        // 一个可用的端口号.
        int port = 0;
        try {
            ServerSocket sock = new ServerSocket(0);
            port = sock.getLocalPort();
            sock.close();
        } catch (Exception e) {
            Toast.makeText(context, "can not set port", Toast.LENGTH_SHORT);
        }
        NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
        nsdServiceInfo.setServiceName(serverName);
        nsdServiceInfo.setServiceType(SERVER_TYPE);
        nsdServiceInfo.setPort(port);
        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                //TODO 注册失败
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                //TODO 解绑失败
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                //TODO 注册成功
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                //TODO 未注册
            }
        };
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
        return instance;
    }


    /**
     * 发现服务并连接
     */
    public NsdServerUtil discoverService() {
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {

            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {

            }

            @Override
            public void onDiscoveryStarted(String serviceType) {

            }

            @Override
            public void onDiscoveryStopped(String serviceType) {

            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                //TODO 发现网络服务时就会触发该事件,可以通过switch或if获取那些你真正关心的服务

                Log.d(TAG, "Service discovery success" + serviceInfo);
                if (!serviceInfo.getServiceType().equals(SERVER_TYPE)) {
// Service type is the string containing the protocol and
// transport layer for this service.
                    Log.d(TAG, "Unknown Service Type: " + serviceInfo.getServiceType());
                }
//                else if(serviceInfo.getServiceName().equals(serverName)){
//// The name of the service tells the user what they'd be
//// connecting to. It could be "Bob's Chat App".
//                    //同名则是自身的服务,过滤掉
//                    Log.d(TAG,"Same machine: "+ serverName);
////                    connectServer(serviceInfo);我加的
//                }
                else if (serviceInfo.getServiceName().contains(serverName)) {
                    //含有此名字的即为同一个应用的服务
                    connectService(serviceInfo);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {

            }
        };

        nsdManager.discoverServices(SERVER_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);

        return instance;
    }

    /**
     * 连接服务
     */
    private NsdServerUtil connectService(NsdServiceInfo serviceInfo) {
        connectListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                if (listener != null) {
                    listener.onResolveFailed(serviceInfo, errorCode);
                }
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                //连接成功
                if (!serviceInfo.getHost().toString().contains(ipAddress)) {
//                        Iterator iterator = hashMap.entrySet().iterator();
//                        while (iterator.hasNext()) {
//                            String key = iterator.next().toString();
//                            if (serviceInfo.getHost().toString().equals(key)) {
//                                return;
//                            }
//                        }
                    hashMap.put(serviceInfo.getHost().toString(), serviceInfo);
                    LogUtils.e("连接成功的" + serviceInfo.getHost().toString() + " 数量:" + hashMap.size());
                    if (listener != null) {
                        listener.onServiceResolved(serviceInfo);
                    }
                } else {
                    LogUtils.e("自身的服务");
                }
            }
        };
        nsdManager.resolveService(serviceInfo, connectListener);
        return instance;
    }


    /**
     * 解绑服务
     */
    public void unregisterService() {
        if (discoveryListener != null) {
            nsdManager.stopServiceDiscovery(discoveryListener);
        }
        if (registrationListener != null) {
            nsdManager.unregisterService(registrationListener);
        }
        discoveryListener = null;
        registrationListener = null;
        nsdManager = null;
    }

    //获取本地ip地址
    private String getLocAddress() {

//        String ipaddress = "";
//
//        try {
//            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
//            // 遍历所用的网络接口
//            while (en.hasMoreElements()) {
//                NetworkInterface networks = en.nextElement();
//                // 得到每一个网络接口绑定的所有ip
//                Enumeration<InetAddress> address = networks.getInetAddresses();
//                // 遍历每一个接口绑定的所有ip
//                while (address.hasMoreElements()) {
//                    InetAddress ip = address.nextElement();
////                    if (!ip.isLoopbackAddress()&& InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
//                    if (!ip.isLoopbackAddress()&& !ip.isLinkLocalAddress()) {
//                        ipaddress = ip.getHostAddress();
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            Log.e(TAG, "获取本地ip地址失败");
//            e.printStackTrace();
//        }
//        Log.e(TAG,"本机IP:" + ipaddress);
//
//        return ipaddress;


        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        Log.e(TAG, "本机IP:" + inetAddress.getHostAddress().toString());
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public interface ResolvedCallBackListener {
        void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode);

        void onServiceResolved(NsdServiceInfo serviceInfo);
    }

    public void setListener(ResolvedCallBackListener listener) {
        this.listener = listener;
    }

    /**
     * 返回局域网中其他设备的信息
     * @return
     */
    public ArrayList<NsdServiceInfo> getServerInfos() {
        infoList.clear();
        if (hashMap.size()==0){
            return null;
        }
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (java.util.Map.Entry)iterator.next();
            infoList.add((NsdServiceInfo) entry.getValue());
        }
        return infoList;
    }

    /**
     * 返回局域网中其他设备的数量
     * @return
     */
    public int getServerNumber() {
        if (getConnectedType(context)==ConnectivityManager.TYPE_WIFI){
            //wifi下返回值
            return hashMap.size() + 1;
        }else {
            //不是wifi下的返回值
            return 0;
        }
    }


    /**
     * 获取网络类型
     * @param context
     * @return
     */
    private int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
