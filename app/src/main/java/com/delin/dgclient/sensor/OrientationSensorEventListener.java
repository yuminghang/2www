package com.delin.dgclient.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 *传感器事件监听 事件处理 获取手机的方位
 */
public final class OrientationSensorEventListener implements SensorEventListener {

    public float x = 0.0f;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            //x表示手机指向的方位，0表示北,90表示东，180表示南，270表示西
            x = sensorEvent.values[SensorManager.DATA_X];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
