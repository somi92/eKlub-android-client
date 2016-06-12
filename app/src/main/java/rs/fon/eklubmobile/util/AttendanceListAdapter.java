package rs.fon.eklubmobile.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rs.fon.eklubmobile.R;

/**
 * Created by milos on 6/12/16.
 */
public class AttendanceListAdapter extends ArrayAdapter<JSONObject> {

    private List<JSONObject> mData;

    public AttendanceListAdapter(Context context, List<JSONObject> data) {
        super(context, 0, data);
        mData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject obj = mData.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.attendance_item, parent, false);
        }

        TextView member = (TextView) convertView.findViewById(R.id.lblMember);
        EditText lateMin = (EditText) convertView.findViewById(R.id.txtLateMin);
        CheckBox isAttendant = (CheckBox) convertView.findViewById(R.id.chbIsAttendant);

        try {
            member.setText(obj.getJSONObject("member").getString("nameSurname"));
            lateMin.setText(obj.getJSONObject("member").getInt("lateMin"));
            isAttendant.setChecked(obj.getJSONObject("member").getBoolean("isAttendant"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
