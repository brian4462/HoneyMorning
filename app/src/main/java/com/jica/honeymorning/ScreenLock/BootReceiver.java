package com.jica.honeymorning.ScreenLock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// 부팅시 자동으로 실행해주는 리시버
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, ScreenService.class);
            context.startService(i);
        }
    }
}
