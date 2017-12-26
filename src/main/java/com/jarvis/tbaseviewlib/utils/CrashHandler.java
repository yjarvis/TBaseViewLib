/**
 * 作者: jarvisT<p>
 * 创建日期:2014-10-23
 */
package com.jarvis.tbaseviewlib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.jarvis.tbaseviewlib.data.CacheData;
import com.jarvis.tbaseviewlib.model.base.DeviceInfo;
import com.jarvis.tbaseviewlib.widget.TDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

/**
 * 捕获处理为抓取的异常
 * <p/>
 * 作者: jarvisT
 * <p/>
 * 示例：CrashHandler.getInstance()
 */
public class CrashHandler implements UncaughtExceptionHandler {

    /**
     * 单例对象
     */
    private static CrashHandler uniqueInstance = null;
    private TDialog dialog;

    private Context context;
    //系统默认的UncaughtException处理类
    private UncaughtExceptionHandler sysDefaultHandler;
    /**收集到的设备信息和错误日志*/
    private DeviceInfo deviceInfoVo;
    private CrashHandlerListener listener;

    public interface CrashHandlerListener{
        /**
         * 抓取到错误日志的监听
         * @param errMsg 错误日志
         */
        public void onCrashHandler(String errMsg);
    }

    private CrashHandler() {
    }

    /**
     * 功能说明：获取CrashHandler单例对象实例 作者：jarvisT 创建日期:2014-10-13 参数：
     *
     * @return 单例对象 示例：CrashHandler.getInstance()
     */
    public static CrashHandler getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new CrashHandler();
        }
        return uniqueInstance;
    }

    /**
     * 功能说明：初始化方法
     * <p/>
     * 作者：jarvisT
     * <p/>
     * 创建日期:2014-10-24
     * <p/>
     * 参数：
     *
     * @param context app上下文内容
     *                <p/>
     *                示例：init(hillbuy)
     */
    public void init(Context context) {
        this.context = context;
        //获取系统默认的UncaughtException处理器
        sysDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && sysDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            sysDefaultHandler.uncaughtException(thread, ex);
            //退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
            TUtils.exitApp();
        } else {
            //退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            TUtils.exitApp();
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                if (null!=listener){
                    listener.onCrashHandler(deviceInfoVo!=null?deviceInfoVo.getErrorMsg():"");
                }
                Looper.loop();
            }
        }).start();
    }

    /**
     * 功能说明：异常发生时处理方法，收集异常信息和设备信息
     * <p/>
     * 作者：jarvisT
     * <p/>
     * 参数：
     *
     * @param ex 异常对象
     *           <p/>
     * @return <p/>
     * 示例：handleException(exception)
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        //收集异常信息
        String exMsg = collectExceptionInfoWithPrintWriter(ex);
        //收集设备参数信息 
        deviceInfoVo = collectDeviceInfo();
        deviceInfoVo.setErrorMsg(exMsg);
        // 保存日志文件或是上传服务器 
        if (deviceInfoVo != null && !TextUtils.isEmpty(exMsg)) {
//            THttpHelp httpHelp = new THttpHelp();
//            String deviceType = "android" + deviceInfoVo.getPhoneType();
//            //TODO 保存错误日志到网络
//            RequestParams params = new RequestParams();
//            params.addBodyParameter("deviceType", deviceType);
//            params.addBodyParameter("expMsg",exMsg);
//            
//            httpHelp.doRequest(context, params,url, false, null);

            //保存错误日志到手机上
            saveCrashInfo2File(deviceInfoVo);
        }
        return true;
    }

    @SuppressWarnings("unused")
//    private String collectExceptionInfo(Throwable ex) {
//        StackTraceElement[] messages = ex.getStackTrace();
//        int length = messages.length;
//        StringBuffer sb = new StringBuffer();
//        sb.append("Exception:");
//        sb.append(ex.getMessage());
//        for (int i = 0; i < length; i++) {
//            sb.append(messages[i].getClassName());
//            sb.append(":");
//            sb.append(messages[i].getFileName());
//            sb.append(":");
//            sb.append(messages[i].getLineNumber());
//            sb.append(":");
//            sb.append(messages[i].getMethodName());
//            sb.append("\n");
//        }
//
//        return sb.toString();
//    }

    /**
     *
     * 功能说明：获取异常的堆栈信息
     * <p>
     * 作者：jarvisT
     * <p>
     * 创建日期:2014-10-24
     * <p>
     * 参数：
     *
     * @param t
     *            异常对象
     *            <p>
     * @return 堆栈中的异常信息
     *         <p>
     *         示例：collectExceptionInfoWithPrintWriter(exception)
     */
    private String collectExceptionInfoWithPrintWriter(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            if (t == null) {
                return null;
            }
            t.printStackTrace(pw);
            return sw.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pw.close();
        }
        return pw.toString();
    }

    /**
     * 功能说明：收集设备信息
     * <p/>
     * 作者：jarvisT
     * 参数：
     *
     * @return 设备对象
     * <p/>
     * 示例：collectDeviceInfo()
     */
    public DeviceInfo collectDeviceInfo() {
        DeviceInfo deviceInfoVo = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            String versionName = "";
            if (pi != null) {
                versionName = TextUtils.isEmpty(pi.versionName) ? "null" : pi.versionName;
            }

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            deviceInfoVo = new DeviceInfo();
            deviceInfoVo.setImei(tm.getDeviceId());
            deviceInfoVo.setLine1Number(tm.getLine1Number());
            deviceInfoVo.setNetworkType(tm.getNetworkType());
            deviceInfoVo.setPhoneType(tm.getPhoneType());
            deviceInfoVo.setSoftwareVersion(versionName);

        } catch (NameNotFoundException e) {

        }
        return deviceInfoVo;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param deviceInfoVo 收集手机的设备信息以及错误日志类
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    public String saveCrashInfo2File(DeviceInfo deviceInfoVo) {
        try {
            String time = DateUtil.getInstance().getFormat(new Date(), "yyyyMMddHHmmss");
            String fileName = "error_" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(CacheData.getInfoPath());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(CacheData.getInfoPath() + fileName);
                fos.write(deviceInfoVo.getErrorMsg().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e("errorLog", "an error occured while writing file...", e);
        }
        return "";
    }

    /**
     * 设置监听
     * @param listener
     */
    public void setCrashHandlerListener(CrashHandlerListener listener) {
        this.listener = listener;
    }
}
