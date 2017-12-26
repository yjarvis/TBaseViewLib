/**
 * 版权：Hillsun Cloud Commerce Technology Co. Ltd<p>
 * 作者: cc<p>
 * 创建日期:2014-10-27
 */
package com.jarvis.tbaseviewlib.model.base;

/**
 * 手机设备信息值对象
 * 作者: jarvisT<p>
 */
public class DeviceInfo {
    //设备IMEI
    private String imei;
    //设备的软件版本号
    private String softwareVersion;
    //手机号
    private String line1Number;
    /*  
     * 当前使用的网络类型：  
     * 例如： NETWORK_TYPE_UNKNOWN  网络类型未知  0  
       NETWORK_TYPE_GPRS     GPRS网络  1  
       NETWORK_TYPE_EDGE     EDGE网络  2  
       NETWORK_TYPE_UMTS     UMTS网络  3  
       NETWORK_TYPE_HSDPA    HSDPA网络  8  
       NETWORK_TYPE_HSUPA    HSUPA网络  9  
       NETWORK_TYPE_HSPA     HSPA网络  10  
       NETWORK_TYPE_CDMA     CDMA网络,IS95A 或 IS95B.  4  
       NETWORK_TYPE_EVDO_0   EVDO网络, revision 0.  5  
       NETWORK_TYPE_EVDO_A   EVDO网络, revision A.  6  
       NETWORK_TYPE_1xRTT    1xRTT网络  7  
     */
    private int networkType;
    //手机类型
    private int phoneType;
    //手机型号  
    private final String phoneModel = android.os.Build.MODEL;
    //sdk
    @SuppressWarnings("deprecation")
    private final String sdk = android.os.Build.VERSION.SDK;
    //系统版本
    private final String sysSdk = android.os.Build.VERSION.RELEASE;
    //异常信息
    private String errorMsg;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getLine1Number() {
        return line1Number;
    }

    public void setLine1Number(String line1Number) {
        this.line1Number = line1Number;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public String getSdk() {
        return sdk;
    }

    public String getSysSdk() {
        return sysSdk;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


}
