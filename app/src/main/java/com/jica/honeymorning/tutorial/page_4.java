package com.jica.honeymorning.tutorial;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jica.honeymorning.R;

public class page_4 extends android.support.v4.app.Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ConstraintLayout layout=(ConstraintLayout) inflater.inflate(R.layout.tutorial_view_04,container,false);

        ConstraintLayout background=(ConstraintLayout) layout.findViewById(R.id.view4);
        Button btnClose=(Button) layout.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int intoFirst = 1;
                SharedPreferences myPref = getActivity().getSharedPreferences("myPref",0);
                SharedPreferences.Editor editor = myPref.edit();
                editor.putInt("Tutorial",intoFirst);
                editor.commit();
                getActivity().finish();
            }
        });

        return layout;
    }
}

