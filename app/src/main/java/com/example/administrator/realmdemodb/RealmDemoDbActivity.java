package com.example.administrator.realmdemodb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmDemoDbActivity extends Activity implements AdapterView.OnItemClickListener {

    private GridView mGridView;
    private CricketTeamAdapter mAdapter;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_demo_db);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();

        // clear previous session
        Realm.deleteRealm(realmConfiguration);

        realm = Realm.getInstance(realmConfiguration);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load from file "teams.json" first time
        if (mAdapter == null) {
            List<CricketTeam> cricketTeams = loadCricketTeams();

            mAdapter = new CricketTeamAdapter(this);
            mAdapter.setData(cricketTeams);

            // GridView that displays list of cricket teams
            mGridView = (GridView) findViewById(R.id.teams_list);
            mGridView.setAdapter(mAdapter);
            mGridView.setOnItemClickListener(RealmDemoDbActivity.this);
            mAdapter.notifyDataSetChanged();
            mGridView.invalidate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private List<CricketTeam> loadCricketTeams() {
        // To Load from local assets.
        InputStream stream;
        try {
            stream = getAssets().open("teams.json");
        } catch (IOException e) {
            return null;
        }

        Gson gson = new GsonBuilder().create();

        JsonElement json = new JsonParser().parse(new InputStreamReader(stream));
        List<CricketTeam> cricketTeams = gson.fromJson(json, new TypeToken<List<CricketTeam>>() {}.getType());

        // Open transaction to store items into realm
        realm.beginTransaction();
        Collection<CricketTeam> realmCricketTeams = realm.copyToRealm(cricketTeams);
        realm.commitTransaction();

        return new ArrayList<CricketTeam>(realmCricketTeams);
    }

    public void updateCricketTeams() {
        // Pull all the cricket teams from realm
        RealmResults<CricketTeam> cricketTeams = realm.where(CricketTeam.class).findAll();

        // Put these items in the Adapter
        mAdapter.setData(cricketTeams);
        mAdapter.notifyDataSetChanged();
        mGridView.invalidate();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CricketTeam modifiedCricketTeam = (CricketTeam) mAdapter.getItem(position);

        final CricketTeam cricketTeam = realm.where(CricketTeam.class).equalTo("name", modifiedCricketTeam.getName()).findFirst();

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                cricketTeam.setVotes(cricketTeam.getVotes() + 1);
            }
        });
        updateCricketTeams();
    }
}
