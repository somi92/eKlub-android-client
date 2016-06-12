package rs.fon.eklubmobile.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import rs.fon.eklubmobile.R;
import rs.fon.eklubmobile.listeners.EKlubEventListener;
import rs.fon.eklubmobile.tasks.GetAllGroupsTask;
import rs.fon.eklubmobile.util.GroupSpinnerAdapter;

public class TrainingActivity extends AppCompatActivity implements EKlubEventListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private ImageButton mDateButton;
    private ImageButton mTimeButton;
    private TextView mDateTime;
    private Spinner mGroup;
    private Button mAttendances;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        loadGroups();

        mDateButton = (ImageButton) findViewById(R.id.btnDate);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        TrainingActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(Color.BLUE);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        mTimeButton = (ImageButton) findViewById(R.id.btnTime);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        TrainingActivity.this, 12, 12, 30, true
                );
                tpd.setAccentColor(Color.BLUE);
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        mDateTime = (TextView) findViewById(R.id.lblDateTimeValue);
        setDate(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        setTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE));

        mGroup = (Spinner) findViewById(R.id.spnGroup);

        mAttendances = (Button) findViewById(R.id.btnAttendances);
        mAttendances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrainingActivity.this, AttendancesActivity.class);
                intent.putExtra("groupId", ((HashMap<String, String>) mGroup.getSelectedItem()).get("id"));
                startActivity(intent);
            }
        });
    }

    private void loadGroups() {
        String url = "192.168.1.181:8080";
        GetAllGroupsTask groupsTask = new GetAllGroupsTask(this);
        groupsTask.execute(url);
    }

    private void populateGroupSpinner(JSONArray groups) throws JSONException {
        List<HashMap<String, String>> groupsData = new ArrayList<>();
        for(int i = 0; i < groups.length(); i++) {
            JSONObject jsonObject = groups.getJSONObject(i);
            HashMap<String, String> group = new HashMap<>();
            group.put("id", jsonObject.getString("id"));
            group.put("name", jsonObject.getString("name"));
            groupsData.add(group);
        }

        ArrayAdapter<HashMap<String, String>> groupsAdapter = new GroupSpinnerAdapter(TrainingActivity.this,
                R.layout.group_spinner_selected_item, groupsData);
        mGroup.setAdapter(groupsAdapter);
    }

    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onDataReceived(JSONObject data) {
        try {
            JSONArray group = data.getJSONArray("payload");
            populateGroupSpinner(group);
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        setTime(hourOfDay, minute);
    }

    private void setDate(int year, int month, int dayOfMonth) {
        String currentDateTime = mDateTime.getText().toString();
        String selectedDate = String.format("%1$04d-%2$02d-%3$02d", year, (month + 1), dayOfMonth);
        currentDateTime = currentDateTime.replace(currentDateTime.substring(0, currentDateTime.indexOf(" ")), selectedDate);
        mDateTime.setText(currentDateTime);
    }

    private void setTime(int hourOfDay, int minute) {
        String currentDateTime = mDateTime.getText().toString();
        String selectedTime = String.format("%1$02d:%2$02d", hourOfDay, minute);
        currentDateTime = currentDateTime.replace(currentDateTime.substring(currentDateTime.indexOf(" ") + 1), selectedTime);
        mDateTime.setText(currentDateTime);
    }
}
