package rs.fon.eklubmobile.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rs.fon.eklubmobile.R;
import rs.fon.eklubmobile.listeners.EKlubEventListener;
import rs.fon.eklubmobile.tasks.GetMembersTask;

public class AttendancesActivity extends AppCompatActivity implements EKlubEventListener {

    private ListView mAttendancesListView;
    private List<JSONObject> mAttendances;

    private int[] mTempEditText;
    private boolean[] mTempIsAttendant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendances);

        mAttendancesListView = (ListView) findViewById(R.id.attendancesListView);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("groupId");
        loadMembers(id);

    }

    private void loadMembers(String id) {
        String url = "192.168.1.181:8080";
        String param = String.format("{\"group\":\"%s\"}", id);
        GetMembersTask task = new GetMembersTask(this);
        task.execute(url, param);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendances, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.done_button) {
            Intent intent = new Intent();
//            intent.putParcelableArrayListExtra("attendances", mAttendances);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onDataReceived(JSONObject data) {
        try {
            JSONArray members = data.getJSONArray("payload");
            populateAttendanceList(members);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskFinished() {

    }

    @Override
    public void onNotificationReceived(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void populateAttendanceList(JSONArray data) throws JSONException {
        mTempEditText = new int[data.length()];
        mTempIsAttendant = new boolean[data.length()];
        mAttendances = new ArrayList<>();
        for(int i=0; i<data.length(); i++) {
            JSONObject member = data.getJSONObject(i);
            JSONObject attendance = new JSONObject();
            attendance.put("member", member);
            attendance.put("lateMin", 0);
            attendance.put("isAttendant", true);
            mTempEditText[i] = 0;
            mTempIsAttendant[i] = true;
            mAttendances.add(attendance);
        }
        AttendanceListAdapter attendancesAdapter = new AttendanceListAdapter();
        mAttendancesListView.setAdapter(attendancesAdapter);

    }

    private class AttendanceListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(mAttendances != null && mAttendances.size() != 0){
                return mAttendances.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mAttendances.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            JSONObject obj = mAttendances.get(position);
            final ViewHolder holder;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AttendancesActivity.this).inflate(R.layout.attendance_item, parent, false);
                holder.textView = (TextView) convertView.findViewById(R.id.lblMember);
                holder.editText = (EditText) convertView.findViewById(R.id.txtLateMin);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.chbIsAttendant);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.position = position;

            try {
                holder.textView.setText(obj.getJSONObject("member").getString("nameSurname"));
                holder.editText.setText(mTempEditText[position] + "");
                holder.checkBox.setChecked(mTempIsAttendant[position]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    int lateMin = 0;
                    try {
                        lateMin = Integer.parseInt(editable.toString());
                    } catch (Exception e) {
                        lateMin = 0;
                    }

                    try {
                        mAttendances.get(holder.position).put("lateMin", lateMin);
                        mTempEditText[holder.position] = lateMin;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    try {
                        mAttendances.get(holder.position).put("isAttendant", b);
                        mTempIsAttendant[holder.position] = b;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return convertView;
        }
    }

    private class ViewHolder {
        TextView textView;
        EditText editText;
        CheckBox checkBox;
        int position;
    }
}
