package com.jarvis.tbaseviewlib.http;

import android.content.Context;
import android.util.Log;

import com.jarvis.tbaseviewlib.R;
import com.jarvis.tbaseviewlib.data.CacheData;
import com.jarvis.tbaseviewlib.utils.TUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * http请求辅助类，还需继续完善
 *
 * @author tansheng  QQ:717549357
 * @date 2015-11-26 下午3:07:40
 */
public class HttpHelp {

    private Context context;

    // ============================================================================================
    public interface HttpHelpCallBack {
        /**
         * 功能说明:网络访问成功的回调
         *
         * @param result 访问成功的数据返回
         * @author 作者：jarvisT
         */
        public void onSuccess(String result);

        /**
         * 功能说明:网络访问失败的回调
         *
         * @param arg0   访问错误信息
         * @param errMsg 访问错误信息
         * @author 作者：jarvisT
         */
        public void onFailure(HttpException arg0, String errMsg);
    }

    public HttpHelp(Context context) {
        this.context = context;
    }

    /**
     * 功能说明:请求网络接口
     *
     * @param url      请求的url地址
     * @param params   请求参数
     * @param isShow   是否弹出正在加载框true弹出，false不弹出
     * @param listener 请求网络数据的回调监听
     * @author 作者：jarvisT
     */
    public void doPostRequest(String url, RequestParams params, final boolean isShow, final HttpHelpCallBack listener) {
        if (isShow) {
            TUtils.openPragressDialog(context, context.getResources().getString(R.string.toast_http_loading));
        }
        HttpUtils httpUtils = new HttpUtils();
        if (CacheData.isDeBug) {
            Log.i("请求url：", url);
//			try {
//				InputStream in = params.getEntity().getContent();
//				InputStreamReader reader = new InputStreamReader(in);
//				String line = "";
//				StringBuilder total = new StringBuilder();
//
//				BufferedReader rd = new BufferedReader(reader);
//				while ((line = rd.readLine()) != null) {
//					total.append(line);
//				}
//				Log.i("请求参数：", url + total.toString());
//			} catch (Exception e) {
//			}
        }
        httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String errMsg) {
                if (arg0 != null) {
                    switch (arg0.getExceptionCode()) {
                        case 0:
                            TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err_inter));
                            break;
                        default:
                            TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err));
                            break;
                    }
                }
                if (null != listener) {
                    listener.onFailure(arg0, errMsg);
                }
                if (isShow) {
                    TUtils.closePragressDialog();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> result) {
                if (CacheData.isDeBug) {
                    try {
                        JSONObject obj = new JSONObject(result.result);
                        Log.i("请求结果：", obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (TUtils.textIsEmpty(result.result)) {
                    TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err_nodata));
                    if (null != listener) {
                        listener.onFailure(null, "");
                    }
                } else {
                    if (null != listener) {
                        try {
                            listener.onSuccess(result.result);
                        } catch (Exception e) {
                            if (CacheData.isDeBug) {
                                Log.i("请求结果：", "解析异常" + e.getMessage());
                            }
                            TUtils.showToast(context, "数据解析异常");
                        }
                    }
                }
                if (isShow) {
                    TUtils.closePragressDialog();
                }
            }
        });
    }

    // ============================================================================================

    /**
     * 功能说明:请求网络接口
     *
     * @param map      请求参数
     * @param isShow   是否弹出正在加载框true弹出，false不弹出
     * @param listener 请求网络数据的回调监听
     * @author 作者：jarvisT
     */
    public void doGetRequest(HashMap<String, Object> map, final boolean isShow, final HttpHelpCallBack listener) {
        if (isShow) {
            TUtils.openPragressDialog(context, context.getResources().getString(R.string.toast_http_loading));
        }
        if (CacheData.isDeBug) {
            Log.i("请求参数：", addParamer(map));
        }
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.GET, addParamer(map), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String errMsg) {
                if (arg0 != null) {
                    switch (arg0.getExceptionCode()) {
                        case 0:
                            TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err_inter));
                            break;
                        default:
                            TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err));
                            break;
                    }
                }
                if (null != listener) {
                    listener.onFailure(arg0, errMsg);
                }
                if (isShow) {
                    TUtils.closePragressDialog();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> result) {
                if (CacheData.isDeBug) {
                    try {
                        JSONObject obj = new JSONObject(result.result);
                        Log.i("请求结果：", obj.toString());
                        // System.out.println("请求结果：" + obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (TUtils.textIsEmpty(result.result)) {
                    TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err_nodata));
                    if (null != listener) {
                        listener.onFailure(null, "");
                    }
                } else {
                    if (null != listener) {
                        try {
                            listener.onSuccess(result.result);
                        } catch (Exception e) {
                            if (CacheData.isDeBug) {
                                Log.i("请求结果：", "解析异常" + e.getMessage());
                            }
                            TUtils.showToast(context, "数据解析异常");
                        }
                    }
                }
                if (isShow) {
                    TUtils.closePragressDialog();
                }
            }
        });
    }

    /**
     * 功能说明:请求网络接口
     *
     * @param map      自动拼接请求的url地址
     * @param baseUrl  请求基础的url地址
     * @param isShow   是否弹出正在加载框true弹出，false不弹出
     * @param listener 请求网络数据的回调监听
     * @author 作者：jarvisT
     */
    public void doGetRequest(HashMap<String, Object> map, String baseUrl, final boolean isShow, final HttpHelpCallBack listener) {
        if (isShow) {
            TUtils.openPragressDialog(context, context.getResources().getString(R.string.toast_http_loading));
        }
        HttpUtils httpUtils = new HttpUtils();
        if (CacheData.isDeBug) {
            Log.i("请求参数：", addParamer(map, baseUrl));
        }
        httpUtils.send(HttpMethod.GET, addParamer(map, baseUrl), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String errMsg) {
                if (arg0 != null) {
                    switch (arg0.getExceptionCode()) {
                        case 0:
                            TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err_inter));
                            break;
                        default:
                            TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err));
                            break;
                    }
                }
                if (null != listener) {
                    listener.onFailure(arg0, errMsg);
                }
                if (isShow) {
                    TUtils.closePragressDialog();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> result) {
                if (CacheData.isDeBug) {
                    try {
                        JSONObject obj = new JSONObject(result.result);
                        Log.i("请求结果：", obj.toString());
                        // System.out.println("请求结果：" + obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (TUtils.textIsEmpty(result.result)) {
                    TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err_nodata));
                    if (null != listener) {
                        listener.onFailure(null, "");
                    }
                } else {
                    if (null != listener) {
                        try {
                            listener.onSuccess(result.result);
                        } catch (Exception e) {
                            if (CacheData.isDeBug) {
                                Log.i("请求结果：", "解析异常" + e.getMessage());
                            }
                            TUtils.showToast(context, "数据解析异常");
                        }
                    }
                }
                if (isShow) {
                    TUtils.closePragressDialog();
                }
            }
        });
    }

    /**
     * 功能说明:请求网络接口
     *
     * @param map      自动拼接请求的url地址
     * @param baseUrl  请求基础的url地址
     * @param params   请求参数
     * @param isShow   是否弹出正在加载框true弹出，false不弹出
     * @param listener 请求网络数据的回调监听
     * @author 作者：jarvisT
     */
    public void doGetRequest(HashMap<String, Object> map, String baseUrl, RequestParams params, final boolean isShow, final HttpHelpCallBack listener) {
        if (isShow) {
            TUtils.openPragressDialog(context, context.getResources().getString(R.string.toast_http_loading));
        }
        HttpUtils httpUtils = new HttpUtils();
        if (CacheData.isDeBug) {
            Log.i("请求参数：", addParamer(map, baseUrl));
        }
        httpUtils.send(HttpMethod.GET, addParamer(map, baseUrl), params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String errMsg) {
                if (arg0 != null) {
                    switch (arg0.getExceptionCode()) {
                        case 0:
                            TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err_inter));
                            break;
                        default:
                            TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err));
                            break;
                    }
                }
                if (null != listener) {
                    listener.onFailure(arg0, errMsg);
                }
                if (isShow) {
                    TUtils.closePragressDialog();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> result) {
                if (CacheData.isDeBug) {
                    try {
                        JSONObject obj = new JSONObject(result.result);
                        Log.i("请求结果：", obj.toString());
                        // System.out.println("请求结果：" + obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (TUtils.textIsEmpty(result.result)) {
                    TUtils.showToast(context, context.getResources().getString(R.string.toast_http_err_nodata));
                    if (null != listener) {
                        listener.onFailure(null, "");
                    }
                } else {
                    if (null != listener) {
                        try {
                            listener.onSuccess(result.result);
                        } catch (Exception e) {
                            if (CacheData.isDeBug) {
                                Log.i("请求结果：", "解析异常" + e.getMessage());
                            }
                            TUtils.showToast(context, "数据解析异常");
                        }
                    }
                }
                if (isShow) {
                    TUtils.closePragressDialog();
                }
            }
        });
    }

    /**
     * get请求方式的添加参数方法
     *
     * @param map
     * @return
     * @author tansheng QQ:717549357
     */
    @SuppressWarnings("rawtypes")
    private static String addParamer(HashMap<String, Object> map) {
        if (map == null || map.size() == 0) {
            return "";
        }
        String url = "";
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            url = url + entry.getKey() + "=" + entry.getValue() + "&";
        }
        return url.substring(0, url.length() - 1);
    }

    /**
     * get请求方式的添加参数方法
     *
     * @param map
     * @param baseUrl 基础地址
     * @return
     * @author tansheng QQ:717549357
     */
    @SuppressWarnings("rawtypes")
    private static String addParamer(HashMap<String, Object> map, String baseUrl) {
        if (map == null || map.size() == 0) {
            return baseUrl;
        }
        String url = baseUrl;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            url = url + entry.getKey() + "=" + entry.getValue() + "&";
        }
        return url.substring(0, url.length() - 1);
    }

    /**
     * MD加密
     *
     * @param sourceStr 需要加密的字符串
     * @param isByte32  true是否返回32位的加密规则，false否则返回16位加密规则
     * @return
     * @author tansheng QQ:717549357
     */
    public static String MD5(String sourceStr, boolean isByte32) {
        String result = "";
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md.update(sourceStr.getBytes());
            // 获得密文
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            if (isByte32) {
                System.out.println("MD5(" + sourceStr + ",32) = " + result);
            } else {
                System.out.println("MD5(" + sourceStr + ",16) = " + result.substring(8, 24));
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return isByte32 ? result : result.substring(8, 24);
    }

}
