package com.example.administrator.realmdemodb;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class CricketTeam extends RealmObject {

    private String name;
    private long votes;

    @Ignore
    private int sessionId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVotes() {
        return votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }
}
