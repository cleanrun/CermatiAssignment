package com.cermati.cermatiassignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemHolder>{

    private final String TAG = "ListAdapter";

    private Context activityContext;
    private List<Model> listData = new ArrayList<>();
    private ClickListener clickListener;

    public ListAdapter(Context activityContext, ClickListener clickListener) {
        this.activityContext = activityContext;
        this.clickListener = clickListener;
        //this.listData = listData;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_search, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        holder.tvName.setText(listData.get(position).getName());
        Picasso.get()
                .load(listData.get(position).getImage())
                .placeholder(R.drawable.progress_animation)
                .into(holder.ivPhoto);

        // showing the model id and url on click
        holder.llList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onListClick(listData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        try{
            if(listData.isEmpty()){
                return 0;
            }else{
                return listData.size();
            }
        }catch (NullPointerException e){
            Log.e(TAG, "getItemCount: " + e.getMessage());
        }
        return 0;
    }

    public void setData(List<Model> listData){
        this.listData = listData;
        notifyDataSetChanged();
    }

    public List<Model> getData(){
        return listData;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private LinearLayout llList;
        private ImageView ivPhoto;
        private TextView tvName;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            llList = itemView.findViewById(R.id.ll_list);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }

    public interface ClickListener{
        void onListClick(Model model);
    }
}
