package com.example.glassbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.glassbox.HomeActivity.jsonObject;
import static com.example.glassbox.HomeActivity.SelectedItem;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Activity activity = this.activity;
    private Context _context;

    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ArrayList<ArrayList<Boolean>> checkboxStatus = new ArrayList<ArrayList<Boolean>>();

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;

        int groupCount = 7;
        for(int i=0; i<groupCount; i++)
        {
            ArrayList<Boolean> childStatus = new ArrayList<Boolean>();
            for(int j=0; j<SelectedItem[i].length; j++) {
                childStatus.add(SelectedItem[i][j]);
            }
            checkboxStatus.add(childStatus);
        }
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        txtListChild.setTextColor(Color.parseColor("#FFFFFF"));

        Button button=(Button)convertView.findViewById(R.id.button_show);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.lblListItem);
        checkBox.setChecked(checkboxStatus.get(groupPosition).get(childPosition));

        checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(childPosition);
                System.out.println(groupPosition);
                String key;
                if (childPosition==10)
                    key = "70";
                else
                    key = Integer.toString(groupPosition) +Integer.toString(childPosition);
                SelectedItem[groupPosition][childPosition] = !SelectedItem[groupPosition][childPosition];
                if (SelectedItem[groupPosition][childPosition]) {
                    checkboxStatus.get(groupPosition).set(childPosition, true);
                    try {
                        jsonObject.put(key, _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    checkboxStatus.get(groupPosition).set(childPosition, false);
                    jsonObject.remove(key);
                }
            }
        });

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, SampleImageShowActivity.class);
                intent.putExtra("PARENT", groupPosition);
                intent.putExtra("CHILD",childPosition);
                _context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
