package rs.fon.eklubmobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rs.fon.eklubmobile.R;
import rs.fon.eklubmobile.listeners.EKlubEventListener;
import rs.fon.eklubmobile.tasks.GetMembersTask;
import rs.fon.eklubmobile.util.AttendanceListAdapter;

public class AttendancesActivity extends AppCompatActivity implements EKlubEventListener {

    private ListView mAttendancesListView;
    private List<JSONObject> mAttendances;

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
        mAttendances = new ArrayList<>();
        for(int i=0; i<data.length(); i++) {
            JSONObject member = data.getJSONObject(i);
            JSONObject attendance = new JSONObject();
            attendance.put("member", member);
            attendance.put("lateMin", 0);
            attendance.put("isAttendant", true);
            mAttendances.add(attendance);
        }
        ArrayAdapter<JSONObject> attendancesAdapter = new AttendanceListAdapter(this, mAttendances);
        mAttendancesListView.setAdapter(attendancesAdapter);
    }
}
