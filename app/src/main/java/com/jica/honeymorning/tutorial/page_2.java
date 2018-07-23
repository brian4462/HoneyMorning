package com.jica.honeymorning.tutorial;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jica.honeymorning.R;

public class page_2 extends android.support.v4.app.Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ConstraintLayout layout=(ConstraintLayout) inflater.inflate(R.layout.tutorial_view_02,container,false);

        ConstraintLayout background=(ConstraintLayout) layout.findViewById(R.id.view2);

        return layout;
    }
}

