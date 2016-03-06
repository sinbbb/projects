package com.example.sec.msg_test;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.perples.recosdk.RECOBeacon;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dell Client on 2015-06-24.
 */
public class RECORangingListAdapter extends BaseAdapter {
    private ArrayList<RECOBeacon> mRangedBeacons;
    private LayoutInflater mLayoutInflater;
    private RECORangingActivity reco = (RECORangingActivity)RECORangingActivity.d;

    public static String major, rssi, time, loc;
    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
    String path = "storage/emulated/0/apk";
    String filename;
    Context context = null;
    int result;
    String Result = "1";



    public MyCount CDT = new MyCount(30*60*1000, 1000);

    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }
        public void onTick(long millisUntilFinished)
        {
            time = formatTime(1800000 - millisUntilFinished);

        }
        public void onFinish(){}
    }

    public RECORangingListAdapter(Context context) {
        super();
        mRangedBeacons = new ArrayList<RECOBeacon>();
        mLayoutInflater = LayoutInflater.from(context);
        CDT.start();
        this.context = context;

    }

    public void updateBeacon(RECOBeacon beacon) {
        synchronized (mRangedBeacons) {
            if (mRangedBeacons.contains(beacon)) {
                mRangedBeacons.remove(beacon);
            }
            mRangedBeacons.add(beacon);
        }
    }

    public void updateAllBeacons(Collection<RECOBeacon> beacons) {
        synchronized (beacons) {
            mRangedBeacons = new ArrayList<RECOBeacon>(beacons);
        }
    }

    public void clear() {
        mRangedBeacons.clear();
    }

    @Override
    public int getCount() {
        return mRangedBeacons.size();
    }

    @Override
    public Object getItem(int position) {
        return mRangedBeacons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("bbb",MainActivity.beacon_id);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_ranging_beacon,	parent, false);
            viewHolder = new ViewHolder();
//            viewHolder.recoMajor = (TextView) convertView.findViewById(R.id.recoMajor);
//            viewHolder.recoRssi = (TextView) convertView.findViewById(R.id.recoRssi);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final RECOBeacon recoBeacon = mRangedBeacons.get(position);
//        viewHolder.recoMajor.setText(recoBeacon.getMajor() + "");
//        viewHolder.recoRssi.setText(recoBeacon.getRssi() + "");

        filename = "test.txt";
        final File file = new File(path + "/" + filename);

        map.put(recoBeacon.getMajor(), recoBeacon.getRssi());

        rssi = String.valueOf(recoBeacon.getRssi()+ "\n");

        Set<Map.Entry<Integer, Integer>> set = map.entrySet();
        final Iterator<Map.Entry<Integer, Integer>> it = set.iterator();
        String str = "";
        temp.clear();

        //map.put(recoBeacon.getMajor(), recoBeacon.getRssi());

        //result = check(recoBeacon.getMajor(), recoBeacon.getRssi());
        result = check(map);
        if(result == 1 || result == 2 || result == 3)
            Result = "1";
        else if(result == 4 || result == 5 || result == 6)
            Result = "2";
        else if(result == 7 || result == 8)
            Result = "3";
        else if(result == 9 || result == 10)
            Result = "4";
        else if(result == 11 || result == 12)
            Result = "5";
        else if(result == 13 || result == 14)
            Result = "6";
        else if(result == 15 || result == 16 || result == 17)
            Result = "7";
//        else
//            Result = "2";
        MainActivity.beacon_id = Result;
        RECORangingActivity.c = true;
       // MainActivity.click_yrh = true;

        reco.finish();





//        RECORangingActivity.b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
////                String str = "";
////                temp.clear();
////
////                //map.put(recoBeacon.getMajor(), recoBeacon.getRssi());
////
////                //result = check(recoBeacon.getMajor(), recoBeacon.getRssi());
////                result = check(map);
////                if(result == 1 || result == 2 || result == 3)
////                    Result = "1";
////                else if(result == 4 || result == 5 || result == 6)
////                    Result = "2";
////                else if(result == 7 || result == 8)
////                    Result = "3";
////                else if(result == 9 || result == 10)
////                    Result = "4";
////                else if(result == 11 || result == 12)
////                    Result = "5";
////                else if(result == 13 || result == 14)
////                    Result = "6";
////                else if(result == 15 || result == 16 || result == 17)
////                    Result = "7";
////                else
////                    Result = "abc";
////                MainActivity.beacon_id = Result;
//
////                while(it.hasNext()){
////                    Map.Entry<Integer, Integer> e = (Map.Entry<Integer, Integer>)it.next();
////
////                    str += "Major : " + e.getKey() + "  Rssi : " + e.getValue() + "\n";
////                }
////
////                RECORangingActivity.tv3.setText(str);
////                RECORangingActivity.tv4.setText("현재위치 : " + Result);
//
//            }
//        });

//        if(result == 1 || result == 2 || result == 3)
//            Result = "1";
//        else if(result == 4 || result == 5 || result == 6)
//            Result = "2";
//        else if(result == 7 || result == 8)
//            Result = "3";
//        else if(result == 9 || result == 10)
//            Result = "4";
//        else if(result == 11 || result == 12)
//            Result = "5";
//        else if(result == 13 || result == 14)
//            Result = "6";
//        else if(result == 15 || result == 16 || result == 17)
//            Result = "7";
//        else
//            Result = "abc";
//        MainActivity.beacon_id = Result;
        //if(! Result.equals(""))
        //Log.d("aaa",Result);

        return convertView;
    }
    public int check(Map<Integer, Integer> value)
    {
        ArrayList<Integer> c = new ArrayList<Integer>();

        Set<Map.Entry<Integer, Integer>> set = value.entrySet();
        final Iterator<Map.Entry<Integer, Integer>> it = set.iterator();

        int temp1 = 0;
        int major = 0, rssi = 0;

        while(it.hasNext()){
            Map.Entry<Integer, Integer> e = (Map.Entry<Integer, Integer>)it.next();
            major = e.getKey();
            rssi = e.getValue();
        }

        switch(major){
            case 1:
                if(rssi >= -92 && rssi <= -90){
                    c.add(1);
                    c.add(3);
                }
                else if( rssi >= -78 &&  rssi <= -76)
                    c.add(2);
                else if( rssi >= -99 &&  rssi <= -97)
                    c.add(4);
                else if(rssi >= -96 && rssi <= -93)
                    c.add(3);
                temp.add(c);
                break;
            case 2:
                if( rssi >= -98 &&  rssi <= -97){
                    c.add(2);
                    c.add(6);
                }
                else if(rssi >= -96 && rssi <= -95)
                {
                    c.add(1);
                    c.add(2);
                    c.add(6);
                }
                else if(rssi == -94){
                    c.add(1);
                    c.add(2);
                }
                else if(rssi >= -93 && rssi <= -92)
                    c.add(2);
                else if( rssi >= -91 &&  rssi <= -90){
                    c.add(2);
                    c.add(3);
                    c.add(5);
                }
                else if(rssi == -89){
                    c.add(3);
                    c.add(5);
                }
                else if( rssi >= -88 &&  rssi <= -85)
                    c.add(3);
                else if( rssi >= -77 &&  rssi <= -73)
                    c.add(4);
                else if( rssi >= -103 &&  rssi <= -101)
                    c.add(7);
                temp.add(c);
                break;
            case 3:
                if( rssi >= -84 &&  rssi <= -78)
                    c.add(7);
                else if( rssi >= -92 &&  rssi <= -88)
                    c.add(6);
                else if( rssi >= -95 &&  rssi <= -93)
                    c.add(8);
                else if(rssi == -98){
                    c.add(4);
                    c.add(5);
                }
                else if(rssi == -99)
                    c.add(4);
                else if(rssi == -100){
                    c.add(4);
                    c.add(9);
                }
                else if( rssi >= -102 &&  rssi <= -101)
                    c.add(9);
                else if( rssi >= -97 &&  rssi <= -96){
                    c.add(5);
                    c.add(8);
                }
                temp.add(c);
                break;
            case 4:
                if( rssi >= -103 &&  rssi <= -102)
                    c.add(14);
                else if( rssi == -101){
                    c.add(7);
                    c.add(14);
                }
                else if( rssi >= -100 &&  rssi <= -99){
                    c.add(7);
                    c.add(13);
                }
                else if( rssi == -98){
                    c.add(12);
                    c.add(13);
                }
                else if(rssi == -97)
                    c.add(12);
                else if( rssi == -96){
                    c.add(6);
                    c.add(10);
                    c.add(12);
                }
                else if(rssi == -95){
                    c.add(6);
                    c.add(10);
                }
                else if( rssi >= -94 && rssi <= -92){
                    c.add(6);
                    c.add(10);
                    c.add(11);
                }
                else if(rssi == -91)
                    c.add(10);
                else if( rssi >= -90 &&  rssi <= -86){
                    c.add(8);
                    c.add(10);
                }
                else if( rssi >= -85 &&  rssi <= -84)
                    c.add(10);
                else if(rssi >= -81 && rssi <= -73)
                    c.add(9);
                temp.add(c);
                break;
            case 5:
                if( rssi >= -81 &&  rssi <= -75)
                    c.add(11);
                else if( rssi >= -91 &&  rssi <= -84)
                    c.add(12);
                else if( rssi >= -92 &&  rssi <= -94){
                    c.add(10);
                    c.add(12);
                    c.add(13);
                }
                else if ( rssi == -95){
                    c.add(9);
                    c.add(10);
                    c.add(12);
                    c.add(13);
                }
                else if( rssi == -96){
                    c.add(8);
                    c.add(9);
                    c.add(10);
                    c.add(12);
                    c.add(13);
                }
                else if(rssi == -97){
                    c.add(8);
                    c.add(9);
                    c.add(12);
                    c.add(13);
                }
                else if( rssi == -98){
                    c.add(8);
                    c.add(9);
                    c.add(12);
                    c.add(13);
                }
                else if( rssi >= -100 && rssi <= -99){
                    c.add(9);
                    c.add(14);
                }
                else if( rssi == -101){
                    c.add(7);
                    c.add(9);
                    c.add(14);
                }
                else if( rssi >= -103 &&  rssi <= -102){
                    c.add(7);
                    c.add(14);
                }
                temp.add(c);
                break;
            case 6:
                if( rssi >= -73 &&  rssi <= -71)
                    c.add(13);
                else if( rssi >= -88 &&  rssi <= -80)
                    c.add(14);
                else if( rssi >= -91 &&  rssi <= -90)
                    c.add(12);
                else if( rssi >= -93 &&  rssi <= -92){
                    c.add(12);
                    c.add(15);
                    c.add(16);
                }
                else if( rssi >= -96 &&  rssi <= -94){
                    c.add(11);
                    c.add(12);
                    c.add(15);
                    c.add(16);
                }
                else if(rssi >= -98 && rssi <= -97){
                    c.add(11);
                    c.add(12);
                }
                else if(rssi >= -104 && rssi <= -100)
                    c.add(9);
                temp.add(c);
                break;
            case 7:
                if( rssi >= -75 &&  rssi <= -69)
                    c.add(16);
                else if( rssi >= -88 &&  rssi <= -85)
                    c.add(15);
                else if(rssi == -89){
                    c.add(15);
                    c.add(17);
                }
                else if( rssi >= -91 &&  rssi <= -90)
                    c.add(17);
                else if( rssi >= -93 &&  rssi <= -92){
                    c.add(14);
                    c.add(17);
                }
                else if( rssi >= -95 &&  rssi <= -94)
                    c.add(14);
                else if( rssi >= -98 &&  rssi <= -96){
                    c.add(13);
                    c.add(14);
                }
                temp.add(c);
                break;
        }

        int count = 0;

        for(int i = 0; i < temp.get(0).size(); i++){
            temp1 = temp.get(0).get(i);
            for(int k = i+1; k < temp.size(); k++){
                for(int j = 0; j < temp.get(k).size(); j++){
                    if(temp1 == temp.get(k).get(j)){
                        count++;
                    }
                }
            }
            if(count == temp.size()-1)
                return temp1;
            else
                count = 0;
        }

        return 0;
    }
    public String formatTime(long mill){
        String output = "";
        long seconds = mill / 1000;
        long minutes = seconds / 60;
        long msec = mill / 10;

        msec = msec % 100;
        seconds = seconds % 60;
        minutes = minutes % 60;

        String msecD = String.valueOf(msec);
        String secondsD = String.valueOf(seconds);
        String minutesD = String.valueOf(minutes);

        if(msec < 10)
            msecD = "0" + msec;
        if (seconds < 10)
            secondsD = "0" + seconds;
        if (minutes < 10)
            minutesD = "0" + minutes;

        output = minutesD + ":" + secondsD + ":" + msecD;

        return output;
    }

    public void filesave(FileOutputStream fos, File file){
        try {
            fos = context.openFileOutput(filename, Context.MODE_APPEND);
            fos = new FileOutputStream(file, true);
            fos.write(time.getBytes());
            fos.write(" ".getBytes());
            fos.write(rssi.getBytes());
            fos.write(" ".getBytes());
            fos.write(String.valueOf(result).getBytes());
            fos.write("\n".getBytes());

            //fos.write(loc.getBytes());
            fos.close();
        }catch(Exception e){}
    }
    static class ViewHolder {
        TextView recoProximityUuid;
        TextView recoMajor;
        TextView recoMinor;
        TextView recoTxPower;
        TextView recoRssi;
        TextView recoProximity;
        TextView recoAccuracy;
    }
    //fos.close();
}
