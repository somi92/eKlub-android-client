package rs.fon.eklubmobile.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import rs.fon.eklubmobile.R;
import rs.fon.eklubmobile.entities.Attendance;
import rs.fon.eklubmobile.entities.Group;
import rs.fon.eklubmobile.entities.Training;
import rs.fon.eklubmobile.listeners.EKlubEventListener;
import rs.fon.eklubmobile.tasks.GetAllGroupsTask;
import rs.fon.eklubmobile.tasks.SaveTrainingTask;
import rs.fon.eklubmobile.util.Constants;
import rs.fon.eklubmobile.util.EKlubApplication;
import rs.fon.eklubmobile.util.GroupSpinnerAdapter;

public class TrainingActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private ImageButton mDateButton;
    private ImageButton mTimeButton;
    private TextView mDateTime;
    private Spinner mGroup;
    private Button mAttendancesButton;
    private Button mSave;

    private EditText mDuration;
    private EditText mDescription;

    private List<Attendance> mAttendances;

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
        mDuration = (EditText) findViewById(R.id.txtDuration);
        mDescription = (EditText) findViewById(R.id.txtDescription);

        mAttendancesButton = (Button) findViewById(R.id.btnAttendances);
        mAttendancesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrainingActivity.this, AttendancesActivity.class);
                intent.putExtra("groupId", ((Group) mGroup.getSelectedItem()).getId() + "");
                if(mAttendances != null && mAttendances.size() > 0) {
                    Attendance[] attendances = new Attendance[mAttendances.size()];
                    attendances = mAttendances.toArray(attendances);
                    intent.putExtra("attendances", attendances);
                }
                startActivityForResult(intent, Constants.SET_ATTENDANCES_REQUEST);
            }
        });

        mSave = (Button) findViewById(R.id.btnSave);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTraining();
            }
        });

        mGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAttendances = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mAttendances = null;
            }
        });
    }

    private void saveTraining() {
        Training training = new Training();
        training.setGroup((Group) mGroup.getSelectedItem());
        int duration = mDuration.getText().toString() != null && mDuration.getText().toString().matches("\\d+")
                ? Integer.parseInt(mDuration.getText().toString()) : 0;
        training.setDurationMinutes(duration);
        String dateTime = mDateTime.getText().toString().replace(" ", "T") + ":00.000+02:00";
        training.setDateTime(dateTime);
        training.setDescription(mDescription.getText().toString());
        training.setAttendances(mAttendances);
        String accessToken = ((EKlubApplication) getApplicationContext()).getmAccessToken();
        SaveTrainingTask st = new SaveTrainingTask(mSaveTrainingTaskListener, training);
        String url = "192.168.1.181:8080";
        st.execute(url, accessToken);
    }

    private void loadGroups() {
        String url = "192.168.1.181:8080";
        String accessToken = ((EKlubApplication) getApplicationContext()).getmAccessToken();
        GetAllGroupsTask groupsTask = new GetAllGroupsTask(mGroupsTaskListener);
        groupsTask.execute(url, accessToken);
    }

    private void populateGroupSpinner(Group[] groups) throws JSONException {
        List<Group> groupsList = Arrays.asList(groups);
        ArrayAdapter<Group> groupsAdapter = new GroupSpinnerAdapter(TrainingActivity.this,
                R.layout.group_spinner_selected_item, groupsList);
        mGroup.setAdapter(groupsAdapter);
    }

    private EKlubEventListener<Group[]> mGroupsTaskListener = new EKlubEventListener<Group[]>() {
        @Override
        public void onTaskStarted() {

        }

        @Override
        public void onDataReceived(Group[] data) {
            try {
                populateGroupSpinner(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTaskFinished() {

        }

        @Override
        public void onNotificationReceived(String message) {
            Toast.makeText(TrainingActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    private EKlubEventListener<String> mSaveTrainingTaskListener = new EKlubEventListener<String>() {
        @Override
        public void onTaskStarted() {

        }

        @Override
        public void onDataReceived(String data) {
            Toast.makeText(TrainingActivity.this, data, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onTaskFinished() {

        }

        @Override
        public void onNotificationReceived(String message) {
            Toast.makeText(TrainingActivity.this, message, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        setTime(hourOfDay, minute);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.SET_ATTENDANCES_REQUEST) {

            if(data == null)
                return;
            Parcelable[] parcelables = data.getParcelableArrayExtra("attendances");
            Attendance[] attendances = Arrays.copyOf(parcelables, parcelables.length, Attendance[].class);
            mAttendances = Arrays.asList(attendances);
        }
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
