package com.jarvis.tbaseviewlib.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static com.jarvis.tbaseviewlib.view.pullableview.PullToRefreshLayout.TAG;

/**
 * Created by tansheng on 2017/8/1.
 */

public class SensorUtil implements SensorEventListener{

    private final SensorManager sm;
    private final Sensor aSensor;
    private final Sensor mSensor;
    // 加速度传感器数据
    private float accelerometerValues[] = new float[3];
    // 地磁传感器数据
    private float magneticFieldValues[] = new float[3];
    // 旋转矩阵，用来保存磁场和加速度的数据
    private float r[] = new float[9];
    // 模拟方向传感器的数据（原始数据为弧度）
    private float values[] = new float[3];
    //方向
    private String orientation;

    private SensorCallBackListener listener;

    public void setListener(SensorCallBackListener listener) {
        this.listener = listener;
    }

    public SensorUtil(Context context){
        sm= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        aSensor=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor=sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    /**
     * 注册传感器
     */
    public void registerSensor(){
        sm.registerListener(this,aSensor,SensorManager.SENSOR_DELAY_UI);
        sm.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);
    }

    /***
     * 解绑传感器
     */
    public void unregisterSensor(){
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magneticFieldValues = event.values;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelerometerValues = event.values;
        calculateOrientation();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private  void calculateOrientation() {
        SensorManager.getRotationMatrix(r, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(r, values);
        // 要经过一次数据格式的转换，转换为度
        values[0] = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);
        if(values[0] >= -5 && values[0] < 5){
            Log.i(TAG, "北");
            orientation="北";
        }
        else if(values[0] >= 5 && values[0] < 85){
            Log.i(TAG, "东北");
            orientation="东北";
        }
        else if(values[0] >= 85 && values[0] <=95){
            Log.i(TAG, "东");
            orientation="东";
        }
        else if(values[0] >= 95 && values[0] <175){
            Log.i(TAG, "东南");
            orientation="东南";
        }
        else if((values[0] >= 175 && values[0] <= 180) || (values[0]) >= -180 && values[0] < -175){
            Log.i(TAG, "南");
            orientation="南";
        }
        else if(values[0] >= -175 && values[0] <-95){
            Log.i(TAG, "西南");
            orientation="西南";
        }
        else if(values[0] >= -95 && values[0] < -85){
            Log.i(TAG, "西");
            orientation="西";
        }
        else if(values[0] >= -85 && values[0] <-5){
            Log.i(TAG, "西北");
            orientation="西北";
        }

        if (listener!=null){
            listener.onSensorChange(values[2],values[1],values[0],orientation);
        }

    }

    public interface SensorCallBackListener{
        void onSensorChange(float x,float y,float z,String orientation);
    }
}
