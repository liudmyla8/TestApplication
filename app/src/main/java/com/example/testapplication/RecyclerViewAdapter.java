package com.example.testapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.realm.RealmResults;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    private LayoutInflater userListLayout;
    private ArrayList<String> userList;
    private static ClickListener clickListener;

    // Constructor of the class
    public RecyclerViewAdapter(Context context, ArrayList<String> userList) {
        this.userListLayout = LayoutInflater.from(context);
        this.userList = userList;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = userListLayout.inflate(R.layout.user_row, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        item.setText(userList.get(listPosition));
    }

    public void setData(RealmResults<User> users) {
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item;
        public TextView changes;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.user_name);
            changes = itemView.findViewById(R.id.user_changes);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerViewAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}
