package com.example.jasmine.realtimecoping2;

import android.database.DataSetObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.content.Intent;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ChatArrayAdapter adp;
    private ListView list;
    private EditText chatText;
    private Button send;
    private boolean left = true;
    private List<Integer> FunnypicArray;
    private List<Integer> KittypicArray;
    String reply;
    String aff = "";
    String previousM;

    //fields
    private static float x;
    private static float y;
    private static float z;
    private static double THRESHOLD;
    public static String UserStatus;
    private List<String> AccStatus=new ArrayList<String>();
    // The following are used for the shake detection
    private SensorManager SensorManager1;
    private Sensor myAccelerometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Smartphone accelerometer setup
        SensorManager1 = (SensorManager) getSystemService(SENSOR_SERVICE);
        myAccelerometer = SensorManager1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorManager1.registerListener(this, myAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //check if the user is being too static
        //WAIT FOR 10 SECONDS WHEN TESTING
              new CountDownTimer(10000, 1000) {
              public void onTick(long millisUntilFinished) {
                 AccStatus.add(UserStatus);
              }
              public void onFinish() {
                  int count = 0;
                  for (int i = 1; i < AccStatus.size(); i++) {
                      if (AccStatus.get(i).equals("static")) {
                         count++;
                     }
                 }
                  if (count > 6) {
                      adp.add(new ChatMessage(left, "Hey how are you doing? Rate your mood on the scale of 1（￣へ￣） to 10  (∩_∩)", "text"));
                  }
              }
              }.start();
        //link to button Send, ListView, and EditText
        send = (Button)findViewById(R.id.btn);
        list = (ListView)findViewById(R.id.listview);
        chatText = (EditText)findViewById(R.id.chat);
        adp = new ChatArrayAdapter(getApplicationContext(),R.layout.chat);
        FunnypicArray=new ArrayList<Integer>(Arrays.asList(R.drawable.funny1, R.drawable.funny2, R.drawable.funny3, R.drawable.funny4, R.drawable.funny5,
                R.drawable.funny6, R.drawable.funny7, R.drawable.funny8));
        KittypicArray = new ArrayList<Integer>(Arrays.asList(R.drawable.kitty1, R.drawable.kitty9, R.drawable.kitty3, R.drawable.kitty4, R.drawable.kitty5,
                R.drawable.kitty6, R.drawable.kitty7, R.drawable.kitty8));

        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChatMessage();
            }
        });
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(adp);
        adp.registerDataSetObserver(new DataSetObserver() {
            public void OnChanged() {
                super.onChanged();
                list.setSelection(adp.getCount() - 1);
            }
        });
        chatText.setText("");
    }

    private boolean sendChatMessage(){
        //runnable handler delays the response to make it feel real
        Runnable GoodResponse = new Runnable() {
            @Override
            public void run() {
                adp.add(new ChatMessage(true, "Glad that you are feeling great! Keep that smiley face:)","text"));
            }
        };
        Runnable AffirmatonQ = new Runnable() {
            @Override
            public void run() {
                adp.add(new ChatMessage(true, "What puts you in a bad mood\n" +
                        "a. School work\n" +
                        "b. Social life\n" +
                        "c. Work\n" +
                        "d. Family\n" +
                        "Type in the letter to tell me.", "text"));
            }
        };final Runnable ResponseFunnyPic2 = new Runnable() {
            @Override
            public void run() {
                if (FunnypicArray.size()!=0){
                    Random r = new Random();
                    int randomIndex = r.nextInt(FunnypicArray.size());
                    adp.add(new ChatMessage(true,FunnypicArray.get(randomIndex),"image"));
                    FunnypicArray.remove(randomIndex);
                }
                else {
                    adp.add(new ChatMessage(true,"currently acquiring more cuteness...stay tuned:)","text"));
                }
            };
        };
        final Runnable ResponseFunnyPic1 = new Runnable() {
            @Override
            public void run() {
                adp.add(new ChatMessage(true, "You know what, I just saw a funny post yesterday. Check it out! Type in anything to get more.", "text"));
                final Handler handler2 = new Handler();
                handler2.postDelayed(ResponseFunnyPic2, 4000 + (long) Math.random() * 2000);
            }
        };
        final Runnable ResponseKittyPic2 = new Runnable() {
            @Override
            public void run() {
                if (KittypicArray.size() != 0) {
                    Random r = new Random();
                    int randomIdex = r.nextInt(KittypicArray.size());
                    adp.add(new ChatMessage(true, KittypicArray.get(randomIdex), "image"));
                    KittypicArray.remove(randomIdex);
                }
                else {
                    adp.add(new ChatMessage(true, "currently acquiring more cuteness...stay tuned:)", "text"));
                }
            }
        };
        final Runnable ResponseKittyPic1 = new Runnable() {
            @Override
            public void run() {
                adp.add(new ChatMessage(true, "You know what, I just saw an adorable cutie yesterday. Check it out! Type in anything to get more.", "text"));
                final Handler handler3 = new Handler();
                handler3.postDelayed(ResponseKittyPic2, 4000 + (long) Math.random() * 2000);
            }
        };

        //sending user's message
        //and get response from the app
        String UserMessage = chatText.getText().toString();
        adp.add(new ChatMessage(false, UserMessage, "text"));
        int itemNum = adp.getCount();
        if (adp.getItem(itemNum-2).type.equals("text")) {
            previousM = (String) adp.getItem(itemNum - 2).message;
            try {
                //after the app asks to rate mood 1~10
                if (previousM.equals("Hey how are you doing? Rate your mood on the scale of 1（￣へ￣） to 10  (∩_∩)")
                        || previousM.equals("Sorry, it doesn't work this way. Rate your mood on the scale of 1（￣へ￣） to 10  (∩_∩)")){
                    if (Integer.parseInt(UserMessage) >= 7){
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(GoodResponse, 1000 + (long) Math.random() * 2000);
                    }
                    if (Integer.parseInt(UserMessage) < 7) {
                        final Handler handler0 = new Handler();
                        handler0.postDelayed(AffirmatonQ, 1000 + (long) Math.random() * 2000);
                        if (Integer.parseInt(UserMessage) >= 4 && Integer.parseInt(UserMessage) < 7) {
                            reply = "ehh";
                        }
                        if (Integer.parseInt(UserMessage) < 4) {
                            reply = "nah";
                        }
                    }
                }
                //after the app asks to specify a~d
                else if (previousM.equals("What puts you in a bad mood\n" +
                        "a. School work\n" +
                        "b. Social life\n" +
                        "c. Work\n" +
                        "d. Family\n" +
                        "Type in the letter to tell me.")
                        ||previousM.equals("Sorry, it doesn't work this way. Tell me by typing in the letter.")) {
                    if (UserMessage.equals("a")) {
                        aff = "If the course is hard for you, it's hard for everybody. It just takes more time for you to digest and you will nail it! And maybe you already are!";
                    }
                    else if (UserMessage.equals("b")) {
                        aff = "You know, sometimes our friends are also going through a hard time, but they won't tell us. So they act out in ways that we wouldn't expect. " +
                                "Give them some space and take some time out for yourself as well. These things are normal for any friendship!";
                    }
                    else if (UserMessage.equals("c")) {
                        aff = "Finding the right team is difficult for everyone. Do not give up hope! Take some time to recharge." +
                                "You're making progress, keep going at it!" + "You chose this line of work for a reason. Remember your end game and keep pursuing it:)";
                    }
                    else if (UserMessage.equals("d")) {
                        aff = "Family can be a hassle sometimes, but they are the ones that truly test us to show us what unconditional love is. " +
                                "Be the bigger person that I know you are. Your patience will get you far.";
                    } else {
                        aff = "Sorry, it doesn't work this way. Tell me by typing in the letter.";
                    }
                    Runnable ResponseAff = new Runnable() {
                        @Override
                        public void run() {
                            adp.add(new ChatMessage(true, aff, "text"));
                            if (!aff.equals("Sorry, it doesn't work this way. Tell me by typing in the letter.")) {
                                //neutral
                                if (reply.equals("ehh")) {
                                    final Handler handler2 = new Handler();
                                    handler2.postDelayed(ResponseFunnyPic1, 12000 + (long) Math.random() * 2000);
                                }
                                //in a bad shape
                                if (reply.equals("nah")) {
                                    final Handler handler3 = new Handler();
                                    handler3.postDelayed(ResponseKittyPic1, 12000 + (long) Math.random() * 2000);
                                }
                                chatText.setText("");
                            }
                        }
                    };
                    final Handler handlerAff = new Handler();
                    handlerAff.postDelayed(ResponseAff, 1000 + (long) Math.random() * 2000);
                }
            }

            catch (NumberFormatException e)
            {
                adp.add(new ChatMessage(true, "Sorry, it doesn't work this way. Rate your mood on the scale of 1（￣へ￣） to 10  (∩_∩)","text"));
            }
        }
        if (adp.getItem(itemNum-2).type.equals("image")){
            //neutral
            if (reply.equals("ehh")) {
                final Handler handler2 = new Handler();
                handler2.postDelayed(ResponseFunnyPic1, 2000 + (long) Math.random() * 2000);
            }
            //in a bad shape
            if (reply.equals("nah")) {
                final Handler handler3 = new Handler();
                handler3.postDelayed(ResponseKittyPic1, 2000 + (long) Math.random() * 2000);
            }
        }

        return true;
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

        THRESHOLD = 20;
        if (Math.sqrt(x * x + y * y + z * z)<THRESHOLD) {
            UserStatus = "static";
        }else {
            UserStatus = "moving";
        }

    }

    public void StartService(View view){
        Intent intent = new Intent(this,MyService.class);
        startService(intent);
    }

    public void StopService(View view){
        Intent intent = new Intent(this,MyService.class);
        stopService(intent);
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
