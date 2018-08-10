package com.jica.honeymorning.todo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;


import com.jica.honeymorning.R;

import com.jica.honeymorning.database.MySQLiteOpenHelper;
import com.jica.honeymorning.database.entity.TodoValue;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodoViewActivity extends AppCompatActivity {

    RecyclerView TodoRecyclerView;
    TodoRecyclerViewAdapter adapter;
    MySQLiteOpenHelper dbhelper;
    List<TodoValue> items;
    SharedPreferences settings;
    LinearLayoutManager manager;

    FloatingActionButton btnAdd;
    EditText editText;

    int clickcnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        settings = getSharedPreferences("myPref", 0);
        //db
        dbhelper = new MySQLiteOpenHelper(getApplicationContext());
        TodoRecyclerView = (RecyclerView) findViewById(R.id.TodoRecyclerView);
        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (FloatingActionButton) findViewById(R.id.fab);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdd(view);
            }
        });

        //ActionBar 뒤로가기 생성
        //ActionBar actionBar = getSupportActonBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeButtonEnabled(true);

        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        items = new ArrayList<TodoValue>();
        Calendar cal = Calendar.getInstance();
        CalendarDay day = CalendarDay.from(cal);
        String year = day.getYear()+"";
        int month = day.getMonth()+1;
        String strMonth;

        if(month<10){
            strMonth = "0"+month;
        }else {
            strMonth = ""+month;
        }

        int date = day.getDay();
        String strDate;
        if(date<10) {
            strDate = "0" + date;
        }else
        {
            strDate = ""+date;
        }

        String today = year + "-" + strMonth + "-" + strDate;
        Log.d("TAG",today);

        items = dbhelper.get_Todo_ByDate(today);
        if (items == null) {
            items = new ArrayList<TodoValue>();
        }

        //Log.d("TAG1",items.get(0).toString());
        TodoRecyclerView.setLayoutManager(manager);
        adapter = new TodoRecyclerViewAdapter(items);
        adapter.setItemClick(new TodoRecyclerViewAdapter.ItemClick() {
            @Override
            public void onClick(View v, int position) {
                if(items.get(position).getStatus()==0) {
                    items.get(position).setStatus(1);
                    dbhelper.update_Todo_Status(items.get(position).getId(), 1);
                }else{
                    items.get(position).setStatus(0);
                    dbhelper.update_Todo_Status(items.get(position).getId(),0);
                }
                adapter.notifyDataSetChanged();
            }
        });
        TodoRecyclerView.setAdapter(adapter);

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:{
                        String msg = editText.getText().toString();
                        if(msg.length() != 0){
                            int year = CalendarDay.today().getYear();
                            int month = CalendarDay.today().getMonth();
                            int day = CalendarDay.today().getDay();
                            String strCurMonth = (month<10)?"0"+(month+1):""+(month+1);
                            String strCurDay = (day<10)?"0"+day:""+day;
                            String strSelectedDate = year+"-"+strCurMonth+"-"+strCurDay;

                            dbhelper.create_Todo(msg);
                            adapter = (TodoRecyclerViewAdapter) TodoRecyclerView.getAdapter();

                            TodoValue item = dbhelper.get_Todo_Recent();

                            items.add(item);
                            adapter.notifyDataSetChanged();

                            /*btnAdd.setRotation(45);
                            Animation ani;
                            ani = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation_minus45);
                            ani.setFillAfter(true);
                            ani.setFillEnabled(true)
                            btnAdd.startAnimation(ani);
                            btnAdd.setColorFilter(getColor(R.color.colorTextTitle));*/
                            hideSoftKeyboard();
                            Animation animation = AnimationUtils.loadAnimation(TodoViewActivity.this,R.anim.alpha_hide);
                            editText.startAnimation(animation);
                            editText.setVisibility(View.INVISIBLE);
                            editText.clearFocus();
                            editText.setCursorVisible(false);
                            clickcnt++;
                        }else {
                            /*
                            btnAdd.setRotation(45);
                            Animation ani;
                            ani = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation_minus45);
                            ani.setFillAfter(true);
                            ani.setFillEnabled(true);
                            btnAdd.startAnimation(ani);
                            btnAdd.setColorFilter(getColor(R.color.colorTextTitle));
                            */
                            hideSoftKeyboard();
                            Animation animation = AnimationUtils.loadAnimation(TodoViewActivity.this,R.anim.alpha_hide);
                            editText.startAnimation(animation);
                            editText.setVisibility(View.INVISIBLE);
                            editText.clearFocus();
                            editText.setCursorVisible(false);
                            clickcnt++;
                        }
                        break;
                    }
                }
                return false;
            }
        });
    }



    private String strTodayDate(){
        int year = CalendarDay.today().getYear();
        int month = CalendarDay.today().getMonth();
        int day = CalendarDay.today().getDay();
        String strCurMonth = (month<10)?"0"+(month+1):""+(month+1);
        String strCurDay = (day<10)?"0"+day:""+day;
        String strTodayDate = year+"-"+strCurMonth+"-"+strCurDay;
        return strTodayDate;
    }


    public void onAdd(View view) {
        clickcnt++;
        if(clickcnt%2==1){
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.alpha_show);
            editText.startAnimation(animation);
            editText.setVisibility(View.VISIBLE);
            editText.setText("");
            editText.setCursorVisible(true);
            editText.requestFocus();

            //키보드 보이기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else {
            /*btnAdd.setRotation(45);
            Animation ani;
            ani = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation_minus45);
            ani.setFillAfter(true);
            ani.setFillEnabled(true);
            btnAdd.startAnimation(ani);
            btnAdd.setColorFilter(getColor(R.color.colorTextTitle));*/
            hideSoftKeyboard();

            Animation animation = AnimationUtils.loadAnimation(this,R.anim.alpha_hide);
            editText.startAnimation(animation);
            editText.setVisibility(View.INVISIBLE);
            editText.clearFocus();
            editText.setCursorVisible(false);
        }
    }

    private void makeData(){
        items = new ArrayList<TodoValue>();
        int year = CalendarDay.today().getYear();
        int month = CalendarDay.today().getMonth();
        int day = CalendarDay.today().getDay();
        String strCurMonth = (month<10)?"0"+(month+1):""+(month+1);
        String strCurDay = (day<10)?"0"+day:""+day;
        String strSelectedDate = year+"-"+strCurMonth+"-"+strCurDay;
        items = dbhelper.get_Todo_ByDateAndStatus(strSelectedDate,1);
        if(items == null){
            items = new ArrayList<TodoValue>();
        }
    }
    private void hideSoftKeyboard(){
        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}



