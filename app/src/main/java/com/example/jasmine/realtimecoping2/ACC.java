package com.example.jasmine.realtimecoping2;

/**
 * Created by Jasmine on 4/30/16.
 */
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;

public class ACC extends AppCompatActivity implements SensorEventListener{
    //fields
    private static float x;
    private static float y;
    private static float z;
    private static double THRESHOLD;
    public static String UserStatus;
    // The following are used for the shake detection
    private SensorManager SensorManager1;
    private Sensor myAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        Sensor mySensor = event.sensor;
        //"locate" the accelerometer and set x,y,z
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
        }

    }


    public void onClick(View view) {
        //set up Accelerometer
        SensorManager1 = (SensorManager) getSystemService(SENSOR_SERVICE);
        myAccelerometer = SensorManager1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorManager1.registerListener(this, myAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        String st1 = "{"+x+","+y+","+z+"}";

        //print shake or no shake

        THRESHOLD = 20;
        if (Math.sqrt(x * x + y * y + z * z)<THRESHOLD) {
            UserStatus = "static";
        }else {
            UserStatus = "moving";
        }
    }

    public void stopProcess(View view) {
        super.onPause();
        SensorManager1.unregisterListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
