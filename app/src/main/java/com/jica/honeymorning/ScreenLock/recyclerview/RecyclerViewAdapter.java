package com.jica.honeymorning.ScreenLock.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jica.honeymorning.R;
import com.jica.honeymorning.database.entity.TodoValue;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    List<TodoValue> items;
    Context mContext;


    public RecyclerViewAdapter(List<TodoValue> itemList) {
        items = itemList;
    }

    //뷰생성, 뷰 홀더 호출
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lockscreen_recyclerview,viewGroup,false);
        mContext = viewGroup.getContext();
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        return holder;
    }
    // 필수 오버라이드 : 재활용되는 View 가 호출, Adapter 가 해당 position 에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final RecyclerViewHolder recyclerViewHolder, int i) {
            recyclerViewHolder.tvTitle.setText(items.get(i).getNote());
    }
    // 필수 오버라이드 : 데이터 갯수 반환
    @Override
    public int getItemCount() {
        return (items==null)?0:items.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        boolean isChecked = false;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            isChecked = (isChecked)?false:true;
        }
    }
    public int remove(int position) {
        int item_id = items.get(position).getId();
        items.remove(position);
        return item_id;
    }
}
