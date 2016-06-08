package rs.fon.eklubmobile.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

import rs.fon.eklubmobile.R;

/**
 * Created by milos on 6/8/16.
 */
public class GroupSpinnerAdapter extends ArrayAdapter<HashMap<String, String>> {

    private List<HashMap<String, String>> mGroups;

    public GroupSpinnerAdapter(Context context, int resource, List<HashMap<String, String>> groups) {
        super(context, resource, groups);
        mGroups = groups;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String, String> group = mGroups.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_spinner_selected_item, parent, false);
        }

        if(group != null) {
            TextView groupName = (TextView) convertView;
            groupName.setText(group.get("name"));
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        HashMap<String, String> group = mGroups.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_spinner_item, parent, false);
        }

        if(group != null) {
            TextView groupName = (TextView) convertView.findViewById(R.id.group_name);
            groupName.setText(group.get("name"));
        }

        return convertView;
    }
}
