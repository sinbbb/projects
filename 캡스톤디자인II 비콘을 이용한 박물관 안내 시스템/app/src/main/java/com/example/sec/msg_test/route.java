package com.example.sec.msg_test;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class route extends ActionBarActivity {
    ImageButton info_bttn;
    ImageButton route_bttn;
    ImageButton home_bttn;
    private BackPressCloseHandler backPressCloseHandler;
    phpDown task, task2;
    String SERVER = "http://203.230.251.247/";
    public Data[] data = new Data[28];
    ArrayAdapter<String> adapter_data;
    ListView dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
//        task = new phpDown();
//        task.execute(SERVER + "title.php");

        adapter_data = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice);


        for(int i=0; i<data.length; i++)
            adapter_data.add(MainActivity.data[i].getName());

        dataList  = (ListView)findViewById(R.id.listview);
        dataList.setAdapter(adapter_data);

        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String number = "1";

                //Toast.makeText(getApplicationContext(), adapter_data.getItem(position), Toast.LENGTH_SHORT).show();

                for (int i = 0; i < MainActivity.data.length; i++) {
                    if (MainActivity.data[i].getName().equals(adapter_data.getItem(position))) {
                        number = String.valueOf(MainActivity.data[i].getNumber());
                    }
                }
                task2 = new phpDown();
                String Query = SERVER + "getID.php?Number=" + number;
                task2.execute(Query);

            }
        });
        getWindow().setWindowAnimations(0);
        backPressCloseHandler = new BackPressCloseHandler(this);
        info_bttn=(ImageButton)findViewById(R.id.info_bttn);
        info_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.click_yrh = false;
                Intent intent1 = new Intent(route.this, nearby.class);
                startActivity(intent1);
                finish();
            }
        });
        route_bttn=(ImageButton)findViewById(R.id.route_bttn);
        route_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(route.this, route.class);
                startActivity(intent1);
                finish();
            }
        });
        home_bttn=(ImageButton)findViewById(R.id.home_bttn);
        home_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.click_route = false;
                MainActivity.click_yrh = false;
                Intent intent1 = new Intent(route.this, MainActivity.class);
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

            Toast.makeText(getApplicationContext(), "Selected thing's ID = " + arr[0], Toast.LENGTH_SHORT).show();
            char[] temp = arr[0].toCharArray();
            MainActivity.b_destination = String.valueOf(temp[1]);
            MainActivity.click_yrh = false;
            MainActivity.click_route = true;
            Intent intent1 = new Intent(route.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
    }

    public void onBackPressed() {
        //super.onBackPressed();
        //backPressCloseHandler.onBackPressed();
        Intent intent1 = new Intent(route.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route, menu);
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
