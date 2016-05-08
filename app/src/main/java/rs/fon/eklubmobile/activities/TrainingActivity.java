package rs.fon.eklubmobile.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONObject;

import java.util.Calendar;

import rs.fon.eklubmobile.R;
import rs.fon.eklubmobile.listeners.EKlubEventListener;
import rs.fon.eklubmobile.tasks.GetAllGroupsTask;

public class TrainingActivity extends AppCompatActivity implements EKlubEventListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private Button mDateButton;
    private Button mTimeButton;
    private TextView mDateTime;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        loadGroups();

        mDateButton = (Button) findViewById(R.id.btnDate);
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

        mTimeButton = (Button) findViewById(R.id.btnTime);
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

        mDateTime = (TextView) findViewById(R.id.lblDateTime);
        setDate(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        setTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE));
    }

    private void loadGroups() {
        String url = "192.168.1.181:8080";
        GetAllGroupsTask groupsTask = new GetAllGroupsTask(this);
        groupsTask.execute(url);
    }

    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onDataReceived(JSONObject data) {
        try {
            String group = data.getJSONArray("payload").getJSONObject(0).toString();
            Toast.makeText(this, group.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {

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
