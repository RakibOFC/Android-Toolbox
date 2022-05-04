package com.rakibofc.androidtoolbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SystemInfoListAdapter extends ArrayAdapter<SystemInfo> {

    private Context context;
    int resource;

    public SystemInfoListAdapter(Context context, int resource, ArrayList<SystemInfo> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;

    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View contentView, ViewGroup parent) {

        String fieldName = getItem(position).getFieldName();
        String fieldData = getItem(position).getFieldData();

        SystemInfo systemInfo = new SystemInfo(fieldName, fieldData);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        contentView = layoutInflater.inflate(resource, parent, false);

        TextView textViewFieldName = contentView.findViewById(R.id.textViewFieldName);
        TextView textViewFieldData = contentView.findViewById(R.id.textViewFieldData);

        textViewFieldName.setText(fieldName);
        textViewFieldData.setText(fieldData);

        return contentView;
    }
}
