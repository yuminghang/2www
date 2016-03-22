package com.delin.dgclient.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/12/22 0022.
 */

public class AccelerationSensorEventListener implements SensorEventListener {

    public boolean isMove = false;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float[] values = sensorEvent.values;
        DecimalFormat df = new DecimalFormat("######0.0");
        Log.d("acc:", "x:" + df.format(values[0]) + " y:" + (df.format(values[1])) + " z:" + df.format(values[2]));
        double a;
        a = Math.sqrt(Math.sqrt(values[1] * values[1] + values[2] * values[2])
                * Math.sqrt(values[1] * values[1] + values[2] * values[2]) + values[0] * values[0]);
        a = Double.parseDouble(df.format(a));
        Log.d("A:", a + "");
        if (9.8 >= a && a >= 9.2) {
            isMove = true;
        } else {
            isMove = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

}