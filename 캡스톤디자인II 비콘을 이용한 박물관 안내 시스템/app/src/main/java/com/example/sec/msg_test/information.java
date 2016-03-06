package com.example.sec.msg_test;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class information extends ActionBarActivity {
    ImageButton info_bttn;
    ImageButton route_bttn;
    ImageButton home_bttn;
    TextView things_name;
    TextView things_info;
    private BackPressCloseHandler backPressCloseHandler;
    phpDown task;
    String SERVER = "http://203.230.251.247/";
    String[] nearby;
    MediaPlayer bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Intent intent = getIntent();
        String name = (String)intent.getSerializableExtra("name");
        things_name = (TextView)findViewById(R.id.things_name);
        things_info = (TextView)findViewById(R.id.things_info);
        nearby = name.split("&");
        String[] value = nearby[0].split("/");

        things_name.setText(value[0]);

        task = new phpDown();
        task.execute(SERVER + "information.php?Number=" + value[1]);

        int[] bgm_id = {R.raw.num1, R.raw.num2, R.raw.num3, R.raw.num4,
                R.raw.num5, R.raw.num6, R.raw.num7, R.raw.num8,
                R.raw.num9, R.raw.num10, R.raw.num11, R.raw.num12,
                R.raw.num13, R.raw.num14, R.raw.num15, R.raw.num16,
                R.raw.num17, R.raw.num18, R.raw.num19, R.raw.num20,
                R.raw.num21, R.raw.num22, R.raw.num23, R.raw.num24,
                R.raw.num25, R.raw.num26, R.raw.num27, R.raw.num28};

        bgm = MediaPlayer.create(this, R.raw.num1);

        for(int i=0; i<MainActivity.data.length; i++)
        {
            if(MainActivity.data[i].getName().equals(value[0]))
            {
                bgm = MediaPlayer.create(this, bgm_id[i]);
            }
        }

        bgm.setLooping(true);;
        bgm.start();

        getWindow().setWindowAnimations(0);
        backPressCloseHandler = new BackPressCloseHandler(this);
        info_bttn=(ImageButton)findViewById(R.id.info_bttn);
        info_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bgm.stop();;
                bgm.release();;
                bgm = null;

                Intent intent1 = new Intent(information.this, nearby.class);
                startActivity(intent1);
                finish();
            }
        });
        route_bttn=(ImageButton)findViewById(R.id.route_bttn);
        route_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bgm.stop();;
                bgm.release();;
                bgm = null;

                Intent intent1 = new Intent(information.this, route.class);
                startActivity(intent1);
                finish();
            }
        });
        home_bttn=(ImageButton)findViewById(R.id.home_bttn);
        home_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bgm.stop();;
                bgm.release();;
                bgm = null;

                Intent intent1 = new Intent(information.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });

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
            things_info.setText(arr[0]);
        }
    }

    public void onBackPressed() {
        bgm.stop();;
        bgm.release();;
        bgm = null;

        Intent intent1 = new Intent(information.this, nearby.class);
        startActivity(intent1);
        finish();
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
