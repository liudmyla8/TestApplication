package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT = "com.example.testapplication.EXTRA_TEXT";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private ArrayList<String> userList;
    private User user;

    URL usersURL;

    {
        try {
            usersURL = new URL("https://api.github.com/users");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Realm realm;
    private RealmResults<User> users;
    private RealmChangeListener<RealmResults<User>> realmChangeListener = (users) -> {
        adapter.setData(users);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        Realm.deleteRealm(Realm.getDefaultConfiguration());
        realm = Realm.getDefaultInstance();

        //setting up the RecyclerView
        recyclerView = findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        //handle click on user
        adapter.setOnItemClickListener((position, v) -> {

            openReposActivity();
            Log.d("onclick", "onItemClick position: " + position);
        });

        users = realm.where(User.class).findAllAsync();
        users.addChangeListener(realmChangeListener);

        //loading from JSON
        try {
            loadUserList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openReposActivity(){

        TextView loginRow = findViewById(R.id.user_name);
        String login = loginRow.getText().toString();

        Intent intent = new Intent(this, RepositoriesActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, login);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        users.removeAllChangeListeners();
        realm.close();
    }

    //generating user list and user repositories
    public void loadUserList() throws IOException {

        // read text returned by server
        BufferedReader in = new BufferedReader(new InputStreamReader(usersURL.openStream()));
        String line;
        while ((line = in.readLine()) != null) {
            // Open a transaction to store items into the realm
            realm.beginTransaction();
            realm.createAllFromJson(User.class, line);
            realm.commitTransaction();
        }
        in.close();
    }

}
