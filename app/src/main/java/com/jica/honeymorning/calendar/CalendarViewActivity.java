package com.jica.honeymorning.calendar;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.honeymorning.R;
import com.jica.honeymorning.database.MySQLiteOpenHelper;
import com.jica.honeymorning.database.entity.RecommendedList;
import com.jica.honeymorning.database.entity.TodoValue;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class CalendarViewActivity extends AppCompatActivity {

    private TextView tvMonth, tvDay, tvTodo;
    private LinearLayout linearLayout_TodoCheckImage, layout_CalList;
    private MaterialCalendarView calendarView;
    private RecyclerView calRecyclerView;
    LinearLayoutManager manager;
    CalRecyclerViewAdapter adapter;
    private Calendar calendar;
    MySQLiteOpenHelper dbhelper;
    List<TodoValue> items;

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        settings = getSharedPreferences("myPref",0);
        //db
        dbhelper = new MySQLiteOpenHelper(getApplicationContext());

        tvMonth = (TextView) findViewById(R.id.tvMonth);
        tvDay = (TextView) findViewById(R.id.tvDay);
        tvTodo = (TextView) findViewById(R.id.tvTodo);
        linearLayout_TodoCheckImage = (LinearLayout) findViewById(R.id.linearLayout_TodoCheckImage);
        layout_CalList = (LinearLayout) findViewById(R.id.layout_CalList);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calRecyclerView = (RecyclerView) findViewById(R.id.calRecyclerView);

        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        //첫 화면에서 오늘 날짜 텍스트뷰에 보여주기
        int curYear = CalendarDay.today().getYear();
        int curMonth = CalendarDay.today().getMonth();
        int curDay = CalendarDay.today().getDay();
        setDateinTextView(curMonth,curDay);
        makeOnedayValueAndShow(curYear,curMonth,curDay);
        //첫 화면에서 오늘 날짜가 선택되도록 설정
        calendarView.setDateSelected(CalendarDay.today(),true);

        //날짜 선택
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                //선택한 날짜를 텍스트뷰에 보여주기
                int selectedDay = date.getDay();
                int selectedMonth = date.getMonth();
                setDateinTextView(selectedMonth,selectedDay);
                makeOnedayValueAndShow(date.getYear(),selectedMonth,selectedDay);

            }
        });

        //db에서 todo가 입력된 날짜 가져오기
        List<TodoValue> todoList = new ArrayList<TodoValue>();
        todoList = dbhelper.get_Todo_All();
        List<String> todo_date = new ArrayList<String>();
        for(int i = 0;i<todoList.size();i++){
            todo_date.add(todoList.get(i).getCreated_at().substring(0,10));
        }
        //날짜 중복 제거
        List<String> todo_date_remove_duplicate = new ArrayList<String>();
        for(int i = 0;i<todo_date.size();i++){
            if(!todo_date_remove_duplicate.contains(todo_date.get(i))){
                todo_date_remove_duplicate.add(todo_date.get(i));
            }
        }
        todo_date.clear();
        //완료한 항목/전체 항목 = 1 인 날짜만 스탬프 찍기
        for(int i = 0;i<todo_date_remove_duplicate.size();i++){
            int cnt = 0;
            List<TodoValue> finishList = dbhelper.get_Todo_ByDate(todo_date_remove_duplicate.get(i));
            for(int j = 0; j<finishList.size();j++){
                if(finishList.get(j).getStatus()==0){
                    cnt++;
                }
            }
            if(cnt/finishList.size()==1){
                todo_date.add(todo_date_remove_duplicate.get(i));
            }
        }
        new com.jica.honeymorning.calendar.CalendarViewActivity.Simulator(todo_date).executeOnExecutor(Executors.newSingleThreadExecutor());

    }

    //텍스트뷰에 날짜를 보여주는 메서드
    private void setDateinTextView(int month, int day) {
        String strMonth="";
        switch (month){
            case 0: strMonth="JAN"; break;
            case 1: strMonth="FEB"; break;
            case 2: strMonth="MAR"; break;
            case 3: strMonth="APR"; break;
            case 4: strMonth="MAY"; break;
            case 5: strMonth="JUN"; break;
            case 6: strMonth="JUL"; break;
            case 7: strMonth="AUG"; break;
            case 8: strMonth="SEP"; break;
            case 9: strMonth="OCT"; break;
            case 10: strMonth="NOV"; break;
            case 11: strMonth="DEC"; break;
        }
        tvMonth.setText(strMonth);
        tvDay.setText(day+"");
    }

    //해당 날짜에 todo리스트를 만들고 진행상황 보여주기
    private void makeOnedayValueAndShow(int year, int month, int day){
        //선택한 날짜 TodoList 만들기
        String strCurMonth = (month<10)?"0"+(month+1):""+(month+1);
        String strCurDay = (day<10)?"0"+day:""+day;
        String strSelectedDate = year+"-"+strCurMonth+"-"+strCurDay;

        List<TodoValue> selectedDay_TodoList = new ArrayList<TodoValue>();
        selectedDay_TodoList = dbhelper.get_Todo_ByDate(strSelectedDate);
        //해당 날짜에 아무 일정이 없으면 랜덤으로 2개 만든다
        if(selectedDay_TodoList == null){
            List<RecommendedList> randomList = new ArrayList<RecommendedList>();
            randomList = dbhelper.get_Recommend_Random(settings.getInt("RandomCount",2));
            //db에 추가
            for(RecommendedList value : randomList){
                dbhelper.create_Todo_WithDate(value.getTodo(),strSelectedDate);
            }
            //생성된 데이터 가져오기
            selectedDay_TodoList = dbhelper.get_Todo_ByDate(strSelectedDate);
        }

        // tvTodo에 선택된 날짜의 (완료한 항목/해당일의 전체 항목) 보여주기
        int cntFinish = 0;
        if(selectedDay_TodoList != null){
            for (TodoValue value:selectedDay_TodoList){
                if(value.getStatus()==0){
                    cntFinish++;
                }
            }
        }
        int cntTotal = 0;
        if(selectedDay_TodoList != null){
            cntTotal = selectedDay_TodoList.size();
        }
        tvTodo.setText(cntFinish+"/"+cntTotal);

        //이미지뷰 완료한 항목과 완료하지 못한 항목 표시
        if(cntTotal>=5){
            linearLayout_TodoCheckImage.removeAllViews();
        }else {
            linearLayout_TodoCheckImage.setVisibility(View.VISIBLE);
            ImageView[] ivs = new ImageView[cntTotal];
            if(cntFinish == 0){
                linearLayout_TodoCheckImage.removeAllViews();
                for(int i=0; i<cntTotal;i++){
                    ivs[i] = new ImageView(getApplicationContext());
                    ivs[i].setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ivs[i].setBackgroundResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                    linearLayout_TodoCheckImage.addView(ivs[i]);
                }
            } else {
                linearLayout_TodoCheckImage.removeAllViews();
                for(int i = 0;i<cntFinish;i++){
                    ivs[i] = new ImageView(getApplicationContext());
                    ivs[i].setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ivs[i].setBackgroundResource(R.drawable.ic_check_circle_black_24dp);
                    linearLayout_TodoCheckImage.addView(ivs[i]);
                }
                for(int i=cntFinish; i<cntTotal;i++){
                    ivs[i] = new ImageView(getApplicationContext());
                    ivs[i].setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ivs[i].setBackgroundResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                    linearLayout_TodoCheckImage.addView(ivs[i]);
                }
            }
        }

        //선택된 날짜 리스트뷰 보이기
        Animation aniUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_up);
        if(layout_CalList.getVisibility()==View.GONE){
            layout_CalList.startAnimation(aniUp);
            layout_CalList.setVisibility(View.VISIBLE);
        }
        //리사이클러뷰 셋
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        items = new ArrayList<TodoValue>();
        items = dbhelper.get_Todo_ByDate(strSelectedDate);
        if(items == null){
            items = new ArrayList<TodoValue>();
        }

        Log.d("TAG1",items.get(0).toString());
        calRecyclerView.setLayoutManager(manager);
        adapter = new CalRecyclerViewAdapter(items);
        adapter.setItemClick(new CalRecyclerViewAdapter.ItemClick() {
            @Override
            public void onClick(View v, int position) {
                if(items.get(position).getStatus()==0){
                    items.get(position).setStatus(1);
                    dbhelper.update_Todo_Status(items.get(position).getId(),1);
                }else {
                    items.get(position).setStatus(0);
                    dbhelper.update_Todo_Status(items.get(position).getId(),0);
                }
                adapter.notifyDataSetChanged();
            }
        });
        calRecyclerView.setAdapter(adapter);

    }

    public void onCloseView(View view) {
        Animation aniDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_down);
        layout_CalList.startAnimation(aniDown);
        layout_CalList.setVisibility(View.GONE);
    }

    private class Simulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        List<String> Time_Result;

        Simulator(List<String> Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 표시해주는곳*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.size() ; i ++){
                String[] time = Time_Result.get(i).split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                calendar.set(year,month-1,dayy);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }
            return dates;
        }

        @Override
        protected void onPostExecute(List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            if (isFinishing()) {
                return;
            }
            calendarView.addDecorator(new EventDecorator(calendarDays, com.jica.honeymorning.calendar.CalendarViewActivity.this));
        }
    }
}

