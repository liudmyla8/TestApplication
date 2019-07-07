package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class RepositoriesActivity extends AppCompatActivity {

    private RecyclerView reposRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewReposAdapter r_adapter;
    private User user;
    private String[] reposList = user.getReposArray();

    private Realm realm;
    private RealmResults<User> users;
    private RealmChangeListener<RealmResults<User>> realmChangeListener = (users) -> {
        r_adapter.setData(users);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_repositories);
        Realm.init(this);

        Intent intent = getIntent();
        String login = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        Realm.deleteRealm(Realm.getDefaultConfiguration());
        realm = Realm.getDefaultInstance();

        //setting up the RecyclerView
        reposRecyclerView = findViewById(R.id.rv_repositories);
        layoutManager = new LinearLayoutManager(this);
        reposRecyclerView.setLayoutManager(layoutManager);
        r_adapter = new RecyclerViewReposAdapter(this, reposList);
        reposRecyclerView.setAdapter(r_adapter);

        users = realm.where(User.class).findAllAsync();
        users.addChangeListener(realmChangeListener);

        URL userReposURL = null;

        {
            try {
                userReposURL = new URL("https://api.github.com/users/" + login + "/repos");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        //loading from JSON
        try {
            loadRepositories(userReposURL);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        users.removeAllChangeListeners();
        realm.close();
    }

    //getting user repositories from URL
    protected void loadRepositories(URL url) throws IOException, JSONException {

        String response = "";

        // read text returned by server
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response += line;
        }
        in.close();

        //creating JSONObject
        final JSONObject jUser = new JSONObject(response);

        //getting list of repositories
        String repos = "";
        for (int i = 0; i < jUser.length(); i++){
            repos += jUser.getString("name");
            if (i != jUser.length() -1) {
                repos += ",";
            }
        }

        //adding list to certain user
        realm.where(User.class).equalTo("login", jUser.getJSONObject("owner").getString("login")).findFirst().setUserRepos(repos);

    }

}
