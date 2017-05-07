package com.kynlem.solution.streetwitness;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kynlem.solution.streetwitness.dao.Incident;

import java.util.ArrayList;

/**
 * Created by oleh on 28.04.17.
 */

public class IncidentAdapter extends BaseAdapter {
    Activity activity;
    LayoutInflater inflater;
    ArrayList<Incident> incidents;

    public IncidentAdapter(Activity activity, ArrayList<Incident> incidents){
        this.activity = activity;
        this.inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.incidents = incidents;
    }

    @Override
    public int getCount() {
        return incidents.size();
    }

    @Override
    public Object getItem(int position) {
        return incidents.get(position);
    }

    public Incident getIncident(int position){
        return (Incident) getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item, parent, false);
        }

        Incident incident = getIncident(position);
        ((TextView) convertView.findViewById(R.id.incidentName)).setText(incident.getTitle());
        ((TextView) convertView.findViewById(R.id.incidentStatus)).setText(incident.getStatus());
        ((TextView) convertView.findViewById(R.id.incidentDate)).setText(incident.getTimestamp());
        ((TextView) convertView.findViewById(R.id.incidentLocation)).setText(incident.getLocation());
        ((ImageView) convertView.findViewById(R.id.incident_logo)).setImageResource(R.drawable.police);

        return convertView;
    }
}
