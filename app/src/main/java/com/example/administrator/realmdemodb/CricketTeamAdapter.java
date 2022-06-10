package com.example.administrator.realmdemodb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class CricketTeamAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<CricketTeam> cricketTeams = null;

    public CricketTeamAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<CricketTeam> details) {
        this.cricketTeams = details;
    }

    @Override
    public int getCount() {
        if (cricketTeams == null) {
            return 0;
        }
        return cricketTeams.size();
    }

    @Override
    public Object getItem(int position) {
        if (cricketTeams == null || cricketTeams.get(position) == null) {
            return null;
        }
        return cricketTeams.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View currentView, ViewGroup parent) {
        if (currentView == null) {
            currentView = inflater.inflate(R.layout.team_listitem, parent, false);
        }

        CricketTeam cricketTeam = cricketTeams.get(position);

        if (cricketTeam != null) {
            ((TextView) currentView.findViewById(R.id.name)).setText(cricketTeam.getName());
            ((TextView) currentView.findViewById(R.id.votes)).setText(String.format(Locale.US, "%d", cricketTeam.getVotes()));
        }

        return currentView;
    }
}
