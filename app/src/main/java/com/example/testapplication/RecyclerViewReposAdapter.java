package com.example.testapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.realm.RealmResults;

public class RecyclerViewReposAdapter extends RecyclerView.Adapter<RecyclerViewReposAdapter.ViewHolder> {

    private LayoutInflater reposListLayout;
    private String[] reposList;

    // Constructor of the class
    public RecyclerViewReposAdapter(Context context, String[] reposList) {
        this.reposListLayout = LayoutInflater.from(context);
        this.reposList = reposList;
    }

    // get the size of the list
    @Override

    public int getItemCount() {
        return reposList == null ? 0 : reposList.length;
    }

    // specify the row layout file and click for each row
    @Override
    public RecyclerViewReposAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = reposListLayout.inflate(R.layout.repository_row, parent, false);
        RecyclerViewReposAdapter.ViewHolder reposViewHolder = new RecyclerViewReposAdapter.ViewHolder(view);
        return reposViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final RecyclerViewReposAdapter.ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        for (int i = 0; i < reposList.length; i++){
            item.setText(reposList[i]);
        }

    }

    public void setData(RealmResults<User> users) {

    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView item;
        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.repos_name);
        }

    }

}
