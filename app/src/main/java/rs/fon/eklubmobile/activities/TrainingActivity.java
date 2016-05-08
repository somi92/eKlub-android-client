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
import java.util.Date;

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
//                dpd.setThemeDark(true);
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
//                tpd.setThemeDark(true);
                tpd.setAccentColor(Color.BLUE);
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        mDateTime = (TextView) findViewById(R.id.lblDateTime);
        initializeDateTime();
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
//        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
//        Toast.makeText(this, date, Toast.LENGTH_LONG).show();
        String monthOfYearString = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)  : (monthOfYear + 1) + "";
        String dayOfMonthString = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
        String currentDateTime = mDateTime.getText().toString();
        String selectedDate = year + "-" + monthOfYearString + "-" + dayOfMonthString;
        currentDateTime = currentDateTime.replace(currentDateTime.substring(0, currentDateTime.indexOf(" ")), selectedDate);
        mDateTime.setText(currentDateTime);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
//        String time = "You picked the following time: "+hourOfDay+"h"+minute;
//        Toast.makeText(this, time, Toast.LENGTH_LONG).show();
        String hourOfDayString = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
        String minuteString = minute < 10 ? "0" + minute : minute + "";
        String selectedTime = hourOfDayString + ":" + minuteString;
        String currentDateTime = mDateTime.getText().toString();
        currentDateTime = currentDateTime.replace(currentDateTime.substring(currentDateTime.indexOf(" ") + 1), selectedTime);
        mDateTime.setText(currentDateTime);
    }

    private void initializeDateTime() {
        int monthOfYear = Calendar.getInstance().get(Calendar.MONTH);
        int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        String monthOfYearString = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)  : (monthOfYear + 1) + "";
        String dayOfMonthString = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
        String hourOfDayString = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
        String minuteString = minute < 10 ? "0" + minute : minute + "";
        mDateTime.setText(Calendar.getInstance().get(Calendar.YEAR)
                + "-" + monthOfYearString
                + "-" + dayOfMonthString
                + " " + hourOfDayString
                + ":" + minuteString);
    }
}
