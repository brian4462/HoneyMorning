package com.jica.honeymorning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.jica.honeymorning.ScreenLock.SettingLockScreenActivity;
import com.jica.honeymorning.calendar.CalendarViewActivity;
import com.jica.honeymorning.todo.TodoViewActivity;
import com.jica.honeymorning.tutorial.TutorialActivity;


public class MainActivity extends AppCompatActivity {
    Button btnParent, btnTodo, btnCalendar, btnLockScreen, btnSetting;
    boolean isShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*** 튜토리얼 뷰(한번만 실행)*/
        SharedPreferences preferences = getSharedPreferences("myPref",0);
        int tutorialviewshow = preferences.getInt("Tutorial",0);
        if(tutorialviewshow != 1){
            Intent intent = new Intent(this,TutorialActivity.class);
            startActivity(intent);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnParent = findViewById(R.id.btnParent);
        btnTodo = findViewById(R.id.btnTodo);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnLockScreen = findViewById(R.id.btnLockScreen);
        btnSetting = findViewById(R.id.btnSetting);

        ButtonHandler buttonHandler = new ButtonHandler();
        btnTodo.setOnClickListener(buttonHandler);
        btnCalendar.setOnClickListener(buttonHandler);
        btnLockScreen.setOnClickListener(buttonHandler);
        btnSetting.setOnClickListener(buttonHandler);
    }

    public void onButtonShow(View view) {
        Animation ani1,ani2;
        isShow = (isShow) ? false : true;
        if(isShow){
            ani1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.customani_button_visible);
            ani1.setDuration(200);
            ani1.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.decelerate_interpolator));

            ani2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.customani_button_visible);
            ani2.setDuration(200);
            ani2.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.accelerate_interpolator));

            btnTodo.startAnimation(ani1);
            btnTodo.setVisibility(Button.VISIBLE);
            btnCalendar.startAnimation(ani2);
            btnCalendar.setVisibility(Button.VISIBLE);
            btnLockScreen.startAnimation(ani1);
            btnLockScreen.setVisibility(Button.VISIBLE);
            btnSetting.startAnimation(ani2);
            btnSetting.setVisibility(Button.VISIBLE);

            btnParent.setSelected(true);
        } else {
            ani1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.customani_button_gone);
            ani1.setDuration(200);
            ani1.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.decelerate_interpolator));

            ani2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.customani_button_gone);
            ani2.setDuration(200);
            ani2.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.accelerate_interpolator));

            btnTodo.startAnimation(ani1);
            btnTodo.setVisibility(Button.GONE);
            btnCalendar.startAnimation(ani2);
            btnCalendar.setVisibility(Button.GONE);
            btnLockScreen.startAnimation(ani1);
            btnLockScreen.setVisibility(Button.GONE);
            btnSetting.startAnimation(ani2);
            btnSetting.setVisibility(Button.GONE);
            btnParent.setSelected(false);
        }
    }

    class ButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Button curButton = (Button)view;
            Intent intent = null;
            if(curButton == btnTodo){
                intent= new Intent(getApplicationContext(), TodoViewActivity.class);
            }else if(curButton == btnCalendar){
                intent = new Intent(getApplicationContext(), CalendarViewActivity.class);
            }else if(curButton == btnLockScreen){
                intent = new Intent(getApplicationContext(), SettingLockScreenActivity.class);
            }else if(curButton == btnSetting){
                intent = new Intent(getApplicationContext(), SettingActivity.class);
            }
            if(intent != null){
                startActivity(intent);
            }
        }
    }
}
