package com.example.sec.msg_test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import android.os.AsyncTask;

public class info extends ActionBarActivity {
    ImageButton info_bttn;
    ImageButton route_bttn;
    ImageButton home_bttn;
    Spinner spinner1, spinner2;
    ArrayAdapter<String> adapter_list, adapter_data;
    ListView dataList;
    //public Data[] data = new Data[28];

    private BackPressCloseHandler backPressCloseHandler;
    String SERVER = "http://203.230.251.247/";
    String abc = "1";
    phpDown task;
    String nearby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        task = new phpDown();
        task.execute(SERVER + "title.php");
//        Intent intent = getIntent();
//        nearby = (String)intent.getSerializableExtra("nearby");
        //Toast.makeText(getApplicationContext(), abc, Toast.LENGTH_SHORT).show();

        //////////////////////////////////////////////////////////////////////////////////////////////////
        adapter_data = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice);
        dataList  = (ListView)findViewById(R.id.listview);

//        String[] name = getResources().getStringArray(
//                R.array.name);
//        String[] writer = getResources().getStringArray(R.array.writer_array);
//        String[] time = getResources().getStringArray(R.array.time_array);
//
//        for(int i=0; i<28; i++)
//        {
//            String t="", w="", n="";
//            int num;
//            if(i < 8)
//            {
//                t=time[0];
//                if(i<3)
//                    w = writer[0];
//                else if(i<6)
//                    w = writer[1];
//                else if(i<8)
//                    w = writer[2];
//
//            }
//            else if(i < 20)
//            {
//                t=time[1];
//                if(i<12)
//                    w = writer[3];
//                else if(i<14)
//                    w = writer[4];
//                else if(i<17)
//                    w = writer[5];
//                else if(i<20)
//                    w = writer[6];
//            }
//            else
//            {
//                t=time[2];
//                if(i<23)
//                    w = writer[7];
//                else if(i<26)
//                    w = writer[8];
//                else if(i<28)
//                    w = writer[9];
//            }
//            n=name[i];
//            num = i+1;
//            Data d = new Data();
//            d.setData(t,w,n, num);
//            data[i] = d;
//        }

        //////////////////////////////////////////////////////////////////////////////////////////////////

        spinner1 = (Spinner)findViewById(R.id.theme);
        spinner2 = (Spinner)findViewById(R.id.sub_theme);
        ArrayAdapter<CharSequence> adapter_main = ArrayAdapter.createFromResource(this, R.array.theme_array, android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter_main);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapter_sub = ArrayAdapter.createFromResource(info.this, R.array.time_array, android.R.layout.simple_spinner_dropdown_item);

                if (position == 1)
                    adapter_sub = ArrayAdapter.createFromResource(info.this, R.array.writer_array, android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter_sub);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), spinner1.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                adapter_data.clear();
                for(int i=0; i<MainActivity.data.length; i++) {
                    if ((MainActivity.data[i].getTime().equals(spinner2.getSelectedItem().toString())) || (MainActivity.data[i].getWriter().equals(spinner2.getSelectedItem().toString())))
                    {
                        for (int j = 0; j < MainActivity.data.length; j++) {
                            if (MainActivity.data[i].getName().equals(MainActivity.data[j].getName())) {
                                adapter_data.add(MainActivity.data[j].getName());
                            }
                        }
                    }
                }
                dataList.setAdapter(adapter_data);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), adapter_data.getItem(position), Toast.LENGTH_SHORT).show();
                int number = 0;
                for(int i=0; i<MainActivity.data.length; i++)
                {
                    if(MainActivity.data[i].getName().equals(adapter_data.getItem(position)))
                        number = MainActivity.data[i].getNumber();
                }

                String name = adapter_data.getItem(position) + "/" + String.valueOf(number) + "&" + nearby;
                Intent intent = new Intent(info.this, information.class);
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
//                String[] titles = nearby.split("/");
//                String title = "";
//                for(int i=0; i<titles.length; i++)
//                {
//                    title = title.concat(titles[i]);
//                    title = title.concat("/");
//                }

                Intent intent1 = new Intent(info.this, nearby.class);
//                intent1.putExtra("nearby", title);
                startActivity(intent1);
                finish();
            }
        });
        route_bttn=(ImageButton)findViewById(R.id.route_bttn);
        route_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(info.this, route.class);
                startActivity(intent1);
                finish();
            }
        });
        home_bttn=(ImageButton)findViewById(R.id.home_bttn);
        home_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(info.this, MainActivity.class);
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
            //Toast.makeText(getApplicationContext(),arr[27], Toast.LENGTH_SHORT).show();

            //mHandler.postDelayed(mRunnable, 5000);
        }
    }

    public void onBackPressed() {
        //super.onBackPressed();
        //backPressCloseHandler.onBackPressed();
//        String[] titles = nearby.split("/");
//        String title = "";
//        for(int i=0; i<titles.length; i++)
//        {
//            title = title.concat(titles[i]);
//            title = title.concat("/");
//        }

        Intent intent1 = new Intent(info.this, nearby.class);
//        intent1.putExtra("nearby", title);
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
