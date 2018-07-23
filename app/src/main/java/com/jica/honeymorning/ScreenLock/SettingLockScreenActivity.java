package com.jica.honeymorning.ScreenLock;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;

import com.jica.honeymorning.R;

public class SettingLockScreenActivity extends AppCompatActivity {
    Switch switchLockscreen, switchListView, switchBackground;
    SharedPreferences settings;
    SwitchHandler switchHandler;
    boolean listViewState, lockscreenState, backgroundState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_lock_screen);

        //ActionBar 뒤로가기 생성
        /*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("잠금화면 설정");
        */

        switchHandler = new SwitchHandler();
        switchLockscreen = findViewById(R.id.switch_Lockscreen);
        switchListView = findViewById(R.id.switch_listView);
        switchBackground = findViewById(R.id.switch_background);

        settings = getSharedPreferences("myPref",0);

        lockscreenState = settings.getBoolean("switchLock",false);
        switchLockscreen.setChecked(lockscreenState);
        listViewState = settings.getBoolean("switchList",false);
        switchListView.setChecked(listViewState);
        backgroundState = settings.getBoolean("switchBackground",false);
        switchBackground.setChecked(backgroundState);

        if(lockscreenState){
            switchBackground.setEnabled(true);
            switchListView.setEnabled(true);
        }


        switchLockscreen.setOnCheckedChangeListener(switchHandler);
        switchListView.setOnCheckedChangeListener(switchHandler);
        switchBackground.setOnCheckedChangeListener(switchHandler);
    }

    public class SwitchHandler implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            switch (compoundButton.getId()){
                case R.id.switch_Lockscreen:{
                    if(isChecked){
                        Intent intent = new Intent(getApplicationContext(), ScreenService.class);
                        startService(intent);
                        Log.d("TAG","SettingLockScreenActivity::Switch 클릭");
                        switchListView.setEnabled(true);
                        switchBackground.setEnabled(true);
                        switchListView.setChecked(true);
                        switchBackground.setChecked(true);
                    }else{
                        Intent intent = new Intent(getApplicationContext(), ScreenService.class);
                        stopService(intent);
                        switchListView.setEnabled(false);
                        switchBackground.setEnabled(false);
                    }
                    SharedPreferences settings = getSharedPreferences("myPref",0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("switchLock",isChecked);
                    editor.commit();
                }
                case R.id.switch_listView:{
                    if(isChecked){
                    }else{
                    }
                    SharedPreferences settings = getSharedPreferences("myPref",0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("switchList",isChecked);
                    editor.commit();
                }
                case R.id.switch_background:{
                    if(isChecked){
                    }else{
                    }
                    SharedPreferences settings = getSharedPreferences("myPref",0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("switchBackground",isChecked);
                    editor.commit();
                }
            }
        }
    }
}

