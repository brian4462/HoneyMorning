package com.jica.honeymorning.ScreenLock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class ScreenService extends Service {
    private ScreenReceiver mReceiver;
    private PackageReceiver pReceiver;

    //최상위 뷰 관련
    WindowManager wm;
    View mView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG","ScreenService::onCreate()..");

        mReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        registerRestartAlarm(true);

        pReceiver = new PackageReceiver(this);
        IntentFilter pFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        pFilter.addDataScheme("package");
        registerReceiver(pReceiver, pFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);

        if(intent != null){
            if(intent.getAction()==null){
                if(mReceiver==null){
                    mReceiver = new ScreenReceiver();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
                    registerReceiver(mReceiver, filter);
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
        registerRestartAlarm(false);

        if(pReceiver != null)
            unregisterReceiver(pReceiver);
    }

    // TaskKill이 발생했을경우 재실행 메서드
    public void registerRestartAlarm(boolean isOn){
        Intent intent = new Intent(ScreenService.this, RestartReceiver.class);
        intent.setAction(RestartReceiver.ACTION_RESTART_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        if(isOn){
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 1000*60, sender);
        }else{
            am.cancel(sender);
        }
    }
}
