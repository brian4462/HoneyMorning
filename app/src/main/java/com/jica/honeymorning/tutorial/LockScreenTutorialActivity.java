package com.jica.honeymorning.tutorial;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jica.honeymorning.R;

public class LockScreenTutorialActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen_tutorial);
    }

    public void onClose(View view) {
        int intoFirst = 1;
        SharedPreferences myPref = getSharedPreferences("myPref",0);
        SharedPreferences.Editor editor = myPref.edit();
        editor.putInt("TutorialLock",intoFirst);
        editor.commit();
        finish();
    }
}
