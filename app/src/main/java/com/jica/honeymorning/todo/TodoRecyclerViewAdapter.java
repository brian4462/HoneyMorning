package com.jica.honeymorning.todo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jica.honeymorning.R;
import com.jica.honeymorning.database.entity.TodoValue;

import java.util.List;

public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoRecyclerViewAdapter.RecyclerViewHolder> {
    List<TodoValue> items;
    Context mContext;
    //아이템 클릭시 실행 함수
    private ItemClick itemClick;

    public TodoRecyclerViewAdapter(List<TodoValue> itemList) {
        items = itemList;
    }

    //뷰생성, 뷰 홀더 호출
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todoview_recyclerview_row,viewGroup,false);
        mContext = viewGroup.getContext();
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        return holder;
    }
    // 필수 오버라이드 : 재활용되는 View 가 호출, Adapter 가 해당 position 에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final RecyclerViewHolder recyclerViewHolder, int i) {
        final int position = i;
        final int status = items.get(i).getStatus();
        final TodoValue item = items.get(i);

        if(status==0){
            recyclerViewHolder.ivTodoCheck.setImageResource(R.drawable.btn_check_on);
        } else {
            recyclerViewHolder.ivTodoCheck.setImageResource(R.drawable.btn_check_off);
        }
        recyclerViewHolder.tvText.setText(items.get(i).getNote());
        recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClick != null){
                    itemClick.onClick(view,position);
                }
            }
        });
    }
    // 필수 오버라이드 : 데이터 갯수 반환
    @Override
    public int getItemCount() {
        return (items==null)?0:items.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView tvText;
        ImageView ivTodoCheck;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.TodoTextview);
            ivTodoCheck = (ImageView) itemView.findViewById(R.id.ivTodocheck);
        }
    }

    public interface ItemClick{
        public void onClick(View v, int position);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick){
        this.itemClick = itemClick;
    }
}