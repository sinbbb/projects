package com.example.sec.msg_test;

import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Vector;

public class nearby extends ActionBarActivity {
    ImageButton info_bttn;
    ImageButton route_bttn;
    ImageButton home_bttn;
    Button search_by_theme;
    ArrayAdapter<String> adapter_data;
    ListView dataList;
    public Data[] data = new Data[28];
    String SERVER = "http://203.230.251.247/";
    phpDown task, task2;
    int number = 1;
    //String temp = "1";


    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        task = new phpDown();
        task.execute(SERVER + "nearby.php?ID=" + MainActivity.beacon_id);

        task2 = new phpDown();
        task2.execute(SERVER + "title.php");

        search_by_theme = (Button)findViewById(R.id.search_by_theme);
        adapter_data = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice);
        dataList  = (ListView)findViewById(R.id.listview);

        search_by_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(nearby.this, info.class);
                startActivity(intent1);
                finish();
            }
        });

        dataList.setAdapter(adapter_data);

        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for(int i=0; i<data.length; i++)
                {
                    if(data[i].getName().equals(adapter_data.getItem(position))) {
                        number = data[i].getNumber();
                    }

                }

                String name = adapter_data.getItem(position) + "/" + String.valueOf(number) + "&";
                for(int i=0; i<adapter_data.getCount(); i++)
                {
                    name = name.concat(adapter_data.getItem(i));
                    name = name.concat("/");
                }

                Intent intent = new Intent(nearby.this, information.class);
                intent.putExtra("name",name);
                startActivity(intent);
                finish();
            }
        });

        getWindow().setWindowAnimations(0);
        backPressCloseHandler = new BackPressCloseHandler(this);
        info_bttn=(ImageButton)findViewById(R.id.info_bttn);
        info_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(nearby.this, nearby.class);
                startActivity(intent1);
                finish();
            }
        });
        route_bttn=(ImageButton)findViewById(R.id.route_bttn);
        route_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(nearby.this, route.class);
                startActivity(intent1);
                finish();
            }
        });
        home_bttn=(ImageButton)findViewById(R.id.home_bttn);
        home_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(nearby.this, MainActivity.class);
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
            if(arr.length > 5) {    // 작품 명 받아와 데이터 초기화
                for (int i = 0; i < arr.length - 1; i++) {
                    Data d = new Data();
                    d.setData(arr[i], i+1);
                    data[i] = d;
                }
            }
            else    // 근처작품 찾기
            {
                for (int i = 0; i < arr.length - 1; i++) {
                    adapter_data.add(arr[i]);
                }
            }
        }
    }

    public void onBackPressed() {
        //super.onBackPressed();
        //backPressCloseHandler.onBackPressed();
        Intent intent1 = new Intent(nearby.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
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