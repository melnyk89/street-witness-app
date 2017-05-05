package com.kynlem.solution.streetwitness;

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
    Context context;
    LayoutInflater inflater;
    ArrayList<Incident> incidents;

    public IncidentAdapter(Context context, ArrayList<Incident> incidents){
        this.context = context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.incidents = incidents;
    }

    @Override
    public int getCount() {
        return 0;
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
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item, parent, false);
        }

        Incident incident = getIncident(position);

        ((TextView) view.findViewById(R.id.tvTitle)).setText(incident.getTitle());
        ((TextView) view.findViewById(R.id.tvStatus)).setText(incident.getStatus());
        ((TextView) view.findViewById(R.id.tvDescription)).setText(incident.getDescription());

        return view;
    }
}
