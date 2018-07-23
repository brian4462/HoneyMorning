package com.jica.honeymorning.tutorial;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jica.honeymorning.R;

public class page_1 extends android.support.v4.app.Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ConstraintLayout layout=(ConstraintLayout) inflater.inflate(R.layout.tutorial_view_01,container,false);

        ConstraintLayout background=(ConstraintLayout) layout.findViewById(R.id.view1);

        return layout;
    }
}

