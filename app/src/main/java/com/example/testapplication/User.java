package com.example.testapplication;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class User implements RealmModel {

    private String login;
    private int id;
    private String node_id;
    private String avatar_url;
    private String gravatar_id;
    private String url;
    private String followers_url;
    private String following_url;
    private String gists_url;
    private String starred_url;
    private String subscriptions_url;
    private String organizations_url;
    private String repos_url;
    private String events_url;
    private String received_events_url;
    private String type;
    private boolean site_admin;

    private String repositories;
    private int changesCount;

    public User() {
    }

    public User(String login, int id) {
        login = "";
        id = 0;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public void setUserRepos(String userRepos) {
        this.repositories = userRepos;
    }

    public String getUserRepos(){
        return repositories;
    }

    public int getSize(String repositories){

        String[] reposList = repositories.split(",");
        int size = reposList.length;

        return size;
    }

    public String[] getReposArray(){

        String[] reposList = repositories.split(",");

        return  reposList;
    }

    public void setChangesCount(int changesCount){
        this.changesCount = changesCount;
    }

    public int getChangesCount(){
        return changesCount;
    }
}
