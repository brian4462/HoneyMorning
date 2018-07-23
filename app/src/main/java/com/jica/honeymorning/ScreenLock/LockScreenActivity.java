package com.jica.honeymorning.ScreenLock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.jica.honeymorning.R;
import com.jica.honeymorning.ScreenLock.recyclerview.RecyclerViewAdapter;
import com.jica.honeymorning.database.MySQLiteOpenHelper;
import com.jica.honeymorning.database.entity.RecommendedList;
import com.jica.honeymorning.database.entity.TodoValue;
import com.jica.honeymorning.tutorial.LockScreenTutorialActivity;
import com.jica.honeymorning.tutorial.TutorialActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.ViewDragHelper;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class LockScreenActivity extends SwipeBackActivity{
    MySQLiteOpenHelper dbhelper;
    SharedPreferences settings;
    //시계 관련 변수
    TextView tvDate, tvHour, tvAm_pm;
    UsedAsync asyncTask;

    //스와이프 관련 변수
    SwipeBackLayout swipeBackLayout;
    ConstraintLayout layoutLockScreen;
    ImageView  ivBackground;
    ViewDragHelper dragHelper;

    //리사이클러뷰 관련 변수
    RecyclerView recyclerView;
    LinearLayout menuLinearLayout;
    LinearLayout layoutRecyclerView;
    LinearLayoutManager manager;
    RecyclerViewAdapter adapter;
    List<TodoValue> items;
    ImageButton btnAdd, btnSync;
    EditText editText;

    int clickcnt = 0;


    @Override
    protected void onPause() {
        super.onPause();

        asyncTask.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (asyncTask.isCancelled()) {
            asyncTask = new UsedAsync();
            asyncTask.execute();
        }
        hideNavigationBar();

        settings = getSharedPreferences("myPref",0);
        if(settings.getBoolean("switchList",false)){
            layoutRecyclerView.setVisibility(View.VISIBLE);
            menuLinearLayout.setVisibility(View.VISIBLE);
        }else {
            layoutRecyclerView.setVisibility(View.GONE);
            menuLinearLayout.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            editText.setCursorVisible(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);


        settings = getSharedPreferences("myPref",0);
        dbhelper = new MySQLiteOpenHelper(getApplicationContext());

        /*** 튜토리얼 뷰(한번만 실행)*/

        int tutorialviewshow = settings.getInt("TutorialLock",0);
        if(tutorialviewshow != 1){
            Intent intent = new Intent(this, LockScreenTutorialActivity.class);
            startActivity(intent);
        }

        // 풀스크린
        hideNavigationBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 배경 설정
        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        if(settings.getBoolean("switchBackground",false)){
            DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(ivBackground);
            Glide.with(this).load(R.raw.ggul4).into(imageViewTarget);
        } else {
            Glide.with(this).load(R.drawable.lockscreen_background_pic).into(ivBackground);
        }


        //화면 스와이프 설정
        layoutLockScreen = (ConstraintLayout) findViewById(R.id.layoutLockScreen);
        swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);
        swipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
            }

            @Override
            public void onScrollOverThreshold() {
            }
        });

        // 화면이 꺼졌을때 액티비티 실행
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /***시계 관련 코드*/
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvHour = (TextView) findViewById(R.id.tvHour);
        tvAm_pm = (TextView) findViewById(R.id.tvAm_pm);

        asyncTask = new UsedAsync();
        asyncTask.execute();

        /***리사이클러 뷰 관련 코드*/

        layoutRecyclerView = (LinearLayout) findViewById(R.id.layoutRecyclerView);
        menuLinearLayout = (LinearLayout) findViewById(R.id.menulinearLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnSync = (ImageButton) findViewById(R.id.btnSync);
        editText = (EditText) findViewById(R.id.editText);

        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        makeData();

        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new RecyclerViewAdapter(items);
        recyclerView.setAdapter(adapter);
        setItemTouchHelper();

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
                            adapter = (RecyclerViewAdapter) recyclerView.getAdapter();
                            TodoValue item = dbhelper.get_Todo_Recent();

                            items.add(item);
                            adapter.notifyDataSetChanged();

                            btnAdd.setRotation(45);
                            Animation ani;
                            ani = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation_minus45);
                            ani.setFillAfter(true);
                            ani.setFillEnabled(true);
                            btnAdd.startAnimation(ani);
                            btnAdd.setColorFilter(getColor(R.color.colorTextTitle));
                            hideSoftKeyboard();
                            editText.clearFocus();
                            editText.setCursorVisible(false);
                            editText.setVisibility(View.GONE);
                            clickcnt++;
                        }else {
                            btnAdd.setRotation(45);
                            Animation ani;
                            ani = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation_minus45);
                            ani.setFillAfter(true);
                            ani.setFillEnabled(true);
                            btnAdd.startAnimation(ani);
                            btnAdd.setColorFilter(getColor(R.color.colorTextTitle));
                            hideSoftKeyboard();
                            editText.clearFocus();
                            editText.setCursorVisible(false);
                            editText.setVisibility(View.GONE);
                            clickcnt++;
                        }
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void hideNavigationBar() {
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }
    public void onCloseLockscreen(View view) {
        if(view.getId() == R.id.ibLock)
            finish();
    }
    /*** 시계 관련 메서드(AsyncTask) */
    class UsedAsync extends AsyncTask<Integer, Integer, Integer> {
        Calendar cal;
        String date, hour, am_pm;


        private void makeForm() {
            String day="";
            cal = new GregorianCalendar();
            int time_Hour;

            switch (cal.get(Calendar.DAY_OF_WEEK)){
                case 1: day = "일"; break;
                case 2: day = "월"; break;
                case 3: day = "화"; break;
                case 4: day = "수"; break;
                case 5: day = "목"; break;
                case 6: day = "금"; break;
                case 7: day = "토"; break;
            }
            am_pm = (cal.get(Calendar.AM_PM) == 0) ? "AM" : "PM";
            time_Hour = cal.get(Calendar.HOUR);
            if(time_Hour == 0){time_Hour=12;}

            date = String.format("%d월 %d일 %s요일",cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH),day);
            hour = String.format("%02d:%02d",time_Hour,cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        }
        @Override
        protected Integer doInBackground(Integer... integers) {
            while(isCancelled() == false){
                makeForm();

                publishProgress();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            makeForm();

            tvDate.setText(date);
            tvHour.setText(hour);
            tvAm_pm.setText(am_pm);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            tvDate.setText(date);
            tvHour.setText(hour);
            tvAm_pm.setText(am_pm);
            super.onProgressUpdate(values);
        }
    }

    /***리사이클러뷰 관련 메서드*/
    //새로 고침
    public void onRefresh(View view) {
        if(items.size()==0){
            List<RecommendedList> randomList = new ArrayList<RecommendedList>();
            randomList = dbhelper.get_Recommend_Random(settings.getInt("RandomCount",2));
            //db에 추가
            String todayDate = strTodayDate();
            for(RecommendedList value: randomList){
                dbhelper.create_Todo_WithDate(value.getTodo(),todayDate);
                TodoValue item = dbhelper.get_Todo_Recent();
                items.add(item);
            }
        }
        adapter.notifyDataSetChanged();

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
            btnAdd.setRotation(0);
            Animation ani;
            ani = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation45);
            ani.setFillAfter(true);
            ani.setFillEnabled(true);
            btnAdd.startAnimation(ani);
            btnAdd.setColorFilter(Color.RED);
            editText.setVisibility(View.VISIBLE);
            editText.setText("");
            editText.setCursorVisible(true);
            editText.requestFocus();

            //키보드 보이기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else {
            btnAdd.setRotation(45);
            Animation ani;
            ani = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation_minus45);
            ani.setFillAfter(true);
            ani.setFillEnabled(true);
            btnAdd.startAnimation(ani);
            btnAdd.setColorFilter(getColor(R.color.colorTextTitle));
            hideSoftKeyboard();
            editText.clearFocus();
            editText.setCursorVisible(false);
            editText.setVisibility(View.GONE);
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
    private void setItemTouchHelper(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            Drawable mark, background;
            int markMargin;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //스와이프할때 호출
                int swipedPosition = viewHolder.getAdapterPosition();
                adapter = (RecyclerViewAdapter) recyclerView.getAdapter();
                switch (swipeDir){
                    case ItemTouchHelper.LEFT: {//삭제
                        int id = adapter.remove(swipedPosition);
                        dbhelper.delete_Todo(id);
                        break;
                    }
                    case ItemTouchHelper.RIGHT: {//완료후 삭제
                        int id = adapter.remove(swipedPosition);
                        dbhelper.update_Todo_Status(id, 0);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override//스와이프 했을때 백그라운드 변화
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemview = viewHolder.itemView;
                switch (actionState){
                    case ItemTouchHelper.ACTION_STATE_SWIPE:{
                        if(dX<1) {
                            mark = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_12dp);
                            markMargin = (int) getApplicationContext().getResources().getDimension(R.dimen.ic_remove_and_update_margin);

                            int itemHeight = itemview.getBottom() - itemview.getTop();// 아이템 높이
                            int markWidth = mark.getIntrinsicWidth(); //mark의 실제 너비
                            int markHeight = mark.getIntrinsicHeight(); //mark의 실제 높이

                            int markLeft = itemview.getRight() - markMargin - markWidth;
                            int markRight = itemview.getRight() - markMargin;
                            int markTop = itemview.getTop() + (itemHeight - markHeight) / 2;
                            int markBottom = markTop + markHeight;
                            mark.setBounds(markLeft, markTop, markRight, markBottom);
                            mark.setAlpha((int)-dX/2);
                            mark.draw(c);
                        }else{
                            mark = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check_12dp);
                            markMargin = (int) getApplicationContext().getResources().getDimension(R.dimen.ic_remove_and_update_margin);

                            int itemHeight = itemview.getBottom() - itemview.getTop();// 아이템 높이
                            int markWidth = mark.getIntrinsicWidth(); //mark의 실제 너비
                            int markHeight = mark.getIntrinsicHeight(); //mark의 실제 높이

                            int markLeft = markMargin;
                            int markRight = markMargin + markWidth;
                            int markTop = itemview.getTop() + (itemHeight - markHeight) / 2;
                            int markBottom = markTop + markHeight;
                            mark.setBounds(markLeft, markTop, markRight, markBottom);
                            mark.setAlpha((int)dX/2);
                            mark.draw(c);
                        }
                        break;
                    }
                    case ItemTouchHelper.ACTION_STATE_IDLE:{
                        mark = null;
                        mark.draw(c);
                    }
                }

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void hideSoftKeyboard(){
        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}

