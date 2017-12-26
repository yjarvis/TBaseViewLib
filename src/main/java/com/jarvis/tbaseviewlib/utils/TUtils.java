package com.jarvis.tbaseviewlib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarvis.tbaseviewlib.data.CacheData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

//import org.apache.http.util.EncodingUtils;


/**
 * 功能说明 集合的常用工具类
 * <p/>
 * 作者: jarvisT
 */
@SuppressLint({"SimpleDateFormat", "CommitPrefEdits"})
@SuppressWarnings({"static-access", "deprecation"})
public class TUtils {

    /**
     * 工具类的回调
     *
     * @author tansheng
     */
    public interface UtilsCallBack {
        public void utilsCallBack();
    }

    /**
     * 将raw文件夹下面的数据库文件写入到手机中
     */
    @SuppressLint("SdCardPath")
    public static void writeDBFile(Context context, int dbID, String dbName) {
        final String DATA_BASE_PATH = "/data/data/" + context.getPackageName() + "/databases";
        try {
            File dir = new File(DATA_BASE_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String dbPath = DATA_BASE_PATH + "/" + dbName;
            if (!(new File(dbPath)).exists()) {
                InputStream is = context.getResources().openRawResource(dbID);
                FileOutputStream fos = new FileOutputStream(dbPath);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 验证是否为手机号码(支持国际格式+86XXXXXXXX)
     *
     * @param mobile 移动、联通、电信的号码
     *               <p/>
     *               移动号段：134（0~8）、135、136、137、138、139、147、150、151、152、157、158、159、
     *               187、188
     *               <p/>
     *               <p/>
     *               联通号段：130、131、132、155、156、185
     *               <p/>
     *               <p/>
     *               电信号段：133、153、180、189、177
     *               <p/>
     * @return 成功返回true，失败false
     */
    public static boolean checkMoblie(String mobile) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证是密码6-16个字符
     *
     * @return 成功返回true，失败false
     */
    public static boolean checkPwd(String pwd) {
        String rule = "[\\S]{6,16}";// 非空6-20个字符
        boolean f = Pattern.matches(rule, pwd);
        if (!f) {
            return f;
        }
        String rule1 = "[\u4e00-\u9fa5]";// 汉字
        for (int i = 0; i < pwd.length(); i++) {
            String str = pwd.substring(i, i + 1);
            f = Pattern.matches(rule1, str);
            if (f)
                return false;
        }
        return true;
    }

    /**
     * 验证是密码必须含有数字和字母
     *
     * @return 成功返回true，失败false
     */
    public static boolean checkPwdNumAndABC(String pwd) {
        String rule = ".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]";// 必须含有字母和数
        boolean f = Pattern.matches(rule, pwd);
        return f;
    }

    /**
     * 验证身份证
     *
     * @param idCard 15或者18位，最后一位可能是字母或者数字
     * @return 成功返回true，失败false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    /**
     * 验证邮箱
     *
     * @param email 传入邮箱
     * @return 成功返回true，失败false
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 功能说明：图片淡入显示效果 </p>作者: jarvisT
     *
     * @param imageView 显示图片的组件
     * @param bitmap    图片资源 示例：
     */
    public static void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
        // 也可以new BitmapDrawable(imageView.getResources(), bitmap)替换成new
        // BitmapDrawable(bitmap)，目的是为了将下载下来的bitmap转换成Drawable
        final TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(imageView.getResources(), bitmap)});
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(500);
    }

    /**
     * 功能说明：图片淡入显示效果 </p>作者: jarvisT
     *
     * @param imageView 显示图片的组件
     * @param drawable  图片资源 示例：
     */
    public static void fadeInDisplay(ImageView imageView, Drawable drawable) {
        final TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), drawable});

        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(500);

    }

    /**
     * 功能说明：Resource资源转成Bitmap </p>作者: jarvisT
     *
     * @param context
     * @param picPath Resource类型的图片资源
     * @return mBitmap 返回Bitmap类型的图片资源
     */
    public static Bitmap ResToBitmap(Context context, int picPath) {
        InputStream is = context.getResources().openRawResource(picPath);
        Bitmap mBitmap = BitmapFactory.decodeStream(is);
        return mBitmap;
    }

    /**
     * 功能说明：Resource资源转成Drawable </p>作者: jarvisT
     *
     * @param context
     * @param picPath Resource类型的图片资源
     * @return drawable 返回drawable类型的图片资源
     */
    public static Drawable ResToDrawable(Context context, int picPath) {
        Resources resources = context.getResources();
        Drawable drawable = resources.getDrawable(picPath);
        return drawable;
    }

    /**
     * 功能说明：View转Bitmap
     * <p/>
     * 作者: jarvisT
     *
     * @param view 传入需要转型的view
     * @return 返回一个bitmap 示例：
     */
    public static Bitmap viewToBitmap(View view) {
//        try {
//            int width = view.getWidth();
//            int height = view.getHeight();
//            if (width != 0 && height != 0) {
//                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(bitmap);
//                view.layout(0, 0, width, height);
//                view.setBackgroundColor(Color.WHITE);
//                view.draw(canvas);
//            }
//        } catch (Exception e) {
//            bitmap = null;
//            e.getStackTrace();
//        }


        view.setDrawingCacheEnabled(true);

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0,
                view.getMeasuredWidth(),
                view.getMeasuredHeight());

        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        view.destroyDrawingCache();//清除缓存，释放内存
        return bitmap;

    }

    /**
     * dip转px
     */
    public static int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dip
     */
    public static int pxTodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 功能说明：获取版本号 作者: jarvisT
     *
     * @param context
     * @return 返回版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName + "";
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 功能说明： 获得当前日期
     *
     * @param format 制定日期格式
     * @return 当前日期
     * @author 作者：jarvisT
     * @date 2015-2-9 下午3:40:43
     */
    public static String getCurrentDate(String format) {
        String currentDate = "";
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(format);
        currentDate = sf.format(date);

        return currentDate;
    }

    /**
     * 功能说明： 时间转换成时间戳(毫秒)
     *
     * @param format 制定日期格式
     * @return 当前日期
     * @author 作者：jarvisT
     * @date 2015-2-9 下午3:40:43
     */
    public static long getDateToMills(String date, String format) {
        SimpleDateFormat format2 = new SimpleDateFormat(format);
        Date time = null;
        try {
            time = format2.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time.getTime();
    }

    /**
     * 功能说明： 简单的时间格式
     * <p/>
     * 作者: jarvisT
     *
     * @param time   需要格式化的时间
     * @param format 可为null或者“” 默认格式化的模版（"MM-dd HH:mm"）
     * @return 返回模版化后的时间
     */
    public static String getFormatTimeByCustom(long time, String format) {
        if (0 == time) {
            return "";
        }
        SimpleDateFormat mDateFormat = null;
        if (!TextUtils.isEmpty(format)) {
            mDateFormat = new SimpleDateFormat(format);
        } else {
            mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        }
        return mDateFormat.format(new Date(time));

    }

    /**
     * 功能说明：时间格式化 作者: jarvisT
     *
     * @param unFormatTime 需要格式化的时间
     * @return xxxx年xx月xx日 xx:xx
     */
    public static String getFormatTime(String unFormatTime) {
        if (unFormatTime.contains("年") || unFormatTime.contains("月") || unFormatTime.contains("日") || unFormatTime.contains("星期") || unFormatTime.contains("周")) {
            return unFormatTime;
        } else {
            if (unFormatTime.length() >= 12) {
                return unFormatTime.substring(0, 4) + "年" + unFormatTime.substring(4, 6) + "月" + unFormatTime.substring(6, 8) + "日" + " " + unFormatTime.substring(8, 10) + ":" + unFormatTime.substring(10, 12);
            } else if (unFormatTime.length() >= 8) {
                return unFormatTime.substring(0, 4) + "年" + unFormatTime.substring(4, 6) + "月" + unFormatTime.substring(6, 8) + "日";
            } else {
                return unFormatTime;
            }
        }
    }

    /**
     * 功能说明：时间格式化 作者: jarvisT
     *
     * @param unFormatTime 需要格式化的时间
     * @return xxxx-xx-xx xx:xx
     */
    public static String getFormatTimeByLine(String unFormatTime) {
        if (unFormatTime.contains("年") || unFormatTime.contains("月") || unFormatTime.contains("日") || unFormatTime.contains("星期") || unFormatTime.contains("周")) {
            return unFormatTime;
        } else {
            if (unFormatTime.length() >= 12) {
                return unFormatTime.substring(0, 4) + "-" + unFormatTime.substring(4, 6) + "-" + unFormatTime.substring(6, 8) + " " + unFormatTime.substring(8, 10) + ":" + unFormatTime.substring(10, 12);
            } else if (unFormatTime.length() >= 8) {
                return unFormatTime.substring(0, 4) + "-" + unFormatTime.substring(4, 6) + "-" + unFormatTime.substring(6, 8);
            } else {
                return unFormatTime;
            }
        }
    }

    /**
     * 功能说明：检查网络是否连接
     * <p/>
     * 作者: jarvisT
     *
     * @param context
     * @return true已经连接网络，false未连接网络
     */
    public static synchronized boolean isNetworkOK(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 功能说明：获取状态栏高度
     * <p/>
     * 作者: jarvisT
     *
     * @param context
     * @return 返回状态栏高度（int）
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }

		/* 方法2： */
        // int resourceId =
        // context.getResources().getIdentifier("status_bar_height", "dimen",
        // "android");
        // if (resourceId > 0) {
        // statusBarHeight =
        // context.getResources().getDimensionPixelSize(resourceId);
        // }

        return statusBarHeight;
    }

    /**
     * 功能说明： 获取一张assets中的图片资源的方法
     * <p/>
     * 作者: jarvisT
     *
     * @param name 图片路径(例：文件夹/图片的名称.png)
     * @return 返回一个Bitmap类型 示例：
     * @作者: jarvisTcontext
     */
    public static Bitmap getBitmap(Context context, String name) {
        Bitmap temp = null;
        try {
            InputStream is = context.getAssets().open(name);
            temp = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 功能说明：从Assets中读取文本文件
     *
     * @author 作者：jarvisT
     * @date 2015-2-3 下午2:45:57
     * @param context
     * @param fileName
     *            文件名
     * @return
     * @throws IOException
     */
//	public static String getFromAssets(Context context, String fileName) throws IOException {
//		String result = "";
//		InputStream in = context.getAssets().open(fileName);
//		// 获取文件的字节数
//		int lenght = in.available();
//		// 创建byte数组
//		byte[] buffer = new byte[lenght];
//		// 将文件中的数据读到byte数组中
//		in.read(buffer);
//		result = EncodingUtils.getString(buffer, "utf-8");
//		return result;
//	}

    /**
     * 功能说明：判断点击的点是否在矩形内的方法
     * <p/>
     * 作者: jarvisT
     *
     * @param x     点击的x坐标
     * @param y     点击的y坐标
     * @param left  矩形框的左边x坐标
     * @param top   矩形框的上边y坐标
     * @param right 矩形框的右边x坐标
     * @param down  矩形框的下边y坐标
     * @return 在矩形框内返回true，不在返回false 示例：
     */
    public static boolean isRect(float x, float y, float left, float top, float right, float down) {
        if (x > left && x < right && y > top && y < down) {
            return true;
        }

        return false;
    }

    private static Toast toast = null;

    /**
     * 功能说明：Toast显示提示
     * <p/>
     * 作者：jarvisT
     *
     * @param context
     * @param text    显示的文本内容
     */
    public static void showToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();

    }

    public static ProgressDialog myProgerssDialog = null;

    /**
     * 功能说明：自定义滚动提示圈
     * <p/>
     * 作者：jarvisT
     *
     * @param context
     * @param message 显示的内容(为null或者“” 则显示“正在加载...”)
     */
    public static void openPragressDialog(Context context, String message) {
        if (myProgerssDialog != null) {
            closePragressDialog();
        }
        myProgerssDialog = new ProgressDialog(context);
        myProgerssDialog.setCanceledOnTouchOutside(false);
        myProgerssDialog.setCancelable(true);
        if (!TextUtils.isEmpty(message)) {
            myProgerssDialog.setMessage(message);
        } else {
            myProgerssDialog.setMessage("正在加载...");
        }

        myProgerssDialog.show();
    }

    /**
     * 功能说明：关闭等待框
     * <p/>
     * 作者: jarvisT
     */
    public static void closePragressDialog() {
        if (myProgerssDialog != null) {
            myProgerssDialog.cancel();
            myProgerssDialog.dismiss();
            myProgerssDialog = null;
        }
    }

    /**
     * 功能说明：检查是否存在SD卡
     * <p/>
     * 作者: jarvisT
     *
     * @return true存在, false不存在
     */
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 功能说明：计算缓存大小 作者: jarvisT
     *
     * @param cachePath 缓存路径
     * @return 缓存大小（单位byte）
     */
    public static double getCacheSize(String cachePath) {
        File dir = new File(cachePath);
        File[] files = dir.listFiles();

        double dirSize = 0;
        try {
                /*for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(".0") || files[i].getName().contains(".jpg") || files[i].getName().contains(".png")) {
                dirSize += files[i].length();
            }
        }
                    if (dirSize != 0) {
                dirSize = Float.parseFloat(getDecimalsDigits(String.valueOf((dirSize / (1024.0 * 1024.0))), 2));
            }*/
            for (File aFileList : files) {
                if (aFileList.isDirectory()) {
                    dirSize = dirSize + getCacheSize(aFileList.getAbsolutePath());
                } else {
                    dirSize = dirSize + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirSize;
    }

    /**
     * 格式化单位
     *
     * @param size 传入未格式化之前的大小
     * @return size 返回格式化后的大小
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }



    /**
     * 删除指定路径的文件夹中的文件
     *
     * @param filePath 文件夹路径
     */
    public static void clearCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 功能说明：退出App
     * <p/>
     * 作者: jarvisT
     *
     * @param activityArrayList 保存的开启过的Activity
     */
    public static void exitApp(ArrayList<Activity> activityArrayList) {
        if (activityArrayList != null) {
            for (int i = 0; i < activityArrayList.size(); i++) {
                activityArrayList.get(i).finish();
            }
        }
        System.exit(0);
    }

    /**
     * 功能说明：退出App
     * <p/>
     * 作者: jarvisT
     */
    public static void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private static long exitTime = 0;

    /**
     * 再按一次返回键退出
     */
    public static void exitAppShow(Context context) {
        if ((System.currentTimeMillis() - exitTime) > 1500) {
            TUtils.showToast(context, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            // 退出程序
            if (CacheData.activityList != null) {
                for (int i = 0; i < CacheData.activityList.size(); i++) {
                    CacheData.activityList.get(i).finish();
                }
            }
            System.exit(0);
        }
    }


    /**
     * 功能说明：截取小数位数 作者: jarvisT
     *
     * @param data   需要截取的数据
     * @param endPos 截取小数点后的位数 示例：
     */
    public static String getDecimalsDigits(String data, int endPos) {
        String[] newDatas = data.split("\\.");
        String result = null;
        if (endPos == 0) {
            return newDatas[0];
        }

        if (newDatas[1].length() >= endPos) {
            result = newDatas[0] + "." + newDatas[1].substring(0, endPos);
        } else {
            result = newDatas[0] + "." + newDatas[1];
        }
        return result;

    }

    /**
     * 重置照片的横向滚动及数据
     *
     * @param activity
     * @param gridview
     * @param adapter
     * @param currentShow 当前界面显示的组件数量
     * @param offset      偏移量(与屏幕没有间距，填0)
     * @author tansheng
     * <p/>
     * <HorizontalScrollView
     * android:layout_width="match_parent"
     * android:layout_height="wrap_content">
     * <p/>
     * <LinearLayout
     * android:layout_width="wrap_content"
     * android:layout_height="wrap_content">
     * <p/>
     * <GridView
     * android:layout_width="match_parent"
     * android:layout_height="wrap_content"
     * android:cacheColorHint="@color/transparent"
     * android:numColumns="5" />
     * </LinearLayout>
     * </HorizontalScrollView>
     */
    public static void refreshGvReviewPhoto(Activity activity, GridView gridview, BaseAdapter adapter, int currentShow, int offset) {

        int size = adapter.getCount();// 显示的个数
        int spcingWidth = 5;// 列间距
        // 整个屏幕中显示的组件数量

        // 获取显示区域的宽度
        int currentWidth = getScreenWidth(activity) - offset;
        // 每个组件的宽度
        int itemWidth = 0;
        if (size >= currentShow) {
            itemWidth = (int) (currentWidth / currentShow) - spcingWidth;// 屏幕宽度除以显示的个数，得到每个组件的宽度，再减去组件的间距
        } else {
            // 屏幕宽度除以显示的个数，得到每个组件的宽度，再减去组件的间距
            itemWidth = (int) (currentWidth / size) - spcingWidth;
        }
        // 所有组件的宽度
        int allWidth = (int) ((itemWidth + spcingWidth) * size);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gridview.setLayoutParams(params);
        gridview.setColumnWidth(itemWidth);// 设置列的宽度
        gridview.setHorizontalSpacing(spcingWidth);// 设置列间距
        gridview.setStretchMode(GridView.NO_STRETCH);
        gridview.setNumColumns(size);// 设置列数
    }

    /**
     * 功能说明：获取listView中的某条Item 作者: jarvisT
     *
     * @param pos      选择的位置
     * @param listView
     * @return 示例：
     */
    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private static Camera camera = null;

    /**
     * 功能说明：打开闪光灯
     * <p/>
     * 作者: jarvisT
     *
     * @param isOpen true开启闪光灯，false关闭闪光灯
     *               <p/>
     *               使用闪光灯需要开启权限 <uses-permission
     *               android:name="android.permission.CAMERA" />
     */
    public static void openFlashLight(boolean isOpen) {
        // 使用闪光灯需要开启权限 <uses-permission android:name="android.permission.CAMERA"
        // />

        if (camera == null) {
            camera = Camera.open();
        }

        Parameters mParameters = camera.getParameters();
        if (isOpen) {
            // 开启闪光灯
            mParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
        } else {
            // 关闭闪光灯
            mParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
        }
        camera.setParameters(mParameters);

    }

    /**
     * 功能说明：使用SharedPreferences保存数据
     * <p/>
     * 作者: jarvisT
     *
     * @param saveString 保存的文件名
     */
    public static Editor saveData(Context context, String saveString) {

        SharedPreferences sp = context.getSharedPreferences(saveString, context.MODE_PRIVATE);
        Editor editor = sp.edit();
        return editor;
    }

    /**
     * 功能说明：读取SharedPreferences保存的数据
     * <p/>
     * 作者: jarvisT
     *
     * @param saveString 保存的文件名
     */
    public static SharedPreferences getSaveData(Context context, String saveString) {
        SharedPreferences sp = context.getSharedPreferences(saveString, context.MODE_PRIVATE);
        return sp;
    }

    /**
     * 保存数据
     *
     * @param key     保存的key
     * @param data    保存的数据
     * @param context
     * @author tansheng QQ:717549357
     */
    public static void savaData(String key, String data, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, data).commit();
    }

    /**
     * 获取保存数据
     *
     * @param key     传入保存的key
     * @param context
     * @return 返回保存的数据
     * @author tansheng QQ:717549357
     */
    public static String getSaveData(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    /**
     * 功能说明：获取屏幕宽度
     * <p/>
     * 作者: jarvisT
     *
     * @param activity activity
     */
    public static int getScreenWidth(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();

    }

    /**
     * 功能说明：获取屏幕高度
     * <p/>
     * 作者: jarvisT
     *
     * @param activity activity
     */
    public static int getScreenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();

    }

    /**
     * 功能说明：给文本加颜色
     *
     * @param string  string 修改的文本
     * @param colorId 颜色ID
     * @param value   替换值
     * @return 示例：
     */
    public static SpannableStringBuilder getFormatString(String string, int colorId, String value) {
        SpannableStringBuilder style = null;
        if (!TextUtils.isEmpty(string) && colorId != 0 && value != null) {
            String basicString = string;
            String formatString = String.format(basicString, value);
            int begin = basicString.indexOf("%");
            int end = begin + value.length();
            style = new SpannableStringBuilder(formatString);
            style.setSpan(new ForegroundColorSpan(colorId), begin, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // 设置指定位置文字的颜色
        } else if (!TextUtils.isEmpty(string) && value != null && colorId == 0) {
            String basicString = string;
            String formatString = String.format(basicString, value);
            style = new SpannableStringBuilder(formatString);
        } else {
            style = new SpannableStringBuilder("");
        }
        return style;
    }

    /**
     * 功能说明：替换文本内容
     *
     * @param string string 修改的文本
     * @param value  替换值
     * @return 示例：
     */
    public static String getFormatString(String string, String value) {
        String newStr = "";
        if (!TextUtils.isEmpty(string) && value != null) {
            newStr = string.replace("#", value);
        }
        return newStr;
    }

    /**
     * 功能说明：修改部分文本的字体大小
     *
     * @param string     string 修改的文本
     * @param startIndex 开始位置
     * @param endIndex   结束位置
     * @param size       文字大小
     * @param color      颜色id
     * @param context    上下文内容
     * @return 示例：
     */
    public static SpannableStringBuilder getFormatString(String string, int startIndex, int endIndex, int size, int color, Context context) {
        SpannableStringBuilder style = null;
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        if (!TextUtils.isEmpty(string) && size != 0) {
            style = new SpannableStringBuilder(string);
            style.setSpan(new TextAppearanceSpan(null, 0, dipTopx(context, size), colorStateList, null), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // 设置指定位置文字的颜色
        } else {
            style = new SpannableStringBuilder("");
        }
        return style;
    }

    /**
     * 对一串文字某一段添加下划线，并在下划线处设置点击事件
     *
     * @param textview 显示文字的组件
     * @param string   显示的文字
     * @param start    需要添加下划线文字的起始位置
     * @param end      需要添加下划线文字的起始位置
     * @param listener 点击事件
     */
    public static void setClickableSpan(TextView textview, String string, int start, int end, final UtilsCallBack listener) {
        SpannableString sp = new SpannableString(string);
        // 设置点击事件
        sp.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.utilsCallBack();
                }
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // SpannableString对象设置给TextView
        textview.setText(sp);
        // 设置TextView可点击
        textview.setMovementMethod(LinkMovementMethod.getInstance());

    }

    /**
     * 拨打电话 </p>需要添加拨打电话权限 <uses-permission
     * android:name="android.permission.CALL_PHONE"/>
     *
     * @param context
     * @param mobile  电话号码
     */
    public static void callMobile(Context context, String mobile) {
        // 调用显示拨号界面
        // Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
        // +mobile));
        // 直接拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
        context.startActivity(intent);
    }

    /**
     * 跳转到发送短信的界面
     *
     * @param context
     * @param mobile  发送短息的电话号码
     */
    public static void sendSMS(Context context, String mobile) {
        Uri smsToUri = Uri.parse("smsto:" + mobile);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        context.startActivity(intent);
    }

    /**
     * 直接发送短信
     *
     * @param context
     * @param mobile  发送短息的电话号码
     * @param message 发送的短息(需要权限"android.permission.SEND_SMS")
     */
    public static void sendSMS(Context context, String mobile, String message) {
        SmsManager manager_sms = SmsManager.getDefault();// 得到短信管理器
        // 由于短信可能较长，故将短信拆分
        ArrayList<String> texts = manager_sms.divideMessage(message);
        for (String text : texts) {
            /**
             * 、 参数说明 destinationAddress:收信人的手机号码 scAddress:发信人的手机号码
             * text:发送信息的内容 sentIntent:发送是否成功的回执，用于监听短信是否发送成功。
             * DeliveryIntent:接收是否成功的回执，用于监听短信对方是否接收成功。
             */
            manager_sms.sendTextMessage(mobile, null, text, null, null);// 分别发送每一条短信
        }
        Toast.makeText(context, "发送成功!", Toast.LENGTH_LONG).show();// 提示成功
    }

    /**
     * 发送邮件
     *
     * @param context
     * @param email   对方邮箱
     * @param title   标题
     * @param content 内容
     */
    public static void sendEmail(Context context, String email, String title, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(intent);

    }
    /**
     * 发送邮件
     *
     * @param context
     * @param email   对方邮箱
     * @param title   标题
     * @param content 内容
     */
    public static void sendEmailAuto(Context context, String email, String title, String content) {
//        Intent intent = new Intent("com.android.email.intent.action.sendInBack");
        Intent intent = new Intent("com.google.android.gm.action.AUTO_SEND");
        intent.setType("plain/text");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL,email);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(intent, "send"));
    }

    /**
     * 如果键盘显示，则隐藏；如果键盘隐藏，则显示
     *
     * @param context
     */
    public static void showOrHideInput(Context context) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 键盘显示和隐藏
     *
     * @param context
     * @param editText 输入栏
     * @param isShow   true:强制显示输入法，false:强制隐藏输入法
     */
    public static void showOrHideInput(Context context, EditText editText, boolean isShow) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            // 显示
            im.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        } else {
            // 隐藏
            im.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    /**
     * 发送通知栏信息
     *
     * @param context
     * @param imageIcon
     *            通知栏图标
     * @param tickerText
     *            显示文本
     * @param contentTitle
     *            通知栏标题
     * @param contentText
     *            通知栏内容
     * @param contentIntent
     *            点击通知栏的跳转操作 </p>使用例子：
     *            <p>
     *            Intent notificationIntent = new Intent(context,
     *            AskLeaveActivity.class);
     *            <p>
     *            PendingIntent contentIntent =
     *            PendingIntent.getActivity(context, 0, notificationIntent, 0);
     *
     *
     */
//	public static void showNotification(Context context, int imageIcon, String tickerText, String contentTitle, String contentText, PendingIntent contentIntent) {
//		Notification notification = new Notification();
//		notification.icon = imageIcon;
//		notification.tickerText = tickerText;
//		// 采用默认声音
//		notification.defaults |= Notification.DEFAULT_ALL;
//		// //让声音、振动无限循环，直到用户响应
//		// notification.flags |= Notification.FLAG_INSISTENT;
//		// //通知被点击后，自动消失
//		// notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		// //点击'Clear'时，不清楚该通知(QQ的通知无法清除，就是用的这个)
//		// notification.flags |= Notification.FLAG_NO_CLEAR;
//
//		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
//		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.notify(1, notification);
//	}

    /**
     * 动态设置ListView的高度,在为ListView设置了Adapter之后使用，就可以解决ScrollView嵌套ListView的问题了。
     *
     * @param listView </p> 但是这个方法有个两个细节需要注意
     *                 ：
     *                 一是Adapter中getView方法返回的View的必须由LinearLayout组成，因为只有LinearLayout才有measure
     *                 ()方法，如果使用其他的布局如RelativeLayout，在调用listItem.measure(0,
     *                 0);时就会抛异常，
     *                 因为除LinearLayout外的其他布局的这个方法就是直接抛异常的，没理由…。我最初使用的就是这个方法，
     *                 但是因为子控件的顶层布局是RelativeLayout，所以一直报错，不得不放弃这个方法。
     *                 二是需要手动把ScrollView滚动至最顶端，因为使用这个方法的话，默认在ScrollView顶端的项是ListView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;

        // ListAdapter listAdapter = listView.getAdapter();
        BaseAdapter listAdapter = (BaseAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 设置控件圆角以及颜色
     *
     * @param radius       圆角度
     * @param borderLength 描边宽度
     * @param borderColor  颜色
     * @return 返回后，直接设置setBackground();此方法需要api>16
     * @author tansheng
     */
    public static ShapeDrawable setShapeBackground(float radius, float borderLength, int borderColor) {
        float[] outerRadii = new float[8];
        // float[] innerRadii = new float[8];
        for (int i = 0; i < 8; i++) {
            outerRadii[i] = radius;
            // innerRadii[i] = radius;
        }

		/*
         * RoundRectShape的三个参数： 1：// 外部矩形弧度 2：内部矩形与外部矩形的距离 3：内部矩形弧度
		 */
        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii, null, null));
        // 指定填充颜色
        sd.getPaint().setColor(borderColor);
        // 指定填充模式
        sd.getPaint().setStyle(Style.FILL);

        return sd;

    }

    /**
     * 设置控件圆角以及颜色，此方法需要在布局中先为此控件设置shape的xml
     *
     * @param view  控件
     * @param color 颜色
     * @return
     * @author tansheng
     */
    public static void setShapeBackground(View view, int color) {
        GradientDrawable gd = (GradientDrawable) view.getBackground();
        gd.setColor(color);
    }

    /**
     * 复制一段文字到剪贴板中
     *
     * @param context
     * @param str     需要复制的文字
     * @author tansheng
     */
    public static void copyText(String str, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(str.trim()); // 将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        // cmb.getText();//获取粘贴信息
    }

    /**
     * 获取设备号
     *
     * @param context
     * @return
     * @author tansheng  QQ:717549357
     * @date 2015-11-25 下午2:42:16
     */
    public static String getImei(Context context) {
        String deviceID = getSaveData("TBase_deviceID", context);
        if (!TextUtils.isEmpty(deviceID)) {
            return deviceID;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // 获取imei值
            deviceID = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(deviceID)) {
                // 获取Android_ID值
                deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                // 随机生成32位值
                if (TextUtils.isEmpty(deviceID)) {
                    String base = "abcdefghijklmnopqrstuvwxyz0123456789";
                    Random random = new Random();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < 32; i++) {
                        int number = random.nextInt(base.length());
                        sb.append(base.charAt(number));
                    }
                    deviceID = sb.toString();
                }
            }

        } catch (Exception e) {
            return deviceID;
        }
        //缓存设备号
        savaData("TBase_deviceID", deviceID, context);
        return deviceID;
    }

    /**
     * 检查字符串是否为null和空
     *
     * @param content
     * @return
     * @author tansheng  QQ:717549357
     * @date 2015-11-26 下午3:05:36
     */
    public static boolean textIsEmpty(String content) {
        if (TextUtils.isEmpty(content) || content.equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * 防止使用String对象出错,如果String 对象为空返回空字符串
     *
     * @param str
     * @return
     * @author tansheng QQ:717549357
     * @date 2015-11-19 下午2:47:16
     */
    public static String safeStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    /**
     * 打开文件
     */
    public static void openFile(Context context, File file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "*/*");
            context.startActivity(intent);
        } catch (Exception e) {
            TUtils.showToast(context, "无对应程序打开此文件");
        }

    }


    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    public static boolean checkChinese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否具有某个权限
     *
     * @param context
     * @param permission 需要判断的权限,如："android.permission.RECORD_AUDIO"
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        PackageManager pm = context.getPackageManager();
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, "packageName");
    }

}
