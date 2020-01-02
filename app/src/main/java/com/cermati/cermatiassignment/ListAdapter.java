package com.cermati.cermatiassignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    public ListAdapter(Context activityContext) {
        this.activityContext = activityContext;
        //this.listData = listData;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_search, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tvName.setText(listData.get(position).getName());
        Picasso.get()
                .load(listData.get(position).getImage())
                .placeholder(R.drawable.progress_animation)
                .into(holder.ivPhoto);
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
        private ImageView ivPhoto;
        private TextView tvName;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
