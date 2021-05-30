package com.example.aiot;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter {
    private final ArrayList<String> urls;
    private final Context context;

    public ViewPagerAdapter(Context context, ArrayList<String> urls) {
        this.urls = urls;
        this.context = context;
        Log.d("DEBUG DISPLAY", urls.toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pager_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myHolder = (MyViewHolder)holder;
        Glide.with(context).load(urls.get(position)).into(myHolder.getImage());
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view_pager);
        }

        public ImageView getImage() {
            return image;
        }
    }
}
