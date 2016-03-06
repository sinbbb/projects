package com.example.sec.msg_test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends ActionBarActivity{
    ImageButton info_bttn;
    ImageButton route_bttn;
    ImageButton home_bttn;
    ImageButton loc_bttn;
    private BackPressCloseHandler backPressCloseHandler;
    phpDown task;
    String SERVER = "http://203.230.251.247/"; //서버
    public String nearby = "";
    public static Data[] data = new Data[28];
    public static final String RECO_UUID = "24DDF411-8CF1-440C-87CD-E368DAF9C93E";
    public static final boolean SCAN_RECO_ONLY = true;
    public static final boolean ENABLE_BACKGROUND_RANGING_TIMEOUT = true;
    public static final boolean DISCONTINUOUS_SCAN = false;
    public static String b_destination = "7";
    public static String beacon_id = "0";
    public static Boolean click_route = false;
    public static Boolean click_yrh = false;

    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        draw_line mDraw = new draw_line(this);
        addContentView(mDraw, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));

        //Toast.makeText(getApplicationContext(), beacon_id, Toast.LENGTH_SHORT).show();

       // setContentView(new main_draw(this));
        getWindow().setWindowAnimations(0);
        backPressCloseHandler = new BackPressCloseHandler(this);

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }

        String[] name = getResources().getStringArray(
                R.array.name);
        String[] writer = getResources().getStringArray(R.array.writer_array);
        String[] time = getResources().getStringArray(R.array.time_array);

        for(int i=0; i<28; i++)
        {
            String t="", w="", n="";
            int num;
            if(i < 8)
            {
                t=time[0];
                if(i<3)
                    w = writer[0];
                else if(i<6)
                    w = writer[1];
                else if(i<8)
                    w = writer[2];

            }
            else if(i < 20)
            {
                t=time[1];
                if(i<12)
                    w = writer[3];
                else if(i<14)
                    w = writer[4];
                else if(i<17)
                    w = writer[5];
                else if(i<20)
                    w = writer[6];
            }
            else
            {
                t=time[2];
                if(i<23)
                    w = writer[7];
                else if(i<26)
                    w = writer[8];
                else if(i<28)
                    w = writer[9];
            }
            n=name[i];
            num = i+1;
            Data d = new Data();
            d.setData(t,w,n, num);
            data[i] = d;
        }

        info_bttn=(ImageButton)findViewById(R.id.info_bttn);
        info_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_yrh = false;
                Intent intent1 = new Intent(MainActivity.this, nearby.class);
                //intent1.putExtra("nearby",nearby);
                startActivity(intent1);
                finish();
            }
        });
        route_bttn=(ImageButton)findViewById(R.id.route_bttn);
        route_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_yrh = false;
                Intent intent1 = new Intent(MainActivity.this, route.class);
                startActivity(intent1);
                finish();
            }
        });
        home_bttn=(ImageButton)findViewById(R.id.home_bttn);
        home_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_yrh = false;
                click_route = false;
                Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        loc_bttn=(ImageButton)findViewById(R.id.loc_bttn);
        loc_bttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //원래 코드
                /*
                beacon_id = "7";
                click_yrh = true;
                Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
*/



                //변경된 코드
                click_route = false;
                Intent intent = new Intent(MainActivity.this, RECORangingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
               // Toast.makeText(getApplicationContext(),beacon_id, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class phpDown extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(conn.getInputStream(),
                                        "UTF-8"));
                        for (;;) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null)
                                break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();

        }

        protected void onPostExecute(String str) {
            String[] arr = str.split("/");
        }

    }

    private boolean isBackgroundRangingServiceRunning(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo runningService : am.getRunningServices(Integer.MAX_VALUE)) {
            if(RECOBackgroundRangingService.class.getName().equals(runningService.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("asdf", beacon_id);
        if(beacon_id.equals("0")){
            Toast.makeText(getApplicationContext(), "Cannot find a beacon", Toast.LENGTH_SHORT).show();
        }
        else {
            click_yrh = true;
            Toast.makeText(getApplicationContext(), beacon_id, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
