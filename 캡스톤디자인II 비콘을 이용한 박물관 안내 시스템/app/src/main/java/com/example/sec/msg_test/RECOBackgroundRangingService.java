package com.example.sec.msg_test;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECOMonitoringListener;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dell Client on 2015-06-24.
 */
public class RECOBackgroundRangingService extends Service implements RECOMonitoringListener, RECORangingListener, RECOServiceConnectListener {
    private long mScanDuration = 1*1000L;
    private long mSleepDuration = 10*1000L;
    private long mRegionExpirationTime = 3*1000L;
    private int mNotificationID = 9999;

    private RECOBeaconManager mRecoManager;
    private ArrayList<RECOBeaconRegion> mRegions;

    @Override
    public void onCreate() {
        Log.i("RECOBackgroundRangingService", "onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("RECOBackgroundRangingService", "onStartCommand");
        /**
         * Create an instance of RECOBeaconManager (to set scanning target and ranging timeout in the background.)
         * If you want to scan only RECO, and do not set ranging timeout in the backgournd, create an instance:
         * 		mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), true, false);
         * WARNING: False enableRangingTimeout will affect the battery consumption.
         *
         * RECOBeaconManager 인스턴스틀 생성합니다. (스캔 대상 및 백그라운드 ranging timeout 설정)
         * RECO만을 스캔하고, 백그라운드 ranging timeout을 설정하고 싶지 않으시다면, 다음과 같이 생성하시기 바랍니다.
         * 		mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), true, false);
         * 주의: enableRangingTimeout을 false로 설정 시, 배터리 소모량이 증가합니다.
         */
        mRecoManager = RECOBeaconManager.getInstance(getApplicationContext(), MainActivity.SCAN_RECO_ONLY, MainActivity.ENABLE_BACKGROUND_RANGING_TIMEOUT);
        this.bindRECOService();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("RECOBackgroundRangingService", "onDestroy()");
        this.tearDown();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("RECOBackgroundRangingService", "onTaskRemoved()");
        super.onTaskRemoved(rootIntent);
    }

    private void bindRECOService() {
        Log.i("RECOBackgroundRangingService", "bindRECOService()");

        mRegions = new ArrayList<RECOBeaconRegion>();
        this.generateBeaconRegion();

        mRecoManager.setMonitoringListener(this);
        mRecoManager.setRangingListener(this);
        mRecoManager.bind(this);
    }

    private void generateBeaconRegion() {
        Log.i("RECOBackgroundRangingService", "generateBeaconRegion()");

        RECOBeaconRegion recoRegion;

        recoRegion = new RECOBeaconRegion(MainActivity.RECO_UUID, "RECO Sample Region");
        recoRegion.setRegionExpirationTimeMillis(this.mRegionExpirationTime);
        mRegions.add(recoRegion);
    }

    private void startMonitoring() {
        Log.i("RECOBackgroundRangingService", "startMonitoring()");

        mRecoManager.setScanPeriod(this.mScanDuration);
        mRecoManager.setSleepPeriod(this.mSleepDuration);

        for(RECOBeaconRegion region : mRegions) {
            try {
                mRecoManager.startMonitoringForRegion(region);
            } catch (RemoteException e) {
                Log.e("RECOBackgroundRangingService", "RemoteException has occured while executing RECOManager.startMonitoringForRegion()");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.e("RECOBackgroundRangingService", "NullPointerException has occured while executing RECOManager.startMonitoringForRegion()");
                e.printStackTrace();
            }
        }
    }

    private void stopMonitoring() {
        Log.i("RECOBackgroundRangingService", "stopMonitoring()");

        for(RECOBeaconRegion region : mRegions) {
            try {
                mRecoManager.stopMonitoringForRegion(region);
            } catch (RemoteException e) {
                Log.e("RECOBackgroundRangingService", "RemoteException has occured while executing RECOManager.stopMonitoringForRegion()");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.e("RECOBackgroundRangingService", "NullPointerException has occured while executing RECOManager.stopMonitoringForRegion()");
                e.printStackTrace();
            }
        }
    }

    private void startRangingWithRegion(RECOBeaconRegion region) {
        Log.i("RECOBackgroundRangingService", "startRangingWithRegion()");

        /**
         * There is a known android bug that some android devices scan BLE devices only once. (link: http://code.google.com/p/android/issues/detail?id=65863)
         * To resolve the bug in our SDK, you can use setDiscontinuousScan() method of the RECOBeaconManager.
         * This method is to set whether the device scans BLE devices continuously or discontinuously.
         * The default is set as FALSE. Please set TRUE only for specific devices.
         *
         * mRecoManager.setDiscontinuousScan(true);
         */

        try {
            mRecoManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.e("RECOBackgroundRangingService", "RemoteException has occured while executing RECOManager.startRangingBeaconsInRegion()");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e("RECOBackgroundRangingService", "NullPointerException has occured while executing RECOManager.startRangingBeaconsInRegion()");
            e.printStackTrace();
        }
    }

    private void stopRangingWithRegion(RECOBeaconRegion region) {
        Log.i("RECOBackgroundRangingService", "stopRangingWithRegion()");

        try {
            mRecoManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.e("RECOBackgroundRangingService", "RemoteException has occured while executing RECOManager.stopRangingBeaconsInRegion()");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e("RECOBackgroundRangingService", "NullPointerException has occured while executing RECOManager.stopRangingBeaconsInRegion()");
            e.printStackTrace();
        }
    }

    private void tearDown() {
        Log.i("RECOBackgroundRangingService", "tearDown()");
        this.stopMonitoring();

        try {
            mRecoManager.unbind();
        } catch (RemoteException e) {
            Log.e("RECOBackgroundRangingService", "RemoteException has occured while executing unbind()");
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnect() {
        Log.i("RECOBackgroundRangingService", "onServiceConnect()");
        this.startMonitoring();
        //Write the code when RECOBeaconManager is bound to RECOBeaconService
    }

    @Override
    public void didDetermineStateForRegion(RECOBeaconRegionState state, RECOBeaconRegion region) {
        Log.i("RECOBackgroundRangingService", "didDetermineStateForRegion()");
        //Write the code when the state of the monitored region is changed
    }

    @Override
    public void didEnterRegion(RECOBeaconRegion region, Collection<RECOBeacon> beacons) {
        //Get the region and found beacon list in the entered region
        Log.i("RECOBackgroundRangingService", "didEnterRegion() - " + region.getUniqueIdentifier());
        this.popupNotification("Inside of " + region.getUniqueIdentifier());
        //Write the code when the device is enter the region

        this.startRangingWithRegion(region); //start ranging to get beacons inside of the region
        //from now, stop ranging after 10 seconds if the device is not exited
    }

    @Override
    public void didExitRegion(RECOBeaconRegion region) {
        Log.i("RECOBackgroundRangingService", "didExitRegion() - " + region.getUniqueIdentifier());
        this.popupNotification("Outside of " + region.getUniqueIdentifier());
        //Write the code when the device is exit the region

        this.stopRangingWithRegion(region); //stop ranging because the device is outside of the region from now
    }

    @Override
    public void didStartMonitoringForRegion(RECOBeaconRegion region) {
        Log.i("RECOBackgroundRangingService", "didStartMonitoringForRegion() - " + region.getUniqueIdentifier());
        //Write the code when starting monitoring the region is started successfully
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> beacons, RECOBeaconRegion region) {
        Log.i("RECOBackgroundRangingService", "didRangeBeaconsInRegion() - " + region.getUniqueIdentifier() + " with " + beacons.size() + " beacons");
        //Write the code when the beacons inside of the region is received
    }

    private void popupNotification(String msg) {
        Log.i("RECOBackgroundRangingService", "popupNotification()");
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.KOREA).format(new Date());
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.test)
                .setContentTitle(msg + " " + currentTime)
                .setContentText(msg);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        builder.setStyle(inboxStyle);
        nm.notify(mNotificationID, builder.build());
        mNotificationID = (mNotificationID - 1) % 1000 + 9000;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //This method is not used
        return null;
    }

    @Override
    public void onServiceFail(RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed.
        //See the RECOErrorCode in the documents.
        return;
    }

    @Override
    public void monitoringDidFailForRegion(RECOBeaconRegion region, RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed to monitor the region.
        //See the RECOErrorCode in the documents.
        return;
    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion region, RECOErrorCode errorCode) {
        //Write the code when the RECOBeaconService is failed to range beacons in the region.
        //See the RECOErrorCode in the documents.
        return;
    }
}