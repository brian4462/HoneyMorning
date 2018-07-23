package com.jica.honeymorning;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.honeymorning.database.MySQLiteOpenHelper;
import com.jica.honeymorning.database.entity.RecommendedList;
import com.jica.honeymorning.database.entity.TodoValue;

import java.util.List;

public class SettingActivity extends AppCompatActivity {

    MySQLiteOpenHelper dbhelper;
    SharedPreferences settings;
    TextView tvRandomcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        dbhelper = new MySQLiteOpenHelper(getApplicationContext());
        settings = getSharedPreferences("myPref",0);

        tvRandomcount = findViewById(R.id.tvRandomcount);
        tvRandomcount.setText(settings.getInt("RandomCount",2)+"개");
    }

    public void onSelect(View view) {
        final Dialog selectDialog = new Dialog(this);
        selectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectDialog.setContentView(R.layout.dialog_select_random);

        final Switch switch_recommend_exercise = (Switch) selectDialog.findViewById(R.id.switch_recommend_exercise);
        final Switch switch_recommend_life = (Switch) selectDialog.findViewById(R.id.switch_recommend_life);
        final Switch switch_recommend_knowledge = (Switch) selectDialog.findViewById(R.id.switch_recommend_knowledge);
        final Switch switch_recommend_fun = (Switch) selectDialog.findViewById(R.id.switch_recommend_fun);
        final Switch switch_recommend_student = (Switch) selectDialog.findViewById(R.id.switch_recommend_student);
        final Switch switch_recommend_work = (Switch) selectDialog.findViewById(R.id.switch_recommend_work);
        final Switch switch_recommend_my = (Switch) selectDialog.findViewById(R.id.switch_recommend_my);

        switch_recommend_exercise.setChecked(settings.getBoolean("Recommend_Exercise",true));
        switch_recommend_life.setChecked(settings.getBoolean("Recommend_Life",true));
        switch_recommend_knowledge.setChecked(settings.getBoolean("Recommend_Knowledge",true));
        switch_recommend_fun.setChecked(settings.getBoolean("Recommend_Fun",true));
        switch_recommend_student.setChecked(settings.getBoolean("Recommend_Student",true));
        switch_recommend_work.setChecked(settings.getBoolean("Recommend_Work",true));
        switch_recommend_my.setChecked(settings.getBoolean("Recommend_My",true));

        Button btn_select_random_cancel = (Button) selectDialog.findViewById(R.id.btn_select_random_cancel);
        btn_select_random_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RecommendedList> items = dbhelper.get_Recommend_ByStatus(1);
                int size = (items==null)?0:items.size();
                if(size<settings.getInt("RandomCount",2)){
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("RandomCount",size);
                    editor.commit();
                    tvRandomcount.setText(settings.getInt("RandomCount",2)+"개");
                    Toast.makeText(SettingActivity.this, "선택리스트에 항목이 적어 추천으로 받을 할일 개수가 변경됩니다.", Toast.LENGTH_SHORT).show();
                }
                selectDialog.dismiss();
            }
        });

        switch_recommend_exercise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Recommend_Exercise",b);
                editor.commit();
                if(b){
                    dbhelper.update_Recommend_Status(1,1);
                }else {
                    dbhelper.update_Recommend_Status(1,0);
                }
            }
        });
        switch_recommend_life.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Recommend_Life",b);
                editor.commit();
                if(b){
                    dbhelper.update_Recommend_Status(2,1);
                }else {
                    dbhelper.update_Recommend_Status(2,0);
                }
            }
        });
        switch_recommend_knowledge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Recommend_Knowledge",b);
                editor.commit();
                if(b){
                    dbhelper.update_Recommend_Status(3,1);
                }else {
                    dbhelper.update_Recommend_Status(3,0);
                }
            }
        });
        switch_recommend_fun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Recommend_Fun",b);
                editor.commit();
                if(b){
                    dbhelper.update_Recommend_Status(4,1);
                }else {
                    dbhelper.update_Recommend_Status(4,0);
                }
            }
        });
        switch_recommend_student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Recommend_Student",b);
                editor.commit();
                if(b){
                    dbhelper.update_Recommend_Status(5,1);
                }else {
                    dbhelper.update_Recommend_Status(5,0);
                }
            }
        });
        switch_recommend_work.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Recommend_Work",b);
                editor.commit();
                if(b){
                    dbhelper.update_Recommend_Status(6,1);
                }else {
                    dbhelper.update_Recommend_Status(6,0);
                }
            }
        });
        switch_recommend_my.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Recommend_My",b);
                editor.commit();
                if(b){
                    dbhelper.update_Recommend_Status(7,1);
                }else {
                    dbhelper.update_Recommend_Status(7,0);
                }
            }
        });
        selectDialog.show();
    }
    public void onMyRandom(View view) {
        final Dialog randomDialog = new Dialog(this);
        randomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        randomDialog.setContentView(R.layout.dialog_my_random);

        final EditText editText = (EditText) randomDialog.findViewById(R.id.etRandom);
        Button btnrandomOk = (Button) randomDialog.findViewById(R.id.btn_myrandom_ok);
        Button btnrandomCancel = (Button) randomDialog.findViewById(R.id.btn_myrandom_cancel);
        editText.setText("");
        btnrandomOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strRandom = editText.getText().toString();
                Log.d("TAG1",strRandom);
                dbhelper.create_recommend(strRandom);
                randomDialog.dismiss();
                Toast.makeText(SettingActivity.this, "추천리스트에 할일이 추가되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
        btnrandomCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomDialog.dismiss();
                Toast.makeText(SettingActivity.this, "입력이 취소되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
        randomDialog.show();
    }
    public void onNumberpickDialog(View view) {
        final Dialog numberpickDialog = new Dialog(this);
        numberpickDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        numberpickDialog.setContentView(R.layout.dialog_numberpick);

        Button btnok = (Button) numberpickDialog.findViewById(R.id.btn_number_ok);
        Button btncancel = (Button) numberpickDialog.findViewById(R.id.btn_number_cancel);

        final NumberPicker np = (NumberPicker) numberpickDialog.findViewById(R.id.numberPicker);
        np.setMinValue(0);
        np.setMaxValue(5);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setDividerColor(np, android.R.color.white);
        np.setWrapSelectorWheel(false);
        np.setValue(settings.getInt("RandomCount",2));
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {

            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRandomcount.setText(String.valueOf(np.getValue()+"개"));
                numberpickDialog.dismiss();
                settings = getSharedPreferences("myPref",0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("RandomCount",np.getValue());
                editor.commit();
                List<RecommendedList> items = dbhelper.get_Recommend_ByStatus(1);
                int size = (items==null)?0:items.size();
                if(size<settings.getInt("RandomCount",2)){
                    editor = settings.edit();
                    editor.putInt("RandomCount",size);
                    editor.commit();
                    tvRandomcount.setText(settings.getInt("RandomCount",2)+"개");
                    Toast.makeText(SettingActivity.this, "선택리스트에 항목이 적어 추천으로 받을 할일 개수가 변경됩니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberpickDialog.dismiss();
            }
        });
        numberpickDialog.show();
    }

    private void setDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for(java.lang.reflect.Field pf: pickerFields){
            if(pf.getName().equals("mSelectDivider")){
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker,colorDrawable);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    public void onClear(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("정말 초기화 하겠습니까?");
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setMessage("모든 설정이 초기값으로 돌아가고 내가 저장한 할일도 사라집니다.");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SettingActivity.this, "설정이 초기화되었습니다.", Toast.LENGTH_SHORT).show();
                dbhelper.clear_db();
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.commit();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

