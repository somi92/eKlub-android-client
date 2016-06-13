package rs.fon.eklubmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import rs.fon.eklubmobile.R;
import rs.fon.eklubmobile.entities.Attendance;
import rs.fon.eklubmobile.entities.Member;
import rs.fon.eklubmobile.listeners.EKlubEventListener;
import rs.fon.eklubmobile.tasks.GetMembersTask;
import rs.fon.eklubmobile.util.Constants;

public class AttendancesActivity extends AppCompatActivity implements EKlubEventListener<Member[]> {

    private ListView mAttendancesListView;
    private List<Attendance> mAttendances;

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
            Attendance[] attendances = new Attendance[mAttendances.size()];
            attendances = mAttendances.toArray(attendances);
            intent.putExtra("attendances", attendances);
            setResult(Constants.SET_ATTENDANCES_REQUEST, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onDataReceived(Member[] members) {
        try {
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

    private void populateAttendanceList(Member[] members) throws JSONException {
        mTempEditText = new int[members.length];
        mTempIsAttendant = new boolean[members.length];
        mAttendances = new ArrayList<>();
        for(int i=0; i<members.length; i++) {
            Attendance attendance = new Attendance();
            attendance.setMember(members[i]);
            attendance.setLateMin(0);
            attendance.setIsAttendant(true);
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
            Attendance attendance = mAttendances.get(position);
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

            holder.textView.setText(attendance.getMember().getNameSurname());
            holder.editText.setText(mTempEditText[position] + "");
            holder.checkBox.setChecked(mTempIsAttendant[position]);

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

                    mAttendances.get(holder.position).setLateMin(lateMin);
                    mTempEditText[holder.position] = lateMin;
                }
            });

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mAttendances.get(holder.position).setIsAttendant(b);
                    mTempIsAttendant[holder.position] = b;
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
